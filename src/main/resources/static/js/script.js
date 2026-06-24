let allChats = [];
let activeChatId = null;
let currentUserData = null;
let filteredChats = [];
let stompClient = null;
let currentSubscription = null;
let replyTarget = null;

function getToken() {
    return sessionStorage.getItem("token");
}

function redirectToAuth() {
    window.location.href = "auth.html";
}

function loadCurrentUser() {
    return fetch("/api/users/me", {
        headers: {
            "Authorization": "Bearer " + getToken()
        }
    })
    .then(res => {
        if (res.status === 401) {
            redirectToAuth();
            throw new Error("Unauthorized");
        }
        return res.json();
    })
    .then(user => {
        currentUserData = user;
        document.querySelector(".profile-avatar").innerText = user.name.charAt(0).toUpperCase();
        document.querySelector(".profile-name h2").innerText = user.name;
        return user;
    });
}

async function showUsers() {
    const response = await fetch("/api/users", {
        headers: {
            "Authorization": "Bearer " + getToken()
        }
    });
    const users = await response.json();
    const list = document.getElementById("usersList");
    list.innerHTML = "";
    users.forEach(user => {
        let div = document.createElement("div");
        div.className = "user-item";
        div.innerText = user.username;
        div.onclick = () => {
            startConversation(user.userId);
        };
        list.appendChild(div);
    });
    document.getElementById("usersModal").classList.remove("hidden");
}

function closeUsersModal() {
    document.getElementById("usersModal").classList.add("hidden");
}

function loadChats() {
    return fetch("/api/conversations", {
        headers: {
            "Authorization": "Bearer " + getToken()
        }
    })
    .then(res => res.json())
    .then(data => {
        console.log("CHAT DATA", data);
        allChats = data || [];
        filteredChats = [...allChats];
        renderThreads();
        if (allChats.length > 0 && activeChatId === null) {
            openChat(allChats[0].conversationId);
        }
    });
}

function renderThreads() {
    const container = document.getElementById("chatThreadsContainer");
    container.innerHTML = "";
    filteredChats.forEach(chat => {
        let div = document.createElement("div");
        div.className = "chat-thread";
        div.innerHTML = `
<div class="thread-avatar">${chat.username.charAt(0).toUpperCase()}</div>
<div class="thread-info">
    <div class="thread-name">${escapeHtml(chat.username)}</div>
    <div class="thread-preview">${escapeHtml(chat.lastMessage || "Start chatting")}</div>
</div>
    `;
        div.onclick = () => {
            openChat(chat.conversationId);
        };
        container.appendChild(div);
    });
}

function openChat(id) {
    activeChatId = id;
    document.getElementById("emptyState").classList.add("hidden");
    document.getElementById("chatActive").classList.remove("hidden");
    let chat = allChats.find(c => c.conversationId === id);
    if (chat) {
        document.querySelector("#activeContact .badge-avatar").innerText = chat.username.charAt(0).toUpperCase();
        document.querySelector("#activeContact .contact-details h3").innerText = chat.username;
    }
    loadMessages(id);
    connectWebSocket();
}

function loadMessages(id) {
    fetch("/api/messages/conversation/" + id, {
        headers: {
            "Authorization": "Bearer " + getToken()
        }
    })
    .then(res => res.json())
    .then(messages => {
        displayMessages(messages);
    });
}

function displayMessages(messages) {
    const container = document.getElementById("messagesThread");
    container.innerHTML = "";
    messages.forEach(msg => {
        appendMessage(msg, false);
    });
    container.scrollTop = container.scrollHeight;
}

function appendMessage(message, scroll = true) {
    const container = document.getElementById("messagesThread");
    let sent = message.senderId === currentUserData.id;
    let div = document.createElement("div");
    div.className = "message-unit " + (sent ? "sent" : "received");
    div.dataset.id = message.messageId;
    div.innerHTML = `
<div class="message-wrapper">
    ${message.replyToContent ? `
                <div class="reply-preview">
                    <div class="reply-name">You</div>
                    <div class="reply-text">${escapeHtml(message.replyToContent)}</div>
                </div>
            ` : ""}
<div class="message-card">${escapeHtml(message.content)}</div>
<div class="message-time">${formatTime(message.createdAt)}</div>
</div>
<div class="message-actions">
    <button onclick="replyMessage('${message.messageId}')">↩ Reply</button>
    ${sent ? `
                <button onclick="editMessage('${message.messageId}')">✎ Edit</button>
                <button onclick="deleteMessage('${message.messageId}')">🗑 Delete</button>
            ` : ""}
</div>
    `;
    container.appendChild(div);
    if (scroll) {
        container.scrollTop = container.scrollHeight;
    }
}

function replyMessage(id) {
    const messageElement = document.querySelector(`[data-id="${id}"]`);
    const messageText = messageElement.querySelector(".message-card").innerText;
    replyTarget = { id: id, content: messageText };
    const input = document.getElementById("messageInput");
    input.placeholder = "Replying to: " + messageText;
    input.focus();
}

function editMessage(id) {
    let messageElement = document.querySelector(`[data-id="${id}"]`);
    let oldText = messageElement.querySelector(".message-card").innerText;
    let input = document.getElementById("messageInput");
    input.value = oldText;
    input.dataset.editId = id;
    input.focus();
}

function deleteMessage(id) {
    if (!confirm("Delete this message?")) return;
    fetch("/api/messages/" + id, {
        method: "DELETE",
        headers: {
            "Authorization": "Bearer " + getToken()
        }
    })
    .then(() => {
        document.querySelector(`[data-id="${id}"]`).remove();
    });
}

function connectWebSocket() {
    if (stompClient && stompClient.connected) {
        subscribeConversation();
        return;
    }
    let socket = new SockJS("/chat");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, () => {
        console.log("WEBSOCKET CONNECTED");
        subscribeConversation();
        subscribeCall();
    });
}

function subscribeConversation() {
    if (!activeChatId) return;
    if (currentSubscription) {
        currentSubscription.unsubscribe();
    }
    let topic = "/topic/conversation/" + activeChatId;
    console.log("SUBSCRIBING TO", topic);
    currentSubscription = stompClient.subscribe(topic, message => {
        let data = JSON.parse(message.body);
        console.log("SOCKET DATA", data);
        if (data.type === "offer" || data.type === "answer" || data.type === "candidate") {
            handleCallSignal(data);
            return;
        }
        appendMessage(data);
    });
}

function sendMessage() {
    let input = document.getElementById("messageInput");
    let text = input.value.trim();
    if (!text || !activeChatId) return;
    fetch("/api/messages", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + getToken()
        },
        body: JSON.stringify({
            conversationId: activeChatId,
            content: text,
            replyTold: replyTarget ? replyTarget.id : null
        })
    })
    .then(res => res.json())
    .then(saved => {
        console.log("SAVED", saved);
        input.value = "";
        replyTarget = null;
        input.placeholder = "Type a message...";
    });
}

async function startConversation(targetUserId) {
    const response = await fetch("/api/conversations/direct", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + getToken()
        },
        body: JSON.stringify({
            targetUserId: targetUserId
        })
    });
    const conversation = await response.json();
    closeUsersModal();
    allChats = [];
    activeChatId = null;
    await loadChats();
    openChat(conversation.conversationId);
}

function formatTime(time) {
    if (!time) return "";
    try {
        let date = new Date(time);
        if (isNaN(date.getTime())) {
            date = new Date(time.replace("T", " "));
        }
        return date.toLocaleTimeString([], {
            hour: "2-digit",
            minute: "2-digit",
            hour12: true
        });
    }
    catch (error) {
        console.error("TIME ERROR", time);
        return "";
    }
}

function escapeHtml(str) {
    if (!str) return "";
    return str.replace(/[&<>]/g, m => ({
        "&": "&amp;",
        "<": "&lt;",
        ">": "&gt;"
    }[m]));
}

window.onload = async function() {
    const token = getToken();
    if (!token) {
        redirectToAuth();
        return;
    }
    await loadCurrentUser();
    await loadChats();
    document.getElementById("sendMessageBtn").onclick = sendMessage;
    document.getElementById("newChatTrigger").onclick = showUsers;
    document.getElementById("messageInput").onkeypress = function(e) {
        if (e.key === "Enter") {
            sendMessage();
        }
    };
};

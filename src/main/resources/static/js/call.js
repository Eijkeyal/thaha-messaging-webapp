let peerConnection = null;
let localStream = null;
let remoteStream = null;
let currentCallType = null;
let pendingOffer = null;
let pendingCandidates = [];

const rtcConfig = {
    iceServers:[
        {
            urls:"stun:stun.l.google.com:19302"
        }
    ]
};

function handleAudioCall(){
    console.log("AUDIO BUTTON CLICK");
    startCall("audio");
}

function handleVideoCall(){
    console.log("VIDEO BUTTON CLICK");
    startCall("video");
}

async function startCall(type){
    currentCallType = type;
    try{
        localStream = await navigator.mediaDevices.getUserMedia({
            audio:true,
            video:type==="video"
        });
        createPeerConnection();
        localStream.getTracks().forEach(track=>{
            peerConnection.addTrack(track, localStream);
        });
        showCallWindow();
        let offer = await peerConnection.createOffer();
        await peerConnection.setLocalDescription(offer);
        sendCallSignal({
            type:"offer",
            offer:offer,
            callType:type
        });
        console.log("OFFER SENT");
    }
    catch(error){
        console.error("START CALL ERROR", error);
    }
}

function createPeerConnection(){
    peerConnection = new RTCPeerConnection(rtcConfig);
    peerConnection.onicecandidate = event=>{
        if(event.candidate){
            sendCallSignal({
                type:"candidate",
                candidate:event.candidate
            });
        }
    };
    peerConnection.ontrack = event=>{
        remoteStream = event.streams[0];
        let remoteVideo = document.getElementById("remoteVideo");
        if(remoteVideo){
            remoteVideo.srcObject = remoteStream;
        }
    };
}

function showIncomingCall(data){
    pendingOffer = data;
    let accept = confirm("Incoming " + data.callType + " call");
    if(accept){
        acceptCall();
    }
}

async function acceptCall(){
    if(!pendingOffer){
        console.log("NO PENDING OFFER");
        return;
    }
    currentCallType = pendingOffer.callType;
    createPeerConnection();
    localStream = await navigator.mediaDevices.getUserMedia({
        audio:true,
        video:currentCallType==="video"
    });
    localStream.getTracks().forEach(track=>{
        peerConnection.addTrack(track, localStream);
    });
    await peerConnection.setRemoteDescription(new RTCSessionDescription(pendingOffer.offer));
    for(let candidate of pendingCandidates){
        await peerConnection.addIceCandidate(new RTCIceCandidate(candidate));
    }
    pendingCandidates = [];
    let answer = await peerConnection.createAnswer();
    await peerConnection.setLocalDescription(answer);
    sendCallSignal({
        type:"answer",
        answer:answer
    });
    showCallWindow();
    console.log("CALL ACCEPTED");
}

async function receiveAnswer(data){
    if(!peerConnection){
        console.log("NO PEER CONNECTION");
        return;
    }
    console.log("CURRENT STATE:", peerConnection.signalingState);
    if(peerConnection.signalingState !== "have-local-offer"){
        console.log("ANSWER IGNORED");
        return;
    }
    try{
        await peerConnection.setRemoteDescription(new RTCSessionDescription(data.answer));
        console.log("ANSWER SET SUCCESS");
    }
    catch(error){
        console.error("ANSWER FAILED", error);
    }
}

async function receiveCandidate(data){
    if(!peerConnection || !peerConnection.remoteDescription){
        pendingCandidates.push(data.candidate);
        console.log("ICE WAITING");
        return;
    }
    await peerConnection.addIceCandidate(new RTCIceCandidate(data.candidate));
}

function handleCallSignal(data){
    console.log("INCOMING CALL SIGNAL", data);
    if(data.type==="offer"){
        showIncomingCall(data);
    }
    else if(data.type==="answer"){
        if(peerConnection && peerConnection.signalingState === "have-local-offer"){
            receiveAnswer(data);
        }
        else{
            console.log("Ignoring answer");
        }
    }
    else if(data.type==="candidate"){
        receiveCandidate(data);
    }
}

function showCallWindow(){
    let window = document.getElementById("callWindow");
    if(window){
        window.classList.remove("hidden");
    }
    let localVideo = document.getElementById("localVideo");
    if(localVideo && localStream){
        localVideo.srcObject = localStream;
    }
}

function endCall(){
    if(localStream){
        localStream.getTracks().forEach(track=>{
            track.stop();
        });
    }
    if(peerConnection){
        peerConnection.close();
    }
    peerConnection = null;
    localStream = null;
    remoteStream = null;
    pendingOffer = null;
    pendingCandidates = [];
    let window = document.getElementById("callWindow");
    if(window){
        window.classList.add("hidden");
    }
}

function sendCallSignal(data){
    let receiverId = getReceiverId();
    if(!receiverId){
        console.log("Receiver missing");
        return;
    }
    stompClient.send("/app/call", {}, JSON.stringify({
        conversationId: activeChatId,
        receiverId: receiverId,
        data: data
    }));
    console.log("CALL SIGNAL SENT", data);
}

function getReceiverId(){
    let chat = allChats.find(c => c.conversationId === activeChatId);
    if(!chat){
        console.error("CHAT NOT FOUND");
        return null;
    }
    return chat.userId;
}

function getReceiverId(){
    let chat = allChats.find(c=>c.conversationId===activeChatId);
    if(!chat){
        console.log("NO ACTIVE CHAT");
        return null;
    }
    console.log("RECEIVER ID", chat.userId);
    return chat.userId;
}

function subscribeCall(){
    stompClient.subscribe("/user/queue/call", message=>{
        let data = JSON.parse(message.body);
        console.log("INCOMING CALL SIGNAL", data);
        handleCallSignal(data);
    });
}

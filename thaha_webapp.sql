-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Jun 24, 2026 at 07:15 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `Chat_App`
--

-- --------------------------------------------------------

--
-- Table structure for table `attachments`
--

CREATE TABLE `attachments` (
  `attachment_id` char(36) NOT NULL,
  `message_id` char(36) NOT NULL,
  `file_name` varchar(255) NOT NULL,
  `file_url` text NOT NULL,
  `file_type` varchar(100) DEFAULT NULL,
  `file_size` bigint(20) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `blocked_users`
--

CREATE TABLE `blocked_users` (
  `block_id` char(36) NOT NULL,
  `blocker_id` char(36) NOT NULL,
  `blocked_id` char(36) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `conversations`
--

CREATE TABLE `conversations` (
  `conversation_id` char(36) NOT NULL,
  `type` varchar(20) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `avatar_url` text DEFAULT NULL,
  `created_by` char(36) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `conversations`
--

INSERT INTO `conversations` (`conversation_id`, `type`, `title`, `avatar_url`, `created_by`, `created_at`, `updated_at`) VALUES
('062a37b1-e83a-49c0-abc0-198f3db927b0', 'DIRECT', 'Rupchandra', NULL, NULL, '2026-06-24 12:00:31', '2026-06-24 12:00:31'),
('3ee7f883-f525-4aff-b9ee-086dc3d7e011', 'DIRECT', 'User', NULL, NULL, '2026-06-24 12:00:33', '2026-06-24 12:00:33'),
('462ff70f-a936-4415-8311-c9bf25a70095', 'DIRECT', 'Jessica', NULL, NULL, '2026-06-24 11:45:06', '2026-06-24 11:45:06'),
('6c90ca2a-b207-4430-91de-025b607de00b', 'DIRECT', 'Hari', NULL, NULL, '2026-06-24 12:00:25', '2026-06-24 12:00:25'),
('6dcf412a-f282-46e8-961f-57b330c4f61e', 'DIRECT', 'Sita', NULL, NULL, '2026-06-24 12:00:37', '2026-06-24 12:00:37'),
('7d78a45a-054f-4049-beef-522442b18b6a', 'DIRECT', 'Domain', NULL, NULL, '2026-06-24 12:00:20', '2026-06-24 12:00:20'),
('ab239bba-d343-42ad-b814-7d73c922750f', 'DIRECT', 'UserA', NULL, NULL, '2026-06-24 12:00:28', '2026-06-24 12:00:28'),
('b86ed287-077b-4614-b848-b1c7c1217a2f', 'DIRECT', 'Test 1', NULL, NULL, '2026-06-24 12:00:22', '2026-06-24 12:00:22'),
('f53e0936-3a72-4aca-97a8-4b7bea83b7ed', 'DIRECT', 'GopiKrishna', NULL, NULL, '2026-06-24 12:00:17', '2026-06-24 12:00:17'),
('fb63f33c-4cd8-48e3-8cca-ba427e29bf02', 'DIRECT', 'Ram', NULL, NULL, '2026-06-24 11:48:34', '2026-06-24 11:48:34');

-- --------------------------------------------------------

--
-- Table structure for table `conversation_participants`
--

CREATE TABLE `conversation_participants` (
  `participant_id` char(36) NOT NULL,
  `conversation_id` char(36) NOT NULL,
  `user_id` char(36) NOT NULL,
  `role` varchar(20) DEFAULT 'MEMBER',
  `joined_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `last_read_at` timestamp NULL DEFAULT NULL,
  `is_muted` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `conversation_participants`
--

INSERT INTO `conversation_participants` (`participant_id`, `conversation_id`, `user_id`, `role`, `joined_at`, `last_read_at`, `is_muted`) VALUES
('00444c50-4e0d-4268-bf8a-cc016d95308a', '3ee7f883-f525-4aff-b9ee-086dc3d7e011', 'f8eb4743-1fa2-427c-8073-9d4fd5af569a', 'MEMBER', '2026-06-24 12:00:33', NULL, 0),
('01dbdbc1-b687-4964-b107-bd751e06fc1e', '462ff70f-a936-4415-8311-c9bf25a70095', 'f8eb4743-1fa2-427c-8073-9d4fd5af569a', 'MEMBER', '2026-06-24 11:45:06', NULL, 0),
('0814d84f-ee5c-4953-9db8-632611803700', 'ab239bba-d343-42ad-b814-7d73c922750f', '7de5c0bd-b6eb-4952-a41f-7740caef5109', 'MEMBER', '2026-06-24 12:00:28', NULL, 0),
('10699211-e730-4424-bb7c-1992433632fa', 'fb63f33c-4cd8-48e3-8cca-ba427e29bf02', '76f023d5-e28b-4d6a-acc4-881b01d4c84c', 'MEMBER', '2026-06-24 11:48:34', NULL, 0),
('20478c60-1004-4bea-bc2c-903ec9de2a74', '6dcf412a-f282-46e8-961f-57b330c4f61e', 'cec04bf0-3c1e-45c4-b0da-6f49750fef0a', 'MEMBER', '2026-06-24 12:00:37', NULL, 0),
('6e0cf671-a04e-4312-a014-d26db5a8630b', '7d78a45a-054f-4049-beef-522442b18b6a', '3a66f3b7-6549-4f73-8ee3-59bb1ea8d96d', 'MEMBER', '2026-06-24 12:00:20', NULL, 0),
('8c474f6b-fa9b-49f2-85c9-b73fc45c79dd', '062a37b1-e83a-49c0-abc0-198f3db927b0', '98bcb235-c5ff-4c5b-a41e-18ee716917ba', 'MEMBER', '2026-06-24 12:00:31', NULL, 0),
('8d90d2c7-5ea0-4c41-97ad-c55fd647e467', '6c90ca2a-b207-4430-91de-025b607de00b', 'f8eb4743-1fa2-427c-8073-9d4fd5af569a', 'MEMBER', '2026-06-24 12:00:25', NULL, 0),
('8ec8d5b0-fdf2-45be-bd0f-784b19eead00', '062a37b1-e83a-49c0-abc0-198f3db927b0', 'f8eb4743-1fa2-427c-8073-9d4fd5af569a', 'MEMBER', '2026-06-24 12:00:31', NULL, 0),
('9e1d26a1-8609-4bfd-bda4-69fc5e335684', 'ab239bba-d343-42ad-b814-7d73c922750f', 'f8eb4743-1fa2-427c-8073-9d4fd5af569a', 'MEMBER', '2026-06-24 12:00:28', NULL, 0),
('a7c56a48-27ed-437d-8ff5-30f00ccda81f', '3ee7f883-f525-4aff-b9ee-086dc3d7e011', 'abc95b62-9d0c-449a-a6e8-df4a4dc04f87', 'MEMBER', '2026-06-24 12:00:33', NULL, 0),
('ba393cff-6749-4790-acb8-0f6f3067b5c0', 'f53e0936-3a72-4aca-97a8-4b7bea83b7ed', '0eaaa8ab-55a3-426d-bbd5-40b487e669fc', 'MEMBER', '2026-06-24 12:00:17', NULL, 0),
('c17affce-e85b-4930-93b0-ba1d237c6e44', 'fb63f33c-4cd8-48e3-8cca-ba427e29bf02', '7f8c6926-fb1e-4b4d-9080-0212466bda8e', 'MEMBER', '2026-06-24 11:48:34', NULL, 0),
('c1bf3803-8bf9-405a-8eb6-f7412737be80', '6c90ca2a-b207-4430-91de-025b607de00b', '76f023d5-e28b-4d6a-acc4-881b01d4c84c', 'MEMBER', '2026-06-24 12:00:25', NULL, 0),
('c989a34a-ae94-4ae6-b3c1-aa6bd460a1b1', '462ff70f-a936-4415-8311-c9bf25a70095', 'f8dc4e9d-e3a5-4388-aa29-1f8e4bb130fc', 'MEMBER', '2026-06-24 11:45:06', NULL, 0),
('dceff4f2-ffd8-429c-9f7e-59740a8eb7de', '7d78a45a-054f-4049-beef-522442b18b6a', 'f8eb4743-1fa2-427c-8073-9d4fd5af569a', 'MEMBER', '2026-06-24 12:00:20', NULL, 0),
('e73dcd7e-0046-401a-816c-64cff5032a69', 'f53e0936-3a72-4aca-97a8-4b7bea83b7ed', 'f8eb4743-1fa2-427c-8073-9d4fd5af569a', 'MEMBER', '2026-06-24 12:00:17', NULL, 0),
('f3109256-1f3e-4a2a-a215-3408d7a04298', '6dcf412a-f282-46e8-961f-57b330c4f61e', 'f8eb4743-1fa2-427c-8073-9d4fd5af569a', 'MEMBER', '2026-06-24 12:00:37', NULL, 0),
('fd6fd312-9692-49b7-b1a9-c3324286b42a', 'b86ed287-077b-4614-b848-b1c7c1217a2f', 'f8eb4743-1fa2-427c-8073-9d4fd5af569a', 'MEMBER', '2026-06-24 12:00:22', NULL, 0),
('fdade830-e4d5-4a31-9e09-a3c30306038d', 'b86ed287-077b-4614-b848-b1c7c1217a2f', '5f7f801d-77c1-4771-b10c-1aeea85de187', 'MEMBER', '2026-06-24 12:00:22', NULL, 0);

-- --------------------------------------------------------

--
-- Table structure for table `messages`
--

CREATE TABLE `messages` (
  `message_id` char(36) NOT NULL,
  `conversation_id` char(36) NOT NULL,
  `sender_id` char(36) NOT NULL,
  `content` text DEFAULT NULL,
  `message_type` varchar(20) DEFAULT 'TEXT',
  `reply_to_id` char(36) DEFAULT NULL,
  `is_edited` tinyint(1) DEFAULT 0,
  `edited_at` timestamp NULL DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT 0,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `reply_to_message_id` varchar(255) DEFAULT NULL,
  `reply_to_content` text DEFAULT NULL,
  `is_delivered` tinyint(1) DEFAULT NULL,
  `is_seen` tinyint(1) DEFAULT NULL,
  `seen_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `messages`
--

INSERT INTO `messages` (`message_id`, `conversation_id`, `sender_id`, `content`, `message_type`, `reply_to_id`, `is_edited`, `edited_at`, `is_deleted`, `created_at`, `updated_at`, `reply_to_message_id`, `reply_to_content`, `is_delivered`, `is_seen`, `seen_at`) VALUES
('0490e7ea-64bf-4c86-abb6-fe5bae49141c', '062a37b1-e83a-49c0-abc0-198f3db927b0', '98bcb235-c5ff-4c5b-a41e-18ee716917ba', 'oi', 'TEXT', NULL, NULL, NULL, NULL, '2026-06-24 16:47:28', '2026-06-24 16:47:28', NULL, NULL, 0, 0, '2026-06-24 16:47:28'),
('30bc2ede-e206-4770-ab2f-636c444e764c', '062a37b1-e83a-49c0-abc0-198f3db927b0', '98bcb235-c5ff-4c5b-a41e-18ee716917ba', 'haha', 'TEXT', NULL, NULL, NULL, NULL, '2026-06-24 16:48:24', '2026-06-24 16:48:24', NULL, NULL, 0, 0, '2026-06-24 16:48:24'),
('5d049c80-b7b3-4310-9cbe-ac8440732005', '462ff70f-a936-4415-8311-c9bf25a70095', 'f8dc4e9d-e3a5-4388-aa29-1f8e4bb130fc', 'hello]', 'TEXT', NULL, NULL, NULL, NULL, '2026-06-24 11:59:59', '2026-06-24 11:59:59', NULL, NULL, 0, 0, '2026-06-24 11:59:59'),
('731ccf60-85ab-41d7-a80a-401db36fbc93', '462ff70f-a936-4415-8311-c9bf25a70095', 'f8dc4e9d-e3a5-4388-aa29-1f8e4bb130fc', 'where thaha for everyone', 'TEXT', NULL, NULL, NULL, NULL, '2026-06-24 12:01:46', '2026-06-24 12:01:46', NULL, NULL, 0, 0, '2026-06-24 12:01:46'),
('a81630da-891a-49b4-b2ef-c634e7929a8a', '462ff70f-a936-4415-8311-c9bf25a70095', 'f8eb4743-1fa2-427c-8073-9d4fd5af569a', 'haha', 'TEXT', NULL, NULL, NULL, NULL, '2026-06-24 12:00:08', '2026-06-24 12:00:08', NULL, NULL, 0, 0, '2026-06-24 12:00:08'),
('d0c28b87-c82f-4813-9a0d-0ad61707155a', '462ff70f-a936-4415-8311-c9bf25a70095', 'f8dc4e9d-e3a5-4388-aa29-1f8e4bb130fc', 'thaha Messaging app', 'TEXT', NULL, NULL, NULL, NULL, '2026-06-24 12:01:22', '2026-06-24 12:01:22', NULL, NULL, 0, 0, '2026-06-24 12:01:22'),
('db7d0062-f932-4053-be08-9dc300a5b127', '062a37b1-e83a-49c0-abc0-198f3db927b0', 'f8eb4743-1fa2-427c-8073-9d4fd5af569a', 'k', 'TEXT', NULL, NULL, NULL, NULL, '2026-06-24 16:48:16', '2026-06-24 16:48:16', NULL, NULL, 0, 0, '2026-06-24 16:48:16'),
('ebc399af-6fd0-4c46-8f95-854d6bef3df7', '462ff70f-a936-4415-8311-c9bf25a70095', 'f8eb4743-1fa2-427c-8073-9d4fd5af569a', 'Testing 123', 'TEXT', NULL, NULL, NULL, NULL, '2026-06-24 12:00:59', '2026-06-24 12:00:59', NULL, NULL, 0, 0, '2026-06-24 12:00:59');

-- --------------------------------------------------------

--
-- Table structure for table `message_deletions`
--

CREATE TABLE `message_deletions` (
  `deletion_id` char(36) NOT NULL,
  `message_id` char(36) NOT NULL,
  `user_id` char(36) NOT NULL,
  `deleted_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `message_reactions`
--

CREATE TABLE `message_reactions` (
  `reaction_id` char(36) NOT NULL,
  `message_id` char(36) NOT NULL,
  `user_id` char(36) NOT NULL,
  `reaction` varchar(20) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `message_status`
--

CREATE TABLE `message_status` (
  `status_id` char(36) NOT NULL,
  `message_id` char(36) NOT NULL,
  `user_id` char(36) NOT NULL,
  `status` varchar(20) NOT NULL,
  `delivered_at` timestamp NULL DEFAULT NULL,
  `read_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` char(36) NOT NULL,
  `username` varchar(50) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `full_name` varchar(100) DEFAULT NULL,
  `avatar_url` text DEFAULT NULL,
  `bio` text DEFAULT NULL,
  `status` varchar(20) DEFAULT 'offline',
  `last_seen` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `username`, `email`, `password_hash`, `full_name`, `avatar_url`, `bio`, `status`, `last_seen`, `created_at`, `updated_at`) VALUES
('0eaaa8ab-55a3-426d-bbd5-40b487e669fc', 'GopiKrishna', 'gopi@gmail.com', '$2a$10$xhBGB.9fxwvLK8uQtFT7WeTdaxXuhWh.0EDbIdkDHFgzsh5hMPR46', NULL, NULL, NULL, 'offline', NULL, '2026-06-24 11:52:14', '2026-06-24 11:52:14'),
('3a66f3b7-6549-4f73-8ee3-59bb1ea8d96d', 'Domain', 'domain@gmail.com', '$2a$10$d340/bMxVxp016FYrMVo3Og4NKTtYZiX1b1JORRochTL5qf3mIPx6', NULL, NULL, NULL, 'offline', NULL, '2026-06-24 11:55:30', '2026-06-24 11:55:30'),
('5f7f801d-77c1-4771-b10c-1aeea85de187', 'Test 1', 'test@gmail.com', '$2a$10$SjxYThyhHypa7lgYpLw0V.PX/wql6F5eTyupRloQ2w9QBwMeckpeq', NULL, NULL, NULL, 'offline', NULL, '2026-06-24 11:50:04', '2026-06-24 11:50:04'),
('76f023d5-e28b-4d6a-acc4-881b01d4c84c', 'Hari', 'hari@gmail.com', '$2a$10$FtPS6micOPIlbYvkSTd4HOGLxyVksw6pSMVNKFVYKtq.urSjj/p8q', NULL, NULL, NULL, 'offline', NULL, '2026-06-24 11:48:08', '2026-06-24 11:48:08'),
('7de5c0bd-b6eb-4952-a41f-7740caef5109', 'UserA', 'user@gmail.com', '$2a$10$3hTfUyxc7jJWrTxOnEKP0.oasNd.jQTzqMFx9t8c35tgnCHehY59y', NULL, NULL, NULL, 'offline', NULL, '2026-06-24 11:49:10', '2026-06-24 11:49:10'),
('7f8c6926-fb1e-4b4d-9080-0212466bda8e', 'Ram', 'ram@gmail.com', '$2a$10$9Y4FRNP.5PwLVA1gbzZ5HeA7M/SNMNOjQHw1xn0U/iLyFBL4hIUAO', NULL, NULL, NULL, 'offline', NULL, '2026-06-24 11:45:51', '2026-06-24 11:45:51'),
('98bcb235-c5ff-4c5b-a41e-18ee716917ba', 'Rupchandra', 'rupchandra@gmail.com', '$2a$10$5tSx99ZA8cHGdoDOhYa3quWR/pyjeidg2Hhj4QWuv2yAuTjHobwG6', NULL, NULL, NULL, 'offline', NULL, '2026-06-24 11:56:16', '2026-06-24 11:56:16'),
('abc95b62-9d0c-449a-a6e8-df4a4dc04f87', 'User', 'userB@gmail.com', '$2a$10$l96sIIB.yI7U.ZZQWDGjCeAI03TTkx5YH.bZ5W4NxzRuj8jZEgu8O', NULL, NULL, NULL, 'offline', NULL, '2026-06-24 11:51:02', '2026-06-24 11:51:02'),
('cec04bf0-3c1e-45c4-b0da-6f49750fef0a', 'Sita', 'sita@gmail.com', '$2a$10$lU78OPiSe695fdaqxnNwqu7isEFCvJPEd6YRa.zREawYDy4ShXxla', NULL, NULL, NULL, 'offline', NULL, '2026-06-24 11:47:29', '2026-06-24 11:47:29'),
('f8dc4e9d-e3a5-4388-aa29-1f8e4bb130fc', 'Eijkeyal', 'pakhrineijkeya@gmail.com', '$2a$10$ljRAjpAnKJd/QtPB4Itea.Oc/hZoMc2V6Zja7rZmBDcsepRt7NPQO', NULL, NULL, NULL, 'offline', NULL, '2026-06-24 11:44:37', '2026-06-24 11:44:37'),
('f8eb4743-1fa2-427c-8073-9d4fd5af569a', 'Jessica', 'pakhrineijkeya7@gmail.com', '$2a$10$ZvBoSseyNsBbixBhYm0M1uf3OgwuALwPhZUPoWa8pwoxkhCwOKAo2', NULL, NULL, NULL, 'offline', NULL, '2026-06-24 11:42:16', '2026-06-24 11:42:16');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `attachments`
--
ALTER TABLE `attachments`
  ADD PRIMARY KEY (`attachment_id`),
  ADD KEY `message_id` (`message_id`);

--
-- Indexes for table `blocked_users`
--
ALTER TABLE `blocked_users`
  ADD PRIMARY KEY (`block_id`),
  ADD UNIQUE KEY `blocker_id` (`blocker_id`,`blocked_id`),
  ADD KEY `blocked_id` (`blocked_id`);

--
-- Indexes for table `conversations`
--
ALTER TABLE `conversations`
  ADD PRIMARY KEY (`conversation_id`),
  ADD KEY `created_by` (`created_by`);

--
-- Indexes for table `conversation_participants`
--
ALTER TABLE `conversation_participants`
  ADD PRIMARY KEY (`participant_id`),
  ADD UNIQUE KEY `conversation_id` (`conversation_id`,`user_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `messages`
--
ALTER TABLE `messages`
  ADD PRIMARY KEY (`message_id`),
  ADD KEY `conversation_id` (`conversation_id`),
  ADD KEY `sender_id` (`sender_id`),
  ADD KEY `reply_to_id` (`reply_to_id`);

--
-- Indexes for table `message_deletions`
--
ALTER TABLE `message_deletions`
  ADD PRIMARY KEY (`deletion_id`),
  ADD UNIQUE KEY `message_id` (`message_id`,`user_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `message_reactions`
--
ALTER TABLE `message_reactions`
  ADD PRIMARY KEY (`reaction_id`),
  ADD UNIQUE KEY `message_id` (`message_id`,`user_id`,`reaction`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `message_status`
--
ALTER TABLE `message_status`
  ADD PRIMARY KEY (`status_id`),
  ADD UNIQUE KEY `message_id` (`message_id`,`user_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `attachments`
--
ALTER TABLE `attachments`
  ADD CONSTRAINT `attachments_ibfk_1` FOREIGN KEY (`message_id`) REFERENCES `messages` (`message_id`);

--
-- Constraints for table `blocked_users`
--
ALTER TABLE `blocked_users`
  ADD CONSTRAINT `blocked_users_ibfk_1` FOREIGN KEY (`blocker_id`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `blocked_users_ibfk_2` FOREIGN KEY (`blocked_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `conversations`
--
ALTER TABLE `conversations`
  ADD CONSTRAINT `conversations_ibfk_1` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `conversation_participants`
--
ALTER TABLE `conversation_participants`
  ADD CONSTRAINT `conversation_participants_ibfk_1` FOREIGN KEY (`conversation_id`) REFERENCES `conversations` (`conversation_id`),
  ADD CONSTRAINT `conversation_participants_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `messages`
--
ALTER TABLE `messages`
  ADD CONSTRAINT `messages_ibfk_1` FOREIGN KEY (`conversation_id`) REFERENCES `conversations` (`conversation_id`),
  ADD CONSTRAINT `messages_ibfk_2` FOREIGN KEY (`sender_id`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `messages_ibfk_3` FOREIGN KEY (`reply_to_id`) REFERENCES `messages` (`message_id`);

--
-- Constraints for table `message_deletions`
--
ALTER TABLE `message_deletions`
  ADD CONSTRAINT `message_deletions_ibfk_1` FOREIGN KEY (`message_id`) REFERENCES `messages` (`message_id`),
  ADD CONSTRAINT `message_deletions_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `message_reactions`
--
ALTER TABLE `message_reactions`
  ADD CONSTRAINT `message_reactions_ibfk_1` FOREIGN KEY (`message_id`) REFERENCES `messages` (`message_id`),
  ADD CONSTRAINT `message_reactions_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `message_status`
--
ALTER TABLE `message_status`
  ADD CONSTRAINT `message_status_ibfk_1` FOREIGN KEY (`message_id`) REFERENCES `messages` (`message_id`),
  ADD CONSTRAINT `message_status_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

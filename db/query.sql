CREATE DATABASE IF NOT EXISTS `dvach_db`;
USE `dvach_db`;

CREATE TABLE `section`
(
    `id`      int                                     NOT NULL AUTO_INCREMENT,
    `name`    varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `article` varchar(3) COLLATE utf8mb4_unicode_ci   NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `name_UNIQUE` (`name`),
    UNIQUE KEY `ID_UNIQUE` (`id`),
    UNIQUE KEY `article_UNIQUE` (`article`)
);

CREATE TABLE `topic`
(
    `id`              int                                     NOT NULL AUTO_INCREMENT,
    `name`            varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `section_article` varchar(3) COLLATE utf8mb4_unicode_ci   NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `id_UNIQUE` (`id`),
    KEY `topic_ibfk_1` (`section_article`),
    CONSTRAINT `topic_ibfk_1` FOREIGN KEY (`section_article`) REFERENCES `section` (`article`)
);

CREATE TABLE `post`
(
    `id`       int                             NOT NULL AUTO_INCREMENT,
    `content`  text COLLATE utf8mb4_unicode_ci NOT NULL,
    `topic_id` int                             NOT NULL,
    `userIP`   varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `datetime` datetime                        NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `id_UNIQUE` (`id`),
    KEY `post_ibfk_1` (`topic_id`),
    CONSTRAINT `post_ibfk_1` FOREIGN KEY (`topic_id`) REFERENCES `topic` (`id`)
);

CREATE TABLE `file`
(
    `id`      int                                     NOT NULL AUTO_INCREMENT,
    `name`    varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `type`    varchar(10) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `post_id` int                                     NOT NULL,
    `size`    int                                     NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `id_UNIQUE` (`id`),
    KEY `file_ibfk_1` (`post_id`),
    CONSTRAINT `file_ibfk_1` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)
);
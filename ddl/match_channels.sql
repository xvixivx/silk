CREATE TABLE `match_channels` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `guild_id` bigint(20) NOT NULL,
  `channel_id` bigint(20) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `region` varchar(64) DEFAULT NULL,
  `platform` varchar(10) DEFAULT NULL,
  `game_type` varchar(10) DEFAULT NULL,
  `receive` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `match_channels_UN` (`guild_id`,`channel_id`),
  CONSTRAINT `match_channels_guilds_FK` FOREIGN KEY (`guild_id`) REFERENCES `guilds` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=124 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
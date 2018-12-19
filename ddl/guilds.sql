CREATE TABLE `guilds` (
  `id` bigint(20) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `region` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
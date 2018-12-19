CREATE TABLE `agents` (
  `name` varchar(64) NOT NULL,
  `display_name` varchar(64) DEFAULT NULL,
  `role` varchar(64) DEFAULT NULL,
  `weapon` varchar(64) DEFAULT NULL,
  `alternate_action` varchar(64) DEFAULT NULL,
  `ability` varchar(64) DEFAULT NULL,
  `ultimate_ability` varchar(64) DEFAULT NULL,
  `damage` int(11) DEFAULT NULL,
  `health` int(11) DEFAULT NULL,
  `image` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
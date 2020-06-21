DROP TABLE IF EXISTS `flyway_schema_history`;
CREATE TABLE `flyway_schema_history`  (
  `installed_rank` int(11) NOT NULL,
  `version` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `script` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `checksum` int(11) NULL DEFAULT NULL,
  `installed_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `installed_on` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `execution_time` int(11) NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`installed_rank`) USING BTREE
) ;


-- 如果是已执行的项目需要添加初始化版本进行跳过
INSERT INTO `flyway_schema_history`(`installed_rank`, `version`, `description`, `type`, `script`, `checksum`, `installed_by`, `installed_on`, `execution_time`, `success`) VALUES (1, '2020061901', 'create-mysql', 'SQL', 'V2020061901__create-mysql.sql', 835502886, 'root', '2020-06-19 10:02:50', 3837, 1);
INSERT INTO `flyway_schema_history`(`installed_rank`, `version`, `description`, `type`, `script`, `checksum`, `installed_by`, `installed_on`, `execution_time`, `success`) VALUES (2, '2020061902', 'create-quarz', 'SQL', 'V2020061902__create-quarz.sql', 390126216, 'root', '2020-06-19 10:03:02', 11618, 1);
INSERT INTO `flyway_schema_history`(`installed_rank`, `version`, `description`, `type`, `script`, `checksum`, `installed_by`, `installed_on`, `execution_time`, `success`) VALUES (3, '2020061903', 'insert mysql', 'SQL', 'V2020061903__insert_mysql.sql', 1065035189, 'root', '2020-06-19 10:03:02', 126, 1);

commit;
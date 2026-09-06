/*
 Navicat Premium Dump SQL

 Source Server         : 阿里云
 Source Server Type    : MySQL
 Source Server Version : 80034 (8.0.34)
 Source Host           : rm-2zej93vs9m24a384w9o.mysql.rds.aliyuncs.com:3306
 Source Schema         : bgp

 Target Server Type    : MySQL
 Target Server Version : 80034 (8.0.34)
 File Encoding         : 65001

 Date: 26/11/2024 11:40:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for mserver
-- ----------------------------
DROP TABLE IF EXISTS `mserver`;
CREATE TABLE `mserver`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `mserver` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `serialnumber` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `ip` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `cpu` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `cpucount` int NULL DEFAULT NULL,
  `thread` int NULL DEFAULT NULL,
  `memory` bigint NULL DEFAULT NULL,
  `systemserver` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `systemversion` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `storage` bigint NULL DEFAULT NULL,
  `nic` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '网卡',
  `isworking` int NULL DEFAULT NULL,
  `create_id` int NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_id` int NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `status` int NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of mserver
-- ----------------------------
INSERT INTO `mserver` VALUES (1, '服务器-1', 'ASDSSSDFSSSDF', 'ubuntu', NULL, '82.156.35.224', 'Intel Xeone 52699v4', 4, 8, 8, 'Ubuntu', '20.04', 60, NULL, 0, NULL, NULL, NULL, NULL, 1);
INSERT INTO `mserver` VALUES (2, '仿真服务器21', '02A6AXH23A000201', 'ubuntu', NULL, '10.146.4.40', 'Intel(R) Xeon(R) Silver 4316', 20, 80, 256, 'Ubuntu', '20.04', 32000, NULL, 0, NULL, NULL, NULL, NULL, 1);
INSERT INTO `mserver` VALUES (3, '仿真服务器22', '02A6AXH23A000202', 'ubuntu', NULL, '10.146.4.41', 'Intel(R) Xeon(R) Silver 4316', 20, 80, 256, 'Ubuntu', '20.04', 32000, NULL, 0, NULL, NULL, NULL, NULL, 1);
INSERT INTO `mserver` VALUES (4, '仿真服务器23', '02A6AXH23A000203', 'ubuntu', NULL, '10.146.4.42', 'Intel(R) Xeon(R) Silver 4316', 20, 80, 256, 'Ubuntu', '20.04', 32000, NULL, 0, NULL, NULL, NULL, NULL, 1);
INSERT INTO `mserver` VALUES (5, '仿真服务器24', '02A6AXH23A000240', 'ubuntu', NULL, '10.146.4.43', 'Intel(R) Xeon(R) Silver 4316', 20, 80, 256, 'Ubuntu', '20.04', 32000, NULL, 0, NULL, NULL, NULL, NULL, 1);
INSERT INTO `mserver` VALUES (6, '仿真服务器25', '02A6AXH23A000211', 'ubuntu', NULL, '10.146.4.44', 'Intel(R) Xeon(R) Silver 4316', 20, 80, 256, 'Ubuntu', '20.04', 32000, NULL, 0, NULL, NULL, NULL, NULL, 1);
INSERT INTO `mserver` VALUES (7, '仿真服务器26', '02A6AXH23A000175', 'ubuntu', NULL, '10.146.4.45', 'Intel(R) Xeon(R) Silver 4316', 20, 80, 256, 'Ubuntu', '20.04', 32000, NULL, 0, NULL, NULL, NULL, NULL, 1);
INSERT INTO `mserver` VALUES (8, '仿真服务器27', '02A6AXH23A000176', 'ubuntu', NULL, '10.146.4.46', 'Intel(R) Xeon(R) Silver 4316', 20, 80, 256, 'Ubuntu', '20.04', 32000, NULL, 0, NULL, NULL, NULL, NULL, 1);
INSERT INTO `mserver` VALUES (9, '仿真服务器28', '02A6AXH23A000180', 'ubuntu', NULL, '10.146.4.47', 'Intel(R) Xeon(R) Silver 4316', 20, 80, 256, 'Ubuntu', '20.04', 32000, NULL, 0, NULL, NULL, NULL, NULL, 1);
INSERT INTO `mserver` VALUES (10, '仿真服务器29', '02A6AXH23A000181', 'ubuntu', NULL, '10.146.4.48', 'Intel(R) Xeon(R) Silver 4316', 20, 80, 256, 'Ubuntu', '20.04', 32000, NULL, 0, NULL, NULL, NULL, NULL, 1);
INSERT INTO `mserver` VALUES (11, '仿真服务器30', '02A6AXH23A000196', 'ubuntu', NULL, '10.146.4.49', 'Intel(R) Xeon(R) Silver 4316', 20, 80, 256, 'Ubuntu', '20.04', 32000, NULL, 0, NULL, NULL, NULL, NULL, 1);
INSERT INTO `mserver` VALUES (12, '仿真服务器31', '02A6AXH23A000197', 'ubuntu', NULL, '10.146.4.50', 'Intel(R) Xeon(R) Silver 4316', 20, 80, 256, 'Ubuntu', '20.04', 32000, NULL, 0, NULL, NULL, NULL, NULL, 1);
INSERT INTO `mserver` VALUES (13, '仿真服务器32', '02A6AXH23A000198', 'ubuntu', NULL, '10.146.4.51', 'Intel(R) Xeon(R) Silver 4316', 20, 80, 256, 'Ubuntu', '20.04', 32000, NULL, 0, NULL, NULL, NULL, NULL, 1);
INSERT INTO `mserver` VALUES (14, '仿真服务器33', '02A6AXH23A000199', 'ubuntu', NULL, '10.146.4.52', 'Intel(R) Xeon(R) Silver 4316', 20, 80, 256, 'Ubuntu', '20.04', 32000, NULL, 0, NULL, NULL, NULL, NULL, 1);
INSERT INTO `mserver` VALUES (15, '仿真服务器34', '02A6AXH23A000200', 'ubuntu', NULL, '10.146.4.53', 'Intel(R) Xeon(R) Silver 4316', 20, 80, 256, 'Ubuntu', '20.04', 32000, NULL, 0, NULL, NULL, NULL, NULL, 1);
INSERT INTO `mserver` VALUES (16, '仿真服务器35', '02A6AXH23A000211', 'ubuntu', NULL, '10.146.4.54', 'Intel(R) Xeon(R) Silver 4316', 20, 80, 256, 'Ubuntu', '20.04', 32000, NULL, 0, NULL, NULL, NULL, NULL, 1);
INSERT INTO `mserver` VALUES (17, '仿真服务器36', '02A6AXH23A000276', 'ubuntu', NULL, '10.146.4.55', 'Intel(R) Xeon(R) Silver 4316', 20, 80, 256, 'Ubuntu', '20.04', 32000, NULL, 0, NULL, NULL, NULL, NULL, 1);
INSERT INTO `mserver` VALUES (18, '仿真服务器37', '02A6AXH23A000157', 'ubuntu', NULL, '10.146.4.56', 'Intel(R) Xeon(R) Silver 4316', 20, 80, 256, 'Ubuntu', '20.04', 32000, NULL, 0, NULL, NULL, NULL, NULL, 1);
INSERT INTO `mserver` VALUES (19, '仿真服务器38', '02A6AXH23A000138', 'ubuntu', NULL, '10.146.4.57', 'Intel(R) Xeon(R) Silver 4316', 20, 80, 256, 'Ubuntu', '20.04', 32000, NULL, 0, NULL, NULL, NULL, NULL, 1);
INSERT INTO `mserver` VALUES (20, '仿真服务器39', '02A6AXH23A000159', 'ubuntu', NULL, '10.146.4.58', 'Intel(R) Xeon(R) Silver 4316', 20, 80, 256, 'Ubuntu', '20.04', 32000, NULL, 0, NULL, NULL, NULL, NULL, 1);
INSERT INTO `mserver` VALUES (21, '仿真服务器40', '02A6AXH23A000166', 'ubuntu', NULL, '10.146.4.59', 'Intel(R) Xeon(R) Silver 4316', 20, 80, 256, 'Ubuntu', '20.04', 32000, NULL, 0, NULL, NULL, NULL, NULL, 1);
INSERT INTO `mserver` VALUES (22, '仿真服务器41', '02A6AXH23A000167', 'ubuntu', NULL, '10.146.4.60', 'Intel(R) Xeon(R) Silver 4316', 20, 80, 256, 'Ubuntu', '20.04', 32000, NULL, 0, NULL, NULL, NULL, NULL, 1);
INSERT INTO `mserver` VALUES (23, '仿真服务器42', '02A6AXH23A000169', 'ubuntu', NULL, '10.146.4.61', 'Intel(R) Xeon(R) Silver 4316', 20, 80, 256, 'Ubuntu', '20.04', 32000, NULL, 0, NULL, NULL, NULL, NULL, 1);
INSERT INTO `mserver` VALUES (24, '仿真服务器43', '02A6AXH23A000171', 'ubuntu', NULL, '10.146.4.62', 'Intel(R) Xeon(R) Silver 4316', 20, 80, 256, 'Ubuntu', '20.04', 32000, NULL, 0, NULL, NULL, NULL, NULL, 1);
INSERT INTO `mserver` VALUES (25, '仿真服务器44', '02A6AXH23A000107', 'ubuntu', NULL, '10.146.4.63', 'Intel(R) Xeon(R) Silver 4316', 20, 80, 256, 'Ubuntu', '20.04', 32000, NULL, 0, NULL, NULL, NULL, NULL, 1);
INSERT INTO `mserver` VALUES (26, '仿真服务器45', '02A6AXH23A000158', 'ubuntu', NULL, '10.146.4.64', 'Intel(R) Xeon(R) Silver 4316', 20, 80, 256, 'Ubuntu', '20.04', 32000, NULL, 0, NULL, NULL, NULL, NULL, 1);

SET FOREIGN_KEY_CHECKS = 1;

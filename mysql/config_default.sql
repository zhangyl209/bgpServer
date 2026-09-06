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

 Date: 26/11/2024 11:38:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_default
-- ----------------------------
DROP TABLE IF EXISTS `config_default`;
CREATE TABLE `config_default`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `config` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `table` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `type` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `inparamsdb` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `status` int NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of config_default
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;

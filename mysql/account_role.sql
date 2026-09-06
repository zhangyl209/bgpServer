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

 Date: 26/11/2024 11:39:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account_role
-- ----------------------------
DROP TABLE IF EXISTS `account_role`;
CREATE TABLE `account_role`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `account` int NULL DEFAULT NULL,
  `role` int NULL DEFAULT NULL,
  `status` int NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of account_role
-- ----------------------------
INSERT INTO `account_role` VALUES (1, 1, 1, 1);
INSERT INTO `account_role` VALUES (2, 2, 2, 1);
INSERT INTO `account_role` VALUES (3, 3, 1, 1);
INSERT INTO `account_role` VALUES (4, 3, 2, 1);
INSERT INTO `account_role` VALUES (5, 5, 1, 1);
INSERT INTO `account_role` VALUES (6, 7, 1, 0);
INSERT INTO `account_role` VALUES (7, 7, 2, 0);
INSERT INTO `account_role` VALUES (8, 5, 2, 0);
INSERT INTO `account_role` VALUES (9, 1, 2, 0);

SET FOREIGN_KEY_CHECKS = 1;

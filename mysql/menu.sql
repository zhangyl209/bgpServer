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

 Date: 26/11/2024 11:40:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `path` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `component` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `redirect` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `parent` int NULL DEFAULT NULL,
  `meta` int NULL DEFAULT NULL,
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `status` int NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 505 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES (1, 'Dashboard', '/dashboard', 'LAYOUT', '/dashboard/analysis', NULL, 1, NULL, 0);
INSERT INTO `menu` VALUES (2, 'Analysis', '/dashboard/analysis', '/dashboard/analysis/index', NULL, 1, 2, NULL, 1);
INSERT INTO `menu` VALUES (3, 'Workbench', '/dashboard/workbench', '/dashboard/workbench/index', NULL, 1, 3, NULL, 1);
INSERT INTO `menu` VALUES (8, 'Aframe', '/aframe', 'LAYOUT', '', NULL, 8, NULL, 0);
INSERT INTO `menu` VALUES (9, 'Physical', 'http://192.168.0.118:5173/#/aframe', 'IFrame', NULL, 8, 9, NULL, 1);
INSERT INTO `menu` VALUES (10, 'Image', '/aframe/image', '/aframe/image/index', NULL, 8, 10, NULL, 1);
INSERT INTO `menu` VALUES (11, 'Image', '/aframe/babylon', '/babylon/index', NULL, 8, 10, NULL, 1);
INSERT INTO `menu` VALUES (100, 'System', '/system', 'LAYOUT', '/system/dept', NULL, 100, 9, 1);
INSERT INTO `menu` VALUES (101, 'DeptManagement', 'dept', '/system-admin/dept/index', NULL, 100, 101, NULL, 0);
INSERT INTO `menu` VALUES (102, 'AccountManagement', 'account', '/system-admin/account/index', NULL, 100, 102, NULL, 1);
INSERT INTO `menu` VALUES (103, 'AccountDetail', 'account_detail/:id', '/system-admin/account/AccountDetail', NULL, 100, 103, NULL, 1);
INSERT INTO `menu` VALUES (200, 'Resource', '/resource', 'LAYOUT', '/resource/list', NULL, 200, 3, 1);
INSERT INTO `menu` VALUES (201, 'ResourceList', 'list', '/resource/config/index', NULL, 200, 201, NULL, 1);
INSERT INTO `menu` VALUES (300, 'Scene', '/scene', 'LAYOUT', NULL, NULL, 300, 1, 1);
INSERT INTO `menu` VALUES (301, 'SceneList', 'list', '/scene/config/index', NULL, 300, 301, NULL, 1);
INSERT INTO `menu` VALUES (302, 'ScenePlan', 'plan', '/scene/plan/index', NULL, 300, 302, NULL, 1);
INSERT INTO `menu` VALUES (303, 'Protocol', 'protocol', '/scene/protocol/index', NULL, 300, 303, NULL, 1);
INSERT INTO `menu` VALUES (304, 'Business', 'business', '/scene/business/index', NULL, 300, 304, NULL, 1);
INSERT INTO `menu` VALUES (305, 'SceneSemiPhysics', 'semiPhysics', '/scene/semiPhysics/index', NULL, 300, 305, NULL, 1);
INSERT INTO `menu` VALUES (400, 'Simulation', '/simulation', 'LAYOUT', '/simulation/control', NULL, 400, 2, 1);
INSERT INTO `menu` VALUES (401, 'Control', 'control', '/simulation/control/index', NULL, 400, 401, NULL, 1);
INSERT INTO `menu` VALUES (500, 'Source', '/source', 'LAYOUT', '/source/rib/index', NULL, 500, 4, 1);
INSERT INTO `menu` VALUES (501, 'RIB', 'rib', '/source/rib/index', NULL, 500, 501, NULL, 1);
INSERT INTO `menu` VALUES (502, 'RouteUpdate', 'routeupdate', '/source/routeupdate/index', NULL, 500, 502, NULL, 1);
INSERT INTO `menu` VALUES (503, 'IRR', 'irr', '/source/irr/index', NULL, 500, 503, NULL, 1);
INSERT INTO `menu` VALUES (504, 'ROA', 'roa', '/source/roa/index', NULL, 500, 504, NULL, 1);

SET FOREIGN_KEY_CHECKS = 1;

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

 Date: 26/11/2024 11:40:21
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for meta
-- ----------------------------
DROP TABLE IF EXISTS `meta`;
CREATE TABLE `meta`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `order_no` int NULL DEFAULT NULL,
  `dynamic_level` int NULL DEFAULT NULL,
  `real_path` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `ignore_auth` int NULL DEFAULT NULL,
  `roles` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `ignore_keep_alive` int NULL DEFAULT NULL,
  `affix` int NULL DEFAULT NULL,
  `icon` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `frame_src` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `transition_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `hide_breadcrumb` int NULL DEFAULT NULL,
  `hide_children_in_menu` int NULL DEFAULT NULL,
  `carry_param` int NULL DEFAULT NULL,
  `single` int NULL DEFAULT NULL,
  `current_active_menu` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `hide_tab` int NULL DEFAULT NULL,
  `hide_menu` int NULL DEFAULT NULL,
  `is_link` int NULL DEFAULT NULL,
  `ignore_route` int NULL DEFAULT NULL,
  `hide_path_for_children` int NULL DEFAULT NULL,
  `status` int NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 505 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of meta
-- ----------------------------
INSERT INTO `meta` VALUES (1, 'routes.dashboard.dashboard', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'bx:bx-home', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 1);
INSERT INTO `meta` VALUES (2, 'routes.dashboard.analysis', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'bx:bx-home', NULL, NULL, 1, NULL, NULL, NULL, '/dashboard', NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `meta` VALUES (3, 'routes.dashboard.workbench', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'bx:bx-home', NULL, NULL, 1, NULL, NULL, NULL, '/dashboard', NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `meta` VALUES (8, 'routes.app.aframe.aframe', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `meta` VALUES (9, 'routes.app.aframe.physical', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `meta` VALUES (10, 'routes.app.aframe.image', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `meta` VALUES (100, 'routes.bgp.system.moduleName', NULL, NULL, NULL, NULL, '1', NULL, NULL, 'ion:settings-outline', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `meta` VALUES (101, 'routes.bgp.system.dept', NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `meta` VALUES (102, 'routes.bgp.system.account', NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `meta` VALUES (103, 'routes.bgp.system.account_detail', NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '/system/account', NULL, 1, NULL, NULL, NULL, 1);
INSERT INTO `meta` VALUES (200, 'routes.bgp.resource.moduleName', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'ion:social-codepen-outline', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `meta` VALUES (201, 'routes.bgp.resource.list', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `meta` VALUES (300, 'routes.bgp.scene.moduleName', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'ion:desktop-outline', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `meta` VALUES (301, 'routes.bgp.scene.list', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `meta` VALUES (302, 'routes.bgp.scene.plan', NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `meta` VALUES (303, 'routes.bgp.scene.protocol', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `meta` VALUES (304, 'routes.bgp.scene.business', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `meta` VALUES (305, 'routes.bgp.scene.semiPhysics', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `meta` VALUES (400, 'routes.bgp.simulation.moduleName', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'ion:caret-forward-circle-outline', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `meta` VALUES (401, 'routes.bgp.simulation.control', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `meta` VALUES (500, 'routes.bgp.source.moduleName', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'mdi:source-merge', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `meta` VALUES (501, 'routes.bgp.source.rib', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `meta` VALUES (502, 'routes.bgp.source.routeupdate', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `meta` VALUES (503, 'routes.bgp.source.irr', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `meta` VALUES (504, 'routes.bgp.source.roa', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

SET FOREIGN_KEY_CHECKS = 1;

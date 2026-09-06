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

 Date: 26/11/2024 11:38:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_input
-- ----------------------------
DROP TABLE IF EXISTS `config_input`;
CREATE TABLE `config_input`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `input` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '输入的名称',
  `remarks` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `table` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '表名',
  `cndwrap` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'cnd.wrap',
  `sort` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '排序',
  `fields` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '过滤字段',
  `actived` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '白名单',
  `locked` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '黑名单',
  `ignorenull` int NULL DEFAULT NULL COMMENT '忽略空值',
  `funcname` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '数据库函数',
  `colname` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '列名',
  `status` int NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10041 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of config_input
-- ----------------------------
INSERT INTO `config_input` VALUES (1, 'update', NULL, 'orgs', 'id > 30 order by id asc', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_input` VALUES (2, 'update', NULL, 'orgs', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_input` VALUES (3, 'getTopDept', NULL, 'department', 'parent=1', '{asc: \"sort\"}', NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_input` VALUES (1000, 'getTopMenu', NULL, 'menu', 'parent is null and status = 1', '{asc: \"sort\"}', NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_input` VALUES (1001, 'getDeptList_q', '获取&查询', NULL, 'status = 1', '{asc: \"sort\"}', NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_input` VALUES (1002, 'getDeptTree', NULL, 'department', 'parent is null and status = 1', '{asc: \"sort\"}', NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_input` VALUES (1003, 'createDept', NULL, 'department', 'id = $id', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_input` VALUES (1004, 'getUserList_q', '获取&查询', NULL, 'status = 1', '{asc: \"id\"}', NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_input` VALUES (1005, 'createUser', NULL, 'account', 'id = $id', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_input` VALUES (1006, 'getRoleList', '获取&查询权限', 'role', 'status = 1', '{asc: \"id\"}', NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_input` VALUES (1007, 'updateOrInsertRole', '新建&更新授权', 'account_role', 'account = $account and role = $role', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_input` VALUES (1100, 'getResourceList_q', '获取&查服务器', NULL, 'status<>0', '{desc: \"id\"}', NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_input` VALUES (1101, 'createResource', NULL, 'mserver', 'id = $id', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_input` VALUES (1200, 'getSceneList_q', '获取&查询场景', NULL, 'status<>0', '{desc: \"id\"}', NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_input` VALUES (1201, 'createScene', NULL, 'scene', 'id = $id', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_input` VALUES (1300, 'getNodeList_q', '获取&查询节点', NULL, 'status<>0', '{desc: \"id\"}', NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_input` VALUES (1301, 'createNode', NULL, 'node', 'id = $id', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_input` VALUES (1400, 'getLinkList_q', '获取&查询链路', NULL, 'status<>0', '{desc: \"id\"}', NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_input` VALUES (1401, 'createLink', NULL, 'link', 'id = $id', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_input` VALUES (1500, 'getSceneServerList_q', '获取&查询场景服务器', NULL, 'status<>0', '{desc: \"id\"}', NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_input` VALUES (1501, 'createSceneServer', NULL, 'scene_server', 'id = $id', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_input` VALUES (1511, 'countSceneServer', '查询场景服务器是否已存在', 'scene_server', 'serverid = $serverid and sceneid = $sceneid and status<>0', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_input` VALUES (1600, 'getNodeCount_q', '获取场景节点数目', NULL, 'status<>0', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_input` VALUES (1710, 'createStatement', NULL, 'statement', 'id = $id', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_input` VALUES (1810, 'createSet', NULL, 'mset', 'id = $id', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_input` VALUES (1900, 'countNode', '统计节点总量', 'node', 'status<>0 and sceneid = $sceneid', NULL, NULL, NULL, 'sceneid', NULL, NULL, NULL, 1);
INSERT INTO `config_input` VALUES (1910, 'countServer', '统计服务器总量', 'scene_server', 'status<>0 and sceneid = $sceneid', NULL, NULL, NULL, 'sceneid', NULL, NULL, NULL, 1);
INSERT INTO `config_input` VALUES (1920, 'countLink', '统计链路总量', 'link', 'status<>0 and sceneid = $sceneid', NULL, NULL, NULL, 'sceneid', NULL, NULL, NULL, 1);
INSERT INTO `config_input` VALUES (2000, 'country_search', '检索国家代码和国家', NULL, 'status<>0 and code like \'%$search%\' or chinese_name like \'%$search%\'', '{desc: \"id\"}', NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_input` VALUES (2040, 'createSemiphysics', NULL, 'semiphysics', 'id = $id', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_input` VALUES (2060, NULL, NULL, 'rib', 'id = $id', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_input` VALUES (2080, NULL, NULL, 'routeupdate', 'id = $id', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_input` VALUES (2100, NULL, NULL, 'irr', 'id = $id', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_input` VALUES (2120, NULL, NULL, 'roa', 'id = $id', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_input` VALUES (2170, 'getBusinessList_q', '获取&查询业务', NULL, 'status = 1', '{desc: \"id\"}', NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_input` VALUES (2180, 'createBusiness', NULL, 'business', 'id = $id', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_input` VALUES (10040, 'countNodeStatement', '统计节点策略数目', 'statement', 'status<>0 and nodeid = $nodeid', NULL, NULL, NULL, 'nodeid', NULL, NULL, NULL, 1);

SET FOREIGN_KEY_CHECKS = 1;

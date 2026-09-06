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

 Date: 26/11/2024 11:38:27
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_funcflow
-- ----------------------------
DROP TABLE IF EXISTS `config_funcflow`;
CREATE TABLE `config_funcflow`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `business` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '业务名称',
  `remarks` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `funcs` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'func逗号隔开',
  `next` int NULL DEFAULT NULL COMMENT '后续funcflow id',
  `type` int NULL DEFAULT NULL COMMENT 'funcflow类型',
  `reducer` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '{key:id,...}用户处理mapper',
  `field_target` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'type==3:替换的目标字段，从该字段获取id;type==4: 获取值的字段',
  `field_table` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '从哪个表里查,-;\r\n如果没有配置从哪个表里查，就从filed_target同名的表里查',
  `field_source` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '从该表的哪个字段查;\r\n如果没有配置，就用id',
  `field_result` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '替换的结果字段',
  `field_relation` int NULL DEFAULT NULL COMMENT '1-等于（结果是Record）；2-in；3-not in',
  `field_cnd` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '增加sql条件',
  `status` int NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12011 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of config_funcflow
-- ----------------------------
INSERT INTO `config_funcflow` VALUES (1, 'b1', NULL, 'com.example.functions.db.STQuery,testf1', 2, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (2, 'b2', NULL, 'testf2', NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (4, 'b4', NULL, 'testMapper', NULL, 2, '{\"a\":\"testf1,testf2\", \"b\":\"testf2\", \"c\":\"testf1\"}', NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (5, 'b5', NULL, 'com.example.functions.db.STQuery', 6, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (6, 'b6', NULL, 'testMapper', 7, 2, '{\"a\":8, \"b\":2, \"c\":7}', NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (7, 'b7', NULL, 'testf1,testf2', NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (8, 'b8', NULL, 'testf1', NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (9, 'getDeptList', '获取部门树', 'com.example.functions.db.STQuery', 10, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (10, 'ResultFlatToBigFlux', '展平数据', 'com.example.functions.ResultFlatToBigFlux', 1011, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (100, 'convertRecordToInParamsDb', NULL, 'convertRecordToInParamsDb', 101, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (101, 'insert', NULL, 'com.example.functions.db.STInsert', NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (102, 'convertRecordToInParamsDb', NULL, '1', 103, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (103, 'update', NULL, 'com.example.functions.db.STUpdate', NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (900, 'insert', '', 'com.example.functions.db.STInsert', NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (901, 'update', NULL, 'com.example.functions.db.STUpdate', NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (902, 'updateOrInsert', '', 'com.example.functions.db.STUpdateOrInsert', NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (903, 'query', '', 'com.example.functions.db.STQuery', NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (904, 'count', NULL, 'com.example.functions.db.STCount', NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (910, 'convertRecordToNutMap', NULL, 'convertRecordToNutMap', NULL, 1, NULL, '', NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1000, 'getMenuList', '获取路由', 'com.example.functions.db.STQuery', 1001, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1001, 'addChildren', NULL, '1002', 1002, 3, NULL, 'id', 'menu', 'parent', 'children', 2, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1002, 'addMeta', NULL, '1003', NULL, 3, NULL, 'meta', NULL, NULL, NULL, 1, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1003, 'addRoles', NULL, '1004', 910, 3, NULL, 'roles', 'role', 'id', NULL, 2, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1004, 'getRole', NULL, '', NULL, 4, NULL, 'role', NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1010, 'getDeptTree', '获取部门树', 'com.example.functions.db.STQuery', 1011, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1011, 'addChildren', NULL, '1011', NULL, 3, NULL, 'id', 'department', 'parent', 'children', 2, '1= 1 order by sort asc', 1);
INSERT INTO `config_funcflow` VALUES (1020, 'createDept', '新建部门', 'addCreateTime', 1021, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1021, 'convertRecordToInParamsDb', '', '1003', 900, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1030, 'updateDept', '更新部门', 'addUpdateTime', 1031, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1031, 'convertRecordToInParamsDb', '', '1003', 901, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1040, 'getDeptList_q', '获取&查询部门树', 'com.example.functions.ResultFlatToBigFlux', 1011, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1050, 'getUserList_q', '获取&查询用户', '1051', NULL, 6, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1051, 'addRoles', NULL, NULL, NULL, 7, NULL, 'id', 'account_role', 'account', 'roles', 2, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1052, 'getRolename', NULL, '', NULL, 4, NULL, 'rolename', NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1060, 'convertRecordToInParamsDb', '', '1005', 900, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1070, 'convertRecordToInParamsDb', '', '1005', 901, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1080, 'convertRecordToInParamsDb', '', '1007', 902, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1100, 'getResourceList_q', '获取&查询服务器', NULL, NULL, 6, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1101, 'convertRecordToInParamsDb', '', '1101', 900, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1102, 'convertRecordToInParamsDb', '', '1101', 901, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1199, NULL, NULL, 'getStatsInfo', NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1200, 'getSceneList_q', '获取&查询场景', '1199', NULL, 6, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1201, 'createScene', '新建场景', 'addCreateTime', 1203, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1202, 'convertRecordToInParamsDb', '', '1201', 901, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1203, 'convertRecordToInParamsDb', '', '1201', 900, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1299, 'addStatements', NULL, NULL, NULL, 3, NULL, 'id', 'statement', 'nodeid', 'statement', 2, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1300, 'getNodeList_q', '获取&查询节点', '1299', NULL, 6, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1301, 'convertRecordToInParamsDb', '', '1301', 900, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1302, 'convertRecordToInParamsDb', '', '1301', 901, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1303, 'assignNode', '自动分配节点到服务器', 'assignNode', NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1400, 'getLinkList_q', '获取&查询链路', '1401', NULL, 6, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1401, 'addSrcnode', NULL, NULL, 1402, 3, NULL, 'srcid', 'node', 'id', 'srcnode', 1, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1402, 'addDestnode', NULL, NULL, NULL, 3, NULL, 'destid', 'node', 'id', 'destnode', 1, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1410, 'convertRecordToInParamsDb', '', '1401', 900, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1420, 'convertRecordToInParamsDb', '', '1401', 901, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1430, 'assignLinkIp', '自动链路节点IP', 'assignLinkIp', NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1500, 'getSceneServerList_q', '获取&查询场景服务器', '1501', NULL, 6, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1501, 'addServer', NULL, NULL, NULL, 3, NULL, 'serverid', 'mserver', 'id', 'server', 1, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1510, 'convertRecordToInParamsDb', '', '1501', 900, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1511, 'countSceneServer', '查询场景服务器是否已存在', '1511', 904, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1520, 'convertRecordToInParamsDb', '', '1501', 901, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1710, 'convertRecordToInParamsDb', '', '1710', 900, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1720, 'convertRecordToInParamsDb', '', '1710', 901, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1730, 'copyStatement', '复制策略', 'copyStatement', NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1810, 'convertRecordToInParamsDb', '', '1810', 900, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1820, 'convertRecordToInParamsDb', '', '1810', 901, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1900, 'convertRecordToInParamsDb', NULL, '1900', 904, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1910, 'convertRecordToInParamsDb', NULL, '1910', 904, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1920, 'convertRecordToInParamsDb', NULL, '1920', 904, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1930, 'getSceneServerList_nodeCount', '获取&查询场景服务器(包括节点数)', '1931', NULL, 6, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1931, 'addServer', NULL, NULL, 1932, 3, NULL, 'serverid', 'mserver', 'id', 'server', 1, NULL, 1);
INSERT INTO `config_funcflow` VALUES (1932, 'addNodeCount', NULL, 'addNodeCount', NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (2020, 'startScene', '开始仿真', 'startScene', NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (2030, 'stopScene', '结束仿真', 'stopScene', NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (2031, 'stopSceneByScene', '结束仿真', 'stopSceneByScene', NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (2040, 'convertRecordToInParamsDb', '', '2040', 900, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (2050, 'convertRecordToInParamsDb', '', '2040', 901, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (2060, 'convertRecordToInParamsDb', '', '2060', 900, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (2070, 'convertRecordToInParamsDb', '', '2060', 901, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (2080, 'convertRecordToInParamsDb', '', '2080', 900, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (2090, 'convertRecordToInParamsDb', '', '2080', 901, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (2100, 'convertRecordToInParamsDb', '', '2100', 900, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (2110, 'convertRecordToInParamsDb', '', '2100', 901, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (2120, 'convertRecordToInParamsDb', '', '2120', 900, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (2130, 'convertRecordToInParamsDb', '', '2120', 901, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (2140, 'multiCreateNode', NULL, 'replaceNodeCountryWithCode', 1301, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (2150, 'multiCreateNode', NULL, 'replaceLinkAsnWithId', 1410, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (2160, NULL, NULL, 'publishPolicies', NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (2170, 'getBusinessList_q', '获取&查询业务', NULL, NULL, 6, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (2171, 'addSrcnode', NULL, NULL, 2172, 3, NULL, 'srcid', 'node', 'id', 'srcnode', 1, NULL, 1);
INSERT INTO `config_funcflow` VALUES (2172, 'addDestnode', NULL, NULL, NULL, 3, NULL, 'destid', 'node', 'id', 'destnode', 1, NULL, 1);
INSERT INTO `config_funcflow` VALUES (2180, 'convertRecordToInParamsDb', '', '2180', 900, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (2190, 'convertRecordToInParamsDb', '', '2180', 901, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (2200, NULL, NULL, 'addNodeToNest', NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (2210, NULL, NULL, 'addLinkToNest', NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (2220, NULL, NULL, 'publishStatement', NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (2230, NULL, NULL, 'importRoutes', NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (2240, NULL, NULL, 'businessPolicies', NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (2250, NULL, NULL, 'getNeighbors', NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (2280, NULL, NULL, 'importLinks', NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (2290, NULL, NULL, 'addServersToScene', NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (10000, 'getNodeTreeInScene', '获取节点树', 'getNodeTreeInScene', NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (10010, 'getLinkTreeInScene', '获取链路树', 'getLinkTreeInScene', NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (10020, 'getServerTreeInScene', '获取服务器树', 'getServerTreeInScene', NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (10030, 'getNodeTreeInSet', '获取分类下节点树', 'getNodeTreeInSet', NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (10040, 'convertRecordToInParamsDb', NULL, '10040', 904, 5, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `config_funcflow` VALUES (10050, 'addCountry', NULL, NULL, 10051, 3, NULL, 'country', 'country', 'id', 'country', 1, NULL, 1);
INSERT INTO `config_funcflow` VALUES (10051, 'addServer', NULL, NULL, NULL, 3, NULL, 'serverid', 'mserver', 'id', 'server', 1, NULL, 1);

SET FOREIGN_KEY_CHECKS = 1;

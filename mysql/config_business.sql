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

 Date: 26/11/2024 11:38:14
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_business
-- ----------------------------
DROP TABLE IF EXISTS `config_business`;
CREATE TABLE `config_business`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `business` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '只有这里用business查询，其他config表都是id',
  `businame` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '中文名称',
  `remarks` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `inputs` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'config_input id 逗号隔开',
  `funcflow` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'config_funcflow id ',
  `sql` varchar(2550) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `status` int NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10051 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of config_business
-- ----------------------------
INSERT INTO `config_business` VALUES (1, 'inserttest', '测试插入数据', NULL, NULL, '100', NULL, 1);
INSERT INTO `config_business` VALUES (2, 'updatetest', '测试更新数据', NULL, NULL, '102', NULL, 1);
INSERT INTO `config_business` VALUES (3, 'getDeptList', '获取部门树', NULL, '3', '1010', NULL, 1);
INSERT INTO `config_business` VALUES (1000, 'getMenuList', '获取后台路由', NULL, '1000', '1000', NULL, 1);
INSERT INTO `config_business` VALUES (1001, 'getDeptList_q', '获取&查询部门树', '不包括跟节点', '1001', '1040', NULL, 1);
INSERT INTO `config_business` VALUES (1002, 'getDeptTree', '获取全部门树', '包括跟节点', '1002', '1010', NULL, 1);
INSERT INTO `config_business` VALUES (1003, 'createDept', '新建部门', '', '', '1020', NULL, 1);
INSERT INTO `config_business` VALUES (1004, 'updateDept', '更新部门', '', '', '1030', NULL, 1);
INSERT INTO `config_business` VALUES (1005, 'getUserList_q', '获取&查询用户', NULL, '1004', '1050', NULL, 1);
INSERT INTO `config_business` VALUES (1006, 'createUser', '新建用户', '', '', '1060', NULL, 1);
INSERT INTO `config_business` VALUES (1007, 'updateUser', '更新用户', '', '', '1070', NULL, 1);
INSERT INTO `config_business` VALUES (1008, 'getRoleList', '获取&查询用户', NULL, '1006', '903', NULL, 1);
INSERT INTO `config_business` VALUES (1009, 'updateOrInsertRole', '新建&更新授权', NULL, NULL, '1080', NULL, 1);
INSERT INTO `config_business` VALUES (1100, 'getResourceList_q', '获取&查询服务器', NULL, '1100', '1100', NULL, 1);
INSERT INTO `config_business` VALUES (1101, 'createResource', '新建服务器', '', '', '1101', NULL, 1);
INSERT INTO `config_business` VALUES (1102, 'updateResource', '更新服务器', '', '', '1102', NULL, 1);
INSERT INTO `config_business` VALUES (1200, 'getSceneList_q', '获取&查询场景', NULL, '1200', '1200', NULL, 1);
INSERT INTO `config_business` VALUES (1201, 'createScene', '新建场景', '', '', '1201', NULL, 1);
INSERT INTO `config_business` VALUES (1202, 'updateScene', '更新场景', '', '', '1202', NULL, 1);
INSERT INTO `config_business` VALUES (1300, 'getNodeList_q', '获取&查询节点', NULL, '1300', '1300', NULL, 1);
INSERT INTO `config_business` VALUES (1301, 'createNode', '新建节点', '', '', '1301', NULL, 1);
INSERT INTO `config_business` VALUES (1302, 'updateNode', '更新节点', '', '', '1302', NULL, 1);
INSERT INTO `config_business` VALUES (1303, 'assignNode', '自动分配节点到服务器', '', NULL, '1303', NULL, 1);
INSERT INTO `config_business` VALUES (1400, 'getLinkList_q', '获取&查询链路', NULL, '1400', '1400', NULL, 1);
INSERT INTO `config_business` VALUES (1410, 'createLink', '新建链路', '', '', '1410', NULL, 1);
INSERT INTO `config_business` VALUES (1420, 'updateLink', '更新链路', '', '', '1420', NULL, 1);
INSERT INTO `config_business` VALUES (1430, 'assignLinkIp', '自动链路节点IP', '', NULL, '1430', NULL, 1);
INSERT INTO `config_business` VALUES (1500, 'getSceneServerList_q', '获取&查询场景服务器', NULL, '1500', '1500', NULL, 1);
INSERT INTO `config_business` VALUES (1510, 'createSceneServer', '新建场景服务器', '', '', '1510', NULL, 1);
INSERT INTO `config_business` VALUES (1511, 'countSceneServer', '查询场景服务器是否已存在', '', '', '1511', NULL, 1);
INSERT INTO `config_business` VALUES (1520, 'updateSceneServer', '更新场景服务器', '', '', '1520', NULL, 1);
INSERT INTO `config_business` VALUES (1600, 'getNodeCount_q', '获取场景节点数目', NULL, '1600', NULL, NULL, 1);
INSERT INTO `config_business` VALUES (1710, 'createStatement', '新建策略', NULL, NULL, '1710', NULL, 1);
INSERT INTO `config_business` VALUES (1720, 'updateStatement', '更新策略', NULL, NULL, '1720', NULL, 1);
INSERT INTO `config_business` VALUES (1730, 'copyStatement', '复制策略', NULL, NULL, '1730', NULL, 1);
INSERT INTO `config_business` VALUES (1810, 'createSet', '新建策略条件项', NULL, NULL, '1810', NULL, 1);
INSERT INTO `config_business` VALUES (1820, 'updateSet', '更新策略条件项', NULL, NULL, '1820', NULL, 1);
INSERT INTO `config_business` VALUES (1900, 'countNode', '统计节点总量（场景）', NULL, NULL, '1900', NULL, 1);
INSERT INTO `config_business` VALUES (1910, 'countServer', '统计服务器总量（场景）', NULL, NULL, '1910', NULL, 1);
INSERT INTO `config_business` VALUES (1920, 'countLink', '统计链路总量（场景）', NULL, NULL, '1920', NULL, 1);
INSERT INTO `config_business` VALUES (1930, 'getSceneServerList_nodeCount', '获取&查询场景服务器(包括节点数)', NULL, '1500', '1930', NULL, 1);
INSERT INTO `config_business` VALUES (2000, 'country_search', '检索国家代码和国家', NULL, '2000', NULL, NULL, 1);
INSERT INTO `config_business` VALUES (2010, 'nodelabel_search', '检索节点分组', NULL, NULL, NULL, 'SELECT DISTINCT mlabel FROM node WHERE status <>0 and mlabel is not NULL and mlabel like \'%$text%\'', 1);
INSERT INTO `config_business` VALUES (2020, 'startScene', '开始仿真', NULL, NULL, '2020', NULL, 1);
INSERT INTO `config_business` VALUES (2030, 'stopScene', '结束仿真', NULL, NULL, '2030', NULL, 1);
INSERT INTO `config_business` VALUES (2031, 'stopSceneByScene', '结束仿真', NULL, NULL, '2031', NULL, 1);
INSERT INTO `config_business` VALUES (2040, 'createSemiphysics', '新建半物理接入', '', '', '2040', NULL, 1);
INSERT INTO `config_business` VALUES (2050, 'updateSemiphysics', '更新半物理接入', NULL, NULL, '2050', NULL, 1);
INSERT INTO `config_business` VALUES (2060, 'createRIB', '新建RIB', '', '', '2060', NULL, 1);
INSERT INTO `config_business` VALUES (2070, 'updateRIB', '更新RIB', '', '', '2070', NULL, 1);
INSERT INTO `config_business` VALUES (2080, 'createRouteupdate', '新建路由更新消息', '', '', '2080', NULL, 1);
INSERT INTO `config_business` VALUES (2090, 'updateRouteupdate', '更新路由更新消息', '', '', '2090', NULL, 1);
INSERT INTO `config_business` VALUES (2100, 'createIrr', '新建IRR', '', '', '2100', NULL, 1);
INSERT INTO `config_business` VALUES (2110, 'updateIrr', '更新IRR', '', '', '2110', NULL, 1);
INSERT INTO `config_business` VALUES (2120, 'createRoa', '新建ROA', '', '', '2120', NULL, 1);
INSERT INTO `config_business` VALUES (2130, 'updateRoa', '更新ROA', '', '', '2130', NULL, 1);
INSERT INTO `config_business` VALUES (2140, 'multiCreateNode', '批量新建节点', '', '', '2140', NULL, 1);
INSERT INTO `config_business` VALUES (2150, 'multiCreateLink', '批量新建链路', '', '', '2150', NULL, 1);
INSERT INTO `config_business` VALUES (2160, 'publishPolicies', '发布策略', NULL, NULL, '2160', NULL, 1);
INSERT INTO `config_business` VALUES (2170, 'getBusinessList_q', '获取&查询业务', NULL, '2170', '2170', NULL, 1);
INSERT INTO `config_business` VALUES (2180, 'createBusiness', '新建业务', '', '', '2180', NULL, 1);
INSERT INTO `config_business` VALUES (2190, 'updateBusiness', '更新业务', '', '', '2190', NULL, 1);
INSERT INTO `config_business` VALUES (2200, 'addNodeToNest', 'nest中增加新节点', '', '', '2200', NULL, 1);
INSERT INTO `config_business` VALUES (2210, 'addLinkToNest', 'nest中增加新链路', '', '', '2210', NULL, 1);
INSERT INTO `config_business` VALUES (2220, 'publishStatement', '运行中发布策略', '', '', '2220', NULL, 1);
INSERT INTO `config_business` VALUES (2230, 'importRoutes', '导入初始路由', '', '', '2230', NULL, 1);
INSERT INTO `config_business` VALUES (2240, 'businessPolicies', '分配商业策略', '', '', '2240', NULL, 1);
INSERT INTO `config_business` VALUES (2250, 'getNeighbors', '获取邻居', '', '', '2250', NULL, 1);
INSERT INTO `config_business` VALUES (2260, 'deleteSceneServer', '场景删除服务器', '', '', NULL, 'delete from scene_server WHERE serverid = $serverid', 1);
INSERT INTO `config_business` VALUES (2270, 'deleteNodeServer', '节点删除服务器', NULL, NULL, NULL, 'UPDATE node SET serverid=NULL  WHERE serverid = $serverid', 1);
INSERT INTO `config_business` VALUES (2280, 'importLinks', '导入链路', NULL, NULL, '2280', NULL, 1);
INSERT INTO `config_business` VALUES (2290, 'addServersToScene', '分配多个服务器', NULL, NULL, '2290', NULL, 1);
INSERT INTO `config_business` VALUES (10000, 'getNodeTreeInScene', '获取节点树', '', NULL, '10000', NULL, 1);
INSERT INTO `config_business` VALUES (10010, 'getLinkTreeInScene', '获取链路树', '', NULL, '10010', NULL, 1);
INSERT INTO `config_business` VALUES (10020, 'getServerTreeInScene', '获取服务器树', '', NULL, '10020', NULL, 1);
INSERT INTO `config_business` VALUES (10030, 'getNodeTreeInSet', '获取分类下节点树', '', NULL, '10030', NULL, 1);
INSERT INTO `config_business` VALUES (10040, 'countNodeStatement', '统计节点策略数目', NULL, NULL, '10040', NULL, 1);
INSERT INTO `config_business` VALUES (10050, 'getNodeInfo', '获取链路两头节点信息', NULL, NULL, '10050', NULL, 1);

SET FOREIGN_KEY_CHECKS = 1;

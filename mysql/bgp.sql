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

 Date: 26/11/2024 11:37:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `realname` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `remarks` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `department` int NULL DEFAULT NULL,
  `token` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `expire_time` datetime NULL DEFAULT NULL,
  `status` int NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = COMPACT;

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
-- Table structure for base_set
-- ----------------------------
DROP TABLE IF EXISTS `base_set`;
CREATE TABLE `base_set`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `matchings` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `type` int NULL DEFAULT NULL COMMENT '1：前缀策略条件项;\r\n2：邻居策略条件项;\r\n3：AsPath长度策略条件项;\r\n4：AsPath策略条件项;',
  `status` int NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for business
-- ----------------------------
DROP TABLE IF EXISTS `business`;
CREATE TABLE `business`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `business` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `srcid` int NULL DEFAULT NULL,
  `srcasn` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `srcip` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `srcport` int NULL DEFAULT NULL,
  `srceth` int NULL DEFAULT NULL,
  `destid` int NULL DEFAULT NULL,
  `destasn` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `destip` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `destport` int NULL DEFAULT NULL,
  `desteth` int NULL DEFAULT NULL,
  `delay` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `runingadd` int NULL DEFAULT NULL,
  `lifecycle` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `typevalue2` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `packagelength` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `starttime` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `endtime` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `routetype` int NULL DEFAULT NULL,
  `routeinfo` varchar(2550) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `networklayer` int NULL DEFAULT NULL,
  `transportlayer` int NULL DEFAULT NULL,
  `applicationlayer` int NULL DEFAULT NULL,
  `sceneid` int NULL DEFAULT NULL,
  `status` int NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4913 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for condition
-- ----------------------------
DROP TABLE IF EXISTS `condition`;
CREATE TABLE `condition`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `setid` int NULL DEFAULT NULL,
  `type` int NULL DEFAULT NULL COMMENT '0：满足任意一个条件匹配;\r\n1：满足所有条件匹配;\r\n2：不满足任意条件的匹配;',
  `statementid` int NULL DEFAULT NULL,
  `nodeid` int NULL DEFAULT NULL,
  `status` int NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for conditon_aspathlength
-- ----------------------------
DROP TABLE IF EXISTS `conditon_aspathlength`;
CREATE TABLE `conditon_aspathlength`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `set_id` int NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `matchings` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `status` int NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for conditon_neighbor
-- ----------------------------
DROP TABLE IF EXISTS `conditon_neighbor`;
CREATE TABLE `conditon_neighbor`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `set_id` int NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `matchings` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `status` int NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for conditon_prefix
-- ----------------------------
DROP TABLE IF EXISTS `conditon_prefix`;
CREATE TABLE `conditon_prefix`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `set_id` int NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `matchings` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `status` int NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for config
-- ----------------------------
DROP TABLE IF EXISTS `config`;
CREATE TABLE `config`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `keyword` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `required` int NULL DEFAULT NULL,
  `type` int NULL DEFAULT NULL COMMENT '0_用户不能修改；1_编目',
  `status` int NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 108 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

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
-- Table structure for country
-- ----------------------------
DROP TABLE IF EXISTS `country`;
CREATE TABLE `country`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `chinese_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` int NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 504 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for department
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `remarks` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `sort` int NULL DEFAULT NULL COMMENT '排序编号',
  `parent` int NULL DEFAULT NULL COMMENT '父机构id',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `mark` int NULL DEFAULT 1 COMMENT '1-启动；0-停用',
  `status` int NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for irr
-- ----------------------------
DROP TABLE IF EXISTS `irr`;
CREATE TABLE `irr`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `ip_prefix` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '前缀',
  `asn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ASN',
  `last_modified` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后修改时间',
  `last_updatetime` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后更新时间',
  `data_source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据源',
  `collect_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采集点',
  `source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '机构',
  `descr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `ov_state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ov_state',
  `md5sum` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'MD5校验',
  `status` int NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38011 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for link
-- ----------------------------
DROP TABLE IF EXISTS `link`;
CREATE TABLE `link`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `networksegment` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `srcid` int NULL DEFAULT NULL,
  `srcasn` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `srcip` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `srcport` int NULL DEFAULT NULL,
  `srceth` int NULL DEFAULT NULL,
  `destid` int NULL DEFAULT NULL,
  `destasn` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `destip` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `destport` int NULL DEFAULT NULL,
  `desteth` int NULL DEFAULT NULL,
  `relationship` int NULL DEFAULT NULL COMMENT '1-P2C、2-C2P、3-P2P、4-N/A',
  `country` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'NC表示跨国链路，CN表示国内链路',
  `delay` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `runingadd` int NULL DEFAULT NULL,
  `lifecycle` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `packagelength` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `starttime` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `endtime` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `sceneid` int NULL DEFAULT NULL,
  `type1` int NULL DEFAULT NULL,
  `type2` int NULL DEFAULT NULL,
  `type3` int NULL DEFAULT NULL,
  `status` int NULL DEFAULT 1,
  `bandwidth` bigint NULL DEFAULT NULL,
  `loss` float(255, 10) NULL DEFAULT NULL,
  `jitter` bigint NULL DEFAULT NULL,
  `isdelay` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 61310 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for link_copy1
-- ----------------------------
DROP TABLE IF EXISTS `link_copy1`;
CREATE TABLE `link_copy1`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `networksegment` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `srcid` int NULL DEFAULT NULL,
  `srcasn` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `srcip` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `srcport` int NULL DEFAULT NULL,
  `srceth` int NULL DEFAULT NULL,
  `destid` int NULL DEFAULT NULL,
  `destasn` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `destip` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `destport` int NULL DEFAULT NULL,
  `desteth` int NULL DEFAULT NULL,
  `delay` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `runingadd` int NULL DEFAULT NULL,
  `lifecycle` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `packagelength` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `starttime` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `endtime` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `routetype` int NULL DEFAULT NULL,
  `routeinfo` varchar(2550) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `networklayer` int NULL DEFAULT NULL,
  `transportlayer` int NULL DEFAULT NULL,
  `applicationlayer` int NULL DEFAULT NULL,
  `sceneid` int NULL DEFAULT NULL,
  `status` int NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5072 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for links
-- ----------------------------
DROP TABLE IF EXISTS `links`;
CREATE TABLE `links`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `srcid` int NULL DEFAULT NULL,
  `srcasn` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `destid` int NULL DEFAULT NULL,
  `destasn` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `relationship` int NULL DEFAULT NULL COMMENT '1-P2C、2-C2P、3-P2P、4-N/A',
  `country` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `type1` int NULL DEFAULT NULL,
  `type2` int NULL DEFAULT NULL,
  `type3` int NULL DEFAULT NULL,
  `status` int NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 127175 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for log_account_login
-- ----------------------------
DROP TABLE IF EXISTS `log_account_login`;
CREATE TABLE `log_account_login`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `account` int NULL DEFAULT NULL,
  `count` bigint NULL DEFAULT NULL,
  `status` int NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

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
-- Table structure for mlabel
-- ----------------------------
DROP TABLE IF EXISTS `mlabel`;
CREATE TABLE `mlabel`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `mlabel` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `sceneid` int NULL DEFAULT NULL,
  `status` int NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for mlabelset
-- ----------------------------
DROP TABLE IF EXISTS `mlabelset`;
CREATE TABLE `mlabelset`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `set` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `mlabel` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `create_id` int NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `status` int NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

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
-- Table structure for mset
-- ----------------------------
DROP TABLE IF EXISTS `mset`;
CREATE TABLE `mset`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `mset` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `matchings` varchar(4096) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `type` int NULL DEFAULT NULL COMMENT '1：前缀策略条件项;\r\n2：邻居策略条件项;\r\n3：AsPath长度策略条件项;\r\n4：AsPath策略条件项;\r\n5:   团体策略\r\n6:   large团体策略\r\n7: 下一跳策略',
  `matchsetoptions` int NULL DEFAULT NULL COMMENT '1：any;\r\n2：all;\r\n3：invert;',
  `statementid` int NULL DEFAULT NULL,
  `nodeid` int NULL DEFAULT NULL,
  `status` int NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4818 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for node
-- ----------------------------
DROP TABLE IF EXISTS `node`;
CREATE TABLE `node`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `asn` int NULL DEFAULT NULL,
  `node` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `org` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `country` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `longitude` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '经度',
  `latitude` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '纬度',
  `mlabel` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `runingadd` int NULL DEFAULT NULL,
  `serverid` int NULL DEFAULT NULL,
  `routetype` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `routeinfobgp` varchar(2550) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `routeinfoospf` varchar(2550) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `networklayer` int NULL DEFAULT NULL,
  `transportlayer` int NULL DEFAULT NULL,
  `applicationlayer` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `monitor` int NULL DEFAULT NULL,
  `initroutes` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '初始路由',
  `sceneid` int NULL DEFAULT NULL,
  `status` int NULL DEFAULT 1,
  `Tier` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `innerid` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_customer_id_staff_id`(`sceneid` ASC, `asn` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 361197 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for nodes
-- ----------------------------
DROP TABLE IF EXISTS `nodes`;
CREATE TABLE `nodes`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `asn` int NULL DEFAULT NULL,
  `node` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `org` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `country` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `longitude` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '经度',
  `latitude` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '纬度',
  `mlabel` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `runingadd` int NULL DEFAULT NULL,
  `serverid` int NULL DEFAULT NULL,
  `sceneid` int NULL DEFAULT NULL,
  `status` int NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `asn`(`asn` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 167603 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for nodes1
-- ----------------------------
DROP TABLE IF EXISTS `nodes1`;
CREATE TABLE `nodes1`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `asn` int NULL DEFAULT NULL,
  `node` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `org` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `country` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `longitude` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '经度',
  `latitude` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '纬度',
  `mlabel` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `runingadd` int NULL DEFAULT NULL,
  `serverid` int NULL DEFAULT NULL,
  `sceneid` int NULL DEFAULT NULL,
  `status` int NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `asn`(`asn` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 142565 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for nodes2
-- ----------------------------
DROP TABLE IF EXISTS `nodes2`;
CREATE TABLE `nodes2`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `asn` int NULL DEFAULT NULL,
  `node` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `org` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `country` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `longitude` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '经度',
  `latitude` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '纬度',
  `mlabel` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `runingadd` int NULL DEFAULT NULL,
  `serverid` int NULL DEFAULT NULL,
  `sceneid` int NULL DEFAULT NULL,
  `status` int NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `asn`(`asn` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 164892 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for nodes3
-- ----------------------------
DROP TABLE IF EXISTS `nodes3`;
CREATE TABLE `nodes3`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `asn` int NULL DEFAULT NULL,
  `node` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `org` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `country` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `longitude` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '经度',
  `latitude` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '纬度',
  `mlabel` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `runingadd` int NULL DEFAULT NULL,
  `serverid` int NULL DEFAULT NULL,
  `sceneid` int NULL DEFAULT NULL,
  `status` int NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `asn`(`asn` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 164892 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for operation_list
-- ----------------------------
DROP TABLE IF EXISTS `operation_list`;
CREATE TABLE `operation_list`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `operation_list` json NULL,
  `sceneid` int NULL DEFAULT NULL,
  `operating_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for perm
-- ----------------------------
DROP TABLE IF EXISTS `perm`;
CREATE TABLE `perm`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `permcode` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `permname` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '中文',
  `status` int NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for rib
-- ----------------------------
DROP TABLE IF EXISTS `rib`;
CREATE TABLE `rib`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型',
  `timestamp` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '时间戳（年月日时分秒）',
  `peer_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邻居IP',
  `peer_asn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邻居ASN',
  `prefix` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '前缀',
  `as_path` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路径',
  `orgin` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '起源',
  `next_hop` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下一跳',
  `local_pref` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Local_pref',
  `med` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'med',
  `communities` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '团队/团体',
  `atomic` int NULL DEFAULT NULL COMMENT ' Ture or False',
  `md5sum` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Md5检验和',
  `status` int NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 207688 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for roa
-- ----------------------------
DROP TABLE IF EXISTS `roa`;
CREATE TABLE `roa`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `asn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ASN',
  `ip_prefix` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '前缀',
  `max_length` int NULL DEFAULT NULL COMMENT '最大长度',
  `trust_anchor` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '信任源',
  `md5sum` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'MD5校验',
  `status` int NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28011 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `role` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `rolename` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '中文',
  `permcode` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `status` int NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Table structure for routeupdate
-- ----------------------------
DROP TABLE IF EXISTS `routeupdate`;
CREATE TABLE `routeupdate`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型',
  `timestamp` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '时间戳（年月日时分秒）',
  `peer_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邻居IP',
  `peer_asn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邻居ASN',
  `prefix` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '前缀',
  `as_path` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路径',
  `orgin` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '起源',
  `next_hop` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下一跳',
  `local_pref` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Local_pref',
  `med` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'med',
  `communities` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '团队/团体',
  `atomic` int NULL DEFAULT NULL COMMENT ' Ture or False',
  `md5sum` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Md5检验和',
  `status` int NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6004 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for scene
-- ----------------------------
DROP TABLE IF EXISTS `scene`;
CREATE TABLE `scene`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `scene` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `remarks` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `info` varchar(2555) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `create_id` int NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_id` int NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `status` int NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for scene_server
-- ----------------------------
DROP TABLE IF EXISTS `scene_server`;
CREATE TABLE `scene_server`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `serverid` int NULL DEFAULT NULL,
  `sceneid` int NULL DEFAULT NULL,
  `status` int NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 76 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for semiphysics
-- ----------------------------
DROP TABLE IF EXISTS `semiphysics`;
CREATE TABLE `semiphysics`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `net` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '网口名称',
  `asn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '虚拟节点的asn',
  `ipv4` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '虚拟节点的ipv4',
  `sceneid` int NULL DEFAULT NULL,
  `status` int NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for session_start
-- ----------------------------
DROP TABLE IF EXISTS `session_start`;
CREATE TABLE `session_start`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `sessionid` int NULL DEFAULT NULL,
  `nodeid` int NULL DEFAULT NULL,
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` int NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for simulation
-- ----------------------------
DROP TABLE IF EXISTS `simulation`;
CREATE TABLE `simulation`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `sceneid` int NULL DEFAULT NULL,
  `sessionid` int NULL DEFAULT NULL,
  `starttime` datetime NULL DEFAULT NULL,
  `startedtime` datetime NULL DEFAULT NULL,
  `startduration` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `stoptime` datetime NULL DEFAULT NULL,
  `stopedtime` datetime NULL DEFAULT NULL,
  `stopduration` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `running` int NULL DEFAULT NULL,
  `status` int NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 264 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for statement
-- ----------------------------
DROP TABLE IF EXISTS `statement`;
CREATE TABLE `statement`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `nodeid` int NULL DEFAULT NULL,
  `statement` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `direction` int NULL DEFAULT NULL COMMENT '1-import; 2-export',
  `scope` int NULL DEFAULT NULL COMMENT '作用域 1-global; 2-邻居IP',
  `action` int NULL DEFAULT NULL COMMENT '1-accept; 2-reject',
  `copyfrom` int NULL DEFAULT NULL,
  `sessionid` int NULL DEFAULT NULL,
  `status` int NULL DEFAULT 1,
  `sceneid` int NULL DEFAULT NULL COMMENT '场景id',
  `published` int NOT NULL DEFAULT 0 COMMENT '是否发布',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2480 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for upload
-- ----------------------------
DROP TABLE IF EXISTS `upload`;
CREATE TABLE `upload`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `filename` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `filepath` varchar(2550) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `amount` int NULL DEFAULT NULL COMMENT 'kb',
  `account` int NULL DEFAULT NULL,
  `uuid` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `status` int NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 242 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- View structure for account_view
-- ----------------------------
DROP VIEW IF EXISTS `account_view`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `account_view` AS select `temp`.`id` AS `id`,`temp`.`username` AS `username`,group_concat(`temp`.`role` separator ',') AS `roles`,`temp`.`token` AS `token` from (select `a`.`id` AS `id`,`a`.`username` AS `username`,`a`.`token` AS `token`,`a`.`create_time` AS `create_time`,`a`.`expire_time` AS `expire_time`,`r`.`role` AS `role`,`a`.`status` AS `status` from ((`account` `a` join `account_role` `ar` on((`a`.`id` = `ar`.`account`))) join `role` `r` on((`ar`.`role` = `r`.`id`))) where ((`a`.`status` = 1) and (`ar`.`status` = 1))) `temp` group by `temp`.`id`;

-- ----------------------------
-- View structure for link_view
-- ----------------------------
DROP VIEW IF EXISTS `link_view`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `link_view` AS select `l`.`id` AS `id`,`l`.`networksegment` AS `networksegment`,`l`.`srcid` AS `srcid`,`l`.`srcasn` AS `srcasn`,`l`.`srcip` AS `srcip`,`l`.`srcport` AS `srcport`,`l`.`srceth` AS `srceth`,`l`.`destid` AS `destid`,`l`.`destasn` AS `destasn`,`l`.`destip` AS `destip`,`l`.`destport` AS `destport`,`l`.`desteth` AS `desteth`,`l`.`delay` AS `delay`,`l`.`runingadd` AS `runingadd`,`l`.`sceneid` AS `sceneid`,`l`.`status` AS `status`,`n1`.`country` AS `srccountry`,`n2`.`country` AS `destcountry` from ((`link` `l` join `node` `n1` on((`l`.`srcid` = `n1`.`id`))) join `node` `n2` on((`l`.`destid` = `n2`.`id`)));

-- ----------------------------
-- View structure for menu_view
-- ----------------------------
DROP VIEW IF EXISTS `menu_view`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `menu_view` AS select `menu`.`id` AS `id`,`menu`.`name` AS `name`,`menu`.`path` AS `path`,`menu`.`component` AS `component`,`menu`.`redirect` AS `redirect`,`menu`.`parent` AS `parent`,`menu`.`meta` AS `meta`,`menu`.`sort` AS `sort`,`menu`.`status` AS `status`,json_object('title',`meta`.`title`) AS `metaObject` from (`menu` join `meta` on((`menu`.`meta` = `meta`.`id`)));

-- ----------------------------
-- View structure for node_view
-- ----------------------------
DROP VIEW IF EXISTS `node_view`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `node_view` AS select `n`.`id` AS `id`,`n`.`asn` AS `asn`,`n`.`node` AS `node`,`n`.`org` AS `org`,`n`.`mlabel` AS `mlabel`,`c`.`chinese_name` AS `country`,`c`.`code` AS `countrycode`,`n`.`longitude` AS `longitude`,`n`.`latitude` AS `latitude`,`n`.`runingadd` AS `runingadd`,`n`.`initroutes` AS `initroutes`,`n`.`serverid` AS `serverid`,`n`.`sceneid` AS `sceneid`,`n`.`applicationlayer` AS `applicationlayer`,`n`.`transportlayer` AS `transportlayer`,`n`.`networklayer` AS `networklayer`,`n`.`routetype` AS `routetype`,`n`.`routeinfobgp` AS `routeinfobgp`,`n`.`routeinfoospf` AS `routeinfoospf`,`n`.`monitor` AS `monitor`,`n`.`status` AS `status`,`s`.`mserver` AS `mserver`,`s`.`ip` AS `ip` from ((`node` `n` left join `mserver` `s` on((`n`.`serverid` = `s`.`id`))) left join `country` `c` on((`n`.`country` = `c`.`id`)));

-- ----------------------------
-- View structure for scene_server_view
-- ----------------------------
DROP VIEW IF EXISTS `scene_server_view`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `scene_server_view` AS select `m`.`id` AS `id`,`m`.`mserver` AS `mserver`,`m`.`serialnumber` AS `serialnumber`,`m`.`username` AS `username`,`m`.`password` AS `password`,`m`.`ip` AS `ip`,`m`.`cpu` AS `cpu`,`m`.`cpucount` AS `cpucount`,`m`.`thread` AS `thread`,`m`.`memory` AS `memory`,`m`.`systemserver` AS `systemserver`,`m`.`systemversion` AS `systemversion`,`m`.`storage` AS `storage`,`m`.`nic` AS `nic`,`m`.`isworking` AS `isworking`,`m`.`create_id` AS `create_id`,`m`.`create_time` AS `create_time`,`m`.`update_id` AS `update_id`,`m`.`update_time` AS `update_time`,`m`.`status` AS `status`,`s`.`sceneid` AS `sceneid` from (`mserver` `m` left join `scene_server` `s` on((`m`.`id` = `s`.`serverid`))) where (`s`.`status` = 1);

SET FOREIGN_KEY_CHECKS = 1;

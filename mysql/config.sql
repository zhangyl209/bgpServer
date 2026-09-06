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

 Date: 26/11/2024 11:38:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
-- Records of config
-- ----------------------------
INSERT INTO `config` VALUES (1, 'monitorDir', '上传监控目录', 'D:\\monitorDir', '监测文件目录', 1, 1, 1);
INSERT INTO `config` VALUES (2, 'monitorInterval', '监控轮询间隔(s)', '5', '轮询文件夹间隔时间(秒)', 1, 1, 1);
INSERT INTO `config` VALUES (3, 'initUpload', '是否重检已有文件', '1', '是否重检已有文件,重检会消耗较长时间', 1, 1, 1);
INSERT INTO `config` VALUES (4, 'bakStorage', '备份/缓存文件目录', 'D:\\bakStorageDir', '备份/缓存文件目录', 1, 1, 1);
INSERT INTO `config` VALUES (5, 'bakStorageAutoDelte', '是否自动清除备份/缓存文件', '0', '是否自动清除备份/缓存文件', 1, 1, 1);
INSERT INTO `config` VALUES (6, 'uploadDirectory2d', '上传二维影像的文件夹', '二维影像上传目录', '上传二维影像的文件夹', 1, 1, 1);
INSERT INTO `config` VALUES (7, 'uploadDirectory3d', '上传三维影像的文件夹', '三维影像上传目录', '上传三维影像的文件夹', 1, 1, 1);
INSERT INTO `config` VALUES (8, 'minioEndpoint', '数据存储URL', 'http://127.0.0.1:9000', NULL, 1, 1, 1);
INSERT INTO `config` VALUES (9, 'minioAccesskey', '数据存储Accesskey', 'PhDNsRYldnRQlHzYHsb1', NULL, 1, 1, 1);
INSERT INTO `config` VALUES (10, 'minioSecretKey', '数据存储SecretKey', 'Wu994e7JYgfxltE0lO7vdJM0KdxP3GVEPTndG5br', NULL, 1, 1, 1);
INSERT INTO `config` VALUES (11, 'minioBucket', '数据存储Bucket', 'museum', NULL, 1, 1, 1);
INSERT INTO `config` VALUES (12, 'bakStorage2d', '备份二维影像文件夹', '2dStorage', NULL, 1, 1, 1);
INSERT INTO `config` VALUES (13, 'bakStorage3d', '备份三维影像文件夹', '3dStorage', NULL, 1, 1, 1);
INSERT INTO `config` VALUES (14, 'doCatalog2dType', '自动编目的二维影像类型', NULL, '除此之外的只备份；不指定则都自动编目', 0, 1, 1);
INSERT INTO `config` VALUES (15, 'ldtoolsLicenseFilePath', 'LeadTools License', 'D:\\zdy\\ldtools\\LEADTOOLS.lic', NULL, 1, 0, 1);
INSERT INTO `config` VALUES (16, 'ldtoolsDeveloperKey', 'LeadTools Key', 'wMwbXtlTq5b5bVzDTdE9k9WRAsBjLjUIld2a3sFpvjO5oKXYKmPI136YpXqhCnFE', NULL, 1, 0, 1);
INSERT INTO `config` VALUES (17, 'ldtoolsLibPath', 'LeadTools LibPath', 'D:\\zdy\\LEADTOOLS 20\\Bin\\CDLL\\x64', NULL, 1, 0, 1);
INSERT INTO `config` VALUES (18, 'doDrawToSetsType', '自动抽点的二维影像类型', NULL, '除此之外的只编目；不指定则都自动抽点', 0, 1, 1);
INSERT INTO `config` VALUES (19, 'liulanPixel', '浏览图像素', '1024', NULL, 1, 1, 1);
INSERT INTO `config` VALUES (20, 'suoluePixel', '缩略图像素', '128', NULL, 1, 1, 1);
INSERT INTO `config` VALUES (21, 'directory3d0', '第一高清三维影像文件夹包含的关键字', '原始级', '第一高清;关键字在其他地方不出现', 1, 1, 1);
INSERT INTO `config` VALUES (22, 'directory3d1', '第二高清三维影像文件夹包含的关键字', '研究级', '第二高清;关键字在其他地方不出现', 1, 1, 1);
INSERT INTO `config` VALUES (23, 'directory3d2', '第三高清三维影像文件夹包含的关键字', '浏览级', '第三高清;关键字在其他地方不出现', 1, 1, 1);
INSERT INTO `config` VALUES (24, 'directory3d3', '网页浏览三维影像文件夹包含的关键字', '展示级', '第三高清;关键字在其他地方不出现', 1, 1, 1);
INSERT INTO `config` VALUES (25, 'map_Ka', 'map_Ka环境光纹理映射文件名包含的关键字', 'tietu', '一般指贴图文件', 0, 1, 1);
INSERT INTO `config` VALUES (26, 'map_Kd', 'map_Kd漫反射纹理映射文件名包含的关键字', 'tietu', '一般指贴图文件', 0, 1, 1);
INSERT INTO `config` VALUES (27, 'map_Ks', 'map_Ks镜面反射纹理映射文件名包含的关键字', NULL, NULL, 0, 1, 1);
INSERT INTO `config` VALUES (28, 'map_Ns', 'map_Ns镜面反射高光纹理映射文件名包含的关键字', NULL, NULL, 0, 1, 1);
INSERT INTO `config` VALUES (29, 'map_d', 'map_d透明度纹理映射文件名包含的关键字', NULL, NULL, 0, 1, 1);
INSERT INTO `config` VALUES (30, 'disp', 'disp位移纹理映射文件名包含的关键字', NULL, NULL, 0, 1, 1);
INSERT INTO `config` VALUES (31, 'decal', 'decal贴花纹理映射文件名包含的关键字', NULL, NULL, 0, 1, 1);
INSERT INTO `config` VALUES (32, 'bump', 'bump凹凸纹理映射文件名包含的关键字', 'faxian', NULL, 0, 1, 1);
INSERT INTO `config` VALUES (33, 'uploadDirectory', '页面上传文件后台保存目录', '/root/uploads', '不指定，会存临时文件夹', 0, 1, 1);
INSERT INTO `config` VALUES (100, 'influxDBUrl', 'influxDB地址', 'http://120.53.103.172:8086', 'http://127.0.0.1:8086', 0, 0, 1);
INSERT INTO `config` VALUES (101, 'influxDBToken', 'influxDB Token', 'U7g_-9VuMLMAVidg3iAEzDp-q5E-WEbYnksFy8yh-ucORjTiushmcg8vG65yEvTyys6kw9wNo0s-eeEj4nzasA==', '', 0, 0, 1);
INSERT INTO `config` VALUES (102, 'influxDBOrg', 'influxDB 组织名配置', 'bgp', '', 0, 0, 1);
INSERT INTO `config` VALUES (103, 'influxDBBucket', 'influxDB Bucket桶名配置', 'bgp', NULL, 0, 0, 1);
INSERT INTO `config` VALUES (104, 'nestIP', 'nest IP', '82.156.35.224', '192.168.100.100', 0, 0, 1);
INSERT INTO `config` VALUES (105, 'serverOnline', '服务器是否在线', '123.56.141.168:8500', '192.168.100.100', 0, 0, 1);
INSERT INTO `config` VALUES (106, 'assignNode', '分配节点到服务器', '123.56.141.168:33333', '192.168.100.100', 0, 0, 1);
INSERT INTO `config` VALUES (107, 'routesAndPolicies', '导入初始路由和分配商业策略', '82.156.35.224:11223', '192.168.100.100', 0, 0, 1);

SET FOREIGN_KEY_CHECKS = 1;

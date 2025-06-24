/*
 Navicat Premium Data Transfer

 Source Server         : shen
 Source Server Type    : MySQL
 Source Server Version : 80035
 Source Host           : localhost:3306
 Source Schema         : db_proj

 Target Server Type    : MySQL
 Target Server Version : 80035
 File Encoding         : 65001

 Date: 24/06/2025 08:44:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for address
-- ----------------------------
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT 'id自动增长列',
  `user_id` int(0) NOT NULL COMMENT '用户编号',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收件人姓名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '收件人固定电话号码',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收件人手机号码',
  `province` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '省份',
  `city` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '城市',
  `district` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '区/县',
  `addr` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '详细地址',
  `zip` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮编',
  `dfault` tinyint(1) DEFAULT 0 COMMENT '是否是默认地址，0-非默认，1-默认',
  `isDel` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除，0：正常，1：已删除',
  `created` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `updated` datetime(0) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '收货地址表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '订单ID，自动增长列',
  `order_no` bigint(0) NOT NULL COMMENT '订单编号',
  `uid` int(0) NOT NULL COMMENT '用户编号',
  `addr_id` int(0) NOT NULL COMMENT '收货地址编号',
  `amount` decimal(20, 2) NOT NULL COMMENT '订单付款金额',
  `type` int(0) NOT NULL COMMENT '付款类型，1-在线支付，2-货到付款',
  `freight` int(0) NOT NULL COMMENT '运费',
  `status` int(0) NOT NULL COMMENT '订单状态：1-未付款，2-已付款，3-已发货，4-交易成功，5-交易关闭，6-已取消',
  `payment_time` datetime(0) DEFAULT NULL COMMENT '支付时间',
  `delivery_time` datetime(0) DEFAULT NULL COMMENT '发货时间',
  `finish_time` datetime(0) DEFAULT NULL COMMENT '交易完成时间',
  `close_time` datetime(0) DEFAULT NULL COMMENT '交易关闭时间',
  `created` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `updated` datetime(0) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_order_no`(`order_no`) USING BTREE,
  INDEX `idx_uid`(`uid`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_created`(`created`) USING BTREE,
  INDEX `fk_order_address`(`addr_id`) USING BTREE,
  CONSTRAINT `fk_order_address` FOREIGN KEY (`addr_id`) REFERENCES `address` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_order_user` FOREIGN KEY (`uid`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '订单项ID，自动增长列',
  `uid` int(0) NOT NULL COMMENT '用户编号',
  `order_id` int(0) NOT NULL COMMENT '所属订单Id',
  `goods_id` int(0) NOT NULL COMMENT '商品编号',
  `goods_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `icon_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品主图',
  `price` decimal(20, 2) NOT NULL COMMENT '商品单价',
  `quantity` int(0) NOT NULL COMMENT '购买的商品数量',
  `total_price` decimal(20, 2) NOT NULL COMMENT '订单项总价格',
  `created` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `updated` datetime(0) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_uid`(`uid`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE,
  INDEX `idx_goods_id`(`goods_id`) USING BTREE,
  CONSTRAINT `fk_order_item_order` FOREIGN KEY (`order_id`) REFERENCES `order` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_order_item_product` FOREIGN KEY (`goods_id`) REFERENCES `product` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_order_item_user` FOREIGN KEY (`uid`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单项表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '商品编号，自动增长列',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称(配件)',
  `product_id` int(0) NOT NULL COMMENT 'action_params表中parent_id为0的分类',
  `parts_id` int(0) NOT NULL COMMENT '配件分类,对应action_params表中parent_id非0参数',
  `icon_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商品主图片',
  `sub_images` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '图片地址，一组小图',
  `detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '商品详情',
  `spec_param` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '规格参数',
  `price` decimal(20, 2) NOT NULL COMMENT '价格：单位元，保留两位小数',
  `stock` int(0) NOT NULL COMMENT '库存',
  `status` int(0) NOT NULL DEFAULT 1 COMMENT '商品的状态：1-待售，刚保存；2-上架，在售；3-下架，停售；默认值为1',
  `is_hot` int(0) NOT NULL DEFAULT 2 COMMENT '是否热销，1-是，2-否；默认值为2',
  `created` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `updated` datetime(0) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE,
  INDEX `idx_parts_id`(`parts_id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_is_hot`(`is_hot`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for product_type
-- ----------------------------
DROP TABLE IF EXISTS `product_type`;
CREATE TABLE `product_type`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT 'id自动增长列',
  `parent_id` int(0) NOT NULL COMMENT '父类ID，id为0时为根结点，为一类节点',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类别名称',
  `sort_order` int(0) DEFAULT NULL COMMENT '同类展示顺序',
  `status` tinyint(1) NOT NULL COMMENT '状态编码，1有效，0无效',
  `level` int(0) DEFAULT NULL COMMENT '节点级别,顶层节点为0，依次类推',
  `created` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `updated` datetime(0) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id`) USING BTREE,
  INDEX `idx_sort_order`(`sort_order`) USING BTREE,
  INDEX `idx_level`(`level`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for shopping_cart
-- ----------------------------
DROP TABLE IF EXISTS `shopping_cart`;
CREATE TABLE `shopping_cart`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '编号，自动增长列',
  `user_id` int(0) NOT NULL COMMENT '用户id',
  `product_id` int(0) NOT NULL COMMENT '商品id',
  `quantity` int(0) NOT NULL COMMENT '商品数量',
  `created` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `updated` datetime(0) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE,
  CONSTRAINT `fk_cart_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_cart_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '购物车表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT 'id自动增长列',
  `account` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码，MD5加密',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '电话',
  `question` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '密码问题',
  `asw` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '找回密码答案',
  `role` int(0) NOT NULL COMMENT '角色 1-普通用户、2-管理员',
  `age` int(0) NOT NULL COMMENT '年龄，在0到120之间取值',
  `sex` tinyint(1) NOT NULL COMMENT '性别，1：男、0：女',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  `del` tinyint(1) NOT NULL DEFAULT 0 COMMENT '正常：0、删除：1，默认为0',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

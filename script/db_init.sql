/*
 Navicat Premium Data Transfer

 Source Server         : mysql@5.7
 Source Server Type    : MySQL
 Source Server Version : 50723
 Source Host           : localhost
 Source Database       : market_cap

 Target Server Type    : MySQL
 Target Server Version : 50723
 File Encoding         : utf-8

 Date: 09/29/2018 23:32:33 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `t_token_holder`
-- ----------------------------
DROP TABLE IF EXISTS `t_token_holder`;
CREATE TABLE `t_token_holder` (
  `token_address` varchar(80) DEFAULT NULL,
  `holder_address` varchar(80) DEFAULT NULL,
  `quantity` varchar(40) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `t_token_ico_info`
-- ----------------------------
DROP TABLE IF EXISTS `t_token_ico_info`;
CREATE TABLE `t_token_ico_info` (
  `token_address` varchar(40) NOT NULL,
  `ico_start_date` bigint(4) DEFAULT NULL,
  `ico_end_date` bigint(4) DEFAULT NULL,
  `hard_cap` varchar(20) DEFAULT NULL,
  `soft_cap` varchar(20) DEFAULT NULL,
  `token_raised` varchar(20) DEFAULT NULL,
  `ico_price` varchar(20) DEFAULT NULL,
  `from_country` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`token_address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `t_token_info`
-- ----------------------------
DROP TABLE IF EXISTS `t_token_info`;
CREATE TABLE `t_token_info` (
  `address` varchar(40) NOT NULL,
  `name` varchar(80) DEFAULT NULL,
  `symbol` varchar(20) DEFAULT NULL,
  `website` varchar(80) DEFAULT NULL,
  `decimals` int(4) DEFAULT NULL,
  PRIMARY KEY (`address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `t_token_kline`
-- ----------------------------
DROP TABLE IF EXISTS `t_token_kline`;
CREATE TABLE `t_token_kline` (
  `id` varchar(40) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `t_token_market_cap`
-- ----------------------------
DROP TABLE IF EXISTS `t_token_market_cap`;
CREATE TABLE `t_token_market_cap` (
  `token_address` varchar(80) NOT NULL COMMENT 'token address',
  `cap_usd` varchar(40) DEFAULT NULL COMMENT '市值(usd)',
  `cap_btc` varchar(40) DEFAULT NULL COMMENT '市值(btc)',
  `cap_eth` varchar(40) DEFAULT NULL COMMENT '市值(eth)',
  `cap_lrc` varchar(40) DEFAULT NULL COMMENT '市值(lrc)',
  `price_usd` varchar(40) DEFAULT NULL COMMENT '价格(usd)',
  `price_btc` varchar(40) DEFAULT NULL COMMENT '价格(btc)',
  `price_eth` varchar(40) DEFAULT NULL COMMENT '价格(eth)',
  `price_lrc` varchar(40) DEFAULT NULL COMMENT '价格(lrc)',
  `volume_usd` varchar(40) DEFAULT NULL COMMENT '24小时交易量(usd)',
  `volume_btc` varchar(40) DEFAULT NULL COMMENT '24小时交易量(btc)',
  `volume_eth` varchar(40) DEFAULT NULL COMMENT '24小时交易量(eth)',
  `volume_lrc` varchar(40) DEFAULT NULL COMMENT '24小时交易量(lrc)',
  `created_at` bigint(4) DEFAULT NULL COMMENT '创建时间',
  `modified_at` bigint(4) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`token_address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;

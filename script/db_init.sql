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
  `token_address` varchar(42) NOT NULL,
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
  `protocol` varchar(42) NOT NULL,
  `deny` TINYINT(1) DEFAULT 0,
  `is_market` TINYINT(1) DEFAULT 0,
  `symbol` varchar(80) NOT NULL,
  `source` varchar(80) DEFAULT NULL,
  `decimals` int(4) NOT NULL,
  PRIMARY KEY (`Protocol`)
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

-- ----------------------------
--  Table structure for `lpr_cmc_ticker_infos`
-- ----------------------------
DROP TABLE IF EXISTS `lpr_cmc_ticker_info`;
CREATE TABLE `lpr_cmc_ticker_infos` (
  `id` int(11) NOT NULL COMMENT 'id',
  `name` varchar(60) DEFAULT NULL COMMENT 'name',
  `symbol` varchar(40) DEFAULT NULL COMMENT 'symbol',
  `website_slug` varchar(60) DEFAULT NULL COMMENT 'website slug',
  `market` varchar(40) DEFAULT NULL COMMENT 'market',
  `rank` int(11) NOT NULL COMMENT 'rank',
  `circulating_supply` varchar(40) DEFAULT NULL COMMENT 'circulating_supply',
  `total_supply` varchar(40) DEFAULT NULL COMMENT 'total_supply',
  `price`varchar(40) DEFAULT NULL COMMENT 'price',
  `volume_24h` varchar(40) DEFAULT NULL COMMENT 'volume_24h',
  `market_cap` varchar(40) DEFAULT NULL COMMENT 'market_cap',
  `percent_change_1h` varchar(40) DEFAULT NULL COMMENT 'percent_change_1h',
  `percent_change_24h` varchar(40) DEFAULT NULL COMMENT 'percent_change_24h',
  `percent_change_7d` varchar(40) DEFAULT NULL COMMENT 'percent_change_7d',
  `last_updated` bigint(20) DEFAULT NULL COMMENT 'last_updated',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;

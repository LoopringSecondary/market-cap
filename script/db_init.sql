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
--  Table structure for `t_token_ico_info`
-- ----------------------------
DROP TABLE IF EXISTS `t_token_ico_info`;
CREATE TABLE `t_token_ico_info` (
  `token_address` varchar(42) NOT NULL,
  `ico_start_date` varchar(20) DEFAULT NULL,
  `ico_end_date` varchar(20) DEFAULT NULL,
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
--  Table structure for `lpr_token_tickers`
-- ----------------------------
DROP TABLE IF EXISTS `lpr_token_tickers`;
CREATE TABLE `lpr_token_tickers` (
  `token_id` int(11) NOT NULL COMMENT 'token_id',
  `token_name` varchar(60) DEFAULT NULL COMMENT 'name',
  `symbol` varchar(40) NOT NULL COMMENT 'symbol',
  `website_slug` varchar(60) NOT NULL COMMENT 'website slug',
  `market` varchar(40) NOT NULL COMMENT 'market',
  `cmc_rank` int(11) DEFAULT NULL COMMENT 'rank',
  `circulating_supply` double  DEFAULT NULL COMMENT 'circulating_supply',
  `total_supply` double DEFAULT NULL COMMENT 'total_supply',
  `max_supply` double DEFAULT NULL COMMENT 'max_supply',
  `price` double DEFAULT NULL COMMENT 'price',
  `volume_24h` double DEFAULT NULL COMMENT 'volume_24h',
  `market_cap` double DEFAULT NULL COMMENT 'market_cap',
  `percent_change_1h` double DEFAULT NULL COMMENT 'percent_change_1h',
  `percent_change_24h` double DEFAULT NULL COMMENT 'percent_change_24h',
  `percent_change_7d` double DEFAULT NULL COMMENT 'percent_change_7d',
  `last_updated` bigint(20) DEFAULT NULL COMMENT 'last_updated',
  PRIMARY KEY (`website_slug`,`market`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_exchange_ticker_info`;
CREATE TABLE `t_exchange_ticker_info` (
  `symbol` varchar(40) NOT NULL COMMENT 'symbol',
  `market` varchar(40) NOT NULL COMMENT 'market',
  `exchange` varchar(60) NOT NULL COMMENT 'exchange',
  `price` double DEFAULT NULL COMMENT 'price',
  `price_usd` double DEFAULT NULL COMMENT 'price_usd',
  `price_cny` double DEFAULT NULL COMMENT 'price_cny',
  `volume_24h_usd` double DEFAULT NULL COMMENT 'volume_24h_usd',
  `volume_24h` double DEFAULT NULL COMMENT 'volume_24h',
  `volume_24h_from` double DEFAULT NULL COMMENT 'volume_24h_from',
  `percent_change_utc0` double DEFAULT NULL COMMENT 'percent_change_utc0',
  `alias` varchar(80) DEFAULT NULL COMMENT 'alias',
  `last_updated` bigint(20) DEFAULT NULL COMMENT 'timestamp',
  PRIMARY KEY (`exchange`,`symbol`,`market`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;

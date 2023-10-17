/*
Navicat MySQL Data Transfer

Source Server         : 192.168.9.101
Source Server Version : 50717
Source Host           : 192.168.9.101:3306
Source Database       : chainexplorer

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2021-10-14 13:31:15
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `addr_balances`
-- ----------------------------
DROP TABLE IF EXISTS `addr_balances`;
CREATE TABLE `addr_balances` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`address`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '钱包地址' ,
`contract`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '代币智能合约地址' ,
`blocknumber`  bigint(20) NOT NULL DEFAULT 0 COMMENT '区块高度' ,
`balance`  decimal(65,0) NOT NULL DEFAULT 0 COMMENT '代币余额' ,
`nonce`  bigint(20) NOT NULL DEFAULT 0 COMMENT '发送交易数' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `unique` (`address`, `contract`, `blocknumber`) USING BTREE ,
INDEX `address` (`address`) USING BTREE ,
INDEX `contract` (`contract`) USING BTREE ,
INDEX `blocknumber` (`blocknumber`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='历史余额变动情况'
AUTO_INCREMENT=1295597

;

-- ----------------------------
-- Table structure for `addresses`
-- ----------------------------
DROP TABLE IF EXISTS `addresses`;
CREATE TABLE `addresses` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`address`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '钱包地址' ,
`contract`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '代币智能合约地址' ,
`balance`  decimal(65,0) NOT NULL DEFAULT 0 COMMENT '代币余额' ,
`nonce`  bigint(20) NOT NULL DEFAULT 0 COMMENT '发送交易数' ,
`blocknumber`  bigint(20) NOT NULL DEFAULT 0 COMMENT '余额变动最后区块高度' ,
`inserted_time`  datetime NULL DEFAULT NULL COMMENT '注册时间' ,
`haslock` int(4) DEFAULT NULL COMMENT '是否有锁仓记录',
`ful_balance`  decimal(65,0) NULL DEFAULT 0 COMMENT 'Ful余额' ,
`ful_nonce`  bigint(20) NULL DEFAULT 0 COMMENT 'Ful交易次数' ,
`ful_owe`  decimal(65,0) NULL DEFAULT 0 COMMENT 'Ful' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `unique` (`address`, `contract`) USING BTREE ,
INDEX `address` (`address`) USING BTREE ,
INDEX `contract` (`contract`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='钱包地址信息'
AUTO_INCREMENT=765643

;

-- ----------------------------
-- Table structure for `addresses_token`
-- ----------------------------
DROP TABLE IF EXISTS `addresses_token`;
CREATE TABLE `addresses_token` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`address`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '钱包地址' ,
`contract`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '代币智能合约地址' ,
`balance`  decimal(65,0) NOT NULL DEFAULT 0 COMMENT '代币余额' ,
`nonce`  bigint(20) NOT NULL DEFAULT 0 COMMENT '发送交易数' ,
`blocknumber`  bigint(20) NOT NULL DEFAULT 0 COMMENT '余额变动最后区块高度' ,
`inserted_time`  datetime NULL DEFAULT NULL COMMENT '注册时间' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `unique` (`address`, `contract`) USING BTREE ,
INDEX `address` (`address`) USING BTREE ,
INDEX `contract` (`contract`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='代币交易钱包地址信息'
AUTO_INCREMENT=415

;

-- ----------------------------
-- Table structure for `addrfriendly`
-- ----------------------------
DROP TABLE IF EXISTS `addrfriendly`;
CREATE TABLE `addrfriendly` (
`address`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '钱包地址' ,
`aliasname`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '地址友好名称' ,
`iscandidate`  tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否是矿工地址' ,
`metadata`  json NULL DEFAULT NULL COMMENT '地址说明内容' ,
PRIMARY KEY (`address`),
INDEX `candidate` (`iscandidate`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='地址友好名称'

;

-- ----------------------------
-- Table structure for `api_config`
-- ----------------------------
DROP TABLE IF EXISTS `api_config`;
CREATE TABLE `api_config` (
`id`  bigint(11) NOT NULL AUTO_INCREMENT ,
`config_key`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配置key' ,
`config_memo`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配置备注' ,
`description`  varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '配置描述' ,
`config_value`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '值' ,
`updated_by`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人' ,
`type`  tinyint(2) NULL DEFAULT NULL COMMENT '类型(0:input  2:response )' ,
`fokid`  bigint(11) NULL DEFAULT NULL ,
`valuesconf`  json NULL DEFAULT NULL COMMENT '数据值' ,
`code`  bigint(11) NULL DEFAULT NULL ,
`resultdesc`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`isshow`  bigint(20) NULL DEFAULT NULL ,
PRIMARY KEY (`id`),
FOREIGN KEY (`fokid`) REFERENCES `api_configplat` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
INDEX `fokid` (`fokid`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='api参数配置子表'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `api_configplat`
-- ----------------------------
DROP TABLE IF EXISTS `api_configplat`;
CREATE TABLE `api_configplat` (
`id`  bigint(11) NOT NULL AUTO_INCREMENT ,
`group_name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分组名称' ,
`url`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求地址' ,
`descion`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`method`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '方法值' ,
`action`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求的action' ,
`method_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`types`  bigint(2) NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='api参数配置主表'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `api_configplat_en`
-- ----------------------------
DROP TABLE IF EXISTS `api_configplat_en`;
CREATE TABLE `api_configplat_en` (
`id`  bigint(11) NOT NULL AUTO_INCREMENT ,
`group_name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分组名称' ,
`url`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求地址' ,
`descion`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`method`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '方法值' ,
`action`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求的action' ,
`method_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`types`  bigint(2) NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='api参数配置主表'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `block_fork`
-- ----------------------------
DROP TABLE IF EXISTS `block_fork`;
CREATE TABLE `block_fork` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`nephewhash`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '分叉区块Hash' ,
`nephewnumber`  bigint(20) NOT NULL DEFAULT 0 COMMENT '分叉区块高度' ,
`istrunk`  tinyint(4) NOT NULL DEFAULT 0 COMMENT '分叉区块是否是主干' ,
`unclehash`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '叔伯区块Hash' ,
`unclehandled`  tinyint(4) NOT NULL DEFAULT 0 COMMENT '叔伯区块是否处理' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `unique` (`nephewhash`, `unclehash`) USING BTREE ,
INDEX `nephewhash` (`nephewhash`) USING BTREE ,
INDEX `nephewnumber` (`istrunk`, `nephewnumber`) USING BTREE ,
INDEX `unclehash` (`unclehash`) USING BTREE ,
INDEX `unclehandled` (`unclehandled`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='区块分叉信息'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `block_rewards`
-- ----------------------------
DROP TABLE IF EXISTS `block_rewards`;
CREATE TABLE `block_rewards` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`blockhash`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '区块Hash' ,
`blocknumber`  bigint(20) NULL DEFAULT NULL COMMENT '区块高度' ,
`istrunk`  tinyint(4) NOT NULL DEFAULT 0 COMMENT '区块是否是主干' ,
`address`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '钱包地址' ,
`rewardtype`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '奖励类型' ,
`rewardhash`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '奖励区块Hash' ,
`reward`  decimal(65,0) NOT NULL DEFAULT 0 COMMENT '区块奖励' ,
`timestamp`  datetime NULL DEFAULT NULL COMMENT '区块时间' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `unique` (`blockhash`, `address`, `rewardtype`, `rewardhash`) USING BTREE ,
INDEX `blockhash` (`blockhash`) USING BTREE ,
INDEX `blocknumber` (`istrunk`) USING BTREE ,
INDEX `address` (`address`) USING BTREE ,
INDEX `rewardtype` (`rewardtype`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='区块奖励'
AUTO_INCREMENT=256757

;

-- ----------------------------
-- Table structure for `blocks`
-- ----------------------------
DROP TABLE IF EXISTS `blocks`;
CREATE TABLE `blocks` (
`hash`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '区块Hash' ,
`blocknumber`  bigint(20) NULL DEFAULT NULL COMMENT '区块高度' ,
`istrunk`  tinyint(4) NOT NULL DEFAULT 0 COMMENT '区块是否是主干' ,
`timestamp`  datetime NOT NULL COMMENT '出块时间' ,
`mineraddress`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '矿工地址' ,
`blocksize`  int(11) NOT NULL DEFAULT 0 COMMENT '区块字节数' ,
`gaslimit`  bigint(20) NOT NULL DEFAULT 0 COMMENT '手续费上限' ,
`gasused`  bigint(20) NOT NULL DEFAULT 0 COMMENT '实际手续费' ,
`reward`  decimal(65,0) NOT NULL DEFAULT 0 COMMENT '区块奖励' ,
`txscount`  int(11) NOT NULL DEFAULT 0 COMMENT '交易数量' ,
`nonce`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区块Nonce' ,
`difficulty`  bigint(20) NULL DEFAULT NULL COMMENT '区块难度系数' ,
`totaldifficulty`  bigint(20) NULL DEFAULT NULL COMMENT '链难度系数' ,
`parenthash`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父区块Hash' ,
`round`  int(11) NULL DEFAULT 0 COMMENT '当前区块高度所在的投票的期数' ,
PRIMARY KEY (`hash`),
INDEX `blocktime` (`timestamp`, `istrunk`) USING BTREE ,
INDEX `blocknumber` (`blocknumber`, `istrunk`) USING BTREE ,
INDEX `mineraddress` (`mineraddress`, `istrunk`) USING BTREE ,
INDEX `istrunk` (`istrunk`, `blocknumber`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='区块信息'

;

-- ----------------------------
-- Table structure for `contractdecompiled`
-- ----------------------------
DROP TABLE IF EXISTS `contractdecompiled`;
CREATE TABLE `contractdecompiled` (
`contract`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '智能合约地址' ,
`version`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '反编译版本' ,
`sourcecode`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '反编译代码' ,
PRIMARY KEY (`contract`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='智能合约反编译信息'

;

-- ----------------------------
-- Table structure for `contracts`
-- ----------------------------
DROP TABLE IF EXISTS `contracts`;
CREATE TABLE `contracts` (
`hash`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '智能合约地址' ,
`transhash`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '智能合约创建交易Hash' ,
`istoken`  tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否是代币' ,
PRIMARY KEY (`hash`),
INDEX `istoken` (`istoken`) USING BTREE ,
INDEX `transhash` (`transhash`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='智能合约列表'

;

-- ----------------------------
-- Table structure for `contractverified`
-- ----------------------------
DROP TABLE IF EXISTS `contractverified`;
CREATE TABLE `contractverified` (
`contract`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '智能合约地址' ,
`name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '智能合约名称' ,
`version`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '智能合约版本' ,
`optimization`  tinyint(4) NOT NULL COMMENT '是否已经优化' ,
`abi`  json NOT NULL COMMENT '智能合约Abi' ,
`sourcecode`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '智能合约代码' ,
PRIMARY KEY (`contract`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='智能合约验证信息'

;

-- ----------------------------
-- Table structure for `dposcandidate`
-- ----------------------------
DROP TABLE IF EXISTS `dposcandidate`;
CREATE TABLE `dposcandidate` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`loopstarttime`  datetime NOT NULL COMMENT '本轮投票开始时间' ,
`candidate`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '候选人地址' ,
`tally`  decimal(65,0) NOT NULL COMMENT '候选人总权益' ,
`state`  int(11) NOT NULL COMMENT '候选人状态：\n    0 - 刚当选\n    1 - 保持当选\n    2 - 刚落选' ,
PRIMARY KEY (`id`),
INDEX `loopstarttime` (`loopstarttime`) USING BTREE ,
INDEX `candidate` (`candidate`) USING BTREE ,
INDEX `unique` (`loopstarttime`, `candidate`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='候选人列表'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `dposdeclare`
-- ----------------------------
DROP TABLE IF EXISTS `dposdeclare`;
CREATE TABLE `dposdeclare` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`proposal`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '提议Hash' ,
`declarer`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '提议表决者地址' ,
`decision`  tinyint(4) NOT NULL COMMENT '决定' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `unique` (`proposal`, `declarer`) USING BTREE ,
INDEX `proposal` (`proposal`) USING BTREE ,
INDEX `declarer` (`declarer`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='建议投票情况'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `dposhardware`
-- ----------------------------
DROP TABLE IF EXISTS `dposhardware`;
CREATE TABLE `dposhardware` (
`address`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '钱包地址' ,
`processor`  decimal(10,3) NOT NULL DEFAULT 0.000 COMMENT 'CPU主频,单位:GHz' ,
`memory`  decimal(10,3) NOT NULL DEFAULT 0.000 COMMENT '内存大小,单位:GB' ,
`storage`  decimal(10,3) NOT NULL DEFAULT 0.000 COMMENT '存储容量,单位:GB' ,
`bandwidth`  decimal(10,3) NOT NULL DEFAULT 0.000 COMMENT '网络带宽,单位:MB' ,
`introduction`  varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '矿机配置简要说明' ,
PRIMARY KEY (`address`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='矿机硬件配置'

;

-- ----------------------------
-- Table structure for `dposnode`
-- ----------------------------
DROP TABLE IF EXISTS `dposnode`;
CREATE TABLE `dposnode` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`voter`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '节点地址' ,
`stake`  decimal(65,0) NOT NULL COMMENT '得票数量' ,
`blocknumber`  bigint(20) NOT NULL COMMENT '区块高度' ,
`round`  int(11) NULL DEFAULT 0 ,
`type`  int(11) NULL DEFAULT 0 ,
PRIMARY KEY (`id`),
UNIQUE INDEX `unique` (`stake`, `voter`, `blocknumber`) USING BTREE ,
INDEX `voter` (`voter`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='节点数据'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `dposproposal`
-- ----------------------------
DROP TABLE IF EXISTS `dposproposal`;
CREATE TABLE `dposproposal` (
`hash`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '提议Hash' ,
`receivednumber`  bigint(20) NOT NULL COMMENT '收到提议的区块高度' ,
`proposaltype`  int(11) NOT NULL COMMENT '提议类型：\n    1 - 增加候选人数\n    2 - 减少候选人数\n    3 - 改变矿工奖励百分比\n    4 - 改变最少投票余额\n    5 - 改变提议保证金额\n    6 - 侧链加入\n    7 - 侧链退出\n    8 - 侧链代币兑换' ,
`proposer`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '提议发起人地址' ,
`currentdeposit`  decimal(65,0) NOT NULL COMMENT '提议发起人缴纳的保证金' ,
`validationloopcnt`  int(11) NOT NULL COMMENT '提议有效区块数' ,
`decisioncount`  int(11) NOT NULL DEFAULT 0 COMMENT '收到投票数' ,
`targetaddress`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '候选人地址' ,
`minerreward`  int(11) NULL DEFAULT NULL COMMENT '矿工奖励千分比' ,
`voterbalance`  decimal(65,0) NULL DEFAULT NULL COMMENT '最少投票余额' ,
`proposaldeposit`  decimal(65,0) NULL DEFAULT NULL COMMENT '提议保证金额' ,
`schash`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '侧链Hash' ,
`scblockcount`  int(11) NULL DEFAULT NULL COMMENT '侧链每周期出块数量' ,
`scblockreward`  decimal(65,0) NULL DEFAULT NULL COMMENT '侧链每周期出块奖励' ,
`screntfee`  decimal(65,0) NULL DEFAULT NULL COMMENT '侧链代币兑换手续费' ,
`screntrate`  decimal(65,0) NULL DEFAULT NULL COMMENT '侧链代币兑换比例' ,
`screntlength`  int(11) NULL DEFAULT NULL COMMENT '侧链代币兑换区块数' ,
PRIMARY KEY (`hash`),
INDEX `receivenumber` (`receivednumber`) USING BTREE ,
INDEX `proposaltype` (`proposaltype`) USING BTREE ,
INDEX `proposer` (`proposer`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='提议列表'

;

-- ----------------------------
-- Table structure for `dposrepresentatives`
-- ----------------------------
DROP TABLE IF EXISTS `dposrepresentatives`;
CREATE TABLE `dposrepresentatives` (
`address`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '候选人地址' ,
`state`  smallint(6) NOT NULL DEFAULT 0 COMMENT '候选人矿机资格' ,
`level`  smallint(6) NOT NULL DEFAULT 0 COMMENT '候选人矿机等级' ,
`allowvoted`  tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否允许投票' ,
`country`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '候选人矿机所在国家' ,
`city`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '候选人矿机所在城市' ,
`location`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '候选人矿机所在位置' ,
`geo`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '候选人矿机GPS定位' ,
`tags`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '候选人矿机标签' ,
PRIMARY KEY (`address`),
INDEX `state` (`state`) USING BTREE ,
INDEX `level` (`level`) USING BTREE ,
INDEX `allowvoted` (`allowvoted`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='候选人简介'

;

-- ----------------------------
-- Table structure for `dpossigners`
-- ----------------------------
DROP TABLE IF EXISTS `dpossigners`;
CREATE TABLE `dpossigners` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`loopstarttime`  datetime NOT NULL COMMENT '本轮当选开始时间' ,
`signeraddress`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '合法矿工地址' ,
`punished`  bigint(20) NOT NULL DEFAULT 0 COMMENT '由于缺块受到的惩罚' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `unique` (`loopstarttime`, `signeraddress`) USING BTREE ,
INDEX `loopstarttime` (`loopstarttime`) USING BTREE ,
INDEX `signeraddress` (`signeraddress`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='出块矿工列表'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `dpossnapshot`
-- ----------------------------
DROP TABLE IF EXISTS `dpossnapshot`;
CREATE TABLE `dpossnapshot` (
`blocknumber`  bigint(20) NOT NULL COMMENT '区块高度' ,
`headertime`  datetime NOT NULL COMMENT '出块时间' ,
`confirmednumber`  int(11) NOT NULL COMMENT '确认数' ,
`loopstarttime`  datetime NOT NULL COMMENT '本轮投票开始时间' ,
`period`  int(11) NOT NULL COMMENT '平均出块时间' ,
`minerreward`  int(11) NOT NULL COMMENT '矿工奖励千分比' ,
`voterbalance`  decimal(65,0) NOT NULL COMMENT '最少投票余额' ,
PRIMARY KEY (`blocknumber`),
INDEX `loopstarttime` (`loopstarttime`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='DPOS参数'

;

-- ----------------------------
-- Table structure for `dposvotes`
-- ----------------------------
DROP TABLE IF EXISTS `dposvotes`;
CREATE TABLE `dposvotes` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`loopstarttime`  datetime NOT NULL COMMENT '本轮投票开始时间' ,
`candidate`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '候选人地址' ,
`voter`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '投票人地址' ,
`stake`  decimal(65,0) NOT NULL COMMENT '权益数量' ,
`blocknumber`  bigint(20) NOT NULL COMMENT '投票所在区块高度' ,
`round`  int(11) NULL DEFAULT 0 ,
PRIMARY KEY (`id`),
INDEX `loopstarttime` (`loopstarttime`) USING BTREE ,
INDEX `candidate` (`candidate`) USING BTREE ,
INDEX `voter` (`voter`) USING BTREE ,
INDEX `blocknumber` (`blocknumber`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='投票选举情况'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `dposvoteswallet`
-- ----------------------------
DROP TABLE IF EXISTS `dposvoteswallet`;
CREATE TABLE `dposvoteswallet` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`loopstarttime`  datetime NOT NULL COMMENT '本轮投票开始时间' ,
`candidate`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '候选人地址' ,
`voter`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '投票人地址' ,
`stake`  decimal(65,0) NOT NULL COMMENT '权益数量' ,
`blocknumber`  bigint(20) NOT NULL COMMENT '投票所在区块高度' ,
`round`  int(11) NULL DEFAULT 0 ,
`isexit`  int(11) NULL DEFAULT 0 COMMENT '是否已退出 1：没有退出,2:已经退出' ,
`exitblocknumber`  bigint(20) NOT NULL COMMENT '退出所在区块高度' ,
`txhash`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '投票hash' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `unique` (`voter`, `blocknumber`, `candidate`) USING BTREE ,
UNIQUE INDEX `txhash` (`txhash`) USING BTREE ,
INDEX `loopstarttime` (`loopstarttime`) USING BTREE ,
INDEX `candidate` (`candidate`) USING BTREE ,
INDEX `voter` (`voter`) USING BTREE ,
INDEX `blocknumber` (`blocknumber`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='钱包投票选举情况'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `job_config`
-- ----------------------------
DROP TABLE IF EXISTS `job_config`;
CREATE TABLE `job_config` (
`id`  bigint(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '作业ID主键' ,
`job_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '作业名称' ,
`namespace`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '命名空间' ,
`zk_list`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'zookeeper连接列表' ,
`job_class`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '作业类名' ,
`sharding_total_count`  int(11) NULL DEFAULT NULL COMMENT '作业分片总数' ,
`load_level`  int(11) NOT NULL DEFAULT 1 COMMENT '每个分片默认负荷' ,
`cron`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'cron表达式' ,
`pause_period_date`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '停止周期日期' ,
`pause_period_time`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '停止周期时间' ,
`sharding_item_parameters`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '分片序列号/参数对照表' ,
`job_parameter`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '作业参数' ,
`monitor_execution`  tinyint(1) NULL DEFAULT 1 COMMENT '监控异常' ,
`process_count_interval_seconds`  int(11) NULL DEFAULT NULL COMMENT '处理总数间隔秒数' ,
`concurrent_data_process_thread_count`  int(11) NULL DEFAULT NULL COMMENT '当前数据处理线程总数' ,
`fetch_data_count`  int(11) NULL DEFAULT NULL COMMENT '获取到的数据总数' ,
`max_time_diff_seconds`  int(11) NULL DEFAULT NULL COMMENT '最大时间相差的秒数' ,
`monitor_port`  int(11) NULL DEFAULT NULL COMMENT '监控端口' ,
`failover`  tinyint(1) NULL DEFAULT NULL COMMENT '是否为失效的作业' ,
`misfire`  tinyint(1) NULL DEFAULT NULL COMMENT '是否为被错过的作业(可能需要触发重发)' ,
`job_sharding_strategy_class`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '作业分片策略类' ,
`description`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '作业描述' ,
`timeout_seconds`  int(11) NULL DEFAULT NULL COMMENT '超时秒数' ,
`show_normal_log`  tinyint(1) NULL DEFAULT NULL COMMENT '是否显示正常日志' ,
`channel_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '渠道名称' ,
`job_type`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '作业类型' ,
`queue_name`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '队列名称' ,
`prefer_list`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '预分配列表' ,
`local_mode`  tinyint(1) NULL DEFAULT NULL COMMENT '是否启用本地模式' ,
`use_disprefer_list`  tinyint(1) NULL DEFAULT NULL COMMENT '是否使用非preferList' ,
`use_serial`  tinyint(1) NULL DEFAULT NULL COMMENT '消息作业是否启用串行消费，默认为并行消费' ,
`job_degree`  tinyint(1) NOT NULL DEFAULT 0 COMMENT '作业重要等级\\n    0: 没有定义\\n    1: 非线上业务\\n    2: 简单业务\\n    3: 一般业务\\n    4: 重要业务\\n    5: 核心业务' ,
`enabled_report`  tinyint(1) NULL DEFAULT NULL COMMENT '上报执行信息\\n    1: 开启上报\\n    0: 不开启上报\\n对于定时作业，默认开启上报；对于消息作业，默认不开启上报' ,
`dependencies`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '依赖的作业' ,
`groups`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属分组' ,
`timeout_4_alarm_seconds`  int(11) NOT NULL DEFAULT 0 COMMENT '超时（告警）秒数' ,
`time_zone`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'Asia/Shanghai' COMMENT '时区' ,
`is_enabled`  tinyint(1) NULL DEFAULT 0 COMMENT '是否启用标志' ,
`job_mode`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '作业模式' ,
`custom_context`  varchar(8192) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '自定义语境参数' ,
`rerun`  tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否重跑标志' ,
`up_stream`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '上游作业' ,
`down_stream`  varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '下游作业' ,
`backup1`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备用字段1' ,
`backup2`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备用字段2' ,
`backup3`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备用字段3' ,
`create_by`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_update_by`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最近一次的更新人' ,
`last_update_time`  timestamp NOT NULL DEFAULT '1979-12-31 08:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '最近一次的更新时间' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `uniq_namespace_job_name` (`namespace`, `job_name`) USING BTREE ,
INDEX `idx_namespace` (`namespace`) USING BTREE ,
INDEX `idx_zk_list` (`zk_list`) USING BTREE ,
INDEX `idx_job_name` (`job_name`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `job_config_history`
-- ----------------------------
DROP TABLE IF EXISTS `job_config_history`;
CREATE TABLE `job_config_history` (
`id`  bigint(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '作业ID主键' ,
`job_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '作业名称' ,
`job_class`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '作业类名' ,
`sharding_total_count`  int(11) NULL DEFAULT NULL COMMENT '作业分片总数' ,
`load_level`  int(11) NOT NULL DEFAULT 1 COMMENT '每个分片默认负荷' ,
`time_zone`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '时区' ,
`cron`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'cron表达式' ,
`pause_period_date`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '停止周期日期' ,
`pause_period_time`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '停止周期时间' ,
`sharding_item_parameters`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '分片序列号/参数对照表' ,
`job_parameter`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '作业参数' ,
`monitor_execution`  tinyint(1) NULL DEFAULT 1 COMMENT '监控异常' ,
`process_count_interval_seconds`  int(11) NULL DEFAULT NULL COMMENT '处理总数间隔秒数' ,
`concurrent_data_process_thread_count`  int(11) NULL DEFAULT NULL COMMENT '当前数据处理线程总数' ,
`fetch_data_count`  int(11) NULL DEFAULT NULL COMMENT '获取到的数据总数' ,
`max_time_diff_seconds`  int(11) NULL DEFAULT NULL COMMENT '最大时间相差的秒数' ,
`monitor_port`  int(11) NULL DEFAULT NULL COMMENT '监控端口' ,
`failover`  tinyint(1) NULL DEFAULT NULL COMMENT '是否为失效的作业' ,
`misfire`  tinyint(1) NULL DEFAULT NULL COMMENT '是否为被错过的作业(可能需要触发重发)' ,
`job_sharding_strategy_class`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '作业分片策略类' ,
`description`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '作业描述' ,
`timeout_4_alarm_seconds`  int(11) NULL DEFAULT NULL COMMENT '超时（告警）秒数' ,
`timeout_seconds`  int(11) NULL DEFAULT NULL COMMENT '超时（Kill线程/进程）秒数' ,
`show_normal_log`  tinyint(1) NULL DEFAULT NULL COMMENT '是否显示正常日志' ,
`channel_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '渠道名称' ,
`job_type`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '作业类型' ,
`queue_name`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '队列名称' ,
`namespace`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '命名空间' ,
`zk_list`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'zookeeper连接列表' ,
`prefer_list`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '预分配列表' ,
`local_mode`  tinyint(1) NULL DEFAULT NULL COMMENT '是否启用本地模式' ,
`use_disprefer_list`  tinyint(1) NULL DEFAULT NULL COMMENT '是否使用非preferList' ,
`use_serial`  tinyint(1) NULL DEFAULT NULL COMMENT '消息作业是否启用串行消费，默认为并行消费' ,
`job_degree`  tinyint(1) NULL DEFAULT NULL COMMENT '作业重要等级\\n    0: 没有定义\\n    1: 非线上业务\\n    2: 简单业务\\n    3: 一般业务\\n    4: 重要业务\\n    5: 核心业务' ,
`enabled_report`  tinyint(1) NULL DEFAULT NULL COMMENT '上报执行信息，对于定时作业，默认开启上报；对于消息作业，默认不开启上报' ,
`groups`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属分组' ,
`dependencies`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '依赖的作业' ,
`is_enabled`  tinyint(1) NULL DEFAULT 0 COMMENT '是否启用标志' ,
`job_mode`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '作业模式' ,
`custom_context`  varchar(8192) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '自定义语境参数' ,
`create_by`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人' ,
`create_time`  timestamp NULL DEFAULT NULL COMMENT '创建时间' ,
`last_update_by`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最近一次的更新人' ,
`last_update_time`  timestamp NULL DEFAULT NULL COMMENT '最近一次的更新时间' ,
PRIMARY KEY (`id`),
INDEX `job_name_idx` (`job_name`) USING BTREE ,
INDEX `namespace_idx` (`namespace`) USING BTREE ,
INDEX `zk_list_idx` (`zk_list`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='作业配置历史表'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `namespace_info`
-- ----------------------------
DROP TABLE IF EXISTS `namespace_info`;
CREATE TABLE `namespace_info` (
`id`  bigint(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键' ,
`namespace`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '域名' ,
`content`  varchar(16383) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '域名详细信息内容' ,
`bus_id`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '业务组id' ,
`is_deleted`  tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除\\n    0: 未删除\\n    1: 删除' ,
`create_time`  timestamp NOT NULL DEFAULT '1979-12-31 08:00:00' COMMENT '创建时间' ,
`created_by`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '创建人' ,
`last_update_time`  timestamp NOT NULL DEFAULT '1979-12-31 08:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '最近更新时间' ,
`last_updated_by`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '最近更新人' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `uniq_namespace_info_namespace` (`namespace`) USING BTREE ,
INDEX `idx_namespace_is_deleted` (`is_deleted`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='域名信息表'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `namespace_version_mapping`
-- ----------------------------
DROP TABLE IF EXISTS `namespace_version_mapping`;
CREATE TABLE `namespace_version_mapping` (
`id`  bigint(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主 键' ,
`namespace`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '域名' ,
`version_number`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '版本号' ,
`is_forced`  tinyint(1) NULL DEFAULT 0 COMMENT '当前版本已经不低于该版本时，是否强制使用该配置版本\\n    0: 不强制\\n    1: 强制' ,
`is_deleted`  tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除\\n    0: 未删除\\n    1: 删除' ,
`create_time`  timestamp NOT NULL DEFAULT '1979-12-31 08:00:00' COMMENT '创建时间' ,
`created_by`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '创建人' ,
`last_update_time`  timestamp NOT NULL DEFAULT '1979-12-31 08:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '最近更新时间' ,
`last_updated_by`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '最近更新人' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `uniq_nvm_namespace` (`namespace`) USING BTREE ,
INDEX `idx_nvm_version_number` (`version_number`) USING BTREE ,
INDEX `idx_nvm_is_deleted` (`is_deleted`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='域名版本配置表'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `namespace_zkcluster_mapping`
-- ----------------------------
DROP TABLE IF EXISTS `namespace_zkcluster_mapping`;
CREATE TABLE `namespace_zkcluster_mapping` (
`id`  bigint(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键' ,
`namespace`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '域名' ,
`name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '业务组' ,
`zk_cluster_key`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '集群key' ,
`is_deleted`  tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除\\n    0: 未删除\\n    1: 删除' ,
`create_time`  timestamp NOT NULL DEFAULT '1979-12-31 08:00:00' COMMENT '创建时间' ,
`created_by`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '创建人' ,
`last_update_time`  timestamp NOT NULL DEFAULT '1979-12-31 08:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '最近更新时间' ,
`last_updated_by`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '最近更新人' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `uniq_namespace` (`namespace`) USING BTREE ,
INDEX `idx_zk_cluster_key` (`zk_cluster_key`) USING BTREE ,
INDEX `idx_zk_is_deleted` (`is_deleted`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='域名集群映射表'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `nfc_bandwidth_config`
-- ----------------------------
DROP TABLE IF EXISTS `nfc_bandwidth_config`;
CREATE TABLE `nfc_bandwidth_config` (
`id`  tinyint(4) NOT NULL AUTO_INCREMENT ,
`pledge_param`  varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '质押调节参数' ,
`min`  int(8) NOT NULL COMMENT '宣称带宽区间min（Mbps）' ,
`max`  int(8) NULL DEFAULT NULL COMMENT '宣称带宽区间max（Mbps）' ,
`val`  decimal(4,2) NULL DEFAULT NULL COMMENT '质押调节参数值' ,
`update_time`  datetime NULL DEFAULT NULL ,
PRIMARY KEY (`id`),
INDEX `idx_param` (`pledge_param`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci
AUTO_INCREMENT=5

;

-- ----------------------------
-- Table structure for `nfc_clt_flwdata`
-- ----------------------------
DROP TABLE IF EXISTS `nfc_clt_flwdata`;
CREATE TABLE `nfc_clt_flwdata` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`en_address`  varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'En钱包地址' ,
`report_time`  datetime NULL DEFAULT NULL COMMENT '计费开始时间' ,
`flow_value`  bigint(60) NULL DEFAULT NULL COMMENT '流量值' ,
`fulnum`  decimal(65,0) NULL DEFAULT NULL COMMENT '消耗ful值' ,
`profitamount`  decimal(65,0) NULL DEFAULT NULL COMMENT '挖矿收益 NFC' ,
`router_address`  decimal(20,8) NULL DEFAULT NULL COMMENT '路由节点钱包地址，如果直连则该值为0' ,
`from_addr`  varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户端IP地址' ,
`to_addr`  varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'EN IP地址' ,
`router_ipaddr`  varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路由IP地址' ,
`trans_hash`  varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '流量交易HASH' ,
`instime`  datetime NULL DEFAULT NULL COMMENT '入库时间' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `idx_hash` (`trans_hash`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='流量挖矿数据表'
AUTO_INCREMENT=2

;

-- ----------------------------
-- Table structure for `nfc_flow_miner`
-- ----------------------------
DROP TABLE IF EXISTS `nfc_flow_miner`;
CREATE TABLE `nfc_flow_miner` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`miner_addr`  varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '矿工地址' ,
`revenue_address`  varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '当前绑定收益地址' ,
`pay_address`  varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '当前付款（ful）地址' ,
`line_type`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '线路类型和运营商ID' ,
`miner_mode`  int(6) NULL DEFAULT 1 COMMENT '模式 1 流量挖矿 2 带宽挖矿' ,
`miner_status` int(6) DEFAULT NULL COMMENT '1 挖矿中  \r\n2 待质押（绑定收益地址）\r\n3 退出',
`addpool` int(6) DEFAULT '0' COMMENT '0 未加入 1加入矿池',
`bandwidth`  decimal(65,0) NULL DEFAULT NULL COMMENT '带宽 单位 Mbps' ,
`pledge_amount`  decimal(65,0) NULL DEFAULT NULL COMMENT '质押金额 单位 NFC' ,
`miner_flow`  bigint(30) NULL DEFAULT NULL COMMENT '挖矿总流量 单位 B' ,
`payful`  decimal(65,0) NULL DEFAULT NULL COMMENT '消耗ful' ,
`revenue_amount`  decimal(65,0) NULL DEFAULT NULL COMMENT '总收益 单位 NFC' ,
`lock_amount`  decimal(65,0) NULL DEFAULT NULL COMMENT '当前锁仓金额 单位 NFC' ,
`release_amount`  decimal(65,0) NULL DEFAULT NULL COMMENT '总释放金额' ,
`draw_amount`  decimal(65,0) NULL DEFAULT NULL COMMENT '已提取金额' ,
`blocknumber`  bigint(30) NULL DEFAULT NULL COMMENT '同步区块高度' ,
`join_time`  datetime NULL DEFAULT NULL COMMENT '成为矿工时间' ,
`sync_time`  datetime NULL DEFAULT NULL COMMENT '同步时间' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `idx_miner_addr` (`miner_addr`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='当前矿工信息表'
AUTO_INCREMENT=22

;

-- ----------------------------
-- Table structure for `nfc_node_miner`
-- ----------------------------
DROP TABLE IF EXISTS `nfc_node_miner`;
CREATE TABLE `nfc_node_miner` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`node_address`  varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节点地址' ,
`revenue_address` varchar(100) DEFAULT NULL COMMENT '当前绑定收益地址',
`node_type`  int(6) NULL DEFAULT 1 COMMENT '1 待候选节点 2见证节点 3退出节点' ,
`fractions`  int(6) NULL DEFAULT NULL COMMENT '惩罚分' ,
`pledge_amount`  decimal(65,0) NULL DEFAULT NULL COMMENT '质押金额 单位 NFC' ,
`blocknumber`  bigint(30) NULL DEFAULT NULL COMMENT '质押区块高度' ,
`join_time`  datetime NULL DEFAULT NULL COMMENT '加入节点时间' ,
`sync_time`  datetime NULL DEFAULT NULL COMMENT '同步时间' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `idx_node` (`node_address`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='节点表'
AUTO_INCREMENT=3

;

-- ----------------------------
-- Table structure for `node_exit`
-- ----------------------------
DROP TABLE IF EXISTS `node_exit`;
CREATE TABLE `node_exit` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`timestamp`  datetime NOT NULL DEFAULT '1979-12-31 08:00:00' COMMENT '加入时间' ,
`pledgeamount`  decimal(65,0) NULL DEFAULT 0 COMMENT '质押金额' ,
`addressname`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '退出节点名称' ,
`deductionamount`  decimal(65,0) NULL DEFAULT 0 COMMENT '扣除费用' ,
`tractamount`  decimal(65,0) NULL DEFAULT 0 COMMENT '提取费用' ,
`lockstartnumber`  bigint(20) NULL DEFAULT NULL COMMENT '锁仓开始时间' ,
`locknumber`  bigint(20) NULL DEFAULT NULL COMMENT '锁仓期' ,
`releasenumber`  bigint(20) NULL DEFAULT NULL COMMENT '释放期' ,
`releaseinterval`  bigint(20) NULL DEFAULT NULL COMMENT '释放间隔' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='Rn退出数据表'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `pageslide`
-- ----------------------------
DROP TABLE IF EXISTS `pageslide`;
CREATE TABLE `pageslide` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`slidetitle`  varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '幻灯片名称' ,
`slidepicture`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '幻灯片图片地址' ,
`slidetype`  int(11) NOT NULL COMMENT '幻灯片类型\n    0 - 首页幻灯片' ,
`slidesort`  int(11) NOT NULL COMMENT '排序编号' ,
`slideurl`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '幻灯片链接' ,
`slidetarget`  int(11) NOT NULL COMMENT '打开方式' ,
`created_by`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '创建人' ,
`create_time`  timestamp NOT NULL DEFAULT '1979-12-31 08:00:00' COMMENT '创建时间' ,
`last_updated_by`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '最近一次的更新人' ,
`last_update_time`  timestamp NOT NULL DEFAULT '1979-12-31 08:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '最近一次的更新时间' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `idx_slide_type` (`slidetype`, `slidesort`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='页面广告幻灯片'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `pledge_data`
-- ----------------------------
DROP TABLE IF EXISTS `pledge_data`;
CREATE TABLE `pledge_data` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`starttime`  datetime NOT NULL COMMENT '锁仓开始时间' ,
`txhash`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易hash' ,
`type`  int(11) NOT NULL COMMENT '类型 1:RN质押 2：CN质押 3：EN质押  4：RN退出 5：CN退出 6：EN退出 7：RN提现 8：CN提现  9：EN提现' ,
`logindex`  int(11) NOT NULL COMMENT '日志索引' ,
`address`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址' ,
`value`  decimal(65,0) NOT NULL DEFAULT 0 COMMENT '交易金额' ,
`blockhash`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '区块Hash' ,
`blocknumber`  bigint(20) NOT NULL DEFAULT 0 COMMENT '区块高度' ,
`totalamount`  decimal(65,0) NULL DEFAULT 0 COMMENT '共计锁仓金额' ,
`leftamount`  decimal(65,0) NULL DEFAULT 0 COMMENT '剩余锁仓金额' ,
`status`  int(11) NULL DEFAULT 0 COMMENT '类型 1:RN节点 2CN节点  3：En节点' ,
`gaslimit`  bigint(20) NOT NULL DEFAULT 0 COMMENT '手续费' ,
`gasused`  bigint(20) NOT NULL DEFAULT 0 COMMENT '实际手续费' ,
`gasprice`  bigint(20) NOT NULL DEFAULT 0 COMMENT '价格' ,
`unlocknumber`  bigint(20) NOT NULL DEFAULT 0 COMMENT '锁仓解锁高度' ,
`loglength`  bigint(20) NOT NULL DEFAULT 0 COMMENT '日志长度' ,
`nodenumber`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节点编号' ,
`presentamount`  decimal(65,0) NULL DEFAULT 0 COMMENT '当前锁仓金额' ,
`locknumheigth`  bigint(20) NOT NULL DEFAULT 0 COMMENT '锁仓时长' ,
`pledgeamount`  decimal(65,0) NULL DEFAULT 0 COMMENT '质押金额' ,
`pledgeaddress`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '质押锁仓地址' ,
`punilshamount`  decimal(65,0) NULL DEFAULT 0 COMMENT '惩罚金额' ,
`receiveaddress`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '罚金接收地址' ,
`releaseheigth`  bigint(20) NOT NULL DEFAULT 0 COMMENT '释放时长' ,
`releaseinterval`  bigint(20) NOT NULL DEFAULT 0 COMMENT '释放间隔' ,
`pledgetotalamount`  decimal(65,0) NULL DEFAULT 0 COMMENT '质押总数量' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `txhash` (`txhash`, `unlocknumber`) USING BTREE ,
INDEX `type` (`type`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='新质押合约数据表'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `pledge_total_data`
-- ----------------------------
DROP TABLE IF EXISTS `pledge_total_data`;
CREATE TABLE `pledge_total_data` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`starttime`  datetime NOT NULL COMMENT '质押最新日期' ,
`type`  int(11) NOT NULL COMMENT '类型 1:RN质押 2：CN质押 3：EN质押 4:RN退出 5：CN退出 6：EN退出 7：RN提现  8：CN提现 9：EN提现   ' ,
`address`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址' ,
`value`  decimal(65,0) NULL DEFAULT 0 COMMENT '交易金额汇总' ,
`blocknumber`  bigint(20) NULL DEFAULT 0 COMMENT '提取完后的区块高度' ,
`pledgetotalamount`  decimal(65,0) NULL DEFAULT 0 COMMENT '质押汇总金额' ,
`cashtotalamount`  decimal(65,0) NULL DEFAULT 0 COMMENT '提现汇总金额' ,
`punilshamount`  decimal(65,0) NULL DEFAULT 0 COMMENT '罚金金额汇总' ,
`realshnumber`  bigint(20) NULL DEFAULT 0 COMMENT '释放高度' ,
`status`  int(11) NULL DEFAULT 0 COMMENT '类型 质押，退出，完成 ' ,
`nodenumber`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节点编号' ,
`locknumber`  bigint(20) NULL DEFAULT 0 COMMENT '锁仓时长' ,
`releaseintervalnum`  bigint(20) NULL DEFAULT 0 COMMENT '释放间隔' ,
`exittime`  datetime NULL DEFAULT NULL COMMENT '退出时间' ,
`pledgeaddress`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合约质押地址' ,
PRIMARY KEY (`id`),
INDEX `type` (`type`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='质押汇总数据表'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `punished`
-- ----------------------------
DROP TABLE IF EXISTS `punished`;
CREATE TABLE `punished` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`timestamp`  datetime NOT NULL DEFAULT '1979-12-31 08:00:00' COMMENT '惩罚时间' ,
`address`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节点地址' ,
`type`  int(11) NOT NULL COMMENT '类型 1:缺席出块 2：未参与出块' ,
`blocknumber`  bigint(20) NULL DEFAULT NULL COMMENT '记录区块高度' ,
`fractions`  bigint(20) NULL DEFAULT NULL COMMENT '惩罚分数' ,
`round`  int(11) NOT NULL COMMENT '区块所在轮次' ,
`addressname`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第三方惩罚节点数据' ,
`pledgeamount`  decimal(65,0) NULL DEFAULT 0 COMMENT '惩罚质押数量' ,
PRIMARY KEY (`id`),
INDEX `type` (`type`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='惩罚数据结构表'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `release_version_info`
-- ----------------------------
DROP TABLE IF EXISTS `release_version_info`;
CREATE TABLE `release_version_info` (
`id`  bigint(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键' ,
`version_number`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '版本号' ,
`package_url`  varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '发布包所在的服务地址' ,
`check_code`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '发布包完整性的校验码' ,
`version_desc`  varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '发布包描述' ,
`is_deleted`  tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除\\n    0: 未删除\\n    1: 删除' ,
`create_time`  timestamp NOT NULL DEFAULT '1979-12-31 08:00:00' COMMENT '创建时间' ,
`created_by`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '创建人' ,
`last_update_time`  timestamp NOT NULL DEFAULT '1979-12-31 08:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '最近更新时间' ,
`last_updated_by`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '最近更新人' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `uniq_rvi_version_number` (`version_number`) USING BTREE ,
INDEX `idx_version_is_deleted` (`is_deleted`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='Saturn发布版本信息表'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `saturn_dashboard_history`
-- ----------------------------
DROP TABLE IF EXISTS `saturn_dashboard_history`;
CREATE TABLE `saturn_dashboard_history` (
`id`  bigint(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键' ,
`record_date`  date NULL DEFAULT NULL COMMENT '记录日期' ,
`zk_cluster`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '所属zk集群' ,
`record_type`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '类型' ,
`topic`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '主题' ,
`content`  longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '内容' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `uniq_dashboard_history_zk_cluster_record_type_topic_record_date` (`zk_cluster`, `record_type`, `topic`, `record_date`) USING BTREE ,
INDEX `dashboard_record_date_idx` (`record_date`) USING BTREE ,
INDEX `dashboard_zk_cluster_idx` (`zk_cluster`) USING BTREE ,
INDEX `dashboard_record_type_idx` (`record_type`) USING BTREE ,
INDEX `dashboard_topic_idx` (`topic`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='dashboard历史记录表'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `saturn_statistics`
-- ----------------------------
DROP TABLE IF EXISTS `saturn_statistics`;
CREATE TABLE `saturn_statistics` (
`id`  bigint(11) NOT NULL AUTO_INCREMENT COMMENT '统计表主键ID' ,
`name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '统计名称，例如top10FailJob' ,
`zklist`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '统计所属zk集群' ,
`result`  longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '统计结果(json结构)' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `uniq_statistics_zk_cluster_topic` (`zklist`, `name`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `stakingdelegators`
-- ----------------------------
DROP TABLE IF EXISTS `stakingdelegators`;
CREATE TABLE `stakingdelegators` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`poolhash`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权益池地址' ,
`delegator`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '代表地址' ,
`stakeamount`  decimal(65,0) NOT NULL COMMENT '权益金额' ,
`orderedwithdraw`  decimal(65,0) NOT NULL COMMENT '订单撤消金额' ,
`withdrawallowed`  decimal(65,0) NOT NULL COMMENT '最大允许撤消金额' ,
`orderedallowed`  decimal(65,0) NOT NULL COMMENT '最大允许订单撤消金额' ,
`epoch`  int(11) NOT NULL COMMENT '撤消周期' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `unique` (`poolhash`, `delegator`) USING BTREE ,
INDEX `poolhash` (`poolhash`) USING BTREE ,
INDEX `delegator` (`delegator`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='权益代表信息'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `stakingpools`
-- ----------------------------
DROP TABLE IF EXISTS `stakingpools`;
CREATE TABLE `stakingpools` (
`hash`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权益地址' ,
`miningaddress`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '矿工地址' ,
`isactive`  tinyint(4) NOT NULL COMMENT '是否激活状态' ,
`isdeleted`  tinyint(4) NOT NULL COMMENT '是否删除' ,
`delegatorscount`  int(11) NOT NULL COMMENT '代表数量' ,
`stakedamount`  decimal(65,0) NOT NULL COMMENT '权益金额' ,
`selfamount`  decimal(65,0) NOT NULL COMMENT '自身权益金额' ,
`isvalidator`  tinyint(4) NOT NULL COMMENT '是否当选' ,
`validatorcount`  int(11) NOT NULL COMMENT '当选次数' ,
`isbanned`  tinyint(4) NOT NULL COMMENT '是否被撤消过' ,
`bannedcount`  int(11) NOT NULL COMMENT '被撤消过的次数' ,
`likelihood`  decimal(5,2) NOT NULL COMMENT '可能性' ,
`stakedratio`  decimal(5,2) NOT NULL COMMENT '权益比例' ,
`banneduntil`  datetime NULL DEFAULT NULL COMMENT '被撤消的有效期' ,
PRIMARY KEY (`hash`),
INDEX `mineraddress` (`miningaddress`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='权益池信息'

;

-- ----------------------------
-- Table structure for `sys_admin`
-- ----------------------------
DROP TABLE IF EXISTS `sys_admin`;
CREATE TABLE `sys_admin` (
`id`  bigint(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键' ,
`admin_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户名' ,
`real_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户真实名字' ,
`password`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户密码' ,
`salt`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码加密盐' ,
`phone`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '电话号码' ,
`email`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '邮箱' ,
`available`  tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否可用\\n    -1: 删除\\n     0: 禁用\\n     1: 可用' ,
`created_by`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '创建人' ,
`create_time`  timestamp NOT NULL DEFAULT '1979-12-31 08:00:00' COMMENT '创建时间' ,
`last_updated_by`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '最近一次的更新人' ,
`last_update_time`  timestamp NOT NULL DEFAULT '1979-12-31 08:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '最近一次的更新时间' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `uniq_admin_admin_name` (`admin_name`) USING BTREE ,
INDEX `idx_admin_available` (`available`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='后台管理用户表'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `sys_admin_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_admin_role`;
CREATE TABLE `sys_admin_role` (
`id`  bigint(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键' ,
`admin_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户名' ,
`role_key`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '角色标识' ,
`namespace`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '域名' ,
`need_approval`  tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否需要审批\\n    0: 不需要审批\\n    1: 需要审批' ,
`is_deleted`  tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除\\n    0: 未删除\\n    1: 删除' ,
`created_by`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '创建人' ,
`create_time`  timestamp NOT NULL DEFAULT '1979-12-31 08:00:00' COMMENT '创建时间' ,
`last_updated_by`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '最近一次的更新人' ,
`last_update_time`  timestamp NOT NULL DEFAULT '1979-12-31 08:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '最近一次的更新时间' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `uniq_admin_role_key` (`admin_name`, `role_key`, `namespace`) USING BTREE ,
INDEX `idx_admin_role_is_deleted` (`is_deleted`) USING BTREE ,
INDEX `idx_admin_role_u_r_n_n_i` (`admin_name`, `role_key`, `namespace`, `need_approval`, `is_deleted`) USING BTREE ,
INDEX `idx_admin_role_r_n_n_i` (`role_key`, `namespace`, `need_approval`, `is_deleted`) USING BTREE ,
INDEX `idx_admin_role_n_n_i` (`namespace`, `need_approval`, `is_deleted`) USING BTREE ,
INDEX `idx_admin_role_n_i` (`need_approval`, `is_deleted`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='后台管理用户角色关系表'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `sys_config`
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
`id`  bigint(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键' ,
`property`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '属性名' ,
`value`  varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '属性值' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `idx_property` (`property`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='系统配置表'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `sys_permission`
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
`id`  bigint(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键' ,
`permission_key`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '权限标识' ,
`permission_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '权限名' ,
`description`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '权限描述' ,
`is_deleted`  tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除\\n    0: 未删除\\n    1: 删除' ,
`created_by`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '创建人' ,
`create_time`  timestamp NOT NULL DEFAULT '1979-12-31 08:00:00' COMMENT '创建时间' ,
`last_updated_by`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '最近一次的更新人' ,
`last_update_time`  timestamp NOT NULL DEFAULT '1979-12-31 08:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '最近一次的更新时间' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `uniq_permission_permission_key` (`permission_key`) USING BTREE ,
INDEX `idx_permission_is_deleted` (`is_deleted`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='后台管理权限表'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
`id`  bigint(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键' ,
`role_key`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '角色标识' ,
`role_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '角色名' ,
`description`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '角色描述' ,
`is_deleted`  tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除\\n    0: 未删除\\n    1: 删除' ,
`is_relating_to_namespace`  tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否关联域\\n    0: 不关联\\n    1: 关联' ,
`created_by`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '创建人' ,
`create_time`  timestamp NOT NULL DEFAULT '1979-12-31 08:00:00' COMMENT '创建时间' ,
`last_updated_by`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '最近一次的更新人' ,
`last_update_time`  timestamp NOT NULL DEFAULT '1979-12-31 08:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '最近一次的更新时间' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `uniq_role_role_key` (`role_key`) USING BTREE ,
INDEX `idx_role_is_deleted` (`is_deleted`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='后台管理角色表'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `sys_role_permission`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
`id`  bigint(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键' ,
`role_key`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '角色标识' ,
`permission_key`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '权限标识' ,
`is_deleted`  tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除\\n    0: 未删除\\n    1: 删除' ,
`created_by`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '创建人' ,
`create_time`  timestamp NOT NULL DEFAULT '1979-12-31 08:00:00' COMMENT '创建时间' ,
`last_updated_by`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '最近一次的更新人' ,
`last_update_time`  timestamp NOT NULL DEFAULT '1979-12-31 08:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '最近一次的更新时间' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `uniq_role_permission_key` (`role_key`, `permission_key`) USING BTREE ,
INDEX `idx_role_permission_key` (`is_deleted`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='后台管理角色权限关系表'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `t_sys_cfg`
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_cfg`;
CREATE TABLE `t_sys_cfg` (
`id`  bigint(64) NOT NULL AUTO_INCREMENT COMMENT '自增主键' ,
`cfgvalue`  bigint(64) NULL DEFAULT NULL COMMENT '参数值' ,
`cfgdesc`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数描述' ,
`cfgname`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数名' ,
`createtime`  datetime NULL DEFAULT NULL ,
`createby`  bigint(20) NULL DEFAULT NULL ,
`modifytime`  datetime NULL DEFAULT NULL ,
`modifyby`  bigint(20) NULL DEFAULT NULL ,
`cfgtype`  tinyint(1) NULL DEFAULT 0 COMMENT '0 类型（配置默认）' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `cfgname` (`cfgname`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='系统参数'
AUTO_INCREMENT=11

;

-- ----------------------------
-- Table structure for `temporary_shared_status`
-- ----------------------------
DROP TABLE IF EXISTS `temporary_shared_status`;
CREATE TABLE `temporary_shared_status` (
`id`  bigint(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键' ,
`status_key`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '状态键' ,
`status_value`  varchar(4000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '状态值' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `uniq_tss_status_key` (`status_key`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='共享状态表'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `token_contract`
-- ----------------------------
DROP TABLE IF EXISTS `token_contract`;
CREATE TABLE `token_contract` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`contractaddress`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '1155合约成功地址' ,
`contractmanager`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '1155智能合约管理地址' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='1155合约'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `tokencatalog`
-- ----------------------------
DROP TABLE IF EXISTS `tokencatalog`;
CREATE TABLE `tokencatalog` (
`contract`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '智能合约地址' ,
`websites`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '网址' ,
`tokenico`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图标' ,
`introduction`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '简介' ,
PRIMARY KEY (`contract`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='代币登记信息'

;

-- ----------------------------
-- Table structure for `tokeninstances`
-- ----------------------------
DROP TABLE IF EXISTS `tokeninstances`;
CREATE TABLE `tokeninstances` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`contract`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '智能合约地址' ,
`tokenid`  decimal(65,0) NOT NULL COMMENT 'ERC-721 tokens have IDs' ,
`tokenURI`  varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代币实例外部资源文件的URI' ,
`metadata`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '代币实例说明' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `unique` (`contract`, `tokenid`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='ERC721代币实例'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `tokenmarket`
-- ----------------------------
DROP TABLE IF EXISTS `tokenmarket`;
CREATE TABLE `tokenmarket` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`contract`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '智能合约地址' ,
`marketdate`  datetime NOT NULL COMMENT '报价日期' ,
`closingprice`  decimal(65,8) NOT NULL COMMENT '收市价格' ,
`openingprice`  decimal(65,8) NOT NULL COMMENT '开市价格' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `unique` (`contract`, `marketdate`) USING BTREE ,
INDEX `marketdate` (`marketdate`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='代币市场价格'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `tokens`
-- ----------------------------
DROP TABLE IF EXISTS `tokens`;
CREATE TABLE `tokens` (
`contract`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '智能合约地址' ,
`type`  int(11) NOT NULL COMMENT '代币类型：\\n    0 - ERC-20,\\n    1 - ERC-721' ,
`name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代币名称' ,
`symbol`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代币符号' ,
`decimals`  int(11) NULL DEFAULT NULL COMMENT '小数位数' ,
`totalsupply`  decimal(65,0) NULL DEFAULT NULL COMMENT '发行量' ,
`cataloged`  tinyint(4) NOT NULL COMMENT '是否登记验证' ,
`description`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
`contractmanager`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合约管理地址' ,
PRIMARY KEY (`contract`),
INDEX `type` (`type`) USING BTREE ,
INDEX `cataloged` (`cataloged`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='代币列表'

;

-- ----------------------------
-- Table structure for `trans_logs`
-- ----------------------------
DROP TABLE IF EXISTS `trans_logs`;
CREATE TABLE `trans_logs` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`transhash`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易Hash' ,
`logindex`  int(11) NOT NULL COMMENT '日志索引' ,
`type`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志类型' ,
`address`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址' ,
`firsttopic`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第一个主题' ,
`secondtopic`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第二个主题' ,
`thirdtopic`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第三个主题' ,
`fourthtopic`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第四个主题' ,
`data`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '日志数据' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `unique` (`transhash`, `logindex`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='交易日志列表'
AUTO_INCREMENT=3699

;

-- ----------------------------
-- Table structure for `trans_token`
-- ----------------------------
DROP TABLE IF EXISTS `trans_token`;
CREATE TABLE `trans_token` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`transhash`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易Hash' ,
`logindex`  int(11) NOT NULL COMMENT '交易日志序号' ,
`cointype`  int(11) NOT NULL DEFAULT 0 COMMENT '代币类型:\n     0 - ERC20\n     1 - ERC721' ,
`fromaddr`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发送方地址' ,
`toaddr`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '接收方地址' ,
`amount`  decimal(65,0) NOT NULL COMMENT '交易金额' ,
`contract`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '代币智能合约地址' ,
`blockhash`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '区块Hash' ,
`blocknumber`  bigint(20) NOT NULL COMMENT '区块高度' ,
`tokenid`  decimal(65,0) NULL DEFAULT NULL COMMENT 'ERC-721 tokens have IDs' ,
`gasused`  bigint(20) NOT NULL DEFAULT 0 COMMENT '实际手续费' ,
`gasprice`  bigint(20) NOT NULL DEFAULT 0 COMMENT '交易手续费率' ,
`gaslimit`  bigint(20) NOT NULL DEFAULT 0 COMMENT '手续费' ,
`nonce`  bigint(20) NOT NULL DEFAULT 0 COMMENT 'nonce值' ,
`timestamp`  datetime NOT NULL COMMENT '交易时间' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `unique` (`transhash`, `logindex`) USING BTREE ,
INDEX `from` (`fromaddr`) USING BTREE ,
INDEX `to` (`toaddr`) USING BTREE ,
INDEX `contract` (`contract`) USING BTREE ,
INDEX `blockhash` (`blockhash`) USING BTREE ,
INDEX `blocknumber` (`blocknumber`) USING BTREE ,
INDEX `cointype` (`cointype`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='代币交易列表'
AUTO_INCREMENT=3437

;

-- ----------------------------
-- Table structure for `transaction`
-- ----------------------------
DROP TABLE IF EXISTS `transaction`;
CREATE TABLE `transaction`  (
  `hash` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易Hash',
  `istrunk` tinyint(4) NOT NULL COMMENT '交易是否是主干',
  `timestamp` datetime(0) NOT NULL,
  `fromaddr` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发送方地址',
  `toaddr` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '接收方地址',
  `value` decimal(65, 0) NOT NULL DEFAULT 0 COMMENT '交易金额',
  `nonce` bigint(20) NOT NULL DEFAULT 0 COMMENT '交易序号',
  `gaslimit` bigint(20) NOT NULL DEFAULT 0 COMMENT '保证金',
  `gasprice` bigint(20) NOT NULL DEFAULT 0 COMMENT '交易手续费率',
  `status` int(11) NOT NULL DEFAULT 0 COMMENT '交易状态',
  `gasused` bigint(20) NOT NULL DEFAULT 0 COMMENT '实际手续费',
  `cumulative` bigint(20) NOT NULL DEFAULT 0 COMMENT '额外手续费',
  `blockhash` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '区块Hash',
  `blocknumber` bigint(20) NOT NULL DEFAULT 0 COMMENT '区块高度',
  `blockindex` int(11) NOT NULL DEFAULT 0 COMMENT '交易在区块的索引',
  `input` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '交易说明',
  `contract` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '对于合约创建交易，该值为新创建的合约地址，否则为null',
  `error` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '交易错误提示信息',
  `internal` tinyint(4) NOT NULL DEFAULT 0 COMMENT '内部交易是否处理',
  `type` tinyint(4) NOT NULL DEFAULT 0 COMMENT '交易类型',
  `ufoprefix` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '前缀',
  `ufoversion` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '版本',
  `ufooperator` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '操作',
  `param1` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `param2` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `param3` decimal(65, 0) DEFAULT NULL,
  `param4` decimal(65, 0) DEFAULT NULL,
  `param5` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `param6` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`hash`) USING BTREE,
  INDEX `from`(`fromaddr`, `nonce`) USING BTREE,
  INDEX `to`(`toaddr`) USING BTREE,
  INDEX `timestamp`(`timestamp`) USING BTREE,
  INDEX `status`(`status`) USING BTREE,
  INDEX `blockhash`(`blockhash`) USING BTREE,
  INDEX `blocknumber`(`blocknumber`) USING BTREE,
  INDEX `istrunk`(`istrunk`, `blocknumber`) USING BTREE,
  INDEX `internal`(`internal`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '交易列表' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for `transdiscard`
-- ----------------------------
DROP TABLE IF EXISTS `transdiscard`;
CREATE TABLE `transdiscard` (
`transhash`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易Hash' ,
`timestamp`  datetime NOT NULL ,
`fromaddr`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发送方地址' ,
`toaddr`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '接收方地址' ,
`value`  decimal(65,0) NOT NULL DEFAULT 0 COMMENT '交易金额' ,
`nonce`  bigint(20) NOT NULL DEFAULT 0 COMMENT '交易序号' ,
`gaslimit`  bigint(20) NOT NULL DEFAULT 0 COMMENT '保证金' ,
`gasprice`  bigint(20) NOT NULL DEFAULT 0 COMMENT '交易手续费率' ,
`input`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '交易说明' ,
PRIMARY KEY (`transhash`),
INDEX `from` (`fromaddr`, `nonce`) USING BTREE ,
INDEX `to` (`toaddr`) USING BTREE ,
INDEX `timestamp` (`timestamp`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='废弃交易列表'

;


SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for transfer_miner
-- ----------------------------
DROP TABLE IF EXISTS `transfer_miner`;
CREATE TABLE `transfer_miner`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `txhash` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '交易hash',
  `type` int(11) DEFAULT NULL COMMENT '类型 1:团队锁仓 2：团队释放 3：节点锁仓   4：节点释放  5：流量锁仓 6：流量释放 7:矿工锁仓 8矿工释放',
  `logindex` int(11) DEFAULT NULL COMMENT '日志索引',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '地址',
  `value` decimal(65, 0) DEFAULT 0 COMMENT '交易金额',
  `blockhash` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '区块Hash',
  `blocknumber` bigint(20) DEFAULT 0 COMMENT '区块高度',
  `totalamount` decimal(65, 0) DEFAULT 0 COMMENT '共计锁仓金额',
  `leftamount` decimal(65, 0) DEFAULT 0 COMMENT '剩余锁仓金额',
  `status` int(11) DEFAULT 0 COMMENT '类型 1:RN节点 2CN节点  3：En节点',
  `gaslimit` bigint(20) DEFAULT 0 COMMENT '手续费',
  `gasused` bigint(20) DEFAULT 0 COMMENT '实际手续费',
  `gasprice` bigint(20) DEFAULT 0 COMMENT '价格',
  `unlocknumber` bigint(20) DEFAULT 0 COMMENT '锁仓解锁高度',
  `loglength` bigint(20) DEFAULT 0 COMMENT '日志长度',
  `starttime` datetime(0) DEFAULT NULL COMMENT '锁仓开始时间',
  `nodenumber` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '节点编号',
  `presentamount` decimal(65, 0) DEFAULT 0 COMMENT '当前锁仓金额',
  `locknumheigth` bigint(20) DEFAULT 0 COMMENT '锁仓时长',
  `pledgeamount` decimal(65, 0) DEFAULT 0 COMMENT '质押金额',
  `pledgeaddress` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '质押锁仓地址',
  `punilshamount` decimal(65, 0) DEFAULT 0 COMMENT '惩罚金额',
  `receiveaddress` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '罚金接收地址',
  `revenueaddress` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '收益地址',
  `releaseheigth` bigint(20) DEFAULT 0 COMMENT '释放时长',
  `releaseinterval` bigint(20) DEFAULT 0 COMMENT '释放间隔',
  `releaseamount` decimal(65, 0) DEFAULT 0 COMMENT '释放总量',
  `pledgetotalamount` decimal(65, 0) DEFAULT 0 COMMENT '质押总数量',
  `curtime` datetime(0) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `txhash`(`txhash`, `unlocknumber`) USING BTREE,
  INDEX `type`(`type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '锁仓释放数据表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;


-- ----------------------------
-- Table structure for `transferminer`
-- ----------------------------
DROP TABLE IF EXISTS `transferminer`;
CREATE TABLE `transferminer` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`txhash`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易hash' ,
`type`  int(11) NOT NULL COMMENT '类型 1:团队锁仓 2：团队释放 3：节点锁仓   4：节点释放  5：流量锁仓 6：流量释放' ,
`logindex`  int(11) NOT NULL COMMENT '日志索引' ,
`address`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址' ,
`value`  decimal(65,0) NOT NULL DEFAULT 0 COMMENT '交易金额' ,
`blockhash`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '区块Hash' ,
`blocknumber`  bigint(20) NOT NULL DEFAULT 0 COMMENT '区块高度' ,
PRIMARY KEY (`id`),
INDEX `type` (`type`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='锁仓释放数据表'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `transforks`
-- ----------------------------
DROP TABLE IF EXISTS `transforks`;
CREATE TABLE `transforks` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`nephewhash`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '分叉交易Hash' ,
`nephewblock`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '分叉交易区块Hash' ,
`unclehash`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '叔伯交易Hash' ,
`uncleblock`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '叔伯交易区块Hash' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `unique` (`nephewhash`, `nephewblock`, `unclehash`, `uncleblock`) USING BTREE ,
INDEX `nephew` (`nephewhash`, `nephewblock`) USING BTREE ,
INDEX `nephewblock` (`nephewblock`) USING BTREE ,
INDEX `uncle` (`unclehash`, `uncleblock`) USING BTREE ,
INDEX `uncleblock` (`uncleblock`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='分叉交易列表'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `transinternal`
-- ----------------------------
DROP TABLE IF EXISTS `transinternal`;
CREATE TABLE `transinternal` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`transhash`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易Hash' ,
`transindex`  int(11) NOT NULL DEFAULT 0 COMMENT '内部交易序号' ,
`fromaddr`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发送方地址' ,
`toaddr`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '接收方地址' ,
`value`  decimal(65,0) NOT NULL DEFAULT 0 COMMENT '交易金额' ,
`gaslimit`  bigint(20) NOT NULL DEFAULT 0 COMMENT '保证金' ,
`gasused`  bigint(20) NOT NULL DEFAULT 0 COMMENT '实际手续费' ,
`blockhash`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '区块Hash' ,
`blocknumber`  bigint(20) NULL DEFAULT NULL ,
`type`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内部交易类型' ,
`contract`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '智能合约创建地址' ,
`calltype`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '调用类型' ,
`input`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '交易说明' ,
`createdcode`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '智能合约创建代码' ,
`initcode`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '智能合约初始代码' ,
`output`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '智能合约输出代码' ,
`traceaddress`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '溯源地址' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `unique` (`transhash`, `transindex`) USING BTREE ,
INDEX `transhash` (`transhash`) USING BTREE ,
INDEX `from` (`fromaddr`) USING BTREE ,
INDEX `to` (`toaddr`) USING BTREE ,
INDEX `blockhash` (`blockhash`) USING BTREE ,
INDEX `blocknumber` (`blocknumber`) USING BTREE ,
INDEX `type` (`type`, `calltype`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='内部交易列表'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `translogs`
-- ----------------------------
DROP TABLE IF EXISTS `translogs`;
CREATE TABLE `translogs` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`transhash`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易Hash' ,
`logindex`  int(11) NOT NULL COMMENT '日志索引' ,
`type`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志类型' ,
`address`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址' ,
`firsttopic`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第一个主题' ,
`secondtopic`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第二个主题' ,
`thirdtopic`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第三个主题' ,
`fourthtopic`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第四个主题' ,
`data`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '日志数据' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `unique` (`transhash`, `logindex`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='交易日志列表'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `transpending`
-- ----------------------------
DROP TABLE IF EXISTS `transpending`;
CREATE TABLE `transpending` (
`hash`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易Hash' ,
`timestamp`  datetime NOT NULL ,
`fromaddr`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发送方地址' ,
`toaddr`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '接收方地址' ,
`value`  decimal(65,0) NOT NULL DEFAULT 0 COMMENT '交易金额' ,
`nonce`  bigint(20) NOT NULL DEFAULT 0 COMMENT '交易序号' ,
`gaslimit`  bigint(20) NOT NULL DEFAULT 0 COMMENT '保证金' ,
`gasprice`  bigint(20) NOT NULL DEFAULT 0 COMMENT '交易手续费率' ,
`input`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '交易说明' ,
PRIMARY KEY (`hash`),
INDEX `from` (`fromaddr`, `nonce`) USING BTREE ,
INDEX `to` (`toaddr`) USING BTREE ,
INDEX `timestamp` (`timestamp`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='Pending交易列表'

;

-- ----------------------------
-- Table structure for `transtemp`
-- ----------------------------
DROP TABLE IF EXISTS `transtemp`;
CREATE TABLE `transtemp` (
`hash`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易Hash' ,
`timestamp`  datetime NOT NULL ,
`fromaddr`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发送方地址' ,
`toaddr`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '接收方地址' ,
`value`  decimal(65,0) NOT NULL DEFAULT 0 COMMENT '交易金额' ,
`nonce`  bigint(20) NOT NULL DEFAULT 0 COMMENT '交易序号' ,
`gaslimit`  bigint(20) NOT NULL DEFAULT 0 COMMENT '保证金' ,
`gasprice`  bigint(20) NOT NULL DEFAULT 0 COMMENT '交易手续费率' ,
`input`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '交易说明' ,
PRIMARY KEY (`hash`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='临时交易列表'

;

-- ----------------------------
-- Table structure for `transtoken`
-- ----------------------------
DROP TABLE IF EXISTS `transtoken`;
CREATE TABLE `transtoken` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`transhash`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易Hash' ,
`logindex`  int(11) NOT NULL COMMENT '交易日志序号' ,
`cointype`  int(11) NOT NULL DEFAULT 0 COMMENT '代币类型:\n     0 - ERC20\n     1 - ERC721' ,
`fromaddr`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发送方地址' ,
`toaddr`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '接收方地址' ,
`amount`  decimal(65,0) NOT NULL COMMENT '交易金额' ,
`contract`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '代币智能合约地址' ,
`blockhash`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '区块Hash' ,
`blocknumber`  bigint(20) NOT NULL COMMENT '区块高度' ,
`tokenid`  decimal(65,0) NULL DEFAULT NULL COMMENT 'ERC-721 tokens have IDs' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `unique` (`transhash`, `logindex`) USING BTREE ,
INDEX `from` (`fromaddr`) USING BTREE ,
INDEX `to` (`toaddr`) USING BTREE ,
INDEX `contract` (`contract`) USING BTREE ,
INDEX `blockhash` (`blockhash`) USING BTREE ,
INDEX `blocknumber` (`blocknumber`) USING BTREE ,
INDEX `cointype` (`cointype`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='代币交易列表'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Table structure for `zk_cluster_info`
-- ----------------------------
DROP TABLE IF EXISTS `zk_cluster_info`;
CREATE TABLE `zk_cluster_info` (
`id`  bigint(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键' ,
`zk_cluster_key`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '集群key值，唯一' ,
`alias`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '别名' ,
`connect_string`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '连接串' ,
`description`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '集群描述' ,
`is_deleted`  tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除\\n    0: 未删除\\n    1: 删除' ,
`create_time`  timestamp NOT NULL DEFAULT '1979-12-31 08:00:00' COMMENT '创建时间' ,
`created_by`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '创建人' ,
`last_update_time`  timestamp NOT NULL DEFAULT '1979-12-31 08:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '最近更新时间' ,
`last_updated_by`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '最近更新人' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `uniq_zk_cluster_info_zk_cluster_key` (`zk_cluster_key`) USING BTREE ,
INDEX `idx_cluster_is_deleted` (`is_deleted`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='zk集群信息表'
AUTO_INCREMENT=1

;

-- ----------------------------
-- Auto increment value for `addr_balances`
-- ----------------------------
ALTER TABLE `addr_balances` AUTO_INCREMENT=1295597;

-- ----------------------------
-- Auto increment value for `addresses`
-- ----------------------------
ALTER TABLE `addresses` AUTO_INCREMENT=765643;

-- ----------------------------
-- Auto increment value for `addresses_token`
-- ----------------------------
ALTER TABLE `addresses_token` AUTO_INCREMENT=415;

-- ----------------------------
-- Auto increment value for `api_config`
-- ----------------------------
ALTER TABLE `api_config` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `api_configplat`
-- ----------------------------
ALTER TABLE `api_configplat` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `api_configplat_en`
-- ----------------------------
ALTER TABLE `api_configplat_en` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `block_fork`
-- ----------------------------
ALTER TABLE `block_fork` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `block_rewards`
-- ----------------------------
ALTER TABLE `block_rewards` AUTO_INCREMENT=256757;

-- ----------------------------
-- Auto increment value for `dposcandidate`
-- ----------------------------
ALTER TABLE `dposcandidate` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `dposdeclare`
-- ----------------------------
ALTER TABLE `dposdeclare` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `dposnode`
-- ----------------------------
ALTER TABLE `dposnode` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `dpossigners`
-- ----------------------------
ALTER TABLE `dpossigners` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `dposvotes`
-- ----------------------------
ALTER TABLE `dposvotes` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `dposvoteswallet`
-- ----------------------------
ALTER TABLE `dposvoteswallet` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `job_config`
-- ----------------------------
ALTER TABLE `job_config` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `job_config_history`
-- ----------------------------
ALTER TABLE `job_config_history` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `namespace_info`
-- ----------------------------
ALTER TABLE `namespace_info` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `namespace_version_mapping`
-- ----------------------------
ALTER TABLE `namespace_version_mapping` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `namespace_zkcluster_mapping`
-- ----------------------------
ALTER TABLE `namespace_zkcluster_mapping` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `nfc_bandwidth_config`
-- ----------------------------
ALTER TABLE `nfc_bandwidth_config` AUTO_INCREMENT=5;

-- ----------------------------
-- Auto increment value for `nfc_clt_flwdata`
-- ----------------------------
ALTER TABLE `nfc_clt_flwdata` AUTO_INCREMENT=2;

-- ----------------------------
-- Auto increment value for `nfc_flow_miner`
-- ----------------------------
ALTER TABLE `nfc_flow_miner` AUTO_INCREMENT=22;

-- ----------------------------
-- Auto increment value for `nfc_node_miner`
-- ----------------------------
ALTER TABLE `nfc_node_miner` AUTO_INCREMENT=3;

-- ----------------------------
-- Auto increment value for `node_exit`
-- ----------------------------
ALTER TABLE `node_exit` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `pageslide`
-- ----------------------------
ALTER TABLE `pageslide` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `pledge_data`
-- ----------------------------
ALTER TABLE `pledge_data` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `pledge_total_data`
-- ----------------------------
ALTER TABLE `pledge_total_data` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `punished`
-- ----------------------------
ALTER TABLE `punished` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `release_version_info`
-- ----------------------------
ALTER TABLE `release_version_info` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `saturn_dashboard_history`
-- ----------------------------
ALTER TABLE `saturn_dashboard_history` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `saturn_statistics`
-- ----------------------------
ALTER TABLE `saturn_statistics` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `stakingdelegators`
-- ----------------------------
ALTER TABLE `stakingdelegators` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `sys_admin`
-- ----------------------------
ALTER TABLE `sys_admin` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `sys_admin_role`
-- ----------------------------
ALTER TABLE `sys_admin_role` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `sys_config`
-- ----------------------------
ALTER TABLE `sys_config` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `sys_permission`
-- ----------------------------
ALTER TABLE `sys_permission` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `sys_role`
-- ----------------------------
ALTER TABLE `sys_role` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `sys_role_permission`
-- ----------------------------
ALTER TABLE `sys_role_permission` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `t_sys_cfg`
-- ----------------------------
ALTER TABLE `t_sys_cfg` AUTO_INCREMENT=11;

-- ----------------------------
-- Auto increment value for `temporary_shared_status`
-- ----------------------------
ALTER TABLE `temporary_shared_status` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `token_contract`
-- ----------------------------
ALTER TABLE `token_contract` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `tokeninstances`
-- ----------------------------
ALTER TABLE `tokeninstances` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `tokenmarket`
-- ----------------------------
ALTER TABLE `tokenmarket` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `trans_logs`
-- ----------------------------
ALTER TABLE `trans_logs` AUTO_INCREMENT=3699;

-- ----------------------------
-- Auto increment value for `trans_token`
-- ----------------------------
ALTER TABLE `trans_token` AUTO_INCREMENT=3437;

-- ----------------------------
-- Auto increment value for `transfer_miner`
-- ----------------------------
ALTER TABLE `transfer_miner` AUTO_INCREMENT=2;

-- ----------------------------
-- Auto increment value for `transferminer`
-- ----------------------------
ALTER TABLE `transferminer` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `transforks`
-- ----------------------------
ALTER TABLE `transforks` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `transinternal`
-- ----------------------------
ALTER TABLE `transinternal` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `translogs`
-- ----------------------------
ALTER TABLE `translogs` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `transtoken`
-- ----------------------------
ALTER TABLE `transtoken` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for `zk_cluster_info`
-- ----------------------------
ALTER TABLE `zk_cluster_info` AUTO_INCREMENT=1;

-- ----------------------------
-- Records of nfc_bandwidth_config
-- ----------------------------
INSERT INTO `nfc_bandwidth_config` VALUES ('1', 'P1', '1', '300', '0.03', null);
INSERT INTO `nfc_bandwidth_config` VALUES ('2', 'P2', '301', '800', '0.80', null);
INSERT INTO `nfc_bandwidth_config` VALUES ('3', 'P3', '801', '1500', '1.20', null);
INSERT INTO `nfc_bandwidth_config` VALUES ('4', 'P4', '1500', null, '1.60', null);

INSERT INTO `transaction`(`hash`, `istrunk`, `timestamp`, `fromaddr`, `toaddr`, `value`, `nonce`, `gaslimit`, `gasprice`, `status`, `gasused`, `cumulative`, `blockhash`, `blocknumber`, `blockindex`, `input`, `contract`, `error`, `internal`, `type`, `ufoprefix`, `ufoversion`, `ufooperator`, `param1`, `param2`, `param3`, `param4`, `param5`, `param6`) VALUES ('NXa3d23721a356ea67e9ace5995448b128bc3fe93ce37e3e4d720ff53a9cd5864f', 1, '2022-02-10 16:50:39', 'NX7a4539ed8a0b8b4583ead1e5a3f604e83419f502', 'NX7a4539ed8a0b8b4583ead1e5a3f604e83419f502', 0, 3, 21352, 176190476190, 1, 21352, 64024, 'NXaeded418ba96481e1b88ef78ca9c779753faf5fa79300f394242a8fb5769beac', 0, 2, '0x5353433a313a5277644c6f636b3a37383a62343a3363', NULL, NULL, 0, 0, 'SSC', '1', 'RwdLock', '0', '1555200', 8640, NULL, NULL, NULL);
INSERT INTO `transaction`(`hash`, `istrunk`, `timestamp`, `fromaddr`, `toaddr`, `value`, `nonce`, `gaslimit`, `gasprice`, `status`, `gasused`, `cumulative`, `blockhash`, `blocknumber`, `blockindex`, `input`, `contract`, `error`, `internal`, `type`, `ufoprefix`, `ufoversion`, `ufooperator`, `param1`, `param2`, `param3`, `param4`, `param5`, `param6`) VALUES ('NXef6e3fa241571abf927935a4cb7f9d079fe986959a80da53e99bcd03472dc67e', 1, '2022-02-10 16:50:39', 'NX7a4539ed8a0b8b4583ead1e5a3f604e83419f502', 'NX7a4539ed8a0b8b4583ead1e5a3f604e83419f502', 0, 2, 21320, 176190476190, 1, 21320, 42672, 'NXaeded418ba96481e1b88ef78ca9c779753faf5fa79300f394242a8fb5769beac', 0, 1, '0x5353433a313a466c774c6f636b3a37383a303a30', NULL, NULL, 0, 0, 'SSC', '1', 'FlwLock', '1555200', '0', 0, NULL, NULL, NULL);
INSERT INTO `transaction`(`hash`, `istrunk`, `timestamp`, `fromaddr`, `toaddr`, `value`, `nonce`, `gaslimit`, `gasprice`, `status`, `gasused`, `cumulative`, `blockhash`, `blocknumber`, `blockindex`, `input`, `contract`, `error`, `internal`, `type`, `ufoprefix`, `ufoversion`, `ufooperator`, `param1`, `param2`, `param3`, `param4`, `param5`, `param6`) VALUES ('NXba78a093041b51720c7242ba7f5cd262ad52ea5a8eaa36fa0b09ca913fe905f2', 1, '2022-02-10 16:50:39', 'NX7a4539ed8a0b8b4583ead1e5a3f604e83419f502', 'NX7a4539ed8a0b8b4583ead1e5a3f604e83419f502', 0, 0, 21320, 176190476190, 1, 21320, 21320, 'NXe39e572d405216f544870385cdc91831976bd5396b3e8805a1c59fce432a8c9a', 0, 0, '0x5353433a313a45786368526174653a3330303030', NULL, NULL, 0, 0, 'SSC', '1', 'ExchRate', '10000', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `transaction`(`hash`, `istrunk`, `timestamp`, `fromaddr`, `toaddr`, `value`, `nonce`, `gaslimit`, `gasprice`, `status`, `gasused`, `cumulative`, `blockhash`, `blocknumber`, `blockindex`, `input`, `contract`, `error`, `internal`, `type`, `ufoprefix`, `ufoversion`, `ufooperator`, `param1`, `param2`, `param3`, `param4`, `param5`, `param6`) VALUES ('NX2c18ff21a5f63839e3608b8cfc9cd0c363ab29e9384ea4a618f51a2b55735077', 1, '2022-02-10 16:50:39', 'NX7a4539ed8a0b8b4583ead1e5a3f604e83419f502', 'NX7a4539ed8a0b8b4583ead1e5a3f604e83419f502', 0, 7, 246930, 176190476190, 1, 21496, 21496, 'NXaeded418ba96481e1b88ef78ca9c779753faf5fa79300f394242a8fb5769beac', 0, 0, '0x5353433a313a5277644c6f636b3a37383a62343a3363', NULL, NULL, 0, 0, 'SSC', '1', 'Deposit', '36000000000000000000', '0', NULL, NULL, NULL, NULL);

/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.9.202
 Source Server Type    : MySQL
 Source Server Version : 50717
 Source Host           : 192.168.9.202:3306
 Source Schema         : chainexplorer

 Target Server Type    : MySQL
 Target Server Version : 50717
 File Encoding         : 65001

 Date: 12/10/2022 15:48:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for nfc_net_statics
-- ----------------------------
DROP TABLE IF EXISTS `nfc_net_statics`;
CREATE TABLE `nfc_net_statics`  (
  `ctime` datetime(0) NOT NULL COMMENT '格式 yyyy-MM-dd',
  `nfc_gbrate` decimal(20, 4) DEFAULT 0.0000 COMMENT '每GB兑换流量值',
  `total_nfc` decimal(65, 0) DEFAULT 0 COMMENT '最近24小时流量挖矿NFC数量',
  `totalflow` decimal(65, 0) DEFAULT 0 COMMENT '每日总流量 MB',
  `incre_flow` decimal(65, 0) DEFAULT 0 COMMENT '增长流量   与头一天相比',
  `total_bw` decimal(65, 0) DEFAULT 0 COMMENT '全网总带宽',
  `incre_bw` decimal(65, 0) DEFAULT 0 COMMENT '增长带宽  与头一天相比',
  PRIMARY KEY (`ctime`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for nfc_clt_flwdata_day
-- ----------------------------
DROP TABLE IF EXISTS `nfc_clt_flwdata_day`;
CREATE TABLE `nfc_clt_flwdata_day`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `en_address` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'En钱包地址',
  `report_time` datetime(0) DEFAULT NULL COMMENT '计费开始时间',
  `flow_value` bigint(60) DEFAULT NULL COMMENT '有效流量值',
  `real_value` bigint(60) DEFAULT NULL COMMENT '真实流量',
  `fulnum` decimal(65, 0) DEFAULT NULL COMMENT '消耗ful值',
  `profitamount` decimal(65, 0) DEFAULT NULL COMMENT '挖矿收益 NFC',
  `instime` datetime(0) DEFAULT NULL COMMENT '入库时间',
  `blocknumber` bigint(20) DEFAULT NULL COMMENT '区块高度',
  `fwflag` tinyint(4) DEFAULT 0 COMMENT '0流量收益 1带宽奖励',
  `lockamount` decimal(65, 0) DEFAULT NULL,
  `releaseamount` decimal(65, 0) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ad_time_blk`(`en_address`, `report_time`, `blocknumber`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '流量挖矿数据日表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tk_pricedata
-- ----------------------------
DROP TABLE IF EXISTS `tk_pricedata`;
CREATE TABLE `tk_pricedata`  (
  `tokenid` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tokenprice` decimal(30, 8) DEFAULT NULL COMMENT '实时价格',
  `ctime` datetime(0) DEFAULT NULL COMMENT '采集时间',
  PRIMARY KEY (`tokenid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;





CREATE TABLE `flow_report` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `miner_addr` varchar(64) DEFAULT NULL,
  `enid` bigint(20) DEFAULT NULL,
  `deviceid` bigint(20) DEFAULT NULL,
  `client_addr` varchar(64) DEFAULT NULL,
  `report_number` bigint(20) DEFAULT NULL,
  `flow_value` decimal(60,0) DEFAULT NULL,
  `ful` decimal(60,0) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `blocknumber` bigint(20) DEFAULT NULL,
  `blockhash` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


alter table `addresses`   
  add column `ful_block` bigint null after `ful_nonce`;

alter table `transaction`   
  change `input` `input` longtext null;
  
  
  

CREATE TABLE `contract` (
  `contract` varchar(100) NOT NULL,
  `contractname` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `symbol` varchar(255) DEFAULT NULL,
  `txhash` varchar(255) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `blocknumber` bigint(20) DEFAULT NULL,
  `verified` int(11) DEFAULT '0',
  `version` varchar(100) DEFAULT NULL,
  `verifydate` datetime DEFAULT NULL,
  `istoken` int(11) DEFAULT '0',
  `type` int(11) DEFAULT NULL,
  `admin1` varchar(255) DEFAULT NULL,
  `admin2` varchar(255) DEFAULT NULL,
  `lockupstart` bigint(20) DEFAULT NULL,
  `lockupperiod` bigint(20) DEFAULT NULL,
  `releaseperiod` bigint(20) DEFAULT NULL,
  `releaseinterval` bigint(20) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `updatetime` datetime DEFAULT NULL,
  PRIMARY KEY (`contract`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `contract_lockup` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `contract` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `txhash` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `lockupnumber` bigint(20) DEFAULT NULL,
  `lockupamount` decimal(65,0) DEFAULT NULL,
  `pickupamount` decimal(65,0) DEFAULT NULL,
  `remainamount` decimal(65,0) DEFAULT NULL,
  `lockupperiod` bigint(20) DEFAULT NULL,
  `releaseperiod` bigint(20) DEFAULT NULL,
  `releaseinterval` bigint(20) DEFAULT NULL,
  `cancelnumber` bigint(20) DEFAULT NULL,
  `cancelamount` decimal(65,0) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `updatetime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `contract` (`contract`,`address`,`txhash`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `contract_source` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `contract` varchar(255) DEFAULT NULL,
  `filename` varchar(255) DEFAULT NULL,
  `sourcecode` longtext,
  `bin` longtext,
  `abi` longtext,
  `ord` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `contract_version` (
  `version` varchar(100) NOT NULL,
  `vername` varchar(255) DEFAULT NULL,
  `metadata_bytecode` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT '1',
  `ord` int(11) DEFAULT NULL,
  PRIMARY KEY (`version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
  
delete from contract_version;
insert into contract_version (version,vername,metadata_bytecode,status,ord) values
('0.8.14','0.8.14+commit.80d49f37','a264697066735822',1,814),
('0.8.13','0.8.13+commit.abaa5c0e','a264697066735822',1,813),
('0.8.12','0.8.12+commit.f00d7308','a264697066735822',1,812),
('0.8.11','0.8.11+commit.d7f03943','a264697066735822',1,811),
('0.8.10','0.8.10+commit.fc410830','a264697066735822',1,810),
('0.8.9','0.8.9+commit.e5eed63a','a264697066735822',1,809),
('0.8.8','0.8.8+commit.dddeac2f','a264697066735822',1,808),
('0.8.7','0.8.7+commit.e28d00a7','a264697066735822',1,807),
('0.8.6','0.8.6+commit.11564f7e','a264697066735822',1,806),
('0.8.5','0.8.5+commit.a4f2e591','a264697066735822',1,805),
('0.8.4','0.8.4+commit.c7e474f2','a264697066735822',1,804),
('0.8.3','0.8.3+commit.8d00100c','a264697066735822',1,803),
('0.8.2','0.8.2+commit.661d1103','a264697066735822',1,802),
('0.8.1','0.8.1+commit.df193b15','a264697066735822',1,801),
('0.8.0','0.8.0+commit.c7dfd78e','a264697066735822',1,800),
('0.7.6','0.7.6+commit.7338295f','a264697066735822',1,706),
('0.7.5','0.7.5+commit.eb77ed08','a264697066735822',1,705),
('0.7.4','0.7.4+commit.3f05b770','a264697066735822',1,704),
('0.7.3','0.7.3+commit.9bfce1f6','a264697066735822',1,703),
('0.7.2','0.7.2+commit.51b20bc0','a264697066735822',1,702),
('0.7.1','0.7.1+commit.f4a555be','a264697066735822',1,701),
('0.7.0','0.7.0+commit.9e61f92b','a264697066735822',1,700),
('0.6.12','0.6.12+commit.27d51765','a264697066735822',1,612),
('0.6.11','0.6.11+commit.5ef660b1','a264697066735822',1,611),
('0.6.10','0.6.10+commit.00c0fcaf','a264697066735822',1,610),
('0.6.9','0.6.9+commit.3e3065ac','a264697066735822',1,609),
('0.6.8','0.6.8+commit.0bbfe453','a264697066735822',1,608),
('0.6.7','0.6.7+commit.b8d736ae','a264697066735822',1,607),
('0.6.6','0.6.6+commit.6c089d02','a264697066735822',1,606),
('0.6.5','0.6.5+commit.f956cc89','a264697066735822',1,605),
('0.6.4','0.6.4+commit.1dca32f3','a264697066735822',1,604),
('0.6.3','0.6.3+commit.8dda9521','a264697066735822',1,603),
('0.6.2','0.6.2+commit.bacdbe57','a264697066735822',1,602),
('0.6.1','0.6.1+commit.e6f7d5a4','a264697066735822',1,601),
('0.6.0','0.6.0+commit.26b70077','a264697066735822',1,600),
('0.5.17','0.5.17+commit.d19bba13','a265627a7a72315820',1,517),
('0.5.16','0.5.16+commit.9c3226ce','a265627a7a72315820',1,516),
('0.5.15','0.5.15+commit.6a57276f','a265627a7a72315820',1,515),
('0.5.14','0.5.14+commit.01f1aaa4','a265627a7a72315820',1,514),
('0.5.13','0.5.13+commit.5b0b510c','a265627a7a72315820',1,513),
('0.5.12','0.5.12+commit.7709ece9','a265627a7a72315820',1,512),
('0.5.11','0.5.11+commit.22be8592','a265627a7a72305820',1,511),
('0.5.10','0.5.10+commit.5a6ea5b1','a265627a7a72305820',1,510),
('0.5.9','0.5.9+commit.c68bc34e','a265627a7a72305820',1,509),
('0.5.8','0.5.8+commit.23d335f2','a165627a7a72305820',1,508),
('0.5.7','0.5.7+commit.6da8b019','a165627a7a72305820',1,507),
('0.5.6','0.5.6+commit.b259423e','a165627a7a72305820',1,506),
('0.5.5','0.5.5+commit.47a71e8f','a165627a7a72305820',1,505),
('0.5.4','0.5.4+commit.9549d8ff','a165627a7a72305820',1,504),
('0.5.3','0.5.3+commit.10d17f24','a165627a7a72305820',1,503),
('0.5.2','0.5.2+commit.1df8f40c','a165627a7a72305820',1,502),
('0.5.1','0.5.1+commit.c8a2cb62','a165627a7a72305820',1,501),
('0.5.0','0.5.0+commit.1d4f565a','a165627a7a72305820',1,500),
('0.4.26','0.4.26+commit.4563c3fc','a165627a7a72305820',1,426),
('0.4.25','0.4.25+commit.59dbf8f1','a165627a7a72305820',1,425),
('0.4.24','0.4.24+commit.e67f0147','a165627a7a72305820',1,424),
('0.4.23','0.4.23+commit.124ca40d','a165627a7a72305820',1,423),
('0.4.22','0.4.22+commit.4cb486ee','a165627a7a72305820',1,422),
('0.4.21','0.4.21+commit.dfe3193c','a165627a7a72305820',1,421),
('0.4.20','0.4.20+commit.3155dd80','a165627a7a72305820',1,420),
('0.4.19','0.4.19+commit.c4cbbb05','a165627a7a72305820',1,419),
('0.4.18','0.4.18+commit.9cf6e910','a165627a7a72305820',1,418),
('0.4.17','0.4.17+commit.bdeb9e52','a165627a7a72305820',1,417),
('0.4.16','0.4.16+commit.d7661dd9','a165627a7a72305820',0,416),
('0.4.15','0.4.15+commit.8b45bddb','a165627a7a72305820',0,415),
('0.4.14','0.4.14+commit.c2215d46','a165627a7a72305820',0,414),
('0.4.13','0.4.13+commit.0fb4cb1a','a165627a7a72305820',0,413),
('0.4.12','0.4.12+commit.194ff033','a165627a7a72305820',0,412),
('0.4.11','0.4.11+commit.68ef5810','a165627a7a72305820',0,411)
;

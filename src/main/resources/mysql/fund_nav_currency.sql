DROP TABLE IF  EXISTS fund_nav_currency ;

-- 存储货币基金净值的表
CREATE TABLE IF NOT EXISTS  `fund_nav_currency` (
  `record_date` varchar(255) NOT NULL COMMENT '记录的时间,一般为1991-01-01格式',
  `fund_code` varchar(255) NOT NULL COMMENT '基金code',
  `profit_per_units` float(15,8) DEFAULT NULL COMMENT '每万份收益',
  `profit_rate` varchar(255) DEFAULT NULL COMMENT '7日年化收益%',
  `buy_state` varchar(255) DEFAULT NULL COMMENT '申购状态',
  `sell_state` varchar(255) DEFAULT NULL COMMENT '赎回状态',
  `div` varchar(255) DEFAULT NULL COMMENT '分红方式',

  `gmt_create` datetime  DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`record_date`,`fund_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
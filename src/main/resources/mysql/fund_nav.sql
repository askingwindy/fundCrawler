DROP TABLE IF  EXISTS fund_nav ;

-- 存储除了货币基金外的其他类型基金
CREATE TABLE IF NOT EXISTS `fund_nav` (
  `record_date` varchar(255) NOT NULL COMMENT '记录的时间,一般为1991-01-01格式',
  `fund_code` varchar(255) NOT NULL COMMENT '基金code',

  `nav`          FLOAT(15, 8) DEFAULT NULL COMMENT '单位净值',
  `add_nav`      FLOAT(15, 8) DEFAULT NULL COMMENT '累计净值',
  `nav_chg_rate` VARCHAR(255) DEFAULT NULL COMMENT '日增长率%',

  `buy_state` varchar(255) DEFAULT NULL COMMENT '申购状态',
  `sell_state` varchar(255) DEFAULT NULL COMMENT '赎回状态',
  `div` varchar(255) DEFAULT NULL COMMENT '分红方式',

  `gmt_create` datetime  DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`record_date`, `fund_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



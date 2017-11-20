CREATE TABLE `funder_experience_info` (
  `funder_code` varchar(255) NOT NULL COMMENT '基金经理代码',
  `fund_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '基金代码',
  `appointment_date` datetime NOT NULL COMMENT '任期开始时间',
  `dimission_date` datetime DEFAULT NULL COMMENT '任期结束时间',
  `ongoing` tinyint(4) NOT NULL COMMENT '是否仍然在任期',
  `total_return` double NOT NULL DEFAULT '0' COMMENT '任职回报',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据最后更新时间',
  PRIMARY KEY (`funder_code`,`fund_code`,`appointment_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC
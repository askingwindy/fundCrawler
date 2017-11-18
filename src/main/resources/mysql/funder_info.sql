CREATE TABLE `funder_info` (
  `funder_code` varchar(255) NOT NULL COMMENT '基金经理代码',
  `funder_name` varchar(255) NOT NULL COMMENT '基金经理名字',
  `description` text NOT NULL COMMENT '基金经理简介',
  `inauguration_date` datetime NOT NULL COMMENT '任期开始时间',
  `incumbent_company` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '现任基金公司',
  `assets_scale` double NOT NULL DEFAULT '0' COMMENT '现任基金资产总规模',
  `best_return` double NOT NULL DEFAULT '0' COMMENT '任职期间最佳基金回报',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据最后更新时间',
  PRIMARY KEY (`funder_code`,`incumbent_company`,`inauguration_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
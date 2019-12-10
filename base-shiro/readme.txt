共有sys_user，sys_user_role，sys_menu，sys_role_menu,sys_role 5张表。

DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) DEFAULT NULL COMMENT '手机号',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态  0：禁用   1：正常',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='系统用户';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admin', 'df655ad8d3229f3269fad2a8bab59b6c', '100@qq.com', '13666666666', '1', null, '2017-06-01 15:33:20');
INSERT INTO `sys_user` VALUES ('2', 'guest', 'df655ad8d3229f3269fad2a8bab59b6c', 'guest@qq.com', '134', '1', null, '2019-04-18 13:53:14');
INSERT INTO `sys_user` VALUES ('3', 'test', 'df655ad8d3229f3269fad2a8bab59b6c', 'test@qq.com', '1399999', '1', null, '2019-04-18 13:53:47');


DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8 COMMENT='用户与角色对应关系';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('67', '1', '5');
INSERT INTO `sys_user_role` VALUES ('68', '1', '6');



DROP TABLE IF EXISTS `sys_menu`;

CREATE TABLE `sys_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(50) DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(200) DEFAULT NULL COMMENT '菜单URL',
  `perms` varchar(500) DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` int(11) DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(50) DEFAULT NULL COMMENT '菜单图标',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单管理';



INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
VALUES
	(1,0,'系统管理',NULL,NULL,0,'fa fa-cog',0),
	(2,1,'用户管理','sys/user.html',NULL,1,'fa fa-user',1),
	(3,1,'角色管理','sys/role.html',NULL,1,'fa fa-user-secret',2),
	(4,1,'菜单管理','sys/menu.html',NULL,1,'fa fa-th-list',3),
	(5,30,'SQL监控','druid/sql.html',NULL,1,'fa fa-bug',4),
	(6,30,'定时任务管理','schedule/job.html',NULL,2,'fa fa-tasks',5),
	(7,6,'查看',NULL,'sys:schedule:list,sys:schedule:info',2,NULL,0),
	(8,6,'新增',NULL,'schedule:job:save',2,NULL,0),
	(9,6,'修改',NULL,'schedule:job:update',2,NULL,0),
	(10,6,'删除',NULL,'schedule:job:delete',2,NULL,0),
	(11,6,'暂停',NULL,'schedule:job:pause',2,NULL,0),
	(12,6,'恢复',NULL,'schedule:job:resume',2,NULL,0),
	(13,6,'立即执行',NULL,'schedule:job:run',2,NULL,0),
	(14,6,'日志列表',NULL,'sys:schedule:log',2,NULL,0),
	(15,2,'查看',NULL,'sys:user:list,sys:user:info',2,NULL,0),
	(16,2,'新增',NULL,'sys:user:save,sys:role:select',2,NULL,0),
	(17,2,'修改',NULL,'sys:user:update,sys:role:select',2,NULL,0),
	(18,2,'删除',NULL,'sys:user:delete',2,NULL,0),
	(19,3,'查看',NULL,'sys:role:list,sys:role:info',2,NULL,0),
	(20,3,'新增',NULL,'sys:role:save,sys:menu:perms',2,NULL,0),
	(21,3,'修改',NULL,'sys:role:update,sys:menu:perms',2,NULL,0),
	(22,3,'删除',NULL,'sys:role:delete',2,NULL,0),
	(23,4,'查看',NULL,'sys:menu:list,sys:menu:info',2,NULL,0),
	(24,4,'新增',NULL,'sys:menu:save,sys:menu:select',2,NULL,0),
	(25,4,'修改',NULL,'sys:menu:update,sys:menu:select',2,NULL,0),
	(26,4,'删除',NULL,'sys:menu:delete',2,NULL,0),
	(27,1,'参数管理','sys/config.html','sys:config:list,sys:config:info,sys:config:save,sys:config:update,sys:config:delete',1,'fa fa-sun-o',6),
	(30,0,'监控管理',NULL,NULL,0,'fa fa-bug',0),
	(36,0,'商品管理',NULL,NULL,0,'fa fa-car',0),
	(41,36,'自有商品管理','product/productcar.html',NULL,1,'fa fa-car',0),
	(47,0,'统计管理',NULL,NULL,0,'fa fa-user-circle-o',0),
	(48,47,'标签折线图','echarts/line.html',NULL,1,'fa fa-bar-chart',0),
	(49,47,'标签柱状图','echarts/bar.html',NULL,1,'fa fa-bar-chart',0),
	(50,47,'标签饼图','echarts/pie.html',NULL,1,'fa fa-bar-chart',0);




DROP TABLE IF EXISTS `sys_role_menu`;

CREATE TABLE `sys_role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色与菜单对应关系';


INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`)
VALUES
	(190,5,1),
	(191,5,2),
	(192,5,15),
	(193,5,16),
	(194,5,17),
	(195,5,18),
	(196,5,3),
	(197,5,19),
	(198,5,20),
	(199,5,21),
	(200,5,22),
	(201,5,4),
	(202,5,23),
	(203,5,24),
	(204,5,25),
	(205,5,26),
	(206,5,5),
	(207,5,6),
	(208,5,7),
	(209,5,8),
	(210,5,9),
	(211,5,10),
	(212,5,11),
	(213,5,12),
	(214,5,13),
	(215,5,14),
	(216,5,27),
	(225,6,1),
	(226,6,2),
	(227,6,15),
	(228,6,17),
	(229,6,18),
	(230,6,4),
	(231,6,23),
	(232,6,24),
	(233,5,30),
	(239,5,36),
	(244,5,41),
	(250,5,47),
	(251,5,48),
	(252,5,49),
	(253,5,50);


DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='角色';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('5', '管理员', '', null, '2017-10-15 18:05:57');
INSERT INTO `sys_role` VALUES ('6', '测试人员', '', null, '2017-10-17 05:32:10');
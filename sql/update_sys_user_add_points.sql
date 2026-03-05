-- 为 sys_user 表添加积分字段（观众当前可用积分）
ALTER TABLE `sys_user` ADD COLUMN `points` int(11) DEFAULT 0 COMMENT '观众当前可用积分';

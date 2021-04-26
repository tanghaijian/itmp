

-- 修改 tbl_requirement_feature 表的注释
alter table tbl_requirement_feature modify column CREATE_TYPE tinyint(2) DEFAULT NULL COMMENT '创建方式（-1=生产问题，0=提前下发，1=自建，2=同步）';

-- 修改案例实际结果字段长度为500
alter table tbl_test_set_case modify column CASE_ACTUAL_RESULT varchar(500)DEFAULT NULL COMMENT '实际结果';


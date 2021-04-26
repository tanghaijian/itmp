-- 月报汇总基表
drop table `tbl_report_monthly_base`;
CREATE TABLE `tbl_report_monthly_base`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `YEAR_MONTH` varchar(7) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '年月(YYYY-MM)',
  `PLAN_WINDOWS_NUMBER` int(2) DEFAULT 0 COMMENT '计划内版本次数',
  `TEMP_WINDOWS_NUMBER` int(2) DEFAULT 0 COMMENT '临时版本次数',
  `TEMP_ADD_TASK_NUMBER` int(20) DEFAULT 0 COMMENT '临时增加任务数',
  `TEMP_DEL_TASK_NUMBER` int(20) DEFAULT 0 COMMENT '临时删除任务数',
  `TOTAL_TASK_NUMBER` int(20) DEFAULT 0 COMMENT '测试任务总数',
  `REQUIREMENT_NUMBER` int(20) DEFAULT 0 COMMENT '业务需求数',
  `DEFECT_NUMBER` int(20) DEFAULT 0 COMMENT '缺陷数',
  `CHANGE_PERCENT` double(5, 2) DEFAULT 0.00 COMMENT '变更率',

  `BASE_STATUS` tinyint(2) DEFAULT 0 COMMENT '月报状态（0:初始、1:数据生成、2:已修改）',
  `AUDIT_STATUS` tinyint(2) DEFAULT 0 COMMENT '审核状态（0:初始，1：发起审核，2：审核完成，3再次发起审核）',
  `UNDETECTED_NUMBER` int(20) DEFAULT 0 COMMENT '漏检数',
  `DETECTED_RATE` double(5, 2) DEFAULT 0 COMMENT '检出率',
  `REPAIR_ROUND` int(20) DEFAULT 0 COMMENT '总修复轮次',
  `REPAIRED_DEFECT_NUMBER` int(20) DEFAULT 0 COMMENT '修复缺陷数',
  `AVG_REPAIR_ROUND` double(5, 2) DEFAULT 0 COMMENT '平均修复轮次',
  `AGILE_SYSTEMNUM` int(2) DEFAULT 0 COMMENT '敏捷系统个数',

  `CREATE_BY` bigint(20) DEFAULT NULL COMMENT '创建者',
  `CREATE_DATE` timestamp(0) DEFAULT NULL COMMENT '创建时间',
  `LAST_UPDATE_BY` bigint(20) DEFAULT NULL COMMENT '上次修改者',
  `LAST_UPDATE_DATE` timestamp(0) DEFAULT NULL COMMENT '上次修改时间',
  `STATUS` tinyint(2)  DEFAULT '1' COMMENT '状态 1=正常；2=删除',
  PRIMARY KEY (`ID`) USING BTREE
) CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '月报汇总基表' ROW_FORMAT = Compressed;


-- 月报系统数据表
drop table `tbl_report_monthly_system_data`;
CREATE TABLE `tbl_report_monthly_system_data`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `YEAR_MONTH` varchar(7) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '年月(YYYY-MM)',
  `SYSTEM_ID` bigint(20) NOT NULL COMMENT '系统表ID',

  `SYSTEM_NAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '系统名称',
  `SYSTEM_TYPE` tinyint(2) NOT NULL COMMENT '系统类型（1:敏捷、2:运维、3:项目）',

  `TASK_NUMBER` int(20) DEFAULT 0 COMMENT '测试任务数',
  `DEFECT_NUMBER` int(20) DEFAULT 0 COMMENT '缺陷数',
  `REPAIRED_DEFECT_NUMBER` int(20) DEFAULT 0 COMMENT '修复缺陷数',
  `UNREPAIRED_DEFECT_NUMBER` int(20) DEFAULT 0 COMMENT '遗留缺陷数',
  `DESIGN_CASE_NUMBER` int(20) DEFAULT 0 COMMENT '设计用例数',
  `DEFECT_PERCENT` double(5, 2) DEFAULT 0.00 COMMENT '缺陷率',
  `AUDIT_STATUS` tinyint(2) DEFAULT 0 COMMENT '审核状态（0:未审核、1:已审核）',

  `TOTAL_REPAIR_ROUND` int(20) DEFAULT 0 COMMENT '累计修复轮次',
  `AVG_REPAIR_ROUND` double(5, 2) DEFAULT 0.00 COMMENT '平均修复轮次',
  `LASTMONTH_UNDEFECTED_NUMBER` int(20) DEFAULT 0 COMMENT '上月漏检缺陷数',
  `LASTMONTH_UNDEFECTED_BELONGER` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '上月漏检归属',

  `TYPE` tinyint(2) NOT NULL COMMENT '数据类型（0:月报数据、1:审核时页面数据、	2:审核时统计业务数据）',

  `CREATE_BY` bigint(20) DEFAULT NULL COMMENT '创建者',
  `CREATE_DATE` timestamp(0) DEFAULT NULL COMMENT '创建时间',
  `LAST_UPDATE_BY` bigint(20) DEFAULT NULL COMMENT '上次修改者',
  `LAST_UPDATE_DATE` timestamp(0) DEFAULT NULL COMMENT '上次修改时间',
  `STATUS` tinyint(2)  DEFAULT '1' COMMENT '状态 1=正常；2=删除',
  PRIMARY KEY (`ID`) USING BTREE
) CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '月报系统数据表' ROW_FORMAT = Compressed;




-- 任务类型表
drop table `tbl_report_monthly_task_type`;
CREATE TABLE `tbl_report_monthly_task_type`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `YEAR_MONTH` varchar(7) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '年月(YYYY-MM)',

  `SYSTEM_TYPE` tinyint(2) NOT NULL COMMENT '系统类型（1:敏捷、2:运维、3:项目）',

  `TYPE` tinyint(2) DEFAULT 0 COMMENT '任务类型(1:业务需求、2:缺陷修复)',

  `TASK_NUMBER` int(20) DEFAULT 0 COMMENT '任务类型数',

  `PERCENTAGE` double(5, 2) DEFAULT 0.00 COMMENT '任务占比',

  `CREATE_BY` bigint(20) DEFAULT NULL COMMENT '创建者',
  `CREATE_DATE` timestamp(0) DEFAULT NULL COMMENT '创建时间',
  `LAST_UPDATE_BY` bigint(20) DEFAULT NULL COMMENT '上次修改者',
  `LAST_UPDATE_DATE` timestamp(0) DEFAULT NULL COMMENT '上次修改时间',
  `STATUS` tinyint(2)  DEFAULT '1' COMMENT '状态 1=正常；2=删除',
  PRIMARY KEY (`ID`) USING BTREE
) CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '任务类型表' ROW_FORMAT = Compressed;



-- 缺陷等级表
drop table `tbl_report_monthly_defect_level`;
CREATE TABLE `tbl_report_monthly_defect_level`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `YEAR_MONTH` varchar(7) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '年月(YYYY-MM)',

  `SYSTEM_TYPE` tinyint(2) NOT NULL COMMENT '系统类型（1:敏捷、2:运维、3:项目）',

  `LEVEL` tinyint(2) DEFAULT 0 COMMENT '缺陷登记(1:建议性缺陷、2:文字错误、3:轻微缺陷、4:一般性确信啊、5:严重缺陷)',

  `DEFECT_NUMBER` int(20) DEFAULT 0 COMMENT '缺陷数',

  `PERCENTAGE` double(5, 2) DEFAULT 0.00 COMMENT '缺陷占比',



  `CREATE_BY` bigint(20) DEFAULT NULL COMMENT '创建者',
  `CREATE_DATE` timestamp(0) DEFAULT NULL COMMENT '创建时间',
  `LAST_UPDATE_BY` bigint(20) DEFAULT NULL COMMENT '上次修改者',
  `LAST_UPDATE_DATE` timestamp(0) DEFAULT NULL COMMENT '上次修改时间',
  `STATUS` tinyint(2)  DEFAULT '1' COMMENT '状态 1=正常；2=删除',
  PRIMARY KEY (`ID`) USING BTREE
) CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '缺陷等级表' ROW_FORMAT = Compressed;







-- 系统分类数据表
drop table `tbl_report_monthly_system_type_data`;
CREATE TABLE `tbl_report_monthly_system_type_data`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `YEAR_MONTH` varchar(7) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '年月(YYYY-MM)',
  `SYSTEM_TYPE` tinyint(2) NOT NULL COMMENT '系统类型（1:敏捷、2:运维、3:项目）',

  `TOTAL_TASK_NUMBER` int(20) DEFAULT 0 COMMENT '测试任务总数',
  `DESIGN_CASE_NUMBER` int(20) DEFAULT 0 COMMENT '设计用例数',
  `DEFECT_NUMBER` int(20) DEFAULT 0 COMMENT '缺陷数',
  `REPAIRED_DEFECT_NUMBER` int(20) DEFAULT 0 COMMENT '修复缺陷数',
  `UNREPAIRED_DEFECT_NUMBER` int(20) DEFAULT 0 COMMENT '遗留缺陷数',
  `DEFECT_PERCENT` double(5, 2) DEFAULT 0.00 COMMENT '缺陷率',
  `SYSTEM_NUM` int(20) NOT NULL DEFAULT '0' COMMENT '系统个数',
  `CREATE_BY` bigint(20) DEFAULT NULL COMMENT '创建者',
  `CREATE_DATE` timestamp(0) DEFAULT NULL COMMENT '创建时间',
  `LAST_UPDATE_BY` bigint(20) DEFAULT NULL COMMENT '上次修改者',
  `LAST_UPDATE_DATE` timestamp(0) DEFAULT NULL COMMENT '上次修改时间',
  `STATUS` tinyint(2)  DEFAULT '1' COMMENT '状态 1=正常；2=删除',
  PRIMARY KEY (`ID`) USING BTREE
) CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统分类数据表' ROW_FORMAT = Compressed;


-- 配置表
drop table `tbl_report_monthly_config`;
CREATE TABLE `tbl_report_monthly_config`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `USER_ID` bigint(20) NOT NULL COMMENT '人员ID',
  `USER_NAME` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',

  `USER_TYPE` tinyint(2) NOT NULL COMMENT '人员类型（1:月报负责人、2:系统审核人）',

  `SYSTEM_ID` bigint(20) DEFAULT 0 COMMENT '系统表ID',
  `SYSTEM_NAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '系统名称',
  `REMARK` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '备注',

  `CREATE_BY` bigint(20) DEFAULT NULL COMMENT '创建者',
  `CREATE_DATE` timestamp(0) DEFAULT NULL COMMENT '创建时间',
  `LAST_UPDATE_BY` bigint(20) DEFAULT NULL COMMENT '上次修改者',
  `LAST_UPDATE_DATE` timestamp(0) DEFAULT NULL COMMENT '上次修改时间',
  `STATUS` tinyint(2)  DEFAULT '1' COMMENT '状态 1=正常；2=删除',
  PRIMARY KEY (`ID`) USING BTREE
) CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '配置表' ROW_FORMAT = Compressed;




-- 审核表
drop table `tbl_report_monthly_audit`;
CREATE TABLE `tbl_report_monthly_audit`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `YEAR_MONTH` varchar(7) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '年月(YYYY-MM)',


  `USER_ID` bigint(20) NOT NULL COMMENT '人员ID',
  `USER_NAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',


  `SYSTEM_ID` bigint(20) NOT NULL COMMENT '系统表ID',
  `SYSTEM_NAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '系统名称',

  `DATA1_ID` bigint(20) NOT NULL COMMENT '页面数据ID',
  `DATA2_ID` bigint(20) NOT NULL COMMENT '系统统计数据ID',

  `CREATE_BY` bigint(20) DEFAULT NULL COMMENT '创建者',
  `CREATE_DATE` timestamp(0) DEFAULT NULL COMMENT '创建时间',
  `LAST_UPDATE_BY` bigint(20) DEFAULT NULL COMMENT '上次修改者',
  `LAST_UPDATE_DATE` timestamp(0) DEFAULT NULL COMMENT '上次修改时间',
  `STATUS` tinyint(2)  DEFAULT '1' COMMENT '状态 1=正常；2=删除',
  PRIMARY KEY (`ID`) USING BTREE
) CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '审核表' ROW_FORMAT = Compressed;

-- 话术模板表
drop table `tbl_report_monthly_module`;
CREATE TABLE `tbl_report_monthly_module`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `YEAR_MONTH` varchar(7) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '年月(YYYY-MM)',


  `PAGE` tinyint(2) NOT NULL COMMENT '模板页码',


  `AREA` tinyint(2) NOT NULL COMMENT '区域',

  `NUM` tinyint(2) NOT NULL COMMENT '序号',

  `CONTENT` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '备注',

  `CREATE_BY` bigint(20) DEFAULT NULL COMMENT '创建者',
  `CREATE_DATE` timestamp(0) DEFAULT NULL COMMENT '创建时间',
  `LAST_UPDATE_BY` bigint(20) DEFAULT NULL COMMENT '上次修改者',
  `LAST_UPDATE_DATE` timestamp(0) DEFAULT NULL COMMENT '上次修改时间',
  `STATUS` tinyint(2)  DEFAULT '1' COMMENT '状态 1=正常；2=删除',
  PRIMARY KEY (`ID`) USING BTREE
) CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '话术模板表' ROW_FORMAT = Compressed;
-- 系统累计数据统计表
drop table `tbl_report_cumulative_system_data`;
CREATE TABLE `tbl_report_cumulative_system_data`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `YEAR_MONTH` varchar(7) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '年月(YYYY-MM)',
  `SYSTEM_ID` bigint(20) NOT NULL COMMENT '系统表ID',

  `SYSTEM_NAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '系统名称',
  `SYSTEM_TYPE` tinyint(2) NOT NULL COMMENT '系统类型（1:敏捷、2:运维、3:项目）',
  `START_MONTH` tinyint(2) NOT NULL COMMENT '开始统计月份',
  `END_MONTH` tinyint(2) NOT NULL COMMENT '最后统计月份',
  `TASK_NUMBER` int(20) DEFAULT 0 COMMENT '测试任务数',
  `DEFECT_NUMBER` int(20) DEFAULT 0 COMMENT '测试发现缺陷数',
  `REPAIRED_DEFECT_NUMBER` int(20) DEFAULT 0 COMMENT '修复缺陷数',
  `UNREPAIRED_DEFECT_NUMBER` int(20) DEFAULT 0 COMMENT '遗留缺陷数',
  `DESIGN_CASE_NUMBER` int(20) DEFAULT 0 COMMENT '设计用例数',
  `DEFECT_PERCENT` double(5, 2) DEFAULT 0.00 COMMENT '缺陷率',
  `UNDEFECTED_NUMBER` int(20) DEFAULT 0 COMMENT '漏检缺陷数',
  `DEFECTED_RATE` double(5, 2) DEFAULT 0.00 COMMENT '检出率',
  `TOTAL_REPAIR_ROUND` int(20) DEFAULT 0 COMMENT '累计修复轮次',
  `AVG_REPAIR_ROUND` double(5, 2) DEFAULT 0.00 COMMENT '平均累计修复轮次',


  `CREATE_BY` bigint(20) DEFAULT NULL COMMENT '创建者',
  `CREATE_DATE` timestamp(0) DEFAULT NULL COMMENT '创建时间',
  `LAST_UPDATE_BY` bigint(20) DEFAULT NULL COMMENT '上次修改者',
    `LAST_UPDATE_DATE` timestamp(0) DEFAULT NULL COMMENT '上次修改时间',
  `STATUS` tinyint(2)  DEFAULT '1' COMMENT '状态 1=正常；2=删除',
  PRIMARY KEY (`ID`) USING BTREE
) CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统累计数据统计表' ROW_FORMAT = Compressed;

-- 漏检经验总结
drop table `tbl_report_monthly_undetected_summary`;
CREATE TABLE `tbl_report_monthly_undetected_summary`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `ITMS_CODE` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'ITMS编号',
  `SYSTEM_NAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '项目',
  `SERIOUS_LEVEL` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '严重性',
  `REPORT_DATE` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '报告日期',
  `SUMMARY` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '摘要',
  `REQUIREMENT_CODE` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '需求编号',
  `REASON_ANALYSIS` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '原因分析',
  `MISSED_REASON` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '漏检原因',
  `EXPERIENCE_SUMMARY` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '测试经验总结',
  `CREATE_BY` bigint(20) DEFAULT NULL COMMENT '创建者',
  `CREATE_DATE` timestamp(0) DEFAULT NULL COMMENT '创建时间',
  `LAST_UPDATE_BY` bigint(20) DEFAULT NULL COMMENT '上次修改者',
  `LAST_UPDATE_DATE` timestamp(0) DEFAULT NULL COMMENT '上次修改时间',
  `STATUS` tinyint(2)  DEFAULT '1' COMMENT '状态 1=正常；2=删除',
  PRIMARY KEY (`ID`) USING BTREE
) CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '漏检经验总结' ROW_FORMAT = Compressed;



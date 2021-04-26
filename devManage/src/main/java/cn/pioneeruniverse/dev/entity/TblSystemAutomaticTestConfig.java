package cn.pioneeruniverse.dev.entity;

import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;
/**
 *
 * @ClassName: TblSystemAutomaticTestConfig
 * @Description: 自动化测试实体类
 * @author author
 *
 */
@TableName("tbl_system_automatic_test_config")
public class TblSystemAutomaticTestConfig extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Long systemId;//系统id

	private Integer environmentType;//环境类型

	private Long systemModuleId;//模块id

	private String testScene;//测试场景

	private Integer testType;//测试类型

	public Long getSystemId() {
		return systemId;
	}

	public void setSystemId(Long systemId) {
		this.systemId = systemId;
	}

	public Integer getEnvironmentType() {
		return environmentType;
	}

	public void setEnvironmentType(Integer environmentType) {
		this.environmentType = environmentType;
	}

	public Long getSystemModuleId() {
		return systemModuleId;
	}

	public void setSystemModuleId(Long systemModuleId) {
		this.systemModuleId = systemModuleId;
	}

	public String getTestScene() {
		return testScene;
	}

	public void setTestScene(String testScene) {
		this.testScene = testScene;
	}
	public Integer getTestType() {
		return testType;
	}

	public void setTestType(Integer testType) {
		this.testType = testType;
	}

}
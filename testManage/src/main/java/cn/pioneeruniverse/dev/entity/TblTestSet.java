package cn.pioneeruniverse.dev.entity;

import com.baomidou.mybatisplus.annotations.TableField;

import cn.pioneeruniverse.common.entity.BaseEntity;

public class TblTestSet extends BaseEntity{

    /**
	 * 
	 */
	private static final long serialVersionUID = 2055248377349260724L;

	private Long testTaskId;
    
    private String testSetName; 
    
    private String testSetNumber;

    private Integer excuteRound;
    
    @TableField(exist = false)
    private String createBys;//
    @TableField(exist = false)
    private String testTaskIds;
    @TableField(exist = false)
    private Long uid;
    
    
    

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getCreateBys() {
		return createBys;
	}

	public void setCreateBys(String createBys) {
		this.createBys = createBys;
	}

	public String getTestTaskIds() {
		return testTaskIds;
	}

	public void setTestTaskIds(String testTaskIds) {
		this.testTaskIds = testTaskIds;
	}

	public Long getTestTaskId() {
		return testTaskId;
	}

	public void setTestTaskId(Long testTaskId) {
		this.testTaskId = testTaskId;
	}

	public String getTestSetName() {
		return testSetName;
	}

	public void setTestSetName(String testSetName) {
		this.testSetName = testSetName;
	}

	public String getTestSetNumber() {
		return testSetNumber;
	}

	public void setTestSetNumber(String testSetNumber) {
		this.testSetNumber = testSetNumber;
	}

	public Integer getExcuteRound() {
		return excuteRound;
	}

	public void setExcuteRound(Integer excuteRound) {
		this.excuteRound = excuteRound;
	}

	
    
    
}
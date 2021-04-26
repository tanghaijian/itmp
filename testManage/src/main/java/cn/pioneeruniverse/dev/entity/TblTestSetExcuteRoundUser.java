package cn.pioneeruniverse.dev.entity;


import cn.pioneeruniverse.common.entity.BaseEntity;

public class TblTestSetExcuteRoundUser extends BaseEntity{

    /**
	 * 
	 */
	private static final long serialVersionUID = 5715571860426103659L;

	private Long testSetId;// 测试集

    private Integer excuteRound;//执行伦茨

    private Long excuteUserId;// 执行人

	public Long getTestSetId() {
		return testSetId;
	}

	public void setTestSetId(Long testSetId) {
		this.testSetId = testSetId;
	}

	public Integer getExcuteRound() {
		return excuteRound;
	}

	public void setExcuteRound(Integer excuteRound) {
		this.excuteRound = excuteRound;
	}

	public Long getExcuteUserId() {
		return excuteUserId;
	}

	public void setExcuteUserId(Long excuteUserId) {
		this.excuteUserId = excuteUserId;
	}

    
}
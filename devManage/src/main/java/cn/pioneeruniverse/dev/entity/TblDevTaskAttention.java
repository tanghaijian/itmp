package cn.pioneeruniverse.dev.entity;

import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 * 
* @ClassName: TblDevTaskAttention
* @Description: 关注开发任务bean
* @author author
* @date 2020年8月24日 下午3:09:27
*
 */
@TableName("tbl_dev_task_attention")
public class TblDevTaskAttention extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Long devTaskId; //开发任务ID

	private Long userId; //用户ID

	public Long getDevTaskId() {
		return devTaskId;
	}

	public void setDevTaskId(Long devTaskId) {
		this.devTaskId = devTaskId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
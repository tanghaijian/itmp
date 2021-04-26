package cn.pioneeruniverse.dev.entity;
import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableName;
/**
 *
 * @ClassName:TblDevTaskLog
 * @Description:工作任务日志
 * @author author
 * @date 2020年8月16日
 *
 */
@TableName("tbl_dev_task_log")
public class TblDevTaskLog extends BaseEntity{

    

	private Long devTaskId; //工作任务ID

    private String logType; //日志类型，汉字说明比如新增工作任务、修改工作任务等

    private String logDetail; //日志明细

    private Long userId;//用户ID

    private String userName;//用户姓名

    private String userAccount;//用户账号


    public Long getDevTaskId() {
		return devTaskId;
	}

	public void setDevTaskId(Long devTaskId) {
		this.devTaskId = devTaskId;
	}

	public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType == null ? null : logType.trim();
    }

    public String getLogDetail() {
        return logDetail;
    }

    public void setLogDetail(String logDetail) {
        this.logDetail = logDetail == null ? null : logDetail.trim();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount == null ? null : userAccount.trim();
    }

    

}
package cn.pioneeruniverse.dev.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

@TableName("tbl_commissioning_window")
public class TblCommissioningWindow extends BaseEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
	@DateTimeFormat(pattern="yyyy-MM-dd")
    private Date windowDate;//窗口日期

    private String windowName; //窗口名

    private Byte windowType; //窗口类型（1:常规，2:非常规）

    private String windowVersion; //窗口版本号
    
    @TableField(exist = false)
    private String featureStatus;	//开发任务状态

    public Date getWindowDate() {
        return windowDate;
    }

    public void setWindowDate(Date windowDate) {
        this.windowDate = windowDate;
    }

    public String getWindowName() {
        return windowName;
    }

    public void setWindowName(String windowName) {
        this.windowName = windowName == null ? null : windowName.trim();
    }

    public Byte getWindowType() {
        return windowType;
    }

    public void setWindowType(Byte windowType) {
        this.windowType = windowType;
    }

    public String getWindowVersion() {
        return windowVersion;
    }

    public void setWindowVersion(String windowVersion) {
        this.windowVersion = windowVersion == null ? null : windowVersion.trim();
    }

	public String getFeatureStatus() {
		return featureStatus;
	}

	public void setFeatureStatus(String featureStatus) {
		this.featureStatus = featureStatus;
	}
      
}
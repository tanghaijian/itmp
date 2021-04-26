package cn.pioneeruniverse.project.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import cn.pioneeruniverse.common.entity.BaseEntity;
/**
 *
 * @ClassName:RequirementFeatureVo
 * @Description
 * @author author
 * @date 2020年8月27日
 *
 */
public class RequirementFeatureVo extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    @TableField(exist=false)
    private String systemName;//系统名称
    @TableField(exist=false)
    private String userName;//开发任务管理人员名称
    @TableField(exist=false)
    private String valueName;//开发任务状态
    @TableField(exist=false)
    private String windowName;//投产窗口名称

    public String getSystemName() {
        return systemName;
    }
    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getValueName() {
        return valueName;
    }
    public void setValueName(String valueName) {
        this.valueName = valueName;
    }
    public String getWindowName() {
        return windowName;
    }
    public void setWindowName(String windowName) {
        this.windowName = windowName;
    }
}
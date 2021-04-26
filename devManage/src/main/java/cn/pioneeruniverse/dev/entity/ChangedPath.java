package cn.pioneeruniverse.dev.entity;

import java.io.Serializable;

/**
 * 
* @ClassName: ChangedPath
* @Description: logSVNEntry辅助
* @author author
* @date 2020年8月11日 上午9:55:04
*
 */
public class ChangedPath implements Serializable {
    private static final long serialVersionUID = -2447525021523407884L;

    private String type;//类型
    private String path;//变化的路径
    private String copyPathMsg;//日志信息

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCopyPathMsg() {
        return copyPathMsg;
    }

    public void setCopyPathMsg(String copyPathMsg) {
        this.copyPathMsg = copyPathMsg;
    }
}

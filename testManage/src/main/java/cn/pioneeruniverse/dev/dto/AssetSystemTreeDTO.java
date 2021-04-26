package cn.pioneeruniverse.dev.dto;

import java.io.Serializable;
/**
 *
 * @ClassName:AssetSystemTreeDTO
 * @Description 系统树dto
 * @author author
 * @date 2020年8月26日
 *
 */

public class AssetSystemTreeDTO implements Serializable {

    private static final long serialVersionUID = -6496067455862633155L;


    private Long ID;                  //ID
    private String systemTreeName;  //名称
    private String version;         //版本号
    private String valueCode;       //数据字典code
    private String valueName;       //数据字典value



    public AssetSystemTreeDTO() {
    }

    public AssetSystemTreeDTO(Long ID, String systemTreeName, String version, String valueCode, String valueName) {
        this.ID = ID;
        this.systemTreeName = systemTreeName;
        this.version = version;
        this.valueCode = valueCode;
        this.valueName = valueName;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getSystemTreeName() {
        return systemTreeName;
    }

    public void setSystemTreeName(String systemTreeName) {
        this.systemTreeName = systemTreeName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getValueCode() {
        return valueCode;
    }

    public void setValueCode(String valueCode) {
        this.valueCode = valueCode;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    @Override
    public String toString() {
        return "AssetSystemTreeDTO{" +
                "ID=" + ID +
                ", systemTreeName='" + systemTreeName + '\'' +
                ", version='" + version + '\'' +
                ", valueCode='" + valueCode + '\'' +
                ", valueName='" + valueName + '\'' +
                '}';
    }


}

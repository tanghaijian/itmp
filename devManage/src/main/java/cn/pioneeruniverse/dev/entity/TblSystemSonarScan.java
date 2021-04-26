package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 
* @ClassName: TblSystemSonarScan
* @Description: 未用
* @author author
* @date 2020年8月25日 上午11:08:50
*
 */
@TableName("tbl_system_sonar_scan")
public class TblSystemSonarScan extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long systemSonarId;

    private String sonarProjectVersion;

    private String scanResult;


    public Long getSystemSonarId() {
        return systemSonarId;
    }

    public void setSystemSonarId(Long systemSonarId) {
        this.systemSonarId = systemSonarId;
    }

    public String getSonarProjectVersion() {
        return sonarProjectVersion;
    }

    public void setSonarProjectVersion(String sonarProjectVersion) {
        this.sonarProjectVersion = sonarProjectVersion == null ? null : sonarProjectVersion.trim();
    }

    public String getScanResult() {
        return scanResult;
    }

    public void setScanResult(String scanResult) {
        this.scanResult = scanResult == null ? null : scanResult.trim();
    }

}
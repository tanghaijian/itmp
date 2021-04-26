package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 13:24 2019/10/21
 * @Modified By:
 */
@TableName("tbl_system_scm_submit_regex")
public class TblSystemScmSubmitRegex extends BaseEntity {

    private Long systemId;//系统ID

    private Long systemScmRepositoryId; //TblSystemScmRepository的id

    private String scmRepositoryName;//仓库名

    private String regex;//正则表达式，一般为文件路径的正则表达式，表示该路径下为Ingore

    private String submitUserNames; //提交人员ID，以,隔开

    @TableField(exist = false)
    private String submitUserRealNames;//提交人姓名，以,隔开

    @TableField(exist = false)
    private Set<String> submitUserNamesCollection; //提交人姓名,以set表示


    public TblSystemScmSubmitRegex() {

    }

    public TblSystemScmSubmitRegex(Long systemId, Long systemScmRepositoryId, String scmRepositoryName) {
        this.systemId = systemId;
        this.systemScmRepositoryId = systemScmRepositoryId;
        this.scmRepositoryName = scmRepositoryName;
    }

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public Long getSystemScmRepositoryId() {
        return systemScmRepositoryId;
    }

    public void setSystemScmRepositoryId(Long systemScmRepositoryId) {
        this.systemScmRepositoryId = systemScmRepositoryId;
    }

    public String getScmRepositoryName() {
        return scmRepositoryName;
    }

    public void setScmRepositoryName(String scmRepositoryName) {
        this.scmRepositoryName = scmRepositoryName;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getSubmitUserNames() {
        return submitUserNames;
    }

    public void setSubmitUserNames(String submitUserNames) {
        this.submitUserNames = submitUserNames;
    }

    public String getSubmitUserRealNames() {
        return submitUserRealNames;
    }

    public void setSubmitUserRealNames(String submitUserRealNames) {
        this.submitUserRealNames = submitUserRealNames;
    }

    public Set<String> getSubmitUserNamesCollection() {
        return submitUserNamesCollection;
    }

    public void setSubmitUserNamesCollection(String submitUserNames) {
        Set<String> submitUserNamesCollection = new HashSet<>();
        submitUserNamesCollection.addAll(Arrays.asList(submitUserNames.split(",|，")));
        this.submitUserNamesCollection = submitUserNamesCollection;
    }
}

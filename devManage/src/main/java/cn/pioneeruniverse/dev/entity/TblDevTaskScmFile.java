package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 15:10 2019/3/8
 * @Modified By:
 */
@TableName("tbl_dev_task_scm_file")
public class TblDevTaskScmFile extends BaseEntity {

    private static final long serialVersionUID = -7247498989696700169L;

    /**
     * 工作任务ID
     **/
    private Long devTaskId;//工作任务ID

    /**
     * 工作任务源码关联ID
     **/
    private Long devTaskScmId; //工作任务源码关联ID

    /**
     * 源码url
     **/
    private String scmUrl; //源码url

    /**
     * 提交版本号
     **/
    private String commitNumber; //提交版本号

    /**
     * 上一次提交版本号
     **/
    private String lastCommitNumber;//上一次提交版本号

    /**
     * 提交的文件
     **/
    private String commitFile; //提交的文件

    /**
     * 操作类型
     **/
    private String operateType;//操作类型

    @TableField(exist = false)
    private String fileContentCharset;

    public Long getDevTaskId() {
        return devTaskId;
    }

    public void setDevTaskId(Long devTaskId) {
        this.devTaskId = devTaskId;
    }

    public Long getDevTaskScmId() {
        return devTaskScmId;
    }

    public void setDevTaskScmId(Long devTaskScmId) {
        this.devTaskScmId = devTaskScmId;
    }

    public String getScmUrl() {
        return scmUrl;
    }

    public void setScmUrl(String scmUrl) {
        this.scmUrl = scmUrl;
    }

    public String getCommitNumber() {
        return commitNumber;
    }

    public void setCommitNumber(String commitNumber) {
        this.commitNumber = commitNumber;
    }

    public String getLastCommitNumber() {
        return lastCommitNumber;
    }

    public void setLastCommitNumber(String lastCommitNumber) {
        this.lastCommitNumber = lastCommitNumber;
    }

    public String getCommitFile() {
        return commitFile;
    }

    public void setCommitFile(String commitFile) {
        this.commitFile = commitFile;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getFileContentCharset() {
        return fileContentCharset;
    }

    public void setFileContentCharset(String fileContentCharset) {
        this.fileContentCharset = fileContentCharset;
    }
}

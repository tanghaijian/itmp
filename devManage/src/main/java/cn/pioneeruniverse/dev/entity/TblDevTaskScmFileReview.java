package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 15:11 2019/3/8
 * @Modified By:
 */
@TableName("tbl_dev_task_scm_file_review")
public class TblDevTaskScmFileReview extends BaseEntity {

    private static final long serialVersionUID = -7579067610766519935L;

    private Integer scmFileType;//对应文件类型（1=SVN；2=GIT）

    private Long devTaskScmFileId;//开发任务文件提交表主键

    private Long reviewUserId;//评审用户

    private String reviewComment;//评审意见

    @TableField(exist = false)
    private String reviewUserName;


    public Long getDevTaskScmFileId() {
        return devTaskScmFileId;
    }

    public void setDevTaskScmFileId(Long devTaskScmFileId) {
        this.devTaskScmFileId = devTaskScmFileId;
    }

    public Long getReviewUserId() {
        return reviewUserId;
    }

    public void setReviewUserId(Long reviewUserId) {
        this.reviewUserId = reviewUserId;
    }

    public String getReviewComment() {
        return reviewComment;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }

    public String getReviewUserName() {
        return reviewUserName;
    }

    public void setReviewUserName(String reviewUserName) {
        this.reviewUserName = reviewUserName;
    }

    public Integer getScmFileType() {
        return scmFileType;
    }

    public void setScmFileType(Integer scmFileType) {
        this.scmFileType = scmFileType;
    }
}

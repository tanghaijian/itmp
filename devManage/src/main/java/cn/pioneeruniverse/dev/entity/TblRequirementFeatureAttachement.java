package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 
* @ClassName: TblRequirementFeatureAttachement
* @Description: 开发任务附件
* @author author
* @date 2020年8月8日 上午10:07:23
*
 */
@TableName("tbl_requirement_feature_attachement")
public class TblRequirementFeatureAttachement extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long requirementFeatureId; //开发任务ID

    private String fileNameNew; //重命名后的文件

    private String fileNameOld;//重命名前的文件

    private String fileType;//文件类型

    private String filePath;//文件存储路径（未用到）

    private String fileS3Bucket; //S3桶名

    private String fileS3Key; //S3 Key

    public Long getRequirementFeatureId() {
        return requirementFeatureId;
    }

    public void setRequirementFeatureId(Long requirementFeatureId) {
        this.requirementFeatureId = requirementFeatureId;
    }

    public String getFileNameNew() {
        return fileNameNew;
    }

    public void setFileNameNew(String fileNameNew) {
        this.fileNameNew = fileNameNew == null ? null : fileNameNew.trim();
    }

    public String getFileNameOld() {
        return fileNameOld;
    }

    public void setFileNameOld(String fileNameOld) {
        this.fileNameOld = fileNameOld == null ? null : fileNameOld.trim();
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType == null ? null : fileType.trim();
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath == null ? null : filePath.trim();
    }

    public String getFileS3Bucket() {
        return fileS3Bucket;
    }

    public void setFileS3Bucket(String fileS3Bucket) {
        this.fileS3Bucket = fileS3Bucket;
    }

    public String getFileS3Key() {
        return fileS3Key;
    }

    public void setFileS3Key(String fileS3Key) {
        this.fileS3Key = fileS3Key;
    }


}
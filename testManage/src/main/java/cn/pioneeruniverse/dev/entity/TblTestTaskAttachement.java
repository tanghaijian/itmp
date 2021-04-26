package cn.pioneeruniverse.dev.entity;

import java.util.Date;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("tbl_test_task_attachement")
public class TblTestTaskAttachement extends BaseEntity {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Long testTaskId;

    /**
     * 新名称
     **/
    private String fileNameNew;// 新名称

    /**
     * 旧名称
     **/
    private String fileNameOld;//旧名称

    /**
     * 文件类型
     **/
    private String fileType;// 文件类型

    /**
     * 路径
     **/
    private String filePath;// 路径

    /**
     * s3桶名
     **/
    private String fileS3Bucket;// s3桶名

    /**
     * s3key值
     **/
    private String fileS3Key;// s3key值

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

    public static long getSerialversionuid() {
        return serialVersionUID;
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

    public Long getTestTaskId() {
        return testTaskId;
    }

    public void setTestTaskId(Long testTaskId) {
        this.testTaskId = testTaskId;
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
}
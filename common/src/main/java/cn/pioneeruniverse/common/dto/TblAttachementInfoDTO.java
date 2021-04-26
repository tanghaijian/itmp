package cn.pioneeruniverse.common.dto;

import cn.pioneeruniverse.common.entity.BaseEntity;
import cn.pioneeruniverse.common.utils.CommonUtil;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
/**
 * Description:
 * Author:liushan
 * Date: 2018/12/24 下午 7:11
 */
public class TblAttachementInfoDTO extends BaseEntity {

	// 关联的其他表的主键，比如缺陷附件ID，开发任务附件ID....
    private Long associatedId;
  //上传前的文件名
    private String fileNameOld;
  //文件类型
    private String fileType;
  //文件路径
    private String filePath;
  //s3桶
    private String fileS3Bucket;
  //s3的key值
    private String fileS3Key; 

    public TblAttachementInfoDTO(){
        this.setFilePath("C:\\itmpFile\\");
    }

    public TblAttachementInfoDTO( HttpServletRequest request){
        Long userId = CommonUtil.getCurrentUserId(request);
        this.setStatus(1);
        this.setCreateBy(userId);
        this.setCreateDate(new Timestamp(System.currentTimeMillis()));
    }

    public Long getAssociatedId() {
        return associatedId;
    }

    public void setAssociatedId(Long associatedId) {
        this.associatedId = associatedId;
    }

    public String getFileNameOld() {
        return fileNameOld;
    }

    public void setFileNameOld(String fileNameOld) {
        this.fileNameOld = fileNameOld;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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

    @Override
    public String toString() {
        return "TblAttachementInfoDTO{" +
                "associatedId=" + associatedId +
                ", fileNameOld='" + fileNameOld + '\'' +
                ", fileType='" + fileType + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileS3Bucket='" + fileS3Bucket + '\'' +
                ", fileS3Key='" + fileS3Key + '\'' +
                '}';
    }
}

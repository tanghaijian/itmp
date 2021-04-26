package cn.pioneeruniverse.project.dto;

import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;
/**
 *
 * @ClassName:SystemDirectoryDocumentDTO
 * @Description:系统目录文档类
 * @author author
 * @date 2020年8月16日
 *
 */
public class SystemDirectoryDocumentDTO implements Serializable {

    private static final long serialVersionUID = -6496067455862633155L;
    private Long id;
    private String documentS3Bucket;  //S3对象存储的BUCKET
    private Long lastUpdateBy;      //上次修改者
    private String lastUpdateDate;   //上次修改时间
    private String documentMongoKey;   //MONGO存储KEY
    private String documentTempMongoKey;  //MONGO存储KEY(存放临时数据)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(Long lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public String getLastUpdateDate() {
        long l = System.currentTimeMillis();
        Date date = new Date(l);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.lastUpdateDate = format.format(date);
        return format.format(date);
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getDocumentMongoKey() {
        return documentMongoKey;
    }

    public void setDocumentMongoKey(String documentMongoKey) {
        this.documentMongoKey = documentMongoKey;
    }

    public String getDocumentTempMongoKey() {
        return documentTempMongoKey;
    }

    public void setDocumentTempMongoKey(String documentTempMongoKey) {
        this.documentTempMongoKey = documentTempMongoKey;
    }

    public String getDocumentS3Bucket() {
        return documentS3Bucket;
    }

    public void setDocumentS3Bucket(String documentS3Bucket) {
        this.documentS3Bucket = documentS3Bucket;
    }

    @Override
    public String toString() {
        return "SystemDirectoryDocumentDTO{" +
                "id=" + id +
                ", documentS3Bucket='" + documentS3Bucket + '\'' +
                ", lastUpdateBy=" + lastUpdateBy +
                ", lastUpdateDate='" + lastUpdateDate + '\'' +
                ", documentMongoKey='" + documentMongoKey + '\'' +
                ", documentTempMongoKey='" + documentTempMongoKey + '\'' +
                '}';
    }
}

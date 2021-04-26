package cn.pioneeruniverse.dev.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;
/**
 *
 * @ClassName: TblArtifactTag
 * @Description: 制品标签类
 * @author author
 *
 */
@TableName("tbl_artifact_tag")
public class TblArtifactTag extends BaseEntity{
	private static final long serialVersionUID = -3768946118855786989L;
	private Long artifactId;//制品包id
	private Integer environmentType;//环境类型
	private Long tagUserId;//打标签用户
	private Date tagTime;//打标签时间
	public Long getArtifactId() {
		return artifactId;
	}
	public void setArtifactId(Long artifactId) {
		this.artifactId = artifactId;
	}
	public Integer getEnvironmentType() {
		return environmentType;
	}
	public void setEnvironmentType(Integer environmentType) {
		this.environmentType = environmentType;
	}
	public Long getTagUserId() {
		return tagUserId;
	}
	public void setTagUserId(Long tagUserId) {
		this.tagUserId = tagUserId;
	}
	public Date getTagTime() {
		return tagTime;
	}
	public void setTagTime(Date tagTime) {
		this.tagTime = tagTime;
	}

}

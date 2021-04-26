package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;

import java.util.Date;

/**
 *
 * @ClassName: TblQualityGate
 * @Description: 质量门禁表
 * @author author
 *
 */

public class TblQualityGate extends BaseEntity {
    private Long id;//主键

    private String qualityGateName;//名称

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQualityGateName() {
        return qualityGateName;
    }

    public void setQualityGateName(String qualityGateName) {
        this.qualityGateName = qualityGateName == null ? null : qualityGateName.trim();
    }




}
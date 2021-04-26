package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 *
 * @ClassName: TblQualityGateSystem
 * @Description: 质量门禁系统关联表
 * @author author
 *
 */
public class TblQualityGateSystem extends BaseEntity {
    private Long id;

    private Long qualityGateId;//质量门禁ID

    private Long systemId;//系统id

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQualityGateId() {
        return qualityGateId;
    }

    public void setQualityGateId(Long qualityGateId) {
        this.qualityGateId = qualityGateId;
    }

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }


}
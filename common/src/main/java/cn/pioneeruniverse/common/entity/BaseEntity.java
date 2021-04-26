package cn.pioneeruniverse.common.entity;

import cn.pioneeruniverse.common.utils.CommonUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;


import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;


public class BaseEntity implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -6496067455862633155L;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建者
     */
    private Long createBy;

    /**
     * 创建时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Timestamp createDate;

    /**
     * 上次修改者
     */
    private Long lastUpdateBy;

    /**
     * 上次修改时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Timestamp lastUpdateDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Long getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(Long lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public Timestamp getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Timestamp lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public void preInsertOrUpdate(HttpServletRequest request) {
        if (this.getId() == null) {
            this.setCreateBy(CommonUtil.getCurrentUserId(request));
            this.setCreateDate(new Timestamp(new Date().getTime()));
            this.setStatus(DEL_FLAG_NORMAL);
        }
        this.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
        this.setLastUpdateDate(new Timestamp(new Date().getTime()));
    }

    /**
     * 删除标记（1：正常；2：删除）
     */
    public static final Integer DEL_FLAG_NORMAL = 1;
    public static final Integer DEL_FLAG_DELETE = 2;
}

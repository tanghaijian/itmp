package cn.pioneeruniverse.dev.entity;

import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;
/**
 * @Author:weiji
 * @Description:第一层系统，对应大地的系统
 */
@TableName("tbl_top_system_info")
public class TblTopSystemInfo extends BaseEntity  {
    private Long id;

    private String systemName;//系统名

    private String systemCode;//系统编码



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName == null ? null : systemName.trim();
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode == null ? null : systemCode.trim();
    }





}
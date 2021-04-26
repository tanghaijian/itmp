package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 9:54 2019/5/29
 * @Modified By:
 */
@TableName("tbl_system_scm_repository")
public class TblSystemScmRepository extends BaseEntity {

    private Long systemId; //系统ID

    private Integer scmType; //仓库类型1git,2svn

    private Long toolId;//工具ID

    private String repositoryName;//仓库名

    private Long gitRepositoryId;//gitlab的projectId

    @TableField(exist = false)
    private String ip;

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public Integer getScmType() {
        return scmType;
    }

    public void setScmType(Integer scmType) {
        this.scmType = scmType;
    }

    public Long getToolId() {
        return toolId;
    }

    public void setToolId(Long toolId) {
        this.toolId = toolId;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getGitRepositoryId() {
        return gitRepositoryId;
    }

    public void setGitRepositoryId(Long gitRepositoryId) {
        this.gitRepositoryId = gitRepositoryId;
    }
}

package cn.pioneeruniverse.dev.service.scm;

import cn.pioneeruniverse.common.bean.PageResult;
import cn.pioneeruniverse.dev.entity.TblSystemScmSubmit;
import cn.pioneeruniverse.dev.entity.TblSystemScmSubmitLog;
import cn.pioneeruniverse.dev.entity.TblUserInfo;
import cn.pioneeruniverse.dev.vo.scm.VersionReq;

import java.util.List;

/**
 * @author by dyx11
 * @version 2020/8/22 18:35
 * @description 资源库版本管理
 */
public interface VersionService {

    /**
     * 查询资源库配置信息
     */
    List<TblSystemScmSubmit> findScmConfig(Long systemId, Integer scmType);

    /**
     * 查询系统用户
     */
    List<TblUserInfo> findProjectUser(Long systemId, String userName);

    /**
     * 查询操作日志
     */
    PageResult<TblSystemScmSubmitLog> findOperationLog(Long systemId, int page);

    /**
     * 修改提交用户配置
     */
    int updateSubmitUser(List<VersionReq> req, Long operation);

}

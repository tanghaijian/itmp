package cn.pioneeruniverse.dev.controller.scm;

import cn.pioneeruniverse.common.bean.PageResult;
import cn.pioneeruniverse.dev.entity.TblSystemScmSubmit;
import cn.pioneeruniverse.dev.entity.TblSystemScmSubmitLog;
import cn.pioneeruniverse.dev.entity.TblUserInfo;
import cn.pioneeruniverse.dev.service.scm.VersionService;
import cn.pioneeruniverse.dev.vo.scm.VersionReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static cn.pioneeruniverse.common.utils.CommonUtil.getToken;
import static cn.pioneeruniverse.common.utils.CommonUtil.getUserIdByToken;

/**
 * @author by qingcheng
 * @version 2020/8/20 16:46
 * @description
 */
@RestController("rebuildVersionController")
@RequestMapping(value = "version")
public class VersionController {

    //======================================================
    // 属性
    private VersionService versionService;

    //======================================================
    // 方法
    /**
     * 获取系统代码提交配置
     * @param systemId 系统id
     * @param scmType scm类型
     * @return  List<TblSystemScmSubmit> 代码仓库配置列表
     */
    @PostMapping(value = "findScmConfig")
    public List<TblSystemScmSubmit> findScmConfig(Long systemId, Integer scmType) {
        return versionService.findScmConfig(systemId, scmType);
    }

    /**
     * 
    * @Title: updateSubmitUser
    * @Description: 修改提交用户
    * @author author
    * @param req 提交的内容和原内容，用于记录日志和改变现有用户配置
    * @param request
    * @return int 新更新的结果数量
     */
    @PostMapping(value = "updateSubmitUser")
    public int updateSubmitUser(@RequestBody List<VersionReq> req, HttpServletRequest request) {
        return versionService.updateSubmitUser(req, getUserIdByToken(getToken(request)));
    }

    /**
     * 查询系统用户
     * @param systemId 系统id
     * @param userName 用户名称
     * @return 系统用户列表
     */
    @PostMapping(value = "findProjectUser")
    public List<TblUserInfo> findProjectUser(Long systemId, String userName) {
        return versionService.findProjectUser(systemId, userName);
    }

    /**
     * 查询操作日志
     * @param systemId 系统id
     * @return 操作日志
     */
    @PostMapping(value = "findOperationLog")
    public PageResult<TblSystemScmSubmitLog> findOperationLog(Long systemId, int page) {
        return versionService.findOperationLog(systemId, page);
    }

    //======================================================
    // GETTER & SETTER
    @Autowired
    public void setVersionService(VersionService versionService) {
        this.versionService = versionService;
    }

}

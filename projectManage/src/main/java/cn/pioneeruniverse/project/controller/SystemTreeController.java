package cn.pioneeruniverse.project.controller;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.dto.SystemTreeVo;
import cn.pioneeruniverse.common.entity.JqGridPage;
import cn.pioneeruniverse.project.entity.TblAssetSystemTree;
import cn.pioneeruniverse.project.service.SystemTree.SystemTreeService;
import cn.pioneeruniverse.project.vo.BusinessSystemTreeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 系统树维护
 * Author:liushan
 * Date: 2019/5/14 下午 2:11
 */
@RestController
@RequestMapping("systemTree")
public class SystemTreeController extends BaseController {

    @Autowired
    private SystemTreeService systemTreeService;

    /**
     * 查询列表
     *
     * @param request
     * @param response
     * @param businessSystemTreeVo 封装的查询信息
     * @return JqGridPage<BusinessSystemTreeVo>
     */
    @PostMapping(value = "list")
    public JqGridPage<BusinessSystemTreeVo> list(HttpServletRequest request,
                                                 HttpServletResponse response,
                                                 BusinessSystemTreeVo businessSystemTreeVo) {
        JqGridPage<BusinessSystemTreeVo> jqGridPage = null;
        try {
            jqGridPage = systemTreeService.getSystemTreeListByTier(new JqGridPage<>(request, response), businessSystemTreeVo);
        } catch (Exception e) {
            logger.error("message" + ":" + e.getMessage(), e);
            return (JqGridPage<BusinessSystemTreeVo>) super.handleException(e, "查询列表失败！");
        }
        return jqGridPage;
    }

    /**
    *@author liushan
    *@Description 获取业务数据列表数据
    *@Date 2020/7/31
     * @param request
     * @param response
     * @param businessSystemTreeVo 条件查询
    *@return cn.pioneeruniverse.common.entity.JqGridPage<cn.pioneeruniverse.project.vo.BusinessSystemTreeVo>
    **/
    @PostMapping(value = "treeList")
    public JqGridPage<BusinessSystemTreeVo> treeList(HttpServletRequest request,
                                                     HttpServletResponse response,
                                                     BusinessSystemTreeVo businessSystemTreeVo) {
        JqGridPage<BusinessSystemTreeVo> jqGridPage = null;
        try {
            jqGridPage = systemTreeService.getTreeList(new JqGridPage<>(request, response), businessSystemTreeVo);
        } catch (Exception e) {
            logger.error("message" + ":" + e.getMessage(), e);
        }
        return jqGridPage;
    }


    /**
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @author liushan
     * @Description 显示左边菜单，默认显示两层
     * @Date 2019/9/3
     * @Param [nodeId]树形节点id（资产树ID）
     **/
    @PostMapping(value = "getBusinessTreeList")
    public List<BusinessSystemTreeVo> getSystemTreeList(Long nodeId, Integer type) {
        try {
            return systemTreeService.getSystemTreeList(nodeId, type);
        } catch (Exception e) {
            handleException(e, "查询列表失败！");
        }
        return null;
    }

    /**
     * 
    * @Title: getSystemTreeByCode
    * @Description: 通过编码获取系统树（对应大地的系统模块）
    * 注意：在新增需求中，新增子系统，查询模块的时候用到了该方法
    * 传递的参数是systemCode,而此处接收的是项目ID，应该是前端调用Url的bug
    * @author author
    * @param projectId 项目ID
    * @return Map<String, Object> status=1正常，2异常
     */
    @PostMapping(value = "getSystemTreeByCode")
    public Map<String, Object> getSystemTreeByCode(Integer projectId) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            result.put("data", systemTreeService.getNewSystemTreeByCode(projectId));
        } catch (Exception e) {
            return handleException(e, "查询失败！");
        }
        return result;
    }

    /**
    *@author liushan
    *@Description 根据系统编号查询，该系统的所有节点
    *@Date 2020/7/31
     * @param systemCode 系统编号
    *@return java.util.List<cn.pioneeruniverse.project.vo.BusinessSystemTreeVo>
    **/
    @PostMapping(value = "getAssetSystemTreeBySystemCode")
    public List<BusinessSystemTreeVo> getAssetSystemTreeBySystemCode(String systemCode) throws Exception {
        return systemTreeService.getSystemTreeByCode(systemCode);
    }

    /**
     * 
    * @Title: getAssetSystemTreeById
    * @Description: 通过id获取资产系统树
    * @author author
    * @param id 资产树ID
    * @return BusinessSystemTreeVo
     */
    @PostMapping(value = "getAssetSystemTreeById")
    public BusinessSystemTreeVo getAssetSystemTreeById(Long id) {
        BusinessSystemTreeVo vo = new BusinessSystemTreeVo();
        vo.setId(id);
        return systemTreeService.getEntityInfo(vo);
    }

    /**
    *@author liushan
    *@Description  flag true新增成功，false 新增失败 entityInfo 新增成功的实体类
    *@Date 2020/7/31
     * @param businessSystemTreeVo 添加数据
     * @param request
    *@return map key status=1正常 status=2异常 entityInfo系统树
    *
    **/
    @PostMapping(value = "insert")
    public Map<String, Object> insertSystemTree(BusinessSystemTreeVo businessSystemTreeVo, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            result = systemTreeService.insertSystemTree(businessSystemTreeVo, request);
            result.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            return handleException(e, "新增条目失败");
        }
        return result;
    }

    /**
    *@author liushan
    *@Description 编辑条目
    *@Date 2020/7/31
     * @param businessSystemTreeVo 修改数据
     * @param request
    *@return Map<String, Object> status=1正常，2异常
    **/
    @PostMapping(value = "update")
    public Map<String, Object> updateSystemTree(BusinessSystemTreeVo businessSystemTreeVo, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            result = systemTreeService.updateSystemTree(businessSystemTreeVo, request);
            result.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            return handleException(e, "编辑条目失败");
        }
        return result;
    }

    /**
     * 获取实体类信息
     *
     * @param businessSystemTreeVo
     * @return map key:entityInfo value:BusinessSystemTreeVo系统树信息
     */
    @PostMapping(value = "getEntityInfo")
    public Map<String, Object> getEntityInfo(BusinessSystemTreeVo businessSystemTreeVo) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            BusinessSystemTreeVo businessTreeServiceEntityInfo = systemTreeService.getEntityInfo(businessSystemTreeVo);
            result.put("entityInfo", businessTreeServiceEntityInfo);
        } catch (Exception e) {
            return handleException(e, "新增条目失败");
        }
        return result;
    }

    /**
     * 移除条目
     *
     * @param ids 条目id
     * @param request
     * @return Map<String, Object> status=1正常，2异常
     */
    @PostMapping(value = "remove")
    public Map<String, Object> removeSystemTrees(Long[] ids, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            systemTreeService.removeSystemTrees(ids, request);
        } catch (Exception e) {
            return handleException(e, "编辑条目失败");
        }
        return result;
    }

    /**
     * 导入条目
     *
     * @param file
     * @param request
     * @return
     */
    @PostMapping(value = "import")
    public Map<String, Object> importSystemTree(String bsVo,
                                                @RequestParam("file") MultipartFile file,
                                                HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            result = systemTreeService.importSystemTree(bsVo, file, request);
        } catch (Exception e) {
            return handleException(e, "导入条目失败,请检查EXCEL数据格式是否正确");
        }
        return result;
    }

    /**
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @author liushan
     * @Description 导入模板
     * @Date 2019/9/5
     * @Param [bsVo, file, request]
     **/
    @PostMapping(value = "importTemplet")
    public Map<String, Object> importTemplet(String bsVo,
                                             @RequestParam("file") MultipartFile file,
                                             HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            result = systemTreeService.importTemplet(bsVo, file, request);
        } catch (Exception e) {
            return handleException(e, "导入条目失败,请检查EXCEL数据格式是否正确");
        }
        return result;
    }

    /**
    *@author liushan
    *@Description 导出数据
    *@Date 2020/7/31
     * @param type 区分导出的格式
 * @param businessSystemTreeVo 导出数据条件
 * @param response
 * @param request
    *@return void
    **/
    @RequestMapping(value = "export")
    public void export(Integer type, BusinessSystemTreeVo businessSystemTreeVo, HttpServletResponse response, HttpServletRequest request) throws Exception {
        systemTreeService.export(type, businessSystemTreeVo, response, request);
    }

    /**
    *@author liushan
    *@Description 同步第一层系统数据
    *@Date 2020/7/31
     * @param systemTreeVo 系统数据
 * @param currentUserId 当前人
    *@return java.util.Map<java.lang.String,java.lang.Object>
    **/
    @PostMapping(value = "insertFirstSystem")
    public Map<String, Object> insertFirstSystem(@RequestBody SystemTreeVo systemTreeVo, Long currentUserId) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            systemTreeService.insertFirstSystem(systemTreeVo, currentUserId);
        } catch (Exception e) {
            return handleException(e, "同步第一层系统信息失败！");
        }
        return result;
    }

    /**
    *@author liushan
    *@Description 模块弹窗
    *@Date 2020/7/31
     * @param systemTree 子系统树条件
     * @param pageNumber
     * @param pageSize
     * @param request
    *@return Map<String, Object> status=fail:异常
    **/
    @RequestMapping(value = "moduleTable", method = RequestMethod.POST)
    public Map<String, Object> moduleTable(TblAssetSystemTree systemTree, Integer pageNumber, Integer pageSize, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            List<TblAssetSystemTree> list = systemTreeService.getModuleList(systemTree, request, pageNumber, pageSize);
            List<TblAssetSystemTree> list2 = systemTreeService.getModuleList(systemTree, request, 1, Integer.MAX_VALUE);
           //当前页数据
            map.put("rows", list);
            //总条数
            map.put("total", list2.size());
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", "fail");
        }
        return map;
    }

}

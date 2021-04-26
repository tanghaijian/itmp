package cn.pioneeruniverse.project.service.BusinessTree;

import cn.pioneeruniverse.common.entity.JqGridPage;
import cn.pioneeruniverse.project.vo.BusinessSystemTreeVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Author:liushan
 * Date: 2019/5/14 下午 2:02
 */
public interface BusinessTreeService {
    
    /**
    *@author author
    *@Description 查询列表
    *@Date 2020/8/26
     * @param baseEntityJqGridPage
 * @param businessSystemTreeVo
    *@return cn.pioneeruniverse.common.entity.JqGridPage<cn.pioneeruniverse.project.vo.BusinessSystemTreeVo>
    **/
    JqGridPage<BusinessSystemTreeVo> getBusinessTreeListByTierNumber(JqGridPage<BusinessSystemTreeVo> baseEntityJqGridPage, BusinessSystemTreeVo businessSystemTreeVo);
    /**
     *@author liushan
     *@Description 查询列表
     *@Date 2019/9/3
     *@Param [jqGridPage, businessSystemTreeVo]
     *@return cn.pioneeruniverse.common.entity.JqGridPage<cn.pioneeruniverse.project.vo.BusinessSystemTreeVo>
     **/
    JqGridPage<BusinessSystemTreeVo> getTreeList(JqGridPage<BusinessSystemTreeVo> baseEntityJqGridPage, BusinessSystemTreeVo businessSystemTreeVo);

    /**
     *@author liushan
     *@Description 显示左边菜单，默认显示两层
     *@Date 2019/9/3
     *@Param [nodeId]
     *@return java.util.Map<java.lang.String,java.lang.Object>
     **/
    List<BusinessSystemTreeVo> getBusinessTreeList(Long nodeId, Integer type);
    /**
     * 编辑条目
     * businessSystemTreeVo.getBusinessSystemTreeStatus() != null  置为有效、无效操作
     * businessSystemTreeVo.getStatus() != null : 移除条目操作
     * @param businessSystemTreeVo
     * @param request
     */
    Map<String, Object> updateBusinessTree(BusinessSystemTreeVo businessSystemTreeVo, HttpServletRequest request) throws Exception;
    /**
     * 新增条目
     * @param assetBusinessTree
     * @return flag true新增成功， flag 新增失败
     * entityInfo 实体类信息
     */
    Map<String, Object> insertBusinessTree(BusinessSystemTreeVo assetBusinessTree, HttpServletRequest request) throws Exception;
    /**
     * 获取当前实体类信息
     * @param id
     * @return
     */
    BusinessSystemTreeVo getEntityInfo(Long id);
    /**
     * 获取子节点
     * @param businessId
     * @return
     */
    Map<String, Object> getChildNodes(Long businessId);
    /**
     * 批量移除条目，需要移除子集条目
     * @param ids
     * @param request
     */
    void removeBusinessTrees(Long[] ids, HttpServletRequest request) throws Exception;
    /**
     * 导入条目
     * 例如：导入第三层，excel前两列填入第一层、第二层条目名称
     *      导入条目数据挂在第二层条目下
     * @param bsVo
     * @param file
     * @param request
     */
    Map<String, Object> importBusinessTree(String bsVo, MultipartFile file, HttpServletRequest request) throws Exception;

    /**
     *@author liushan
     *@Description 导入模板
     *@Date 2019/9/5
     *@Param [bsVo, file, request]
     *@return java.util.Map<java.lang.String,java.lang.Object>
     **/
    Map<String,Object> importTemplet(String bsVo, MultipartFile file, HttpServletRequest request) throws Exception;
    /**
     *@author liushan
     *@Description 查询当前层级下一个层级id
     *@Date 2019/9/5
     *@Param [currentAssetTreeId]
     *@return java.lang.Long
     **/
    Long getNextAssetTreeId(Long assetTreeTierId, Long assetTreeType) throws Exception;
    /**
     *@author author
     *@Description 导出条目操作
     *@Date 2020/8/26
     * @param type
     * @param businessSystemTreeVo
     * @param response
     * @param request
     *@return void
     **/
    void export(Integer type, BusinessSystemTreeVo businessSystemTreeVo, HttpServletResponse response, HttpServletRequest request) throws Exception;

    /**
    *@author liushan
    *@Description 导入当前节点的子节点
    *@Date 2019/9/6
    *@Param [sbVo, assetTreeType, currentUserId, type, file]
    *@return java.util.Map<java.lang.String,java.lang.Object>
    **/
    Map<String, Object> importTempletWithBS(String sbVo, Integer assetTreeType, Long currentUserId, int type, MultipartFile file) throws Exception;

    /**
    *@author liushan
    *@Description 导入当前所有层级的子节点
    *@Date 2019/9/6
    *@Param [bsVo, assetTreeType, currentUserId, type, file]
    *@return java.util.Map<java.lang.String,java.lang.Object>
    **/
    Map<String,Object> importTemplet(String bsVo, Integer assetTreeType, Long currentUserId, int type, MultipartFile file) throws Exception;
}

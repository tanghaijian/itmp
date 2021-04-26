package cn.pioneeruniverse.project.service.SystemTree;

import cn.pioneeruniverse.common.dto.SystemTreeVo;
import cn.pioneeruniverse.common.entity.JqGridPage;
import cn.pioneeruniverse.project.entity.TblAssetSystemTree;
import cn.pioneeruniverse.project.vo.BusinessSystemTreeVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Author:liushan
 * Date: 2019/5/14 下午 2:12
 */
public interface SystemTreeService {
    /**
     * 查询系统树条目列表
     * @param baseEntityJqGridPage
     * @param businessSystemTreeVo
     * @return
     */
    JqGridPage<BusinessSystemTreeVo> getSystemTreeListByTier(JqGridPage<BusinessSystemTreeVo> baseEntityJqGridPage, BusinessSystemTreeVo businessSystemTreeVo);

    JqGridPage<BusinessSystemTreeVo> getTreeList(JqGridPage<BusinessSystemTreeVo> baseEntityJqGridPage, BusinessSystemTreeVo businessSystemTreeVo);

    List<BusinessSystemTreeVo> getSystemTreeList(Long nodeId, Integer type) throws Exception;

    /**
     *@author liushan
     *@Description 根据系统编号查询，该系统的所有节点
     *@Date 2019/9/17
     *@Param [businessSystemTreeVo]
     *@return java.util.List<cn.pioneeruniverse.project.vo.BusinessSystemTreeVo>
     *
     * @param systemCode
     * */
    List<BusinessSystemTreeVo> getSystemTreeByCode(String systemCode) throws Exception ;

    /**
     *@author wucheng
     *@Description 根据系统编号查询，该系统的所有节点
     *@Date 2020-03-19
     *@Param [businessSystemTreeVo]
     *@return java.util.List<cn.pioneeruniverse.project.vo.BusinessSystemTreeVo>
     *
     * @param projectId
     * */
    List<BusinessSystemTreeVo> getNewSystemTreeByCode(Integer projectId) throws Exception ;


    /**
     * 新增条目
     * @param businessSystemTreeVo
     * @param request
     */
    Map<String, Object>  insertSystemTree(BusinessSystemTreeVo businessSystemTreeVo, HttpServletRequest request) throws Exception;

    /**
     * 编辑条目
     * @param businessSystemTreeVo
     * @param request
     */
    Map<String, Object> updateSystemTree(BusinessSystemTreeVo businessSystemTreeVo, HttpServletRequest request) throws Exception;

    /**
     * 查询当前条目信息
     * @param businessSystemTreeVo
     * @return
     */
    BusinessSystemTreeVo getEntityInfo(BusinessSystemTreeVo businessSystemTreeVo);

    /**
     * 删除条目，逻辑删除
     * @param ids
     * @param request
     */
    void removeSystemTrees(Long[] ids, HttpServletRequest request) throws Exception;

    Map<String,Object> importSystemTree(String bsVo, MultipartFile file, HttpServletRequest request) throws Exception;

    Map<String,Object> importTemplet(String bsVo, MultipartFile file, HttpServletRequest request) throws Exception;

    void export(Integer type, BusinessSystemTreeVo businessSystemTreeVo, HttpServletResponse response, HttpServletRequest request) throws Exception;

    /**
     *@author liushan
     *@Description 添加系统
     *@Date 2019/9/23
     *@Param [systemTreeVo]
     *@return void
     **/
    void insertFirstSystem(SystemTreeVo systemTreeVo, Long currentUserId) throws Exception;

	List<TblAssetSystemTree> getModuleList(TblAssetSystemTree systemTree, HttpServletRequest request,
			Integer pageNumber, Integer pageSize);
}

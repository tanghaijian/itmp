package cn.pioneeruniverse.project.service.SystemTree.impl;

import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.dto.SystemTreeVo;
import cn.pioneeruniverse.common.entity.JqGridPage;
import cn.pioneeruniverse.common.utils.CollectionUtil;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.DateUtil;
import cn.pioneeruniverse.common.utils.ExportExcel;
import cn.pioneeruniverse.project.dao.mybatis.TblAssetSystemTreeMapper;
import cn.pioneeruniverse.project.dao.mybatis.TblAssetTreeTierMapper;
import cn.pioneeruniverse.project.dto.ProjectHomeDTO;
import cn.pioneeruniverse.project.entity.TblAssetSystemTree;
import cn.pioneeruniverse.project.entity.TblAssetTreeTier;
import cn.pioneeruniverse.project.service.BusinessTree.BusinessTreeService;
import cn.pioneeruniverse.project.service.SystemTree.SystemTreeService;
import cn.pioneeruniverse.project.service.projectHome.ProjectHomeService;
import cn.pioneeruniverse.project.vo.BusinessSystemTreeVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Author:liushan
 * Date: 2019/5/14 下午 2:13
 */
@Transactional(readOnly = true)
@Service("SystemTreeService")
public class SystemTreeServiceImpl extends BaseController implements SystemTreeService{

    public final static Logger logger = LoggerFactory.getLogger(SystemTreeServiceImpl.class);
    @Autowired
    private TblAssetSystemTreeMapper systemTreeMapper;
    @Autowired
    private TblAssetTreeTierMapper assetTreeTierMapper;
    @Autowired
    private BusinessTreeService businessTreeService;
    @Autowired
    private ProjectHomeService projectHomeService;
    private  static final Long assetTreeType = 2L;
    /**
     * 查询系统树条目列表
     * @param jqGridPage
     * @param businessSystemTreeVo
     * @return
     */
    @Override
    public JqGridPage<BusinessSystemTreeVo> getSystemTreeListByTier(JqGridPage<BusinessSystemTreeVo> jqGridPage, BusinessSystemTreeVo businessSystemTreeVo) {
        PageHelper.startPage(jqGridPage.getJqGridPrmNames().getPage(), jqGridPage.getJqGridPrmNames().getRows());
        List<BusinessSystemTreeVo> list = systemTreeMapper.getSystemTreeListByTier(businessSystemTreeVo);
        PageInfo<BusinessSystemTreeVo> pageInfo = new PageInfo<>(list);
        jqGridPage.processDataForResponse(pageInfo);
        return jqGridPage;
    }

    /**
     *@author liushan
     *@Description 查询列表
     *@Date 2019/9/3
     *@Param [jqGridPage, businessSystemTreeVo]
     *@return cn.pioneeruniverse.common.entity.JqGridPage<cn.pioneeruniverse.project.vo.BusinessSystemTreeVo>
     **/
    @Override
    public JqGridPage<BusinessSystemTreeVo> getTreeList(JqGridPage<BusinessSystemTreeVo> jqGridPage, BusinessSystemTreeVo businessSystemTreeVo) {
        PageHelper.startPage(jqGridPage.getJqGridPrmNames().getPage(), jqGridPage.getJqGridPrmNames().getRows());
        if(businessSystemTreeVo.getId().longValue() == 0) {
            businessSystemTreeVo.setId(null);
        }
        List<BusinessSystemTreeVo> list = systemTreeMapper.getListByParentId(businessSystemTreeVo);
        PageInfo<BusinessSystemTreeVo> pageInfo = new PageInfo<>(list);
        jqGridPage.processDataForResponse(pageInfo);
        return jqGridPage;
    }

    /**
     *@author liushan
     *@Description 显示左边菜单，默认显示两层
     *@Date 2019/9/3
     *@Param [nodeId]
     *@return java.util.Map<java.lang.String,java.lang.Object>
     **/
    @Override
    public List<BusinessSystemTreeVo> getSystemTreeList(Long nodeId, Integer type) throws Exception {
        List<BusinessSystemTreeVo> list = new ArrayList<>();
        if(nodeId != null){
            if(nodeId.longValue() == 0) {
                return systemTreeMapper.getFirstTree();
            }
            list = systemTreeMapper.getTreeByParentId(nodeId);
        } else { // 默认显示两层
            BusinessSystemTreeVo vo = new BusinessSystemTreeVo();
            List<TblAssetTreeTier> treeTiers = assetTreeTierMapper.getAssetTreeList(assetTreeType.intValue());
            if(treeTiers!=null && treeTiers.size() !=0){
                vo.setAssetTreeTierNumber(treeTiers.get(treeTiers.size()-1).getTierNumber());// 最大的层级编号
                vo.setBusinessSystemTreeId(treeTiers.get(treeTiers.size()-1).getId()); // 最大的层级id
                vo.setAssetTreeTierId(treeTiers.get(0).getId());// 第一层的id
            }
            vo.setId(0L);
            vo.setBusinessSystemTreeName("系统树维护目录展示");
            vo.setBusinessSystemTrees(systemTreeMapper.getFirstSecondTree());
            list.add(vo);
        }
        return list;
    }

    /**
    *@author liushan
    *@Description 根据系统编号查询，该系统的所有节点
    *@Date 2019/9/17
    *@Param [systemCode]
    *@return java.util.List<cn.pioneeruniverse.project.vo.BusinessSystemTreeVo>
    *
    */
    @Override
    public List<BusinessSystemTreeVo> getSystemTreeByCode(String systemCode) throws Exception {
        List<BusinessSystemTreeVo> list = new ArrayList<>();
        String[] split = systemCode.split(",");
        //查找系统树的第一层：系统
        BusinessSystemTreeVo  vo = systemTreeMapper.getSystemByCode(systemCode);
        if(vo != null && vo.getId() != null){
            list.add(vo);
            //通过系统查找到其子树
            list.addAll(systemTreeMapper.getSystemTreeById(vo.getId(),vo.getId()+","));
        }

        return list;
    }

    /**
     *@author wucheng
     *@Description 根据系统编号查询，该系统的所有节点
     *@Date 2020-03-19
     *@Param [businessSystemTreeVo]
     *@return java.util.List<cn.pioneeruniverse.project.vo.BusinessSystemTreeVo>
     * @param projectId
     */
    @Override
    public List<BusinessSystemTreeVo> getNewSystemTreeByCode(Integer projectId) throws Exception {
        List<BusinessSystemTreeVo> list = new ArrayList<>();
        //项目关联系统
        List<ProjectHomeDTO> interactedSystem = projectHomeService.projectSystemList(projectId);
        String systemCode = "";
        if (!interactedSystem.isEmpty()){
            for(int i=0;i<interactedSystem.size();i++){
                systemCode = systemCode + interactedSystem.get(i).getSystemCode()+",";
            }
        }
        String[] split = systemCode.split(",");
        for (int i =0; i<split.length; i++){
            BusinessSystemTreeVo  vo = systemTreeMapper.getSystemByCode(split[i]);
            if(vo != null && vo.getId() != null){
                list.add(vo);
                list.addAll(systemTreeMapper.getSystemTreeById(vo.getId(),vo.getId()+","));
            }
        }
        return list;
    }

    /**
     * 新增条目
     * @param assetBusinessTree
     * @param request
     */
    @Override
    @Transactional(readOnly = false)
    public Map<String, Object> insertSystemTree(BusinessSystemTreeVo assetBusinessTree, HttpServletRequest request) throws Exception{
        Map<String, Object> result = new HashMap<>();
        result.put("flag",true);
        assetBusinessTree = (BusinessSystemTreeVo) CommonUtil.setBaseValue(assetBusinessTree,request);
        // 查询当前层的数据是否存在正在添加的条目
        Boolean flag = isTreeName(assetBusinessTree.getParentIds(),assetBusinessTree.getBusinessSystemTreeName(),assetBusinessTree.getAssetTreeTierId());
        if(flag == true){ result.put("flag",false);return result; }
        if(assetBusinessTree.getParentId() != null && assetBusinessTree.getParentIds() != null){
            assetBusinessTree.setAssetTreeTierId(businessTreeService.getNextAssetTreeId(assetBusinessTree.getAssetTreeTierId(),this.assetTreeType));
        }

        systemTreeMapper.insertBusinessSystemTreeVo(assetBusinessTree);
        result.put("entityInfo",new Gson().toJson(systemTreeMapper.getEntityInfo(assetBusinessTree.getId())));
        return result;
    }

    /**
     * 编辑条目
     * businessSystemTreeVo.getBusinessSystemTreeStatus() != null  置为有效、无效操作
     * businessSystemTreeVo.getStatus() != null : 移除条目操作
     * 判断是否添加重复条目数据
     * @param businessSystemTreeVo
     * @param request
     */
    @Override
    @Transactional(readOnly = false)
    public Map<String, Object> updateSystemTree(BusinessSystemTreeVo businessSystemTreeVo, HttpServletRequest request) throws Exception{
        Map<String, Object> result = new HashMap<>();
        result.put("flag",true);
        // 有状态，需要修改字节点的状态
        if(businessSystemTreeVo.getBusinessSystemTreeStatus() != null || businessSystemTreeVo.getStatus() != null){
            BusinessSystemTreeVo oldBSVo = systemTreeMapper.getEntityInfo(businessSystemTreeVo.getId());
            if (oldBSVo.getParentIds() == null) {
                oldBSVo.setParentIds(String.valueOf(oldBSVo.getId()));
            } else {
                oldBSVo.setParentIds(oldBSVo.getParentIds() + "," + oldBSVo.getId());
            }
            List<BusinessSystemTreeVo> childTier = systemTreeMapper.selectChildTier(oldBSVo.getParentIds());
            if(childTier != null && childTier.size() > 0){
                for(BusinessSystemTreeVo treeVo:childTier){
                    treeVo.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
                    treeVo.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));

                    if(businessSystemTreeVo.getBusinessSystemTreeStatus() != null){
                        treeVo.setBusinessSystemTreeStatus(businessSystemTreeVo.getBusinessSystemTreeStatus());
                    } else if(businessSystemTreeVo.getStatus() != null){
                        treeVo.setStatus(businessSystemTreeVo.getStatus());
                    }
                    systemTreeMapper.updateBusinessSystemTreeVo(treeVo);
                }
            }
        } else if(businessSystemTreeVo.getBusinessSystemTreeName() != null){
            // 查询当前层的数据是否存在正在添加的条目
            Boolean flag = isTreeName(businessSystemTreeVo.getParentIds(),businessSystemTreeVo.getBusinessSystemTreeName(),businessSystemTreeVo.getAssetTreeTierId());
            if(flag == true){ result.put("flag",false);return result; }
        }
        businessSystemTreeVo.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
        businessSystemTreeVo.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
        systemTreeMapper.updateBusinessSystemTreeVo(businessSystemTreeVo);
        return  result;
    }

    /**
     * 
    * @Title: getEntityInfo
    * @Description: 获取系统资产树
    * @author author
    * @param businessSystemTreeVo
    * @return
    * @throws
     */
    @Override
    public BusinessSystemTreeVo getEntityInfo(BusinessSystemTreeVo businessSystemTreeVo) {
        return systemTreeMapper.getEntityInfo(businessSystemTreeVo.getId());
    }

    /**
     * 批量移除条目，逻辑删除
     * @param ids
     * @param request
     */
    @Override
    @Transactional(readOnly = false)
    public void removeSystemTrees(Long[] ids, HttpServletRequest request) throws Exception{
        if(ids != null && ids.length > 0 ){
            for(Long id:ids){
                BusinessSystemTreeVo vo = new BusinessSystemTreeVo();
                vo.setId(id);
                vo.setStatus(2);
                this.updateSystemTree(vo,request);
            }
        }
    }

    /**
    *@author liushan
    *@Description 导入数据
    *@Date 2020/7/31
    *@Param [bsVo, file, request]
    *@return java.util.Map<java.lang.String,java.lang.Object>
    **/
    @Override
    @Transactional(readOnly = false)
    public Map<String, Object> importSystemTree(String bsVo, MultipartFile file, HttpServletRequest request) throws Exception {
        return businessTreeService.importTemplet(bsVo,assetTreeType.intValue(),CommonUtil.getCurrentUserId(request),2,file);
    }

    /**
    *@author liushan
    *@Description 导入当前节点下的子节点
    *@Date 2019/9/5
    *@Param [bsVo, file, request]
    *@return java.util.Map<java.lang.String,java.lang.Object>
    **/
    @Override
    @Transactional(readOnly = false)
    public Map<String, Object> importTemplet(String bsVo, MultipartFile file, HttpServletRequest request) throws Exception {
        return businessTreeService.importTempletWithBS(bsVo,assetTreeType.intValue(),CommonUtil.getCurrentUserId(request),2,file);
    }

    /**
     * 导出条目
     * @param type
     * @param businessSystemTreeVo
     * @param response
     * @param request
     */
    @Override
    public void export(Integer type, BusinessSystemTreeVo businessSystemTreeVo, HttpServletResponse response, HttpServletRequest request) throws Exception{
        List<BusinessSystemTreeVo> list = new ArrayList<>();
        String fileName = "系统树条目信息"+DateUtil.getDateString(new Timestamp(System.currentTimeMillis()),"yyyyMMddHHmmss");
        if(type.intValue() == 1){
            list = systemTreeMapper.getSystemTreeListByTier(businessSystemTreeVo);
        } else if(type.intValue() == 2){
            if(businessSystemTreeVo.getId().longValue() == 0) {
                businessSystemTreeVo.setId(null);
            }
            list = systemTreeMapper.getListByParentIdWithName(businessSystemTreeVo);
        } else if(type.intValue() == 3){
            fileName = "系统树层级条目导入模板";
        }
        List<TblAssetTreeTier> treeTiers = assetTreeTierMapper.getAssetTreeList(assetTreeType.intValue());
        List<Object> assetTreeName = CollectionUtil.extractToList(treeTiers, "assetTreeName");
        Long changeRowNum = 0L;
        if(businessSystemTreeVo.getId() != null && businessSystemTreeVo.getId() != 0 ){
            TblAssetTreeTier assetTree= assetTreeTierMapper.selectById(businessSystemTreeVo.getAssetTreeTierId());
            changeRowNum = assetTree.getTierNumber();
        }
        new ExportExcel().setWorkHead(BusinessSystemTreeVo.class,"getParentTreeNames",changeRowNum,assetTreeName,"系统树条目信息")
                .setChangeRowNumList(list)
                .write(response, fileName+".xlsx")
                .dispose();
    }

    /**
    *@author liushan
    *@Description 同步第一层系统信息
    *@Date 2019/9/23
    *@Param [systemTreeVo]
    *@return void
    **/
    @Override
    @Transactional(readOnly = false)
    public void insertFirstSystem(SystemTreeVo systemTreeVo, Long currentUserId) throws Exception{
        if(systemTreeVo != null && StringUtils.isNotBlank(systemTreeVo.getSystemTreeCode())){
            BusinessSystemTreeVo vo = systemTreeMapper.getSystemByCode(systemTreeVo.getSystemTreeCode());
            BusinessSystemTreeVo assetBusinessTree = new BusinessSystemTreeVo(currentUserId);
            assetBusinessTree.setBusinessSystemTreeName(systemTreeVo.getSystemTreeName());
            assetBusinessTree.setBusinessSystemTreeCode(systemTreeVo.getSystemTreeCode());
            if(vo != null){// 修改操作
                assetBusinessTree.setId(vo.getId());
                systemTreeMapper.updateBusinessSystemTreeVo(assetBusinessTree);
            } else { // 新增操作
                TblAssetTreeTier assetTreeTier = assetTreeTierMapper.getAssetTree(assetTreeType.intValue(),1);
                if(assetTreeTier != null){
                    assetBusinessTree.setAssetTreeTierId(assetTreeTier.getId());
                    systemTreeMapper.insertBusinessSystemTreeVo(assetBusinessTree);
                }
            }
        }
    }

    /**
     *  查询当前树的当前层的数据是否存在正在添加的条目
     * @param parentIds
     * @param treeName
     * @return
     * @throws Exception
     */
    private Boolean isTreeName(String parentIds,String treeName,Long assetTreeTierId) throws Exception{
        Boolean flag = false;
        if(parentIds != null && !parentIds.equals("")){
            List<BusinessSystemTreeVo> voList = systemTreeMapper.getTiersByParentIds(parentIds);
            if(voList != null && voList.size() > 0 ){
                List<String> treeNames = CollectionUtil.extractToList(voList, "businessSystemTreeName");
                flag = treeNames.contains(treeName);
            }
        } else {
            List<Object> treeNames = systemTreeMapper.getTierByTierId(assetTreeTierId);
            flag = treeNames.contains(treeName);
        }
        return flag;
    }

    /**
    *@author liushan
    *@Description 获取列表数据带分页
    *@Date 2020/7/31
    *@Param [systemTree, request, pageNumber, pageSize]
    *@return java.util.List<cn.pioneeruniverse.project.entity.TblAssetSystemTree>
    **/
	@Override
	public List<TblAssetSystemTree> getModuleList(TblAssetSystemTree systemTree, HttpServletRequest request,
			Integer pageNumber, Integer pageSize) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<>();
		int start = (pageNumber - 1) * pageSize;
		map.put("start", start);
		map.put("pageSize", pageSize);
		map.put("systemTree", systemTree);
		List<TblAssetSystemTree> list = systemTreeMapper.getModuleList(map);
		return list;
	}
}

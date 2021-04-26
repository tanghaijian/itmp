package cn.pioneeruniverse.project.service.BusinessTree.impl;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.entity.JqGridPage;
import cn.pioneeruniverse.common.utils.*;
import cn.pioneeruniverse.project.dao.mybatis.TblAssetBusinessTreeMapper;
import cn.pioneeruniverse.project.dao.mybatis.TblAssetSystemTreeMapper;
import cn.pioneeruniverse.project.dao.mybatis.TblAssetTreeTierMapper;
import cn.pioneeruniverse.project.entity.TblAssetTreeTier;
import cn.pioneeruniverse.project.service.BusinessTree.BusinessTreeService;
import cn.pioneeruniverse.project.vo.BusinessSystemTreeVo;
import com.alibaba.fastjson.JSONObject;
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
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.*;

/**
 * Description:业务树业务逻辑层
 * Author:liushan
 * Date: 2019/5/14 下午 2:02
 */
@Transactional(readOnly = true)
@Service("BusinessTreeService")
public class BusinessTreeServiceImpl extends BaseController implements BusinessTreeService {

    public final static Logger logger = LoggerFactory.getLogger(BusinessTreeServiceImpl.class);

    @Autowired
    private TblAssetBusinessTreeMapper businessTreeMapper;
    @Autowired
    private TblAssetTreeTierMapper assetTreeTierMapper;
    @Autowired
    private TblAssetSystemTreeMapper systemTreeMapper;

    private  static final Long assetTreeType = 1L;

    // 控制导出方法
    private final int num_0 = 0;
    private final int num_1 = 1;
    private final int num_2 = 2;
    private final int num_3 = 3;

    /**
     * 查询列表
     * @param jqGridPage
     * @param businessSystemTreeVo 封装的业务树查询条件
     * @return JqGridPage<BusinessSystemTreeVo>
     */
    @Override
    public JqGridPage<BusinessSystemTreeVo> getBusinessTreeListByTierNumber(JqGridPage<BusinessSystemTreeVo> jqGridPage, BusinessSystemTreeVo businessSystemTreeVo) {
        PageHelper.startPage(jqGridPage.getJqGridPrmNames().getPage(), jqGridPage.getJqGridPrmNames().getRows());
        List<BusinessSystemTreeVo> list = businessTreeMapper.getBusinessTreeListByTierNumber(businessSystemTreeVo);
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
        if(businessSystemTreeVo.getId().longValue() == this.num_0) {
            businessSystemTreeVo.setId(null);
        }
        List<BusinessSystemTreeVo> list = businessTreeMapper.getListByParentId(businessSystemTreeVo);
        PageInfo<BusinessSystemTreeVo> pageInfo = new PageInfo<>(list);
        jqGridPage.processDataForResponse(pageInfo);
        return jqGridPage;
    }

    /**
    *@author liushan
    *@Description 显示左边菜单，默认显示两层
    *@Date 2019/9/3
    *@Param [nodeId] 节点id（业务树父id）
    *@return java.util.Map<java.lang.String,java.lang.Object>
    **/
    @Override
    public List<BusinessSystemTreeVo>  getBusinessTreeList(Long nodeId, Integer type) {
        List<BusinessSystemTreeVo> list = new ArrayList<>();
        if(nodeId != null){
            if(nodeId.longValue() == this.num_0) {
                return businessTreeMapper.getFirstTree();
            }
            list = businessTreeMapper.getTreeByParentId(nodeId);
        } else { // 默认显示两层
            BusinessSystemTreeVo vo = new BusinessSystemTreeVo();
            List<TblAssetTreeTier> treeTiers = assetTreeTierMapper.getAssetTreeList(assetTreeType.intValue());
            if(treeTiers!=null && treeTiers.size() != this.num_0){
                vo.setAssetTreeTierNumber(treeTiers.get(treeTiers.size()-1).getTierNumber());// 最大的层级编号
                vo.setBusinessSystemTreeId(treeTiers.get(treeTiers.size()-1).getId()); // 最大的层级id
                vo.setAssetTreeTierId(treeTiers.get(this.num_0).getId());// 第一层的id
            }
            vo.setId(0L);
            vo.setBusinessSystemTreeName("业务树维护目录展示");
            vo.setBusinessSystemTrees(businessTreeMapper.getFirstSecondTree());
            list.add(vo);
        }
        return list;
    }

    /**
     * 编辑条目
     * businessSystemTreeVo.getBusinessSystemTreeStatus() != null  置为有效、无效操作
     * businessSystemTreeVo.getStatus() != null : 移除条目操作
     * @param businessSystemTreeVo 
     * @param request
     */
    @Override
    @Transactional(readOnly = false)
    public Map<String, Object> updateBusinessTree(BusinessSystemTreeVo businessSystemTreeVo, HttpServletRequest request) throws Exception{
        Map<String, Object> result = new HashMap<>();

        result.put("flag",true);
        // 有状态，需要修改字节点的状态
        if(businessSystemTreeVo.getBusinessSystemTreeStatus() != null || businessSystemTreeVo.getStatus() != null){
            BusinessSystemTreeVo oldBSVo = businessTreeMapper.getEntityInfo(businessSystemTreeVo.getId());
            if (oldBSVo.getParentIds() == null) {
                oldBSVo.setParentIds(String.valueOf(oldBSVo.getId()));
            } else {
                oldBSVo.setParentIds(oldBSVo.getParentIds() + "," + oldBSVo.getId());
            }
            List<BusinessSystemTreeVo> childTier = businessTreeMapper.selectChildTier(oldBSVo.getParentIds());
            if(childTier != null && childTier.size() > this.num_0){
                for(BusinessSystemTreeVo treeVo:childTier){
                    treeVo.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
                    treeVo.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));

                    if(businessSystemTreeVo.getBusinessSystemTreeStatus() != null){
                        treeVo.setBusinessSystemTreeStatus(businessSystemTreeVo.getBusinessSystemTreeStatus());
                    } else if(businessSystemTreeVo.getStatus() != null){
                        treeVo.setStatus(businessSystemTreeVo.getStatus());
                    }
                    businessTreeMapper.updateBusinessSystemTreeVo(treeVo);
                }
            }
        } else if(businessSystemTreeVo.getBusinessSystemTreeName() != null){
            // 查询当前层的数据是否存在正在添加的条目
            Boolean flag = isTreeName(businessSystemTreeVo.getParentIds(),businessSystemTreeVo.getBusinessSystemTreeName(),businessSystemTreeVo.getAssetTreeTierId());
            if(flag == true){ result.put("flag",false);return result; }
        }

        businessSystemTreeVo.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
        businessSystemTreeVo.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
        businessTreeMapper.updateBusinessSystemTreeVo(businessSystemTreeVo);
        return result;
    }

    /**
     * 新增条目
     * assetBusinessTree.getParentId() != null 拆分条目
     * @param assetBusinessTree
     * @param request
     */
    @Override
    @Transactional(readOnly = false)
    public Map<String, Object> insertBusinessTree(BusinessSystemTreeVo assetBusinessTree, HttpServletRequest request) throws Exception{
        Map<String, Object> result = new HashMap<>();
        result.put("flag",true);
        assetBusinessTree = (BusinessSystemTreeVo) CommonUtil.setBaseValue(assetBusinessTree,request);
        // 查询当前层的数据是否存在正在添加的条目
        Boolean flag = isTreeName(assetBusinessTree.getParentIds(),assetBusinessTree.getBusinessSystemTreeName(),assetBusinessTree.getAssetTreeTierId());
        if(flag == true){ result.put("flag",false);return result; }
        if(assetBusinessTree.getParentId() != null && assetBusinessTree.getParentIds() != null){
            assetBusinessTree.setAssetTreeTierId(this.getNextAssetTreeId(assetBusinessTree.getAssetTreeTierId(),assetTreeType));
        }
        businessTreeMapper.insertBusinessSystemTreeVo(assetBusinessTree);
        result.put("entityInfo",new Gson().toJson(this.getEntityInfo(assetBusinessTree.getId())));
        return result;
    }

    /**
     * 获取当前实体类信息
     * @param id
     * @return
     */
    @Override
    public BusinessSystemTreeVo getEntityInfo(Long id) {
        return businessTreeMapper.getEntityInfo(id);
    }

    /**
     * 获取子节点
     * @param businessId 业务树id
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> getChildNodes(Long businessId) {
        Map<String, Object> result = new HashMap<>();
        BusinessSystemTreeVo vo = businessTreeMapper.getEntityInfo(businessId);
        String parentIds = "";
        if(vo.getParentIds() == null){
            parentIds = String.valueOf(vo.getId());
        } else {
            parentIds = vo.getParentIds()+","+String.valueOf(vo.getId());
        }
        List<BusinessSystemTreeVo> voList = businessTreeMapper.getTiersByParentIds(parentIds);
        //业务树信息
        result.put("entityInfo",vo);
        //业务树子节点信息
        result.put("childNodes",voList);
        return result;
    }

    /**
     * 批量移除条目，需要移除子集条目
     * @param ids
     * @param request
     */
    @Override
    @Transactional(readOnly = false)
    public void removeBusinessTrees(Long[] ids, HttpServletRequest request) throws Exception{
        if(ids != null && ids.length > this.num_0){
            for(Long id:ids){
                BusinessSystemTreeVo vo = new BusinessSystemTreeVo();
                vo.setId(id);
                vo.setStatus(2);
                this.updateBusinessTree(vo,request);
            }
        }
    }

    /**
     * 导入条目
     * 例如：导入第三层，excel前两列填入第一层、第二层条目名称
     *      导入条目数据挂在第二层条目下
     * @param bsVo 业务树信息
     * @param file 附件
     * @param request
     */
    @Override
    @Transactional(readOnly = false)
    public Map<String, Object> importBusinessTree(String bsVo, MultipartFile file, HttpServletRequest request) throws Exception{
        return this.importTemplet(bsVo,assetTreeType.intValue(),CommonUtil.getCurrentUserId(request),1,file);
    }

    /**
    *@author liushan
    *@Description 导入模板,导入当前节点下的父节点,获取当前id
    *@Date 2019/9/5
    *@Param [bsVo, file, request]
    *@return java.util.Map<java.lang.String,java.lang.Object>
    **/
    @Override
    @Transactional(readOnly = false)
    public Map<String, Object> importTemplet(String bsVo, MultipartFile file, HttpServletRequest request) throws Exception {
        return this.importTempletWithBS(bsVo,assetTreeType.intValue(),CommonUtil.getCurrentUserId(request),1,file);
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
        String fileName = "业务树条目信息"+DateUtil.getDateString(new Timestamp(System.currentTimeMillis()),"yyyyMMddHHmmss");
        if(type.intValue() == this.num_1){
            // 旧需求：按照层级导出同一层数据
            list = businessTreeMapper.getBusinessTreeListByTierNumber(businessSystemTreeVo);
        } else if(type.intValue() == this.num_2){
            // 导出条目
            if(businessSystemTreeVo.getId().longValue() == this.num_0) {
                businessSystemTreeVo.setId(null);
            }
            list = businessTreeMapper.getListByParentIdWithName(businessSystemTreeVo);
        } else if(type.intValue() == this.num_3){
            // 导出模板
            fileName = "业务树层级条目导入模板";
        }
        List<TblAssetTreeTier> treeTiers = assetTreeTierMapper.getAssetTreeList(assetTreeType.intValue());
        List<Object> assetTreeName = CollectionUtil.extractToList(treeTiers, "assetTreeName");
        Long changeRowNum = 0L;
        if(businessSystemTreeVo.getId() != null && businessSystemTreeVo.getId() != this.num_0 ){
            TblAssetTreeTier assetTree= assetTreeTierMapper.selectById(businessSystemTreeVo.getAssetTreeTierId());
            changeRowNum = assetTree.getTierNumber();
        }
        new ExportExcel().setWorkHead(BusinessSystemTreeVo.class,"getParentTreeNames",changeRowNum,assetTreeName,"业务树条目信息")
                .setChangeRowNumList(list)
                .write(response, fileName+".xlsx")
                .dispose();
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
            List<BusinessSystemTreeVo> voList = businessTreeMapper.getTiersByParentIds(parentIds);
            if(voList != null && voList.size() > this.num_0 ){
                List<String> treeNames = CollectionUtil.extractToList(voList, "businessSystemTreeName");
                flag = treeNames.contains(treeName);
            }
        } else {
            List<Object> treeNames = businessTreeMapper.getTierByTierId(assetTreeTierId);
            flag = treeNames.contains(treeName);
        }
        return flag;
    }

    /**
    *@author liushan
    *@Description 查询当前层级下一个层级id
    *@Date 2019/9/5
    *@Param [currentAssetTreeId]
    *@return java.lang.Long
    **/
    @Override
    public Long getNextAssetTreeId(Long currentAssetTreeId,Long assetTreeType) throws Exception{
        List<TblAssetTreeTier> treeTiers =  assetTreeTierMapper.getTierNumbersByTreeType(assetTreeType);
        List<Long> ids = CollectionUtil.extractToList(treeTiers, "id");
        if(ids.contains(currentAssetTreeId)){
            return ids.get(ids.indexOf(currentAssetTreeId)+1);
        }
        return  null;
    }

    /**
    *@author liushan
    *@Description 导入当前节点下的子节点
    *@Date 2019/9/6
    *@Param [bsVo, assetTreeType, currentUserId, type, file]
    *@return java.util.Map<java.lang.String,java.lang.Object>
    **/
    @Override
    @Transactional(readOnly = false)
    public Map<String, Object> importTempletWithBS(String bsVo,Integer assetTreeType, Long currentUserId,int type,MultipartFile file) throws Exception {
        Map<String, Object> result = new HashMap<>();
        // 反馈行数问题
        List<String> excelRow = new ArrayList<>();
        if(StringUtils.isNotBlank(bsVo) && !Objects.isNull(file) && !file.isEmpty()){
            BusinessSystemTreeVo businessSystemTreeVo = JSONObject.parseObject(bsVo, BusinessSystemTreeVo.class);
            TblAssetTreeTier assetTree = assetTreeTierMapper.selectById(businessSystemTreeVo.getAssetTreeTierId());
            int tierNumber = assetTree.getTierNumber().intValue();
            List<Object> treeNames = new ArrayList<>();
            Boolean flag;
            Integer index;
            Long nextAssetTreeId = this.getNextAssetTreeId(businessSystemTreeVo.getAssetTreeTierId(),assetTreeType.longValue());

            // 判断表头
            List<TblAssetTreeTier> treeTiers = assetTreeTierMapper.getAssetTreeList(assetTreeType);
            List<Object> head = new ArrayList<>();
            if(businessSystemTreeVo.getId().longValue() != this.num_0){
                for (int i = 0,len = tierNumber; i < len; i++) {
                    head.add(treeTiers.get(i).getAssetTreeName());
                }
            }
            head.add("条目名称*");
            head.add("条目简称");
            head.add("条目编码");
            head.add("说明");

            InputStream in =null;
            try {
                in = file.getInputStream();
            } catch (IOException e) {
                logger.error("业务树导入数据文件不存在，出现异常：",e.getMessage());
                return super.handleException(e,"业务树导入数据文件出现问题："+e.getMessage());
            }
            List<List<Object>> listob = null;
            try {
                listob = new ExcelUtil().getBankListWithFirstRowsByExcel(in,file);
            } catch (Exception e) {
                logger.error("业务树导入数据文件,出现异常：",e.getMessage());
                return super.handleException(e,"业务树导入数据文件出现问题："+e.getMessage());
            }

            if(listob != null && listob.size() > this.num_0){
                Boolean heahFlag = CollectionUtil.getIsEquals(listob.get(this.num_0),head);
                if(heahFlag){
                    if(businessSystemTreeVo.getId().longValue() != this.num_0){
                        if(type == 1){
                            treeNames.add(businessTreeMapper.selectParentTreeNames(businessSystemTreeVo.getParentIds()));
                        } else {
                            treeNames.add(systemTreeMapper.selectParentTreeNames(businessSystemTreeVo.getParentIds()));
                        }
                    } else {
                        // 第一层的条目名称
                        if(type == 1){
                            treeNames = businessTreeMapper.getTierByTierId(businessSystemTreeVo.getAssetTreeTierId());
                        } else {
                            treeNames = systemTreeMapper.getTierByTierId(businessSystemTreeVo.getAssetTreeTierId());
                        }
                    }
                    listob.remove(0);
                    for (int i = 0,len = listob.size(); i < len; i++) {
                        BusinessSystemTreeVo bs = new BusinessSystemTreeVo(currentUserId);
                        List<Object> lo = listob.get(i);
                        // 非第一层条目
                        if(businessSystemTreeVo.getId().longValue() != this.num_0){
                            index = tierNumber+1;
                            // 获取下一个节点层级
                            bs.setAssetTreeTierId(nextAssetTreeId);
                            // 查询列表的目录是不是当前节点目录
                            List<String> parentName = new ArrayList<>();
                            try{
                                // 获取目录数据
                                for (int ii = 0; ii < tierNumber; ii++){
                                    parentName.add(lo.get(ii).toString());
                                }
                            }catch (IndexOutOfBoundsException io){
                                logger.error("导入数据文件，出现异常：",io.getMessage());
                                excelRow.add("<div>EXCEL第  <span style='color: red'>"+ String.valueOf(i+2) + "</span>  行条目名称不能为空</div>");
                                result.put("excelRow",new Gson().toJson(excelRow));
                                result.put("status", Constants.ITMP_RETURN_SUCCESS);
                            }catch (Exception e){
                                logger.error("导入数据文件，出现异常：",e.getMessage());
                                return super.handleException(e,"业务树导入数据文件出现问题："+e.getMessage());
                            }
                            // excel目录名称
                            String tierName = StringUtils.join(parentName,"/");
                            flag = treeNames.get(0).equals(tierName);
                            // 目录名称对应
                            if(flag){
                                // 查询节点下是否包含当前插入的子节点名称
                                try {
                                    String name = lo.get(index-1).toString();
                                    if(name == "") {
                                        throw new Exception();
                                    }
                                    BusinessSystemTreeVo vo = null;
                                    if(type == 1){
                                       vo = businessTreeMapper.getBusinessByParentIdAndName(businessSystemTreeVo.getParentId(),name);
                                    } else{
                                        vo = systemTreeMapper.getBusinessByParentIdAndName(businessSystemTreeVo.getParentId(),name);
                                    }
                                    if(vo != null) {
                                        excelRow.add("<div>EXCEL第  <span style='color: red'>" + String.valueOf(i + 2) + "</span>  行当前目录下的条目名称重复</div>");
                                        continue;
                                    } else {
                                        bs.setParentId(businessSystemTreeVo.getParentId());
                                        bs.setParentIds(businessSystemTreeVo.getParentIds());
                                    }
                                } catch (Exception e){
                                    excelRow.add("<div>EXCEL第  <span style='color: red'>"+ String.valueOf(i+2) + "</span>  行条目名称不能为空</div>");
                                    continue;
                                }
                            } else {// 不对应
                                excelRow.add("<div>EXCEL第  <span style='color: red'>"+ String.valueOf(i+2) + "</span>  行目录与当前节点目录不对应</div>");
                                continue;
                            }

                        } else { // 第一层条目
                            index = tierNumber;
                            bs.setAssetTreeTierId(businessSystemTreeVo.getAssetTreeTierId());
                            flag = treeNames.contains(lo.get(0).toString());
                            if(flag){ // 重复
                                excelRow.add("<div>EXCEL第  <span style='color: red'>"+ String.valueOf(i+2) + "</span>  行目录已存在</div>");
                                continue;
                            }
                        }

                        try {
                            if(lo.get(index-1) == "") {
                                throw new Exception();
                            }
                            // 条目名称
                            bs.setBusinessSystemTreeName(lo.get(index-1).toString());
                        } catch (Exception e){
                            excelRow.add("<div>EXCEL第  <span style='color: red'>"+ String.valueOf(i+2) + "</span>  行条目名称不能为空</div>");
                        }

                        // 简称
                        try {
                            bs.setBusinessSystemTreeShortName(lo.get(index).toString());
                        } catch(IndexOutOfBoundsException indexOut){
                            bs.setBusinessSystemTreeShortName(null);
                        }

                        // 编号
                        try {
                            bs.setBusinessSystemTreeCode(lo.get(index+1).toString());
                        } catch(IndexOutOfBoundsException indexOut){
                            bs.setBusinessSystemTreeCode(null);
                        }

                        // 说明
                        try {
                            bs.setRemark(lo.get(index+2).toString());
                        } catch(IndexOutOfBoundsException indexOut){
                            bs.setRemark(null);
                        }

                        if(type == 1){
                            businessTreeMapper.insertBusinessSystemTreeVo(bs);
                        } else {
                            systemTreeMapper.insertBusinessSystemTreeVo(bs);
                        }
                    }
                } else {
                    excelRow.add("<div>请上传正确的EXCEL文件</div>");
                }
            }
        }
        result.put("excelRow",new Gson().toJson(excelRow));
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        return result;
    }


    /**
    *@author liushan
    *@Description
    *@Date 2019/9/6
    *@Param [bsVo, assetTreeType, currentUserId, type, file] type ： 1 业务  2 系统
    *@return java.util.Map<java.lang.String,java.lang.Object>
    **/
    @Override
    @Transactional(readOnly = false)
    public Map<String, Object> importTemplet(String bsVo,Integer assetTreeType, Long currentUserId,int type,MultipartFile file) throws Exception{
        Map<String, Object> result = new HashMap<>();
        List<String> excelRow = new ArrayList<>();
        Set<Long> nodeParentId = new HashSet<>();

        if(StringUtils.isNotBlank(bsVo) && !Objects.isNull(file) && !file.isEmpty()){
            BusinessSystemTreeVo businessSystemTreeVo = JSONObject.parseObject(bsVo, BusinessSystemTreeVo.class);
            TblAssetTreeTier assetTree = assetTreeTierMapper.selectById(businessSystemTreeVo.getAssetTreeTierId());
            int tierNumber = assetTree.getTierNumber().intValue();
            List<Map<String,Object>> parentNames = new ArrayList<>();
            List<Object> treeNames = new ArrayList<>();
            Boolean flag;
            Integer index;
            Long nextAssetTreeId = this.getNextAssetTreeId(businessSystemTreeVo.getAssetTreeTierId(),assetTreeType.longValue());

            // 判断表头
            List<TblAssetTreeTier> treeTiers = assetTreeTierMapper.getAssetTreeList(assetTreeType);
            List<Object> head = new ArrayList<>();
            if(businessSystemTreeVo.getId().longValue() != this.num_0){
                for (int i = 0,len = tierNumber; i < len; i++) {
                    head.add(treeTiers.get(i).getAssetTreeName());
                }
            }
            head.add("条目名称*");
            head.add("条目简称");
            head.add("条目编码");
            head.add("说明");

            InputStream in =null;
            try {
                in = file.getInputStream();
            } catch (IOException e) {
                logger.error("业务树导入数据文件不存在，出现异常：",e.getMessage());
                return super.handleException(e,"业务树导入数据文件出现问题："+e.getMessage());
            }
            List<List<Object>> listob = null;
            try {
                listob = new ExcelUtil().getBankListWithFirstRowsByExcel(in,file);
            } catch (Exception e) {
                logger.error("业务树导入数据文件,出现异常：",e.getMessage());
                return super.handleException(e,"业务树导入数据文件出现问题："+e.getMessage());
            }

            if(listob != null && listob.size() > 0){
                Boolean heahFlag = CollectionUtil.getIsEquals(listob.get(0),head);
                if(heahFlag){
                    if(businessSystemTreeVo.getId().longValue() != this.num_0){
                        // 查询当前层级下的目录
                        List<String>  parentIds = new ArrayList<>();
                        if(type == 1){
                            if(tierNumber == 1){
                                List<BusinessSystemTreeVo>  firstBusTree = businessTreeMapper.getFirstTree();
                                parentIds = CollectionUtil.extractToList(firstBusTree, "id");
                            } else {
                                parentIds = businessTreeMapper.selectAllParentIdsByAssetTreeId(businessSystemTreeVo.getAssetTreeTierId());
                            }
                        } else if(type == 2){
                            if(tierNumber == 1){
                                List<BusinessSystemTreeVo>  firstSysTree = systemTreeMapper.getFirstTree();
                                parentIds = CollectionUtil.extractToList(firstSysTree, "id");
                            } else {
                                parentIds = systemTreeMapper.selectAllParentIdsByAssetTreeId(businessSystemTreeVo.getAssetTreeTierId());
                            }
                        }
                        Set<Object> set = new HashSet<>();
                        set.addAll(parentIds);
                        for (Object str : set) {
                            if(type == 1){
                                parentNames.add(businessTreeMapper.getAllTierNameWithTree(str.toString()));
                            } else if(type == 2){
                                parentNames.add(systemTreeMapper.getAllTierNameWithTree(str.toString()));
                            }
                        }
                        treeNames = CollectionUtil.extractToList(parentNames, "parentTreeName");
                    } else {
                        // 第一层的条目名称
                        if(type == 1){
                            treeNames = businessTreeMapper.getTierByTierId(businessSystemTreeVo.getAssetTreeTierId());
                        } else if(type == 2){
                            treeNames = systemTreeMapper.getTierByTierId(businessSystemTreeVo.getAssetTreeTierId());
                        }

                    }
                    listob.remove(0);
                    for (int i = 0,len = listob.size(); i < len; i++) {
                        BusinessSystemTreeVo bs = new BusinessSystemTreeVo(currentUserId);
                        List<Object> lo = listob.get(i);
                        // 非第一层条目
                        if(businessSystemTreeVo.getId().longValue() != 0){
                            index = tierNumber+1;
                            // 获取下一个节点层级
                            bs.setAssetTreeTierId(nextAssetTreeId);

                            // 1.判断目录是否正确
                            List<String> parentName = new ArrayList<>();
                            try{
                                // 获取目录数据
                                for (int ii = 0; ii < tierNumber; ii++){
                                    parentName.add(lo.get(ii).toString());
                                }
                            }catch (IndexOutOfBoundsException io){
                                logger.error("导入数据文件，出现异常：",io.getMessage());
                                excelRow.add("<div>EXCEL第  <span style='color: red'>"+ String.valueOf(i+2) + "</span>  行目录不能为空</div>");
                                result.put("excelRow",new Gson().toJson(excelRow));
                                result.put("status", Constants.ITMP_RETURN_SUCCESS);
                            }catch (Exception e){
                                logger.error("导入数据文件，出现异常：",e.getMessage());
                                return super.handleException(e,"导入数据文件出现问题："+e.getMessage());
                            }
                            String tierName = StringUtils.join(parentName,",");
                            flag = treeNames.contains(tierName);
                            // 目录存在
                            if(flag){
                                try {
                                    // 2.条目名称是否正确
                                    String name = lo.get(index-1).toString();
                                    if(name.equals("")) {
                                        throw new Exception();
                                    }
                                    int idIndex = treeNames.indexOf(tierName);

                                    String parentIds = parentNames.get(idIndex).get("parentIds").toString();
                                    Long parentId = Long.parseLong(parentIds.substring(parentIds.lastIndexOf(",") + 1));
                                    BusinessSystemTreeVo vo = null;
                                    if(type == 1){
                                        vo = businessTreeMapper.getBusinessByParentIdAndName(parentId,name);
                                    } else{
                                        vo = systemTreeMapper.getBusinessByParentIdAndName(parentId,name);
                                    }
                                    if(vo != null) {
                                        excelRow.add("<div>EXCEL第  <span style='color: red'>" + String.valueOf(i + 2) + "</span>  行当前目录下的条目名称重复</div>");
                                        continue;
                                    } else {
                                        bs.setParentId(parentId);
                                        bs.setParentIds(parentIds);
                                        nodeParentId.add(parentId);
                                    }
                                } catch (Exception e){
                                    excelRow.add("<div>EXCEL第  <span style='color: red'>"+ String.valueOf(i+2) + "</span>  行条目名称不能为空</div>");
                                }
                            } else{
                                excelRow.add("<div>EXCEL第  <span style='color: red'>"+ String.valueOf(i+2) + "</span>  行目录不存在</div>");
                                continue;
                            }
                        } else { // 第一层条目
                            index = tierNumber;
                            bs.setAssetTreeTierId(businessSystemTreeVo.getAssetTreeTierId());
                            flag = treeNames.contains(lo.get(0).toString());
                            // 重复
                            if(flag == true){
                                excelRow.add("<div>EXCEL第  <span style='color: red'>"+ String.valueOf(i+2) + "</span>  行目录已存在</div>");
                                continue;
                            }
                        }

                        try {
                            if(lo.get(index-1) == "") {
                                throw new Exception();
                            }
                            // 条目名称
                            bs.setBusinessSystemTreeName(lo.get(index-1).toString());
                        } catch (Exception e){
                            excelRow.add("<div>EXCEL第  <span style='color: red'>"+ String.valueOf(i+2) + "</span>  行条目名称不能为空</div>");
                        }

                        // 简称
                        try {
                            bs.setBusinessSystemTreeShortName(lo.get(index).toString());
                        } catch(IndexOutOfBoundsException indexOut){
                            bs.setBusinessSystemTreeShortName(null);
                        }

                        // 编号
                        try {
                            bs.setBusinessSystemTreeCode(lo.get(index+1).toString());
                        } catch(IndexOutOfBoundsException indexOut){
                            bs.setBusinessSystemTreeCode(null);
                        }

                        // 说明
                        try {
                            bs.setRemark(lo.get(index+2).toString());
                        } catch(IndexOutOfBoundsException indexOut){
                            bs.setRemark(null);
                        }

                        if(type == 1){
                            businessTreeMapper.insertBusinessSystemTreeVo(bs);
                        } else if(type == 2){
                            systemTreeMapper.insertBusinessSystemTreeVo(bs);
                        }
                    }
                } else {
                    excelRow.add("<div>请上传正确的EXCEL文件</div>");
                }
            }
        }
        Gson gson = new Gson();
        result.put("excelRow",gson.toJson(excelRow));
        result.put("nodeParentIds", gson.toJson(nodeParentId));
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        return result;
    }
}

package cn.pioneeruniverse.project.dao.mybatis;

import cn.pioneeruniverse.project.entity.TblAssetBusinessTree;
import cn.pioneeruniverse.project.vo.BusinessSystemTreeVo;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TblAssetBusinessTreeMapper extends BaseMapper<TblAssetBusinessTree> {
    int deleteByPrimaryKey(Long id);

    int insertSelective(TblAssetBusinessTree record);

    TblAssetBusinessTree selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblAssetBusinessTree record);

    int updateByPrimaryKey(TblAssetBusinessTree record);

    /**
    *@author author
    *@Description 添加业务树
    *@Date 2020/8/26
     * @param assetBusinessTree
    *@return void
    **/
    void insertBusinessSystemTreeVo(BusinessSystemTreeVo assetBusinessTree);

    /**
    *@author author
    *@Description 根据层级查询业务树
    *@Date 2020/8/26
     * @param businessSystemTreeVo
    *@return java.util.List<cn.pioneeruniverse.project.vo.BusinessSystemTreeVo>
    **/
    List<BusinessSystemTreeVo> getBusinessTreeListByTierNumber(BusinessSystemTreeVo businessSystemTreeVo);

    /**
    *@author author
    *@Description 查询业务树实体类
    *@Date 2020/8/26
     * @param id
    *@return cn.pioneeruniverse.project.vo.BusinessSystemTreeVo
    **/
    BusinessSystemTreeVo getEntityInfo(Long id);

    /**
    *@author author
    *@Description 修改业务树
    *@Date 2020/8/26
     * @param businessSystemTreeVo
    *@return void
    **/
    void updateBusinessSystemTreeVo(BusinessSystemTreeVo businessSystemTreeVo);

    /**
    *@author author
    *@Description 查询节点子节点
    *@Date 2020/8/26
     * @param parentIds
    *@return java.util.List<cn.pioneeruniverse.project.vo.BusinessSystemTreeVo>
    **/
    List<BusinessSystemTreeVo> selectChildTier(String parentIds);

    /**
    *@author author
    *@Description 条件查询业务树和关联系统树
    *@Date 2020/8/26
     * @param businessSystemTreeVo
    *@return java.util.List<cn.pioneeruniverse.project.vo.BusinessSystemTreeVo>
    **/
    List<BusinessSystemTreeVo> getBusinessTreeListWithSystem(BusinessSystemTreeVo businessSystemTreeVo);

    /**
    *@author author
    *@Description 根据PARENT_IDS 查询业务树 
    *@Date 2020/8/26
     * @param parentIds
    *@return java.util.List<cn.pioneeruniverse.project.vo.BusinessSystemTreeVo>
    **/
    List<BusinessSystemTreeVo> getTiersByParentIds(String parentIds);

    /**
    *@author author
    *@Description 
    *@Date 2020/8/26
     * @param parentIds
    *@return java.util.Map<java.lang.String,java.lang.Object>
    **/
    Map<String,Object> getAllTierNameWithTree(@Param("parentIds") String parentIds);

    /**
    *@author author
    *@Description 查询所有PARENT_IDS
    *@Date 2020/8/26
     * @param 
    *@return java.util.List<java.lang.String>
    **/
    List<String> selectAllParentIds();

    /**
    *@author author
    *@Description 合并父级名称
    *@Date 2020/8/26
     * @param parentIds
    *@return java.lang.String
    **/
    String selectParentTreeNames(String parentIds);

    /**
    *@author author
    *@Description 根据层级id查询业务树名称
    *@Date 2020/8/26
     * @param assetTreeTierId
    *@return java.util.List<java.lang.Object>
    **/
    List<Object> getTierByTierId(Long assetTreeTierId);

    /**
    *@author author
    *@Description 查询层级为1 的业务树和关联的系统树信息 
    *@Date 2020/8/26
     * @param businessSystemTreeVo
    *@return java.util.List<cn.pioneeruniverse.project.vo.BusinessSystemTreeVo>
    **/
    List<BusinessSystemTreeVo> getBusinessTreeFirstListWithSystem(BusinessSystemTreeVo businessSystemTreeVo);

    /**
    *@author liushan
    *@Description 固定获取业务树第一二层
    *@Date 2019/9/3
    *@Param []
    *@return java.util.List<cn.pioneeruniverse.project.vo.BusinessSystemTreeVo>
    **/
    List<BusinessSystemTreeVo> getFirstSecondTree();

    /**
    *@author liushan
    *@Description 获取子节点
    *@Date 2019/9/3
    *@Param []
    *@return java.util.List<cn.pioneeruniverse.project.vo.BusinessSystemTreeVo>
    **/
    List<BusinessSystemTreeVo> getTreeByParentId(@Param("parentFrontId") Long parentFrontId);

    /**
    *@author author
    *@Description 根据PARENT_ID 和条件查询列表
    *@Date 2020/8/26
     * @param vo
    *@return java.util.List<cn.pioneeruniverse.project.vo.BusinessSystemTreeVo>
    **/
    List<BusinessSystemTreeVo> getListByParentId(BusinessSystemTreeVo vo);

    /**
    *@author author
    *@Description 第一层业务树 
    *@Date 2020/8/26
     * @param 
    *@return java.util.List<cn.pioneeruniverse.project.vo.BusinessSystemTreeVo>
    **/
    List<BusinessSystemTreeVo> getFirstTree();

    /**
    *@author author
    *@Description 根据PARENT_ID 和条件 查询列表带业务名称 
    *@Date 2020/8/26
     * @param businessSystemTreeVo
    *@return java.util.List<cn.pioneeruniverse.project.vo.BusinessSystemTreeVo>
    **/
    List<BusinessSystemTreeVo> getListByParentIdWithName(BusinessSystemTreeVo businessSystemTreeVo);

    /**
    *@author author
    *@Description 根据父id和名称查询业务树
    *@Date 2020/8/26
     * @param parentId
 * @param name
    *@return cn.pioneeruniverse.project.vo.BusinessSystemTreeVo
    **/
    BusinessSystemTreeVo getBusinessByParentIdAndName(@Param("parentId") Long parentId, @Param("name") String name);

    /**
    *@author author
    *@Description 根据层级查询所有的父ids
    *@Date 2020/8/26
     * @param assetTreeId
    *@return java.util.List<java.lang.String>
    **/
    List<String> selectAllParentIdsByAssetTreeId(Long assetTreeId);
}
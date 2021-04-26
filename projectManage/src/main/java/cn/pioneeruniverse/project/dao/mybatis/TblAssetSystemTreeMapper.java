package cn.pioneeruniverse.project.dao.mybatis;

import cn.pioneeruniverse.project.entity.TblAssetSystemTree;
import cn.pioneeruniverse.project.vo.BusinessSystemTreeVo;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface TblAssetSystemTreeMapper extends BaseMapper<TblAssetSystemTree> {
    int deleteByPrimaryKey(Long id);

    int insertSelective(TblAssetSystemTree record);

    TblAssetSystemTree selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblAssetSystemTree record);

    int updateByPrimaryKey(TblAssetSystemTree record);

    /**
    *@author author
    *@Description 根据 业务id 查询所 关联的系统条目信息
    *@Date 2020/8/27
     * @param businessSystemTreeId
    *@return java.util.List<cn.pioneeruniverse.project.entity.TblAssetSystemTree>
    **/
    List<TblAssetSystemTree> selectListByBusId(Long businessSystemTreeId);

    /**
    *@author author
    *@Description 系统树根据层级查询
     *@Date 2020/8/27
     * @param assetTreeTier
    *@return java.util.List<cn.pioneeruniverse.project.vo.BusinessSystemTreeVo>
    **/
    List<BusinessSystemTreeVo> getSystemTreeListByTier(BusinessSystemTreeVo assetTreeTier);

    /**
    *@author author
    *@Description 添加操作
    *@Date 2020/8/27
     * @param businessSystemTreeVo
    *@return void
    **/
    void insertBusinessSystemTreeVo(BusinessSystemTreeVo businessSystemTreeVo);

    /**
    *@author author
    *@Description 查询实体类信息
    *@Date 2020/8/27
     * @param id
    *@return cn.pioneeruniverse.project.vo.BusinessSystemTreeVo
    **/
    BusinessSystemTreeVo getEntityInfo(Long id);

    /**
    *@author author
    *@Description 查询节点子节点
    *@Date 2020/8/27
     * @param parentIds
    *@return java.util.List<cn.pioneeruniverse.project.vo.BusinessSystemTreeVo>
    **/
    List<BusinessSystemTreeVo> selectChildTier(String parentIds);

    /**
    *@author author
    *@Description 修改操作
    *@Date 2020/8/27
     * @param treeVo
    *@return void
    **/
    void updateBusinessSystemTreeVo(BusinessSystemTreeVo treeVo);

    /**
    *@author author
    *@Description 查询所有父节点
    *@Date 2020/8/27
     * @param 
    *@return java.util.List<java.lang.String>
    **/
    List<String> selectAllParentIds();

    /**
    *@author author
    *@Description 查询SYSTEM_TREE_NAME 名称 
    *@Date 2020/8/27
     * @param parentIds
    *@return java.util.Map<java.lang.String,java.lang.Object>
    **/
    Map<String,Object> getAllTierNameWithTree(@Param("parentIds") String parentIds);

    /**
    *@author author
    *@Description 根据层级id，查询系统树名称
    *@Date 2020/8/27
     * @param assetTreeTierId
    *@return java.util.List<java.lang.Object>
    **/
    List<Object> getTierByTierId(Long assetTreeTierId);

    /**
    *@author author
    *@Description 根据PARENT_IDS查询系统树
    *@Date 2020/8/27
     * @param parentIds
    *@return java.util.List<cn.pioneeruniverse.project.vo.BusinessSystemTreeVo>
    **/
    List<BusinessSystemTreeVo> getTiersByParentIds(String parentIds);

    /**
    *@author author
    *@Description 查询系统树
    *@Date 2020/8/27
     * @param businessSystemTreeVo
    *@return java.util.List<cn.pioneeruniverse.project.vo.BusinessSystemTreeVo>
    **/
    List<BusinessSystemTreeVo> getSystemTreeList(BusinessSystemTreeVo businessSystemTreeVo);

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
    *@Description 根据父id和名称查询业务树
    *@Date 2020/8/26
     * @param parentId
 * @param name
    *@return cn.pioneeruniverse.project.vo.BusinessSystemTreeVo
    **/
    BusinessSystemTreeVo getBusinessByParentIdAndName(@Param("parentId") Long parentId, @Param("name") String name);

    /**
    *@author author
    *@Description 根据 ParentId 和条件查询列表 
    *@Date 2020/8/26
     * @param businessSystemTreeVo
    *@return java.util.List<cn.pioneeruniverse.project.vo.BusinessSystemTreeVo>
    **/
    List<BusinessSystemTreeVo> getListByParentId(BusinessSystemTreeVo businessSystemTreeVo);

    /**
    *@author author
    *@Description 获取第一层数据
    *@Date 2020/8/26
     * @param 
    *@return java.util.List<cn.pioneeruniverse.project.vo.BusinessSystemTreeVo>
    **/
    List<BusinessSystemTreeVo> getFirstTree();

    /**
    *@author author
    *@Description 获取parentId等于id的数据
    *@Date 2020/8/26
     * @param nodeId
    *@return java.util.List<cn.pioneeruniverse.project.vo.BusinessSystemTreeVo>
    **/
    List<BusinessSystemTreeVo> getTreeByParentId(Long nodeId);

    /**
    *@author author
    *@Description 获取第一层 和 第二层数据
    *@Date 2020/8/26
     * @param 
    *@return java.util.List<cn.pioneeruniverse.project.vo.BusinessSystemTreeVo>
    **/
    List<BusinessSystemTreeVo> getFirstSecondTree();

    /**
    *@author author
    *@Description 根据 ParentId 和条件查询 列表带名称 
    *@Date 2020/8/26
     * @param businessSystemTreeVo
    *@return java.util.List<cn.pioneeruniverse.project.vo.BusinessSystemTreeVo>
    **/
    List<BusinessSystemTreeVo> getListByParentIdWithName(BusinessSystemTreeVo businessSystemTreeVo);

    /**
    *@author author
    *@Description 根据assetTreeId 层级获取数据-
    *@Date 2020/8/26
     * @param assetTreeId
    *@return java.util.List<java.lang.String>
    **/
    List<String> selectAllParentIdsByAssetTreeId(Long assetTreeId);

    /**
    *@author author
    *@Description 根据businessSystemTreeCode 获取 数据
    *@Date 2020/8/26
     * @param businessSystemTreeCode
    *@return cn.pioneeruniverse.project.vo.BusinessSystemTreeVo
    **/
    BusinessSystemTreeVo getSystemByCode(String businessSystemTreeCode);

    /**
    *@author liushan
    *@Description 根据id查询所有的子节点
    *@Date 2019/9/17
    *@Param [id]
    *@return java.util.List<cn.pioneeruniverse.project.vo.BusinessSystemTreeVo>
    **/
    List<BusinessSystemTreeVo>  getSystemTreeById(@Param("id") Long id,@Param("likeId") String likeId);

    /**
    *@author author
    *@Description 根据条件获取信息
    *@Date 2020/8/26
     * @param map
    *@return java.util.List<cn.pioneeruniverse.project.entity.TblAssetSystemTree>
    **/
	List<TblAssetSystemTree> getModuleList(Map<String, Object> map);
}
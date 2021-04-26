package cn.pioneeruniverse.project.dao.mybatis;

import cn.pioneeruniverse.project.entity.TblAssetTreeTier;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
*@author author
*@Description 层级
*@Date 2020/8/26
*@return
**/
public interface TblAssetTreeTierMapper extends BaseMapper<TblAssetTreeTier> {
    int deleteByPrimaryKey(Long id);

    int insertSelective(TblAssetTreeTier record);

    TblAssetTreeTier selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblAssetTreeTier record);

    int updateByPrimaryKey(TblAssetTreeTier record);

    /**
    *@author liushan
    *@Description 查询 业务树层级列表
    *@Date 2020/8/13
     * @param assetTreeType
    *@return java.util.List<cn.pioneeruniverse.project.entity.TblAssetTreeTier>
    **/
    List<TblAssetTreeTier> getAssetTreeList(Integer assetTreeType);
    
    /**
    *@author liushan
    *@Description 根据类型查询层级信息
    *@Date 2020/8/13
     * @param assetTreeType
    *@return java.util.List<cn.pioneeruniverse.project.entity.TblAssetTreeTier>
    **/
    List<TblAssetTreeTier> getTierNumbersByTreeType(Long assetTreeType);

    /**
    *@author liushan
    *@Description 根据层级类型和层级号查询
    *@Date 2020/8/13
     * @param assetTreeType
 * @param tierNumber
    *@return cn.pioneeruniverse.project.entity.TblAssetTreeTier
    **/
    TblAssetTreeTier getAssetTree(@Param("assetTreeType") int assetTreeType, @Param("tierNumber") int tierNumber);
}
package cn.pioneeruniverse.project.service.AssetTree;

import cn.pioneeruniverse.project.entity.TblAssetTreeTier;
import cn.pioneeruniverse.project.vo.BusinessSystemTreeVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Author:liushan
 * Date: 2019/5/14 下午 3:11
 */
public interface AssetTreeService {
    /**
     * 业务树层级列表
     * @param assetTreeType
     * @return
     */
    List<TblAssetTreeTier> getAssetTreeList(Integer assetTreeType);

    /**
    *@author author
    *@Description 保存系统/业务树层级
    *@Date 2020/8/25
     * @param assetTreeTier
 * @param request
    *@return void
    **/
    void saveAssetTree(TblAssetTreeTier assetTreeTier, HttpServletRequest request) throws Exception;

    /**
    *@author author
    *@Description 编辑
    *@Date 2020/8/25
     * @param assetTreeTier
 * @param request
    *@return void
    **/
    void editAssetTree(TblAssetTreeTier assetTreeTier, HttpServletRequest request);
    /**
     * 判断层级有没有条目
     * assetTreeType 资产树类型（1:业务树，2:系统树）
     * @param assetTreeTier
     *  true ： 可以   false: 不可以
     * @return
     */
    Map<String,Object> getTreeListByTierId(BusinessSystemTreeVo assetTreeTier);
}

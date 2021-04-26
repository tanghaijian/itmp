package cn.pioneeruniverse.project.controller;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.project.entity.TblAssetTreeTier;
import cn.pioneeruniverse.project.service.AssetTree.AssetTreeService;
import cn.pioneeruniverse.project.vo.BusinessSystemTreeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 业务/系统层级维护
 * Author:liushan
 * Date: 2019/5/14 下午 3:09
 */
@RestController
@RequestMapping("assetTree")
public class AssetTreeController extends BaseController {

    @Autowired
    private AssetTreeService assetTreeService;

    /**
     * 系统/业务树层级列表
     * @param assetTreeType   资产树类型（1:业务树，2:系统树）
     * @return result.put("data", list); 业务树/ 系统树
     */
    @PostMapping(value="assetTreeList")
    public Map<String, Object> assetTreeList(Integer assetTreeType){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            List<TblAssetTreeTier> list = assetTreeService.getAssetTreeList(assetTreeType);
            result.put("data", list);
        } catch (Exception e) {
            return this.handleException(e, "获取用户菜单失败");
        }
        return result;
    }


    /**
     * 保存系统/业务树层级
     * @param assetTreeTier
     * @return map status 1正常返回，2异常返回
     */
    @PostMapping(value="saveAssetTree")
    public Map<String, Object> saveAssetTree(TblAssetTreeTier assetTreeTier,HttpServletRequest request){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            assetTreeService.saveAssetTree(assetTreeTier,request);
        } catch (Exception e) {
            return this.handleException(e, "获取用户菜单失败");
        }
        return result;
    }


    /**
     * 编辑 系统/业务树层级
     * @param assetTreeTier
     * @param request
     * @return map status 1正常返回，2异常返回
     */
    @PostMapping(value="editAssetTree")
    public Map<String, Object> editAssetTree(TblAssetTreeTier assetTreeTier,HttpServletRequest request){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.ITMP_RETURN_SUCCESS);
        try {
            assetTreeService.editAssetTree(assetTreeTier,request);
        } catch (Exception e) {
            return this.handleException(e, "获取用户菜单失败");
        }
        return result;
    }

    /**
     * 查询层级有没有条目
     * @param assetTreeTier
     * @return true ： 可以   false: 不可以
     *         status 1正常返回，2异常返回
     */
    @PostMapping(value="getTreeListByTierId")
    public Map<String, Object> getTreeListByTierId(BusinessSystemTreeVo assetTreeTier){
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            result = assetTreeService.getTreeListByTierId(assetTreeTier);
            result.put("status", Constants.ITMP_RETURN_SUCCESS);
        } catch (Exception e) {
            return this.handleException(e, "获取用户菜单失败");
        }
        return result;
    }
}

package cn.pioneeruniverse.project.service.AssetTree.impl;

import cn.pioneeruniverse.common.utils.CollectionUtil;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.project.dao.mybatis.TblAssetBusinessTreeMapper;
import cn.pioneeruniverse.project.dao.mybatis.TblAssetSystemTreeMapper;
import cn.pioneeruniverse.project.dao.mybatis.TblAssetTreeTierMapper;
import cn.pioneeruniverse.project.entity.TblAssetTreeTier;
import cn.pioneeruniverse.project.service.AssetTree.AssetTreeService;
import cn.pioneeruniverse.project.vo.BusinessSystemTreeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 层级业务逻辑层
 * Author:liushan
 * Date: 2019/5/14 下午 3:11
 */
@Transactional(readOnly = true)
@Service("AssetTreeService")
public class AssetTreeServiceImpl implements AssetTreeService {
    @Autowired
    private TblAssetTreeTierMapper assetTreeTierMapper;
    @Autowired
    private TblAssetBusinessTreeMapper businessTreeMapper;
    @Autowired
    private TblAssetSystemTreeMapper systemTreeMapper;

    /**
     * 业务树层级列表
     * @param assetTreeType
     * @return
     */
    @Override
    public List<TblAssetTreeTier> getAssetTreeList(Integer assetTreeType) {
       return assetTreeTierMapper.getAssetTreeList(assetTreeType);
    }

    /**
     * 保存系统/业务树层级
     * @param assetTreeTier
     * @param request
     */
    @Override
    @Transactional(readOnly = false)
    public void saveAssetTree(TblAssetTreeTier assetTreeTier, HttpServletRequest request) throws Exception{
        assetTreeTier = (TblAssetTreeTier) CommonUtil.setBaseValue(assetTreeTier,request);
        // 判断当前的编号，处于什么位置
        Long tierNumber = assetTreeTier.getTierNumber();
        List<TblAssetTreeTier> assetTreeTiers = assetTreeTierMapper.getTierNumbersByTreeType(assetTreeTier.getAssetTreeType());
        List<Long> tierNumbers = CollectionUtil.extractToList(assetTreeTiers, "tierNumber");

        if ( tierNumbers != null && tierNumbers.size() > 0){
            // 存在的最大的层级 大于 要添加的层级说明添加的是中间层
            Long max = Collections.max(tierNumbers);
            if(max >= tierNumber){
                int index =  tierNumbers.indexOf(tierNumber);
                for(int i = index,len = assetTreeTiers.size(); i < len;i++){
                    TblAssetTreeTier tblAssetTreeTier = new TblAssetTreeTier();
                    tblAssetTreeTier.setLastUpdateBy(CommonUtil.getCurrentUserId(request));
                    tblAssetTreeTier.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
                    tblAssetTreeTier.setId(assetTreeTiers.get(i).getId());
                    tblAssetTreeTier.setTierNumber(assetTreeTiers.get(i).getTierNumber()+1);
                    assetTreeTierMapper.updateById(tblAssetTreeTier);
                }
            }
        }

        assetTreeTierMapper.insertSelective(assetTreeTier);

    }

    /**
     * 编辑
     * @param assetTreeTier
     * @param request
     */
    @Override
    @Transactional(readOnly = false)
    public void editAssetTree(TblAssetTreeTier assetTreeTier, HttpServletRequest request) {
        assetTreeTier.setCreateBy(CommonUtil.getCurrentUserId(request));
        assetTreeTier.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
        assetTreeTierMapper.updateByPrimaryKeySelective(assetTreeTier);
    }

    /**
     * 判断层级有没有条目
     * assetTreeType 资产树类型（1:业务树，2:系统树）
     * @param assetTreeTier
     *  true ： 可以   false: 不可以
     * @return
     */
    @Override
    public Map<String, Object> getTreeListByTierId(BusinessSystemTreeVo assetTreeTier) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("flag",true);
        List<BusinessSystemTreeVo> voList = null;
        if(assetTreeTier.getAssetTreeType().longValue() == 1){
            // 1:业务树
            voList =businessTreeMapper.getBusinessTreeListByTierNumber(assetTreeTier);
        } else if( assetTreeTier.getAssetTreeType().longValue() == 2 ){
            // 2:系统树
           voList = systemTreeMapper.getSystemTreeListByTier(assetTreeTier);
        }
        if(voList != null && voList.size() > 0){
            resultMap.put("flag",false);
        }
        return resultMap;
    }
}

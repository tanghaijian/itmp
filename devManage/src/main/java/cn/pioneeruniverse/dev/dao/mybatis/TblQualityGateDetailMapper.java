package cn.pioneeruniverse.dev.dao.mybatis;


import cn.pioneeruniverse.dev.entity.TblQualityGateDetail;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;
/**
 *
 * @ClassName: TblQualityGateDetailMapper
 * @Description: 质量门禁mapper接口
 * @author author
 *
 */
public interface TblQualityGateDetailMapper extends BaseMapper<TblQualityGateDetail> {
    int deleteByPrimaryKey(Long id);


    /**
     * 插入
     * @param record
     * @return
     */
    int insertSelective(TblQualityGateDetail record);

    /**
     * 查询
     * @param id
     * @return
     */

    TblQualityGateDetail selectByPrimaryKey(Long id);

    /**
     * 更新
     * @param record
     * @return
     */

    int updateByPrimaryKeySelective(TblQualityGateDetail record);

    int updateByPrimaryKey(TblQualityGateDetail record);

    /**
     * 根据主键查询信息
     * @param id
     * @return
     */

    List<Map<String, Object>> getQualityGateDetail(Long id);
}
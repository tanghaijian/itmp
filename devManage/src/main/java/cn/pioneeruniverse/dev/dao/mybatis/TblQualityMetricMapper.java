package cn.pioneeruniverse.dev.dao.mybatis;

import cn.pioneeruniverse.dev.entity.TblProjectUser;
import cn.pioneeruniverse.dev.entity.TblQualityMetric;
import com.baomidou.mybatisplus.mapper.BaseMapper;
/**
 *
 * @ClassName: TblQualityMetricMapper
 * @Description: 质量指标mapper
 * @author author
 *
 */
public interface TblQualityMetricMapper extends BaseMapper<TblQualityMetric> {
    /**
     * 删除
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入
     * @param record
     * @return
     */

    int insertSelective(TblQualityMetric record);

    /**
     * 查询
     * @param id
     * @return
     */

    TblQualityMetric selectByPrimaryKey(Long id);

    /**
     * 更新
     * @param record
     * @return
     */

    int updateByPrimaryKeySelective(TblQualityMetric record);

    int updateByPrimaryKey(TblQualityMetric record);
}
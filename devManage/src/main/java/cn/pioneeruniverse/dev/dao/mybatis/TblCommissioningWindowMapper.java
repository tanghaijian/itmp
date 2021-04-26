package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblCommissioningWindow;
/**
 *
 * @ClassName: TblCommissioningWindowMapper
 * @Description: 项目mapper
 * @author author
 * @date 2020年8月12日 下午16:20
 *
 */
public interface TblCommissioningWindowMapper extends BaseMapper<TblCommissioningWindow> {
    int deleteByPrimaryKey(Long id);

    int insertSelective(TblCommissioningWindow record);

    TblCommissioningWindow selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblCommissioningWindow record);

    int updateByPrimaryKey(TblCommissioningWindow record);

    List<TblCommissioningWindow> getAll();

    List<TblCommissioningWindow> selectAfterTime();

    TblCommissioningWindow selectBeforeTime();

    String getWindowName(Long commissioningWindowId);

    List<TblCommissioningWindow> findWindowBySystemId(Long systemId);

    List<TblCommissioningWindow> getAllComWindow(Map<String,Object> map);

    List<TblCommissioningWindow> selectWindows(HashMap<String, Object> map);

    int getAllComWindowTotal(Map<String, Object> window);
    List<TblCommissioningWindow> getAllWindowDesc(Long systemId);

    TblCommissioningWindow findWindowByFeatureId(Long featureId);

    List<TblCommissioningWindow> getWindowsByartId(String artifactids);

    List<TblCommissioningWindow> getWindowsByartId(Map<String, Object> map);

    List<TblCommissioningWindow> getAllWindow();


    TblCommissioningWindow selectBeforeTimeOrderBy();

    List<TblCommissioningWindow> selectAfterTimeLimit();

	List<Map<String, Object>> getReqFeatureGroupbyWindow(Long requirementId);
}
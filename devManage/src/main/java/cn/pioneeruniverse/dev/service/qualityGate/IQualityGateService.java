package cn.pioneeruniverse.dev.service.qualityGate;


import cn.pioneeruniverse.dev.entity.TblQualityGate;
import cn.pioneeruniverse.dev.entity.TblQualityGateDetail;
import cn.pioneeruniverse.dev.entity.TblQualityMetric;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
/**
 *
 * @ClassName: IQualityGateService
 * @Description: 质量seevice
 * @author author
 *
 */
public interface IQualityGateService {

    /**
     * 查询
     * @return List<TblQualityGate>
     */
    List<TblQualityGate> getQualityGate();

    List<Map<String, Object>> getQualityGateDetail(Long id);

    /**
     * 新增
     * @param tblQualityGate
     */

    void addQualityGate(TblQualityGate tblQualityGate);

    /**
     * 删除
     * @param id
     */

    void deleteQualityGate(long id);

    void addQualityGateDetail(TblQualityGateDetail tblQualityGateDetail);

    void updateQualityGateDetail(TblQualityGateDetail tblQualityGateDetail);

    void deleteQualityGateDetail(long id);

    List<TblQualityMetric> getMetrics();

    void bindQualityGate(long systemId, Long qualityGateId, HttpServletRequest reques);
}

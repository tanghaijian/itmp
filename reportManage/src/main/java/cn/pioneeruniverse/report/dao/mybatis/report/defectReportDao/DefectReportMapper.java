package cn.pioneeruniverse.report.dao.mybatis.report.defectReportDao;

import cn.pioneeruniverse.report.entity.ReportUiFavorite;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DefectReportMapper {

    /**
     * 报表收藏数据增加
     * @param reportUiFavorite
     * @return
     */
    Integer addDefectReport(ReportUiFavorite reportUiFavorite);

    /**
     * 报表收藏列表数据
     * @param menuUrl
     * @param createBy
     * @return
     */
    List<ReportUiFavorite> selectDefectReportList(@Param("menuUrl") String menuUrl, @Param("createBy")Long createBy);


    /**
     * 报表收藏数据删除
     * @param id
     * @return
     */
    Integer updateDefectReport(@Param("id") Long id);

    /**
     * id查询收藏报表数据
     * @param id
     * @return
     */
    ReportUiFavorite selectDefectReportById(Long id);


    /**
     *  报表内容修改
     * @param reportUiFavorite
     * @return
     */
    Integer updateInfoById(ReportUiFavorite reportUiFavorite);
    
    /**
     * 
    * @Title: updateSelectById
    * @Description: 根据ID将该次收藏方案置为最近收藏方案
    * @author author
    * @param id
    * @return
    * @throws
     */
    Integer updateSelectById(@Param("id")Long id);
    /**
     * 
    * @Title: updateSelectByUrl
    * @Description: 根据url将历次收藏方案置为非最近收藏方案
    * @author author
    * @param id
    * @param menuUrl
    * @return
    * @throws
     */
    Integer updateSelectByUrl(@Param("id")Long id,@Param("menuUrl") String menuUrl);

}

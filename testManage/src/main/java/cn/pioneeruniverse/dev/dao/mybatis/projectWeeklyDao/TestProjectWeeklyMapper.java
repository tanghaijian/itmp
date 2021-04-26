package cn.pioneeruniverse.dev.dao.mybatis.projectWeeklyDao;


import cn.pioneeruniverse.dev.dto.TestDefectInfoDTO;
import cn.pioneeruniverse.dev.dto.TestTaskDeliverDTO;
import cn.pioneeruniverse.dev.vo.TestDefectResolvedVO;
import cn.pioneeruniverse.dev.vo.TestRequirementFeatureTimeTraceVO;
import cn.pioneeruniverse.dev.vo.TestDefectInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TestProjectWeeklyMapper {

    /**
    * @author author
    * @Description 任务交付累计流图
    * @Date 2020/9/7
    * @param taskDeliver
    * @return java.util.List<cn.pioneeruniverse.dev.vo.TestRequirementFeatureTimeTraceVO>
    **/
    List<TestRequirementFeatureTimeTraceVO> selectBySystemId1(TestTaskDeliverDTO taskDeliver);

    /**
    * @author author
    * @Description 缺陷统计图
    * @Date 2020/9/7
    * @param defectInfo
    * @return java.util.List<cn.pioneeruniverse.dev.vo.TestDefectInfoVO>
    **/
    List<TestDefectInfoVO> getDefectInfoCountHandler(TestDefectInfoDTO defectInfo);

    /**
    * @author author
    * @Description 缺陷待解决统计图
    * @Date 2020/9/7
    * @param defectInfo
    * @return java.util.List<cn.pioneeruniverse.dev.vo.TestDefectResolvedVO>
    **/
    List<TestDefectResolvedVO> getDefectResolvedHandler(TestDefectInfoDTO defectInfo);

    /**
    * @author author
    * @Description 批量查询系统
    * @Date 2020/9/7
    * @param id
    * @return java.util.List<cn.pioneeruniverse.dev.vo.TestDefectResolvedVO>
    **/
    List<TestDefectResolvedVO> getSystemInfoList(@Param("id") List<Long> id);

    /**
    * @author author
    * @Description 缺陷列表数据查询
    * @Date 2020/9/7
    * @param defectInfo
    * @return java.util.List<cn.pioneeruniverse.dev.vo.TestDefectResolvedVO>
    **/
    List<TestDefectResolvedVO>selectDefectInfoList(TestDefectInfoDTO defectInfo);

}

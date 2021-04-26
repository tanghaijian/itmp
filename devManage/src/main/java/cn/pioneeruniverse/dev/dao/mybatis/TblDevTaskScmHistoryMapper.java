package cn.pioneeruniverse.dev.dao.mybatis;

import cn.pioneeruniverse.dev.entity.TblDevTaskScmHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description: 工作任务代码关联历史
 * @Date: Created in 14:51 2019/6/5
 * @Modified By:
 */
public interface TblDevTaskScmHistoryMapper {

    /**
    *@author author
    *@Description 添加工作任务代码关联历史信息
    *@Date 2020/8/24
     * @param tblDevTaskScmHistory
    *@return int
    **/
    int insertDevTaskScmHistory(TblDevTaskScmHistory tblDevTaskScmHistory);

    /**
    *@author author
    *@Description 查询最近N天的代码提交总览
    *@Date 2020/8/24
     * @param systemId
 * @param day
    *@return cn.pioneeruniverse.dev.entity.TblDevTaskScmHistory
    **/
    TblDevTaskScmHistory findScmCountBySystemId(@Param(value = "systemId") Long systemId,
                                     
                                                @Param(value = "day") Long day);
    /**
    *@author author
    *@Description 查询最近7天的代码提交总览
    *@Date 2020/8/24
     * @param systemId
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDevTaskScmHistory>
    **/
    List<TblDevTaskScmHistory> find7DayScmCountBySystemId(Long systemId);

}

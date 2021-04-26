package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;

import cn.pioneeruniverse.dev.entity.TblDevTaskScmHistory;
import org.apache.ibatis.annotations.Param;
import cn.pioneeruniverse.dev.entity.TblDevTaskScm;


/**
*@author author
*@Description 工作任务代码关联表
*@Date 2020/8/24
*@return 
**/
public interface TblDevTaskScmMapper {

    /**
    *@author author
    *@Description 添加或修改工作任务关联表
    *@Date 2020/8/5
     * @param tblDevTaskScm
    *@return int
    **/
    int insertOrUpdateDevTaskScm(TblDevTaskScm tblDevTaskScm);

    /**
    *@author author
    *@Description 查询列表
    *@Date 2020/8/5
     * @param tblDevTaskScm
 * @param currentUserId
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblDevTaskScm>
    **/
    List<TblDevTaskScm> selectDevTaskScmPage(@Param(value = "tblDevTaskScm") TblDevTaskScm tblDevTaskScm, @Param("currentUserId") Long currentUserId);
    /**
     *@author author
     *@Description 根据工作任务查询工作任务信息
     *@Date 2020/8/5
     * @param devTaskId
     *@return cn.pioneeruniverse.dev.entity.TblDevTaskScm
     **/
    TblDevTaskScm getDevTaskDetailByDevTaskId(Long devTaskId);

}
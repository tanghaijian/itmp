package cn.pioneeruniverse.project.service.projectinfo;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;

import cn.pioneeruniverse.project.entity.TblProjectInfo;

import javax.servlet.http.HttpServletRequest;

public interface ProjectInfoService {
    /**
     *
     * @Title: selectProjects
     * @Description: 查询项目
     * @author author
     * @param tblProjectInfo 查询条件
     * @param page 第几页
     * @param rows 每页数量
     * @return List<TblProjectInfo>
     */
    List<TblProjectInfo> selectProjects(TblProjectInfo tblProjectInfo, Integer page, Integer rows);
    /**
     *
     * @Title: insertProject
     * @Description: 保存项目
     * @author author
     * @param tblProjectInfo 项目信息
     */
    void insertProject(TblProjectInfo tblProjectInfo);
    /**
     *
     * @Title: deleteProjectById
     * @Description: 删除项目
     * @author author
     * @param id 项目id
     */
    void deleteProjectById(Long id);
    /**
     *
     * @Title: selectProjectById
     * @Description: 项目详情
     * @author author
     * @param id 项目id
     * @return TblProjectInfo
     */
    TblProjectInfo selectProjectById(Long id);

    /**
     *
     * @Title: updateProject
     * @Description: 更新项目
     * @author author
     * @param tblProjectInfo 项目信息
     */
    void updateProject(TblProjectInfo tblProjectInfo);
    /**
     *
     * @Title: selectCount
     * @Description: 项目总数
     * @author author
     * @return Integer 项目总数
     */
    Integer selectCount();
    /**
     *
     * @Title: getAllProjectInfo
     * @Description: 获取所有项目
     * @author author
     * @return List<TblProjectInfo>
     */
    List<TblProjectInfo> getAllProjectInfo();

    /**
     * 根据当人用户 查询所处的项目
     * 系统管理员显示 所有项目
     *
     * @param request
     * @return Map<String, Object>
     */
    Map<String, Object> getAllProject(HttpServletRequest request);
}

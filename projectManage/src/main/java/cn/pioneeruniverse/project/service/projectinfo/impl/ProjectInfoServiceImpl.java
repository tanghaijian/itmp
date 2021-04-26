package cn.pioneeruniverse.project.service.projectinfo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.pioneeruniverse.project.dao.mybatis.ProjectInfoMapper;
import cn.pioneeruniverse.project.entity.TblProjectInfo;
import cn.pioneeruniverse.project.service.projectinfo.ProjectInfoService;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
public class ProjectInfoServiceImpl implements ProjectInfoService {

    @Autowired
    private ProjectInfoMapper projectInfoMapper;

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
    @Override
    public List<TblProjectInfo> selectProjects(TblProjectInfo tblProjectInfo, Integer page, Integer rows) {
//		PageInfo<TblProjectInfo>
//		PageHelper.startPage(Integer.getInteger(currentPage), Integer.getInteger(pageSize));   ,currentPage,pageSize
        HashMap<String, Object> map = new HashMap<>();
        Integer start = (page - 1) * rows;
        map.put("start", start);
        map.put("pageSize", rows);
        map.put("tblProjectInfo", tblProjectInfo);
        List<TblProjectInfo> list = projectInfoMapper.selectProjects(map);

//		PageInfo<TblProjectInfo> info = new PageInfo<TblProjectInfo>(list);

        return list;
    }

    /**
     * 
    * @Title: insertProject
    * @Description: 保存项目
    * @author author
    * @param tblProjectInfo 项目信息
     */
    @Override
    public void insertProject(TblProjectInfo tblProjectInfo) {
        this.projectInfoMapper.insertProject(tblProjectInfo);
    }

    /**
     * 
    * @Title: deleteProjectById
    * @Description: 删除项目
    * @author author
    * @param id 项目id
     */
    @Override
    public void deleteProjectById(Long id) {
        this.projectInfoMapper.deleteProjectById(id);
    }

    /**
     * 
    * @Title: selectProjectById
    * @Description: 项目详情
    * @author author
    * @param id 项目id
    * @return TblProjectInfo
     */
    @Override
    public TblProjectInfo selectProjectById(Long id) {
        return this.projectInfoMapper.selectProjectById(id);

    }

    /**
     * 
    * @Title: updateProject
    * @Description: 更新项目
    * @author author
    * @param tblProjectInfo 项目信息
     */
    @Override
    public void updateProject(TblProjectInfo tblProjectInfo) {
        this.projectInfoMapper.updateProject(tblProjectInfo);
    }

    /**
     * 
    * @Title: selectCount
    * @Description: 项目总数
    * @author author
    * @return Integer 项目总数
     */
    @Override
    public Integer selectCount() {
        return projectInfoMapper.selectCount();
    }

    /**
     * 
    * @Title: getAllProjectInfo
    * @Description: 获取所有项目
    * @author author
    * @return List<TblProjectInfo>
     */
    @Override
    @Transactional(readOnly = true)
    public List<TblProjectInfo> getAllProjectInfo() {
        return projectInfoMapper.getAllProject();
    }

    /**
     * 根据当人用户 查询所处的项目
     * 系统管理员显示 所有项目
     *
     * @param request
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> getAllProject(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        List<TblProjectInfo> projects = new ArrayList<>();
        if (new CommonUtils().currentUserWithAdmin(request) == false) {
            projects = projectInfoMapper.getAllProjectByCurrentUserId(CommonUtil.getCurrentUserId(request));
        } else {
            projects = projectInfoMapper.getAllProject();
        }
        //项目信息
        map.put("projects", projects);
        return map;
    }


}

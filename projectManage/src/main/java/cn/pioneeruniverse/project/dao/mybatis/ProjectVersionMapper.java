package cn.pioneeruniverse.project.dao.mybatis;

import cn.pioneeruniverse.project.entity.TblSystemInfo;
import cn.pioneeruniverse.project.entity.TblSystemVersion;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

public interface ProjectVersionMapper extends BaseMapper<TblSystemVersion> {

    List<TblSystemVersion> getProjectVersionByCon(Long projectId);

    List<TblSystemInfo> selectSystemByProjectId(Long projectId);

    List<TblSystemVersion> getSystemVersionByVersion(TblSystemVersion record);

    Integer insertVersion(TblSystemVersion record);

    List<TblSystemVersion> getProjectVersionByVersion(TblSystemVersion record);

    List<TblSystemVersion> getBeforeUpdate(String ids);

    Integer deleteVersion(String ids);

    Integer updateStatusById(TblSystemVersion record);
}
package cn.pioneeruniverse.project.service.projectversion;

import cn.pioneeruniverse.project.entity.TblSystemVersion;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface IProjectVersionService {

     Map<String, Object> selectSystemByProjectId(Long projectId);
     /**
      * @author author
      * @Description 根据条件查询所有版本
      * @Date 2020/9/9
      * @param projectId
      * @return java.util.Map<java.lang.String,java.lang.Object>
      **/
     Map<String, Object> getProjectVersionByCon(Long projectId);
     /**
      * @author author
      * @Description 添加版本
      * @Date 2020/9/9
      * @param systemVersion
      * @param request
      * @return void
      **/
     void insertVersion(TblSystemVersion systemVersion, HttpServletRequest request);
     /**
      * @author author
      * @Description 获取修改信息
      * @Date 2020/9/9
      * @param systemVersion
      * @return java.util.Map<java.lang.String,java.lang.Object>
      **/
     Map<String, Object> getProjectVersionBySystemVersion(TblSystemVersion systemVersion);
     /**
      * @author author
      * @Description 修改版本
      * @Date 2020/9/9
      * @param systemVersion
      * @param request
      * @return void
      **/
     void updateVersion(TblSystemVersion systemVersion, HttpServletRequest request);

     void closeOrOpenSystemVersion(TblSystemVersion systemVersion, HttpServletRequest request);
}

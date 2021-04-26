package cn.pioneeruniverse.dev.service.defect.dev;

import cn.pioneeruniverse.common.entity.BootStrapTablePage;
import cn.pioneeruniverse.dev.entity.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Author:liushan
 * Date: 2018/12/10 下午 1:44
 */
public interface DevDefectService {
    /**
    *@author author
    *@Description 同步缺陷数据
    *@Date 2020/8/21
     * @param objectJson
    *@return void
    **/
    void syncDefect(String objectJson) throws Exception;
    
    /**
    *@author author
    *@Description 同步缺陷附件和日志
    *@Date 2020/8/21
     * @param objectJson
    *@return void
    **/
    void syncDefectAtt(String objectJson) throws Exception;

    /**
    *@author author
    *@Description 编辑缺陷 修改开发管理平台的参数
    *@Date 2020/8/21
     * @param objectJson
    *@return void
    **/
    void updateDevDefect(String objectJson);

    /**
    *@author author
    *@Description 缺陷实体信息和文件 同时处理
    *@Date 2020/8/21
     * @param objectJson
    *@return void
    **/
    void syncDefectWithFiles(String objectJson) throws Exception;
}

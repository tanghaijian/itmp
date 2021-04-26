package cn.pioneeruniverse.dev.service.dbconfig;

import cn.pioneeruniverse.common.entity.BaseEntity;
import cn.pioneeruniverse.common.entity.JqGridPage;
import cn.pioneeruniverse.dev.entity.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
*  数据库配置
* @author:weiji
*
*/

public interface IDbConfigService {

    List<TblSystemInfo> getSystemByUserId(HttpServletRequest request);

    Map<String, Object> getEnvAndModuleBySystemId(long systemId);

    void addDbConfig(TblSystemDbConfig tblSystemDbConfig);

    JqGridPage<TblSystemDbConfigVo> findDbConfigListPage(JqGridPage<TblSystemDbConfigVo> baseEntityJqGridPage, TblSystemDbConfigVo tblSystemDbConfigVo,HttpServletRequest request);

    void deleteDbConfig(long id);

    void updateDbConfig(TblSystemDbConfig tblSystemDbConfig);
}

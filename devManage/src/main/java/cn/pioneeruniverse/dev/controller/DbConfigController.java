package cn.pioneeruniverse.dev.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.pioneeruniverse.common.constants.Constants;
import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.entity.JqGridPage;
import cn.pioneeruniverse.dev.entity.TblSystemDbConfig;
import cn.pioneeruniverse.dev.entity.TblSystemDbConfigVo;
import cn.pioneeruniverse.dev.entity.TblSystemInfo;
import cn.pioneeruniverse.dev.service.dbconfig.IDbConfigService;

/**
 * @ClassName: DbConfigController
 * @Description: 数据库管理功能
 * @author author
 * @date Sep 7, 2020 14:33:07 AM
 *
 */
@RestController
@RequestMapping("dbConfig")
public class DbConfigController extends BaseController{
	@Autowired
	private IDbConfigService dbConfigService;





	/**
	 *  条件查询所有数据库信息带分页(jqGrid)
	 * @param tblSystemDbConfigVo 参数信息
	 * @param request
	 * @param response
	 * @return JqGridPage<TblSystemDbConfigVo>
	 */
	@RequestMapping(value = "getAllDbConfig", method = RequestMethod.POST)
	public JqGridPage<TblSystemDbConfigVo> getAllDbConfig(TblSystemDbConfigVo tblSystemDbConfigVo, HttpServletRequest request, HttpServletResponse response) {
		JqGridPage<TblSystemDbConfigVo> jqGridPage = null;
		try {
			jqGridPage=dbConfigService.findDbConfigListPage(new JqGridPage<>(request, response),tblSystemDbConfigVo,request);
		} catch (Exception e) {
			super.handleException(e, e.getMessage());
		}
		return jqGridPage;
	}



	/**
	 *  查询当前登陆用户所在项目组系统
	 * @return Map<String,Object> 系统列表
	 */
    @RequestMapping(value = "getSystemByUserId", method = RequestMethod.POST)
	public  Map<String, Object>  getSystemByUserId(HttpServletRequest request){
		Map<String, Object> result = new HashMap<>();

		try {
			List<TblSystemInfo> systemInfos=dbConfigService.getSystemByUserId(request);
			//系统列表信息
            result.put("systemInfos",systemInfos);
            result.put("status", Constants.ITMP_RETURN_SUCCESS);

        }catch (Exception e){
            result.put("status", Constants.ITMP_RETURN_FAILURE);
            result.put("message", "查询失败");
            super.handleException(e, e.getMessage());
        }

		return result;



	}
	/**
	 *  查询系统模块和系统环境
	 * @param systemId 系统id
	 * @return  Map<String, Object> key：status=1成功，2失败
	 */
    @RequestMapping(value = "getEnvAndModuleBySystemId", method = RequestMethod.POST)
	public  Map<String, Object>  getEnvAndModuleBySystemId(long systemId,HttpServletRequest request){
        Map<String, Object> result = new HashMap<>();
        try {
            result =dbConfigService.getEnvAndModuleBySystemId(systemId);
            result.put("status", Constants.ITMP_RETURN_SUCCESS);
        }catch (Exception e){
            result.put("status", Constants.ITMP_RETURN_FAILURE);
            result.put("message", "查询失败");
            super.handleException(e, e.getMessage());
        }
		return  result;


	}

	/**
	 *  新增数据库
	 * @param tblSystemDbConfig 参数
	 * @param request
	 * @return  Map<String, Object> key：status=1成功，2失败
	 */
    @RequestMapping(value = "addDbConfig", method = RequestMethod.POST)
	public  Map<String, Object>   addDbConfig(TblSystemDbConfig tblSystemDbConfig,HttpServletRequest request){
        Map<String, Object> result = new HashMap<>();
        try {
        	if(tblSystemDbConfig.getUrl()!=null){
        		String url=tblSystemDbConfig.getUrl();
        		url=url.replaceAll("&amp;","&");
        		tblSystemDbConfig.setUrl(url);
			}
            dbConfigService.addDbConfig(tblSystemDbConfig);
            result.put("status", Constants.ITMP_RETURN_SUCCESS);
        }catch (Exception e){
            result.put("status", Constants.ITMP_RETURN_FAILURE);
            result.put("message", "查询失败");
            super.handleException(e, e.getMessage());
        }
        return  result;



	}


	/**
	 *  删除数据库
	 * @param id 数据库id
	 * @param request
	 * @return  Map<String, Object> key：status=1成功，2失败
	 */
    @RequestMapping(value = "deleteDbConfig", method = RequestMethod.POST)
	public   Map<String, Object>  deleteDbConfig(long id,HttpServletRequest request){
        Map<String, Object> result = new HashMap<>();
        try {
            dbConfigService.deleteDbConfig(id);
            result.put("status", Constants.ITMP_RETURN_SUCCESS);

        }catch (Exception e){
            result.put("status", Constants.ITMP_RETURN_FAILURE);
            result.put("message", "删除失败");
            super.handleException(e, e.getMessage());
        }
        return  result;

	}

	/**
	 *  更新数据库
	 * @param tblSystemDbConfig 参数
	 * @param request
	 * @return  Map<String, Object> key：status=1成功，2失败
	 */
    @RequestMapping(value = "updateDbConfig", method = RequestMethod.POST)
	public   Map<String, Object>   updateDbConfig(TblSystemDbConfig tblSystemDbConfig,HttpServletRequest request){
        Map<String, Object> result = new HashMap<>();
        try {
			if(tblSystemDbConfig.getUrl()!=null){
				String url=tblSystemDbConfig.getUrl();
				url=url.replaceAll("&amp;","&");
				tblSystemDbConfig.setUrl(url);
			}
            dbConfigService.updateDbConfig(tblSystemDbConfig);
            result.put("status", Constants.ITMP_RETURN_SUCCESS);
        }catch (Exception e){
            result.put("status", Constants.ITMP_RETURN_FAILURE);
            result.put("message", "更新失败");
            super.handleException(e, e.getMessage());
        }
        return  result;
	}

}

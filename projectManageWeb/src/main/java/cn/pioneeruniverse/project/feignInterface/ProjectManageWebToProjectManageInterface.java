package cn.pioneeruniverse.project.feignInterface;

import cn.pioneeruniverse.project.entity.TblRequirementInfo;
import cn.pioneeruniverse.project.feignFallback.ProjectManageWebToProjectManageFallback;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
/**
 *
 * @ClassName: ProjectManageWebToProjectManageInterface
 * @Description: projectManagemWeb模块请求projectManage模块微服务接口
 * @author author
 * @date 2020年8月24日 15:14:25
 *
 */
@FeignClient(value="projectManage",fallbackFactory= ProjectManageWebToProjectManageFallback.class)
public interface ProjectManageWebToProjectManageInterface {

	/**
	 *
	 * @Title: toEditRequirementById
	 * @Description: 根据id获取需求信息
	 * @author author
	 * @param rIds 需求ID
	 * @return map
	 * @throws
	 */
	@RequestMapping(value="requirement/toEditRequirementById",method=RequestMethod.POST)
	Map<String, Object> toEditRequirementById(@RequestParam("rIds") Long rIds);

	/**
	 *
	 * @Title: getExcelRequirement
	 * @Description: 导出需求
	 * @author author
	 * @param findRequirment 查询条件
	 * @param uid 用户ID
	 * @param roleCodes 角色权限
	 * @return 
	 * @throws
	 */
	@RequestMapping(value="requirement/getExcelRequirement",method=RequestMethod.POST)
	List<TblRequirementInfo> getExcelRequirement(@RequestParam("findRequirment") String findRequirment,
                                                 @RequestParam("uid") Long uid, @RequestBody List<String> roleCodes);

	/**
	 *
	 * @Title: getRequirementsByIds
	 * @Description: 获取需求编号
	 * @author author
	 * @param reqIds 需求ID集合
	 * @return List<String>
	 * @throws
	 */
	@RequestMapping(value="requirement/getRequirementsByIds",method=RequestMethod.POST)
	List<String> getRequirementsByIds(@RequestParam("reqIds") String reqIds);

	/**
	 *
	 * @Title: findRequirementField
	 * @Description: 获取需求自定义字段
	 * @author author
	 * @param id 需求ID
	 * @return List<String>
	 * @throws
	 */
	@RequestMapping(value="requirement/getRequirementFiled2",method=RequestMethod.POST)
	Map<String, Object> findRequirementField(@RequestParam("id") Long id);

	/**
	 *
	 * @Title: getsystems
	 * @Description: 获取系统
	 * @author author
	 * @param id 需求ID
	 * @return List<String>
	 * @throws
	 */
	@RequestMapping(value="requirement/getsystems",method=RequestMethod.POST)
	List<String> getsystems(@RequestParam("id") Long id);

	/**
	 *
	 * @Title: changeCancelStatus
	 * @Description: 运维项目详情
	 * @author author
	 * @param id 项目id
	 * @param type 跳转页面方式
	 * @return map
	 * @throws
	 */
	@RequestMapping(value="oamproject/selectProjectAndUserById",method=RequestMethod.GET)
	Map<String, Object> selectProjectAndUserById(@RequestParam("id")Long id,@RequestParam("type")String type);

	/**
	 *
	 * @Title: selectProjectById
	 * @Description: 项目详情
	 * @author author
	 * @param id 项目id
	 * @return map
	 * @throws
	 */
	@RequestMapping(value="project/selectProjectById",method=RequestMethod.POST)
	Map<String, Object> selectProjectById(@RequestParam("id")Long id);

	/*@RequestMapping(value="requirement/getCountRequirement",method=RequestMethod.POST)
    int getCountRequirement(@RequestParam("requirmentJson") String requirmentJson);*/

	//根据id查询需求信息
	/*@RequestMapping(value="requirement/findRequirementById",method=RequestMethod.POST)
	Map<String,Object> findRequirementById(@RequestParam("rId")Long rId,@RequestParam("parentId")Long parentId);*/

	/*@RequestMapping(value="requirement/getDataDicList",method=RequestMethod.POST)
	List<TblDataDic> getDataDicList(@RequestParam("datadictype")String datadictype);*/
}

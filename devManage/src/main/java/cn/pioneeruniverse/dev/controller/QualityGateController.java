package cn.pioneeruniverse.dev.controller;

import cn.pioneeruniverse.common.controller.BaseController;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.dev.entity.TblQualityGate;
import cn.pioneeruniverse.dev.entity.TblQualityGateDetail;
import cn.pioneeruniverse.dev.entity.TblQualityMetric;
import cn.pioneeruniverse.dev.service.qualityGate.IQualityGateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
* @ClassName: QualityGateController
* @Description: 质量门禁（未用）
* @author author
* @date 2020年8月16日 下午7:24:14
*
 */
@RestController
@RequestMapping("quality")
public class QualityGateController extends BaseController {
	@Autowired
	private IQualityGateService qualityGateService;

	/**
	 * 获取质量门禁
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="getQualityGate",method=RequestMethod.POST)
	public Map<String,Object> getQualityGate(HttpServletRequest request) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			List<TblQualityGate> list=qualityGateService.getQualityGate();
			result.put("rows",list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 增加质量门禁
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="addQualityGate",method=RequestMethod.POST)
	public Map<String,Object> addQualityGate(TblQualityGate tblQualityGate,HttpServletRequest request) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			tblQualityGate.setStatus(1);
			tblQualityGate.setCreateBy(CommonUtil.getCurrentUserId(request));
			tblQualityGate.setCreateDate(new Timestamp(new Date().getTime()));
			qualityGateService.addQualityGate(tblQualityGate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 删除质量门禁
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="deleteQualityGate",method=RequestMethod.POST)
	public Map<String,Object> deleteQualityGate(long id,HttpServletRequest request) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			qualityGateService.deleteQualityGate(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	/**
	 * 获取质量门禁详细
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="getQualityGateDetail",method=RequestMethod.POST)
	public Map<String,Object> getQualityGateDetail(Long id,HttpServletRequest request) {
		Map<String,Object> result = new HashMap<String,Object>();
		List<Map<String,Object>> list=qualityGateService.getQualityGateDetail(id);
		result.put("rows",list);
		return result;
	}

	/**
	 * 增加质量门禁详细
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="addQualityGateDetail",method=RequestMethod.POST)
	public Map<String,Object> addQualityGateDetail(TblQualityGateDetail tblQualityGateDetail, HttpServletRequest request) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			tblQualityGateDetail.setCreateBy(CommonUtil.getCurrentUserId(request));
			tblQualityGateDetail.setCreateDate(new Timestamp(new Date().getTime()));
			tblQualityGateDetail.setStatus(1);
			qualityGateService.addQualityGateDetail(tblQualityGateDetail);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	/**
	 * 修改质量门禁详细
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="updateQualityGateDetail",method=RequestMethod.POST)
	public Map<String,Object> updateQualityGateDetail(TblQualityGateDetail tblQualityGateDetail, HttpServletRequest request) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			qualityGateService.updateQualityGateDetail(tblQualityGateDetail);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 删除质量门禁详细
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="deleteQualityGateDetail",method=RequestMethod.POST)
	public Map<String,Object> deleteQualityGateDetail(long id, HttpServletRequest request) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			qualityGateService.deleteQualityGateDetail(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 获取指标
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="getMetrics",method=RequestMethod.POST)
	public Map<String,Object> getMetrics(HttpServletRequest request) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			List<TblQualityMetric> list=qualityGateService.getMetrics();
			result.put("rows",list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	/**
	 * 绑定质量门禁
	 * @param request
	 * @return Map<String,Object>
	 */
	@RequestMapping(value="bindQualityGate",method=RequestMethod.POST)
	public Map<String,Object> bindQualityGate(long systemId,Long qualityGateId,HttpServletRequest request) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			qualityGateService.bindQualityGate(systemId,qualityGateId,request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}

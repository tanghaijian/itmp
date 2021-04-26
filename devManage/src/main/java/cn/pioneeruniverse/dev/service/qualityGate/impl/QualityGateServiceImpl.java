package cn.pioneeruniverse.dev.service.qualityGate.impl;
import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.dev.dao.mybatis.TblQualityGateDetailMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblQualityGateMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblQualityGateSystemMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblQualityMetricMapper;
import cn.pioneeruniverse.dev.entity.TblQualityGate;
import cn.pioneeruniverse.dev.entity.TblQualityGateDetail;
import cn.pioneeruniverse.dev.entity.TblQualityGateSystem;
import cn.pioneeruniverse.dev.entity.TblQualityMetric;
import cn.pioneeruniverse.dev.service.qualityGate.IQualityGateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class QualityGateServiceImpl implements IQualityGateService {
	@Autowired
	private TblQualityGateMapper tblQualityGateMapper;
	@Autowired
	private TblQualityGateDetailMapper tblQualityGateDetailMapper;
	@Autowired
	private TblQualityMetricMapper tblQualityMetricMapper;
	@Autowired
	private TblQualityGateSystemMapper tblQualityGateSystemMapper;


	/**
	 * 
	* @Title: getQualityGate
	* @Description: 获取扫描门禁信息
	* @author author
	* @return List<TblQualityGate>
	 */
	@Override
	public List<TblQualityGate> getQualityGate() {
		Map<String,Object> param=new HashMap<>();
		param.put("status",1);
		List<TblQualityGate> qualityGates= null;
		try {
			qualityGates = tblQualityGateMapper.selectByMap(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return qualityGates;
	}
	@Override
	public void addQualityGate(TblQualityGate tblQualityGate) {
		try {
			tblQualityGateMapper.insertSelective(tblQualityGate);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteQualityGate(long id) {
		TblQualityGate tblQualityGate=tblQualityGateMapper.selectById(id);
		tblQualityGate.setStatus(2);
		try {
			tblQualityGateMapper.updateByPrimaryKey(tblQualityGate);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addQualityGateDetail(TblQualityGateDetail tblQualityGateDetail) {
		tblQualityGateDetail.setStatus(1);
		tblQualityGateDetailMapper.insertSelective(tblQualityGateDetail);
	}

	@Override
	public void updateQualityGateDetail(TblQualityGateDetail tblQualityGateDetail) {
		tblQualityGateDetailMapper.updateByPrimaryKey(tblQualityGateDetail);
	}

	@Override
	public void deleteQualityGateDetail(long id) {
		TblQualityGateDetail tblQualityGateDetail=tblQualityGateDetailMapper.selectById(id);
		tblQualityGateDetail.setStatus(2);
		try {
			tblQualityGateDetailMapper.updateByPrimaryKey(tblQualityGateDetail);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<TblQualityMetric> getMetrics() {
		Map<String,Object> param=new HashMap<>();
		param.put("status",1);
		List<TblQualityMetric> metrics= null;
		try {
			metrics = tblQualityMetricMapper.selectByMap(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return metrics;
	}

	@Override
	public void bindQualityGate(long systemId, Long qualityGateId, HttpServletRequest request) {
		//先解绑
		Map<String,Object> param=new HashMap<>();
		param.put("status",1);
		param.put("systemId",systemId);
		try {
			List<TblQualityGateSystem>  tblQualityGateSystems=tblQualityGateSystemMapper.selectByMap(param);
			if(tblQualityGateSystems!=null && tblQualityGateSystems.size()>0){
				TblQualityGateSystem tblQualityGateSystem=tblQualityGateSystems.get(0);
				tblQualityGateSystem.setStatus(2);
				tblQualityGateSystemMapper.updateByPrimaryKey(tblQualityGateSystem);
				//绑定新质量门禁
				TblQualityGateSystem tblQualityGateSystemNew=new TblQualityGateSystem();
				tblQualityGateSystemNew.setCreateDate(new Timestamp(new Date().getTime()));
				tblQualityGateSystemNew.setCreateBy(CommonUtil.getCurrentUserId(request));
				tblQualityGateSystemNew.setQualityGateId(qualityGateId);
				tblQualityGateSystemNew.setStatus(1);
				tblQualityGateSystemMapper.insertSelective(tblQualityGateSystemNew);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<Map<String, Object>> getQualityGateDetail(Long id) {
		List<Map<String, Object>> maps=new ArrayList<>();
		try {
			 maps=tblQualityGateDetailMapper.getQualityGateDetail(id);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return maps;
	}


}

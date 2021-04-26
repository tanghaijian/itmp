package cn.pioneeruniverse.project.service.requirementsystem.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSONObject;
import cn.pioneeruniverse.project.common.SynRequirementSystemUtil;
import cn.pioneeruniverse.project.dao.mybatis.RequirmentSystemMapper;
import cn.pioneeruniverse.project.entity.TblRequirementSystem;
import cn.pioneeruniverse.project.service.requirementsystem.RequirementSystemService;
import cn.pioneeruniverse.project.vo.SynRequirementSystem;

@Transactional(readOnly = true)
@Service("requirementSystemService")
public class RequirementSystemServiceImpl implements RequirementSystemService {
	
	@Autowired
	private RequirmentSystemMapper reqSystemMapper;
	
	/**
	 * 
	* @Title: updateReqSystemData
	* @Description: 更新需求和系统关联关系
	* @author author
	* @param reqSystemList 需求关联系统列表
	* @param reqId 需求id
	 */
	@Override
	@Transactional(readOnly = false)
	public void updateReqSystemData(String reqSystemList,String reqId) {
		reqSystemMapper.deleteReqSystem(Long.valueOf(reqId));
        List<SynRequirementSystem> resultList = JSONObject.parseArray(reqSystemList, SynRequirementSystem.class);
        for (SynRequirementSystem synReqSystem : resultList) {
        	TblRequirementSystem trs = SynRequirementSystemUtil.SynTblRequirementSystem(synReqSystem);
			trs.setRequirementId(Long.valueOf(reqId));
			if(trs.getSystemId()==null){
				trs.setStatus(1);
				trs.setCreateBy((long) 1);
				trs.setLastUpdateBy((long) 1);
				trs.setCreateDate(new Timestamp(new Date().getTime()));
				trs.setLastUpdateDate(new Timestamp(new Date().getTime()));
				reqSystemMapper.insertReqSystem(trs);
			}else{
				TblRequirementSystem trs1 = reqSystemMapper.selectReqSystemByReqSystem(trs);
				if(trs1!=null&&trs1.getId()!=null){
					trs.setId(trs1.getId());
					trs.setStatus(1);
					trs.setLastUpdateBy((long) 1);
					trs.setLastUpdateDate(new Timestamp(new Date().getTime()));
					reqSystemMapper.updateReqSystem(trs);
				}else {
					trs.setStatus(1);
					trs.setCreateBy((long) 1);
					trs.setLastUpdateBy((long) 1);
					trs.setCreateDate(new Timestamp(new Date().getTime()));
					trs.setLastUpdateDate(new Timestamp(new Date().getTime()));
					reqSystemMapper.insertReqSystem(trs);
				}
			}
        }
	}

	/**
	 * 
	* @Title: getFunctionCountByReqId
	* @Description: 获取功能点数
	* @author author
	* @param requirementId 需求id
	* @return
	 */
	@Override
	public int getFunctionCountByReqId(Long requirementId) {		
		return reqSystemMapper.getFunctionCountByReqId(requirementId);
	}
}

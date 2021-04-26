package cn.pioneeruniverse.dev.service.workRemark.Impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.dev.dao.mybatis.TblDevTaskAttachementMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblDevTaskLogAttachementMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblDevTaskLogMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblDevTaskRemarkAttachementMapper;
import cn.pioneeruniverse.dev.dao.mybatis.TblDevTaskRemarkMapper;
import cn.pioneeruniverse.dev.entity.TblDevTaskAttachement;
import cn.pioneeruniverse.dev.entity.TblDevTaskLog;
import cn.pioneeruniverse.dev.entity.TblDevTaskLogAttachement;
import cn.pioneeruniverse.dev.entity.TblDevTaskRemark;
import cn.pioneeruniverse.dev.entity.TblDevTaskRemarkAttachement;
import cn.pioneeruniverse.dev.service.workRemark.workTaskRemark;

/**
* @author author
* @Description
* @Date 2020/9/4
* @return
**/
@Service("workTaskRemarkImpl")
public class WorkTaskRemarkImpl implements workTaskRemark{
	@Autowired
    private TblDevTaskRemarkMapper tblDevTaskRemarkMapper;
	@Autowired
    private TblDevTaskRemarkAttachementMapper tblDevTaskRemarkAttachementMapper;
	@Autowired
	private TblDevTaskLogMapper tblDevTaskLogMapper;
	@Autowired
	private TblDevTaskLogAttachementMapper tblDevTaskLogAttachementMapper;
	@Autowired
	private TblDevTaskAttachementMapper tblDevTaskAttachementMapper;
	@Autowired
    private RedisUtils redisUtils;
	
	@Override
	public void addTaskRemark(String remark, Long userId, String userName, String userAccount,List<TblDevTaskRemarkAttachement> files) {
		TblDevTaskRemark tblDevTaskRemark=JSON.parseObject(remark, TblDevTaskRemark.class);
		tblDevTaskRemark.setCreateBy(userId);
		tblDevTaskRemark.setUserId(userId);
		tblDevTaskRemark.setUserName(userName);
		tblDevTaskRemark.setUserAccount(userAccount);
		tblDevTaskRemarkMapper.addTaskRemark(tblDevTaskRemark);
		Long id=tblDevTaskRemark.getId();
		
		if(null!=files&&files.size() > 0) {
		for(int i=0;i<files.size();i++) {
			files.get(i).setCreateBy(userId);
			files.get(i).setDevTaskRemarkId(id);
		}
		tblDevTaskRemarkAttachementMapper.addRemarkAttachement(files);
		}
		
	}

	@Override
	public List<TblDevTaskRemark> selectRemark(String testTaskId) {
		
		List<TblDevTaskRemark> list=tblDevTaskRemarkMapper.selectRemark(Long.parseLong(testTaskId));
		
		return list;
	}

	@Override
	public List<TblDevTaskRemarkAttachement> findTaskRemarkAttchement(List<Long> ids) {
		return tblDevTaskRemarkAttachementMapper.findTaskRemarkAttachement(ids);
	}

	@Override
	public List<TblDevTaskLog> findLogList(Long id) {
		List<TblDevTaskLog> list=tblDevTaskLogMapper.selectByPrimaryKey(id);
		return list;
	}

	@Override
	public List<TblDevTaskLogAttachement> findLogAttachement(List<Long> ids) {
		List<TblDevTaskLogAttachement> list=tblDevTaskLogAttachementMapper.findTaskLogAttachement(ids);
		return list;
	}

	/**
	* @author author
	* @Description 查询所有日志
	* @Date 2020/9/4
	* @param testId
	* @return java.util.List<cn.pioneeruniverse.dev.entity.TblDevTaskLogAttachement>
	**/
	@Override
	public List<TblDevTaskLogAttachement> findTestLogFile(Long testId) {
		
		return tblDevTaskLogAttachementMapper.findTestLogFile(testId);
	}

	@Override
	public List<TblDevTaskAttachement> findAttachement(Long testId) {
		List<TblDevTaskAttachement> list=tblDevTaskAttachementMapper.selectByPrimaryKey(testId);
		return list;
	}

	@Override
	public void updateNo(List<TblDevTaskAttachement> list, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		map.put("list", list);
		map.put("lastUpdateBy", CommonUtil.getCurrentUserId(request));
		tblDevTaskAttachementMapper.updateNo(map);
	}
	
}

package cn.pioneeruniverse.dev.service.notice.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;

import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.CommonUtils;
import cn.pioneeruniverse.common.utils.DateUtil;
import cn.pioneeruniverse.dev.dao.mybatis.NoticeInfoDao;
import cn.pioneeruniverse.dev.dao.mybatis.TblProjectInfoMapper;
import cn.pioneeruniverse.dev.entity.TblNoticeInfo;
import cn.pioneeruniverse.dev.entity.TblProjectInfo;
import cn.pioneeruniverse.dev.service.notice.INoticeService;
import cn.pioneeruniverse.dev.service.worktask.WorkTaskService;


/**
 * 公告实现类
 * @author zhoudu
 *
 */
@Service("iNoticeService")
public class NoticeServiceImpl extends ServiceImpl<NoticeInfoDao, TblNoticeInfo> implements INoticeService {

	@Autowired
	private NoticeInfoDao noticeInfoDao;
	@Autowired
	private TblProjectInfoMapper tblProjectInfoMapper;
	
	@Autowired 
	private WorkTaskService workTaskService;

	/**
	 * 
	* @Title: getAllNotice
	* @Description: 获取所有公告列表内容
	* @author author
	* @param request
	* @param notice 查询条件
	* @param pageIndex 第几页
	* @param pageSize 每页大小
	* @param type 1获取新建类项目公告，其他所有公告
	* @param programId  项目id，形如1,2,3
	* @return Map<String, Object>
	 */
	@Override
	public Map<String, Object> getAllNotice(HttpServletRequest request, TblNoticeInfo notice, Integer pageIndex, Integer pageSize,Integer type,String programId) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		Boolean isAdmin = new CommonUtils().currentUserWithAdmin(request);
		if (!isAdmin) {
			List<TblProjectInfo> projectList = workTaskService.findProjectByUser(request);
			List<Long> projectIdList = new ArrayList<Long>();
			for (TblProjectInfo tblProjectInfo : projectList) {
				projectIdList.add(tblProjectInfo.getId());
			}
			if (projectIdList.size() > 0) {
				notice.setCurrentUserProjectList(projectIdList);
			}
			notice.setNoticeType(2);
		}
		
		if (pageIndex != null && pageSize != null) {//分页查询
			List<TblNoticeInfo> list = null;
			PageInfo<TblNoticeInfo> pageInfo = null;
			PageHelper.startPage(pageIndex, pageSize);
			if (type != null && programId != null && type == 1){
				List<String> programList = new ArrayList<>();
				String[] split = programId.split(",");
				for (int i=0;i<split.length;i++){
					programList.add(split[i]);
				}
				list = noticeInfoDao.getProgramNotice(programList);
				pageInfo = new PageInfo<TblNoticeInfo>(list);
			}else{
				list = noticeInfoDao.getAllNotice(notice);
				pageInfo = new PageInfo<TblNoticeInfo>(list);
			}
			//返回的数据
			result.put("rows", list);
			//总数
			result.put("records", pageInfo.getTotal());
			//总页数
			result.put("total", pageInfo.getPages());
			//第几页
			result.put("page", pageIndex < pageInfo.getPages() ? pageIndex : pageInfo.getPages());
		} else {
			result.put("rows", noticeInfoDao.getAllNotice(notice));
		}
		return result;
	}


	/**
	 * 
	* @Title: selectNoticeById
	* @Description: 根据id获取公告信息
	* @author author
	* @param id 公告id
	* @return TblNoticeInfo
	 */
	@Override
	public TblNoticeInfo selectNoticeById(Long id) {
		TblNoticeInfo notice = noticeInfoDao.selectByPrimaryKey(id);
		if (StringUtil.isNotEmpty(notice.getProjectIds())) {
			String[] idArr = notice.getProjectIds().split(",");
			List<Long> idList = new ArrayList<Long>();
			for (String idStr : idArr) {
				if (StringUtil.isNotEmpty(idStr)) {
					idList.add(Long.parseLong(idStr));
				}
			}
			List<TblProjectInfo> projectList = tblProjectInfoMapper.selectBatchIds(idList);
			String projectNames = "";
			for (TblProjectInfo tblProjectInfo : projectList) {
				projectNames += tblProjectInfo.getProjectName() + ",";
			}
			if (StringUtil.isNotEmpty(projectNames)) {
				projectNames = projectNames.substring(0, projectNames.length() - 1);
				notice.setProjectNames(projectNames);
			}
		}
		return notice;
	}

	/**
	 * 
	* @Title: insertNotice
	* @Description: 新增公告
	* @author author
	* @param request
	* @param notice 公告信息
	 */
	@Override
	public void insertNotice(HttpServletRequest request, TblNoticeInfo notice) {
		CommonUtil.setBaseValue(notice, request);
		noticeInfoDao.insert(notice);
	}

	/**
	 * 
	* @Title: updateNotice
	* @Description: 更新公告
	* @author author
	* @param request
	* @param notice 公告信息
	 */
	@Override
	public void updateNotice(HttpServletRequest request, TblNoticeInfo notice) {
		Timestamp stamp = new Timestamp(System.currentTimeMillis());
		Long userId = CommonUtil.getCurrentUserId(request);
		notice.setLastUpdateDate(stamp);
		notice.setLastUpdateBy(userId);
		noticeInfoDao.updateByPrimaryKeySelective(notice);
	}

	/**
	 * 
	* @Title: deleteNotice
	* @Description: 删除公告
	* @author author
	* @param request
	* @param notice 公告信息
	 */
	@Override
	public void deleteNotice(HttpServletRequest request, TblNoticeInfo notice) {
		Timestamp stamp = new Timestamp(System.currentTimeMillis());
		Long userId = CommonUtil.getCurrentUserId(request);
		//逻辑删除
		notice.setStatus(2);
		notice.setLastUpdateDate(stamp);
		notice.setLastUpdateBy(userId);
		noticeInfoDao.updateByPrimaryKeySelective(notice);
	}

	/**
	 * 
	* @Title: deleteNoticeList
	* @Description: 批量删除公告
	* @author author
	* @param request
	* @param ids 多个公告id
	 */
	@Override
	public void deleteNoticeList(HttpServletRequest request, String ids) {
		Timestamp stamp = new Timestamp(System.currentTimeMillis());
		Long userId = CommonUtil.getCurrentUserId(request);
		if (StringUtil.isNotEmpty(ids)) {
			String[] idArr = ids.split(",");
			for (String id : idArr) {
				if (StringUtil.isNotEmpty(id)) {
					TblNoticeInfo notice = new TblNoticeInfo();
					notice.setId(Long.parseLong(id));
					//逻辑删除
					notice.setStatus(2);
					notice.setLastUpdateDate(stamp);
					notice.setLastUpdateBy(userId);
					noticeInfoDao.updateByPrimaryKeySelective(notice);
				}
			}
		}
	}

	/**
	 * 
	* @Title: getAllValidSystemNotice
	* @Description: 获取系统类公告
	* @author author
	* @param request
	* @return List<TblNoticeInfo>
	 */
	@Override
	public List<TblNoticeInfo> getAllValidSystemNotice(HttpServletRequest request) {
		String currentTime = DateUtil.formatDate(new Date());
		TblNoticeInfo notice = new TblNoticeInfo();
		//1系统类，2项目类
		notice.setNoticeType(1);
		notice.setStatus(1);
		
		EntityWrapper<TblNoticeInfo> wrapper = new EntityWrapper<TblNoticeInfo>(notice);
		wrapper.le("START_TIME", currentTime).ge("END_TIME", currentTime);
		
		List<TblNoticeInfo> list = noticeInfoDao.selectList(wrapper);
		return list;
	}
	
	/**
	 * 
	* @Title: getAllValidProjectNotice
	* @Description: 获取有效项目公告
	* @author author
	* @param request
	* @return List<TblNoticeInfo>
	 */
	@Override
	public List<TblNoticeInfo> getAllValidProjectNotice(HttpServletRequest request) {
		String currentTime = DateUtil.formatDate(new Date());
		Long userId = CommonUtil.getCurrentUserId(request);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("currentTime", currentTime);
		
		List<TblNoticeInfo> list = noticeInfoDao.getAllValidProjectNotice(map);
		return list;
	}



}


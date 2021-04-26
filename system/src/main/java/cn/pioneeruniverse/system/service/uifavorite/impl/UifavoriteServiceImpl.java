package cn.pioneeruniverse.system.service.uifavorite.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.pioneeruniverse.common.utils.CommonUtil;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.system.dao.mybatis.uifavorite.UifavoriteDao;
import cn.pioneeruniverse.system.entity.UiFavorite;
import cn.pioneeruniverse.system.service.uifavorite.UiFavoriteService;

/**
 * 
* @ClassName: UifavoriteServiceImpl
* @Description: 搜索条件收藏方案实现类
* @author author
* @date 2020年9月3日 上午10:03:05
*
 */
@Service
@Transactional(readOnly = true)
public class UifavoriteServiceImpl implements UiFavoriteService{
	@Autowired
	private UifavoriteDao uifavoriteDao;
	@Autowired
	private RedisUtils redisUtils;

	/**
	 * 
	* @Title: addAndUpdate
	* @Description: 新增或修改收藏方案并将其放入redis中
	*               redis格式 key：   TBL_UI_FAVORIITE 
	*                        value:"json格式其中：
								    key=用户ID
								    value=数组
								        数组中是json，
								    key=页面url
								    value=页面收藏的内容，一般为查询条件"
	* @author author
	* @param uiFavorite 收藏方案
	* @param request
	 */
	@Override
	@Transactional(readOnly = false)
	public void addAndUpdate(UiFavorite uiFavorite, HttpServletRequest request) {
		Long uid = CommonUtil.getCurrentUserId(request);
		//原有的收藏方案
		UiFavorite uiFavorite2 = uifavoriteDao.findByMenuUrlAndUserId(uiFavorite);
		//更新
		if (uiFavorite2!=null) {
			uiFavorite.setLastUpdateBy(uid);
			uiFavorite.setLastUpdateDate(new Timestamp(new Date().getTime()));
			uifavoriteDao.update(uiFavorite);
		}else {
			//新增
			uiFavorite.setCreateBy(uid);
			uiFavorite.setCreateDate(new Timestamp(new Date().getTime()));
			uifavoriteDao.insert(uiFavorite);
		}
		//取出redis是否已经包含当前登录uid的list 如果有往list里添加收藏信息
		Map<String, Object> object = (Map<String, Object>) redisUtils.get("TBL_UI_FAVORIITE");
		//redis中已经存在，则更新
		if (object!=null && object.size()>0) {
			if (!object.containsKey(uid.toString())) {
				List<Map<String, Object>> list = new ArrayList<>();
				Map<String, Object> infoMap = new HashMap<>();
				infoMap.put(uiFavorite.getMenuUrl(), uiFavorite.getFavoriteContent());
				list.add(infoMap);
				object.put(uid.toString(), list);
			}else {
				List<Map<String, Object>> list = new ArrayList<>();
				list =  (List<Map<String, Object>>)object.get(uid.toString());
				if (list!=null&&list.size()>0) {
					Boolean flag = false;
					for (Map<String, Object> infomap : list) {
						 flag = false;
						if (infomap.containsKey(uiFavorite.getMenuUrl())) {
							flag = true;
							infomap.put(uiFavorite.getMenuUrl(), uiFavorite.getFavoriteContent());
						}
					}
					if(!flag) {
						Map<String, Object> infomap2 = new HashMap<>();
						infomap2.put(uiFavorite.getMenuUrl(), uiFavorite.getFavoriteContent());
						list.add(infomap2);
					}
					object.put(uid.toString(), list);
				}else {
					Map<String, Object> infomap = new HashMap<>();
					infomap.put(uiFavorite.getMenuUrl(), uiFavorite.getFavoriteContent());
					list.add(infomap);
					object.put(uid.toString(), list);
				}
			}
		}else {
			//redis中不存在则新增
			object = new HashMap<>();
			List<Map<String, Object>> list = new ArrayList<>();
			Map<String, Object> infoMap = new HashMap<>();
			infoMap.put(uiFavorite.getMenuUrl(), uiFavorite.getFavoriteContent());
			list.add(infoMap);
			object.put(uid.toString(), list);
		}
		redisUtils.set("TBL_UI_FAVORIITE",object);
		
	}

}

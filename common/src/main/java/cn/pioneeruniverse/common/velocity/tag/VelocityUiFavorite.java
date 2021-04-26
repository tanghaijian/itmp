package cn.pioneeruniverse.common.velocity.tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.common.utils.SpringContextHolder;

/**
 * 页面收藏
 * 
 * @author:tingting
 * @version:2019年1月28日 下午4:33:21
 */
public class VelocityUiFavorite {

	private static RedisUtils redisUtils = SpringContextHolder.getBean(RedisUtils.class);
	private static final Logger logger = LoggerFactory.getLogger(VelocityAuthority.class);

	public String getFavoriteContent(String token, String menuUrl) {
		String favoriteContent = "null";
		try {
			Object redisUser = redisUtils.get(token);
			String uid = "";
			if (redisUser != null) {
				Map<String, Object> user = (Map<String, Object>) redisUser;
				uid = user.get("id").toString();
			}
			Map<String, Object> object = (Map<String, Object>) redisUtils.get("TBL_UI_FAVORIITE");
			if (object != null) {
				List<Map<String, Object>> list = (List<Map<String, Object>>) object.get(uid);
				if (list != null && list.size() > 0) {
					for (Map<String, Object> innerMap : list) {
						if (innerMap.containsKey(menuUrl)) {
							favoriteContent = (String) innerMap.get(menuUrl);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取页面收藏失败！");
		}
		return favoriteContent;
	}
}

package cn.pioneeruniverse.dev.service.notice;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.pioneeruniverse.dev.entity.TblNoticeInfo;
/**
 *
 * @ClassName: INoticeService
 * @Description: 通告service
 * @author author
 *
 */
public interface INoticeService {
	/**
	 * 查询
	 * @param request
	 * @param notice
	 * @param pageIndex
	 * @param pageSize
	 * @param type
	 * @param programId
	 * @return Map<String, Object>
	 */

	Map<String, Object> getAllNotice(HttpServletRequest request, TblNoticeInfo notice, Integer pageIndex, Integer pageSize,Integer type,String programId);

	TblNoticeInfo selectNoticeById(Long id);

	/**
	 * 插入
	 * @param request
	 * @param message
	 */

	void insertNotice(HttpServletRequest request, TblNoticeInfo message);

	void updateNotice(HttpServletRequest request, TblNoticeInfo message);
	
	void deleteNotice(HttpServletRequest request, TblNoticeInfo message);
	
	void deleteNoticeList(HttpServletRequest request, String ids);

	List<TblNoticeInfo> getAllValidSystemNotice(HttpServletRequest request);

	List<TblNoticeInfo> getAllValidProjectNotice(HttpServletRequest request);


}

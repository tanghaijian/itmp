package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblNoticeInfo;
import org.apache.ibatis.annotations.Param;
/**
 *
 * @ClassName: NoticeInfoDao
 * @Description: 通知dao接口
 * @author author
 *
 */
public interface NoticeInfoDao extends BaseMapper<TblNoticeInfo> {

	/**
	 * 插入
	 * @param record
	 * @return
	 */
	Integer insert(TblNoticeInfo record);

	int insertSelective(TblNoticeInfo record);

	/**
	 * 查询
	 * @param id
	 * @return
	 */
	TblNoticeInfo selectByPrimaryKey(Long id);

	/**
	 * 更新
	 * @param record
	 * @return
	 */
	int updateByPrimaryKeySelective(TblNoticeInfo record);

	int updateByPrimaryKey(TblNoticeInfo record);
	
	int deleteByPrimaryKey(Long id);
	
	List<TblNoticeInfo> getAllNotice(TblNoticeInfo notice);

	List<TblNoticeInfo> getAllValidProjectNotice(Map<String, Object> map);

	//主页项目公告信息
	List<TblNoticeInfo> getProgramNotice(@Param("programId") List<String> programId);

}
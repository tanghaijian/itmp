package cn.pioneeruniverse.system.dao.mybatis.message;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import cn.pioneeruniverse.system.entity.TblMessageInfo;

public interface MessageInfoDao extends BaseMapper<TblMessageInfo>{
	/**
	* @author author
	* @Description 根据id删除消息
	* @Date 2020/9/14
	* @param id
	* @return int
	**/
	int deleteByPrimaryKey(Long id);

	/**
	* @author author
	* @Description 插入消息
	* @Date 2020/9/14
	* @param record
	* @return java.lang.Integer
	**/
	Integer insert(TblMessageInfo record);

	/**
	* @author author
	* @Description 根据选择信息插入消息
	* @Date 2020/9/14
	* @param record
	* @return int
	**/
	int insertSelective(TblMessageInfo record);

	/**
	* @author author
	* @Description 根据id查询消息
	* @Date 2020/9/14
	* @param id
	* @return cn.pioneeruniverse.system.entity.TblMessageInfo
	**/
	TblMessageInfo selectByPrimaryKey(Long id);

	/**
	* @author author
	* @Description 根据选择信息更新消息
	* @Date 2020/9/14
	* @param record
	* @return int
	**/
	int updateByPrimaryKeySelective(TblMessageInfo record);

	/**
	* @author author
	* @Description 根据id更新消息
	* @Date 2020/9/14
	* @param record
	* @return int
	**/
	int updateByPrimaryKey(TblMessageInfo record);

	/**
	* @author author
	* @Description 获取所有消息
	* @Date 2020/9/14
	* @param message
	* @return java.util.List<cn.pioneeruniverse.system.entity.TblMessageInfo>
	**/
	List<TblMessageInfo> getAllMessage(TblMessageInfo message);
}
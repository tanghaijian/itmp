package cn.pioneeruniverse.system.dao.mybatis.messagequeue;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.system.entity.TblMessageQueue;

public interface MessageQueueDao extends BaseMapper<TblMessageQueue>{
    /**
    * @author author
    * @Description 根据ID删除消息
    * @Date 2020/9/14
    * @param id
    * @return int
    **/
    int deleteByPrimaryKey(Long id);

    /**
    * @author author
    * @Description 根据ID插入消息
    * @Date 2020/9/14
    * @param record
    * @return java.lang.Integer
    **/
    Integer insert(TblMessageQueue record);

    /**
    * @author author
    * @Description 根据选择信息插入消息
    * @Date 2020/9/14
    * @param record
    * @return int
    **/
    int insertSelective(TblMessageQueue record);

    /**
    * @author author
    * @Description 根据ID查询消息
    * @Date 2020/9/14
    * @param id
    * @return cn.pioneeruniverse.system.entity.TblMessageQueue
    **/
    TblMessageQueue selectByPrimaryKey(Long id);

    /**
    * @author author
    * @Description 根据选择信息更新消息
    * @Date 2020/9/14
    * @param record
    * @return int
    **/
    int updateByPrimaryKeySelective(TblMessageQueue record);

    /**
    * @author author
    * @Description 删除消息
    * @Date 2020/9/14
    * @param record
    * @return int
    **/
    int updateByPrimaryKey(TblMessageQueue record);
}
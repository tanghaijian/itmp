package cn.pioneeruniverse.system.dao.mybatis.messagequeue;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.system.entity.TblMessageQueueHistory;

public interface MessageQueueHistoryDao extends BaseMapper<TblMessageQueueHistory>{
    /**
    * @author author
    * @Description 删除消息历史
    * @Date 2020/9/14
    * @param id
    * @return int
    **/
    int deleteByPrimaryKey(Long id);

    /**
    * @author author
    * @Description 插入消息历史
    * @Date 2020/9/14
    * @param record
    * @return java.lang.Integer
    **/
    Integer insert(TblMessageQueueHistory record);

    /**
    * @author author
    * @Description 根据选择信息插入消息历史
    * @Date 2020/9/14
    * @param record
    * @return int
    **/
    int insertSelective(TblMessageQueueHistory record);

    /**
    * @author author
    * @Description 根据id查询出消息历史
    * @Date 2020/9/14
    * @param id
    * @return cn.pioneeruniverse.system.entity.TblMessageQueueHistory
    **/
    TblMessageQueueHistory selectByPrimaryKey(Long id);

    /**
    * @author author
    * @Description 根据选择信息更新消息历史
    * @Date 2020/9/14
    * @param record
    * @return int
    **/
    int updateByPrimaryKeySelective(TblMessageQueueHistory record);

    /**
    * @author author
    * @Description 根据ID更新消息历史
    * @Date 2020/9/14
    * @param record
    * @return int
    **/
    int updateByPrimaryKey(TblMessageQueueHistory record);
}
package cn.pioneeruniverse.dev.dao.mybatis;

import cn.pioneeruniverse.dev.entity.TblAttachementInfo;
import com.baomidou.mybatisplus.mapper.BaseMapper;

public interface TblAttachementInfoMapper extends BaseMapper<TblAttachementInfo> {
    /**
    * @author author
    * @Description 物理删除 
    * @Date 2020/9/21
    * @param id
    * @return int
    **/
    int deleteByPrimaryKey(Long id);

    /**
    * @author author
    * @Description 判断新增操作
    * @Date 2020/9/21
    * @param record
    * @return int
    **/
    int insertSelective(TblAttachementInfo record);
    
    /**
    * @author author
    * @Description 根据id查询
    * @Date 2020/9/21
    * @param id
    * @return cn.pioneeruniverse.dev.entity.TblAttachementInfo
    **/
    TblAttachementInfo selectByPrimaryKey(Long id);

    /**
    * @author author
    * @Description 判断修改操作
    * @Date 2020/9/21
    * @param record
    * @return int
    **/
    int updateByPrimaryKeySelective(TblAttachementInfo record);

    /**
    * @author author
    * @Description 根据id修改操作
    * @Date 2020/9/21
    * @param record
    * @return int
    **/
    int updateByPrimaryKey(TblAttachementInfo record);
}
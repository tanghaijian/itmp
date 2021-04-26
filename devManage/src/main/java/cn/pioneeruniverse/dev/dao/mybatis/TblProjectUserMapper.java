package cn.pioneeruniverse.dev.dao.mybatis;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblProjectUser;

public interface TblProjectUserMapper extends BaseMapper<TblProjectUser> {
    int deleteByPrimaryKey(Long id);

    int insertSelective(TblProjectUser record);

    TblProjectUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblProjectUser record);

    /**
    * @author author
    * @Description 根据id修改
    * @Date 2020/9/23
    * @param record
    * @return int
    **/
    int updateByPrimaryKey(TblProjectUser record);
}
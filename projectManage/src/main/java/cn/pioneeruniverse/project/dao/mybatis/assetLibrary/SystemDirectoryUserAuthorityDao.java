package cn.pioneeruniverse.project.dao.mybatis.assetLibrary;

import cn.pioneeruniverse.project.entity.TblSystemDirectoryUserAuthority;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 14:01 2019/12/5
 * @Modified By:
 */
public interface SystemDirectoryUserAuthorityDao extends BaseMapper<TblSystemDirectoryUserAuthority> {

	List<TblSystemDirectoryUserAuthority> selectBySystemDirectoryIdAndUid(@Param("systemDirectoryId")Long id,@Param("userId") Long uid);



}

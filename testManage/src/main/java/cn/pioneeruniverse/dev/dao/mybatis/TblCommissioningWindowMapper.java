package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.pioneeruniverse.dev.entity.TblCommissioningWindow;
import com.baomidou.mybatisplus.mapper.BaseMapper;

public interface TblCommissioningWindowMapper  extends BaseMapper<TblCommissioningWindow> {
    int deleteByPrimaryKey(Long id);

    int insertSelective(TblCommissioningWindow record);

    TblCommissioningWindow selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblCommissioningWindow record);

    int updateByPrimaryKey(TblCommissioningWindow record);

	List<TblCommissioningWindow> getAll();

	String getWindowName(Long commissioningWindowId);

	String getWindowNameById(Long commissioningWindowId);

	/**
	* @author author
	* @Description 查询投产窗口
	* @Date 2020/9/21
	* @param map
	* @return java.util.List<cn.pioneeruniverse.dev.entity.TblCommissioningWindow>
	**/
    List<TblCommissioningWindow> getAllComWindow(Map<String,Object> map);

    /**
    * @author author
    * @Description 统计投产窗口总数
    * @Date 2020/9/21
    * @param map
    * @return int
    **/
    int getAllComWindowTotal(Map<String,Object> map);
    
    List<Long> selectIdBySql(@Param("windowNameSql")String windowNameSql);
}
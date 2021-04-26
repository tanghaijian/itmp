package cn.pioneeruniverse.project.dao.mybatis;

import java.util.List;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import cn.pioneeruniverse.project.entity.TblDataDic;



public interface DataDicDao extends BaseMapper<TblDataDic> {
	
	/**
	* @author author
	* @Description 根据编号获取数据字典
	* @Date 2020/9/3
	* @param termCode
	* @return java.util.List<cn.pioneeruniverse.project.entity.TblDataDic>
	**/
	List<TblDataDic> getDataDicList(String termCode);
}

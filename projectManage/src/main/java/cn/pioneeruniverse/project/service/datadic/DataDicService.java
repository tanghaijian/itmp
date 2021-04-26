package cn.pioneeruniverse.project.service.datadic;

import java.util.List;

import cn.pioneeruniverse.project.entity.TblDataDic;


public interface DataDicService {

	/**
	* @author author
	* @Description 通过编码获取数据字典
	* @Date 2020/9/3
	* @param termCode
	* @return java.util.List<cn.pioneeruniverse.project.entity.TblDataDic>
	**/
	List<TblDataDic> getDataDicList(String termCode);
	
	
}

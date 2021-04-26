package cn.pioneeruniverse.project.service.datadic.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.pioneeruniverse.common.utils.RedisUtils;
import cn.pioneeruniverse.project.dao.mybatis.DataDicDao;
import cn.pioneeruniverse.project.entity.TblDataDic;
import cn.pioneeruniverse.project.service.datadic.DataDicService;

/**
 * @deprecated
* @ClassName: DataDicServiceImpl
* @Description: 未用
* @author author
* @date 2020年8月19日 下午9:23:29
*
 */
@Service("dateDicService")
public class DataDicServiceImpl implements DataDicService {

	@Autowired
	private DataDicDao dateDicDao;
	@Autowired
	public RedisUtils redisUtils;
	/**
	* @author author
	* @Description 通过编码获取数据字典
	* @Date 2020/9/3
	* @param termCode
	* @return java.util.List<cn.pioneeruniverse.project.entity.TblDataDic>
	**/
	@Override
	public List<TblDataDic> getDataDicList(String termCode) {	
		return dateDicDao.getDataDicList(termCode);
	}

}

package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;
import java.util.Map;
/**
 *
 * @ClassName: TblDataDicMapper
 * @Description: 数据字典mapper
 * @author author
 * @date 2020年8月12日 下午16:20
 *
 */
public interface TblDataDicMapper {
	/**
	 * 查询
	 * @param map
	 * @return
	 */
	List<Long> findDploys(Map<String, Object> map);

}

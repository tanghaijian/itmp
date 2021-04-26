package cn.pioneeruniverse.project.dao.mybatis.assetLibrary;

import java.util.List;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import cn.pioneeruniverse.project.entity.TblSystemDocumentType;
/**
 *
 * @ClassName:SystemDocumentType
 * @Description
 * @author author
 * @date 2020年8月24日
 *
 */
public interface SystemDocumentType extends BaseMapper<TblSystemDocumentType>{
	TblSystemDocumentType selectTypeById(Long id);

	List<TblSystemDocumentType> getSomeDocumentType();

	TblSystemDocumentType selectIdByType(String documentType);
}

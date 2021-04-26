package cn.pioneeruniverse.project.dao.mybatis.assetLibrary;

import cn.pioneeruniverse.project.entity.TblSystemDirectoryDocumentHistory;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

public interface TblSystemDirectoryDocumentHistoryMapper extends BaseMapper<TblSystemDirectoryDocumentHistory> {
    int deleteByPrimaryKey(Long id);

    int insertSelective(TblSystemDirectoryDocumentHistory record);

    TblSystemDirectoryDocumentHistory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblSystemDirectoryDocumentHistory record);

    int updateByPrimaryKey(TblSystemDirectoryDocumentHistory record);

    List<Map<String, Object>> getDocumentHistory(long parseLong);

    List<Map<String, Object>> getRequireIdBydocId(long systemDirectoryDocumentId);

    List<Map<String, Object>> getRequireIdBydocIdAndVersion(Map<String, Object> param);
}
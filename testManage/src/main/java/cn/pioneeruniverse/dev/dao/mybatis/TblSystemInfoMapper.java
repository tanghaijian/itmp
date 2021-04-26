package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.pioneeruniverse.dev.entity.TblSystemInfo;
import cn.pioneeruniverse.dev.entity.TblTestTask;

public interface TblSystemInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TblSystemInfo record);

    int insertSelective(TblSystemInfo record);

    TblSystemInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblSystemInfo record);

    int updateByPrimaryKey(TblSystemInfo record);

	List<Map<String, Object>> getAllSystemInfo(Map<String, Object> map);

    List<Map<String, Object>> getAllSystemInfo2(Map<String, Object> map);

	List<Map<String, Object>> getAllSystemInfoModal(Map<String, Object> map);

	List<TblSystemInfo> getAllsystem(TblSystemInfo system);

	List<TblSystemInfo> getAll();

    String getSystemNameById(Long id);

	String getSystemName(Long id);

	List<TblSystemInfo> getSystems();

	List<Long> getSystemIdBySystemName(String value);
	
	List<Long> getSystemIdBySystemName2(@Param("systemName") String systemName);
	
	List<Long> getSystemIdByInjectSql(@Param("systemNameSql")String systemNameSql);
	
	List<TblSystemInfo> getSystemBySystemName(@Param("systemName") String systemName);

    int countGetAllSystemInfo(Map<String,Object> map);

    Long findSystemIdBySystemCode(String systemCode);
    
    /**
     * 获取所有系统
     * @param systemCode
     * @return
     */
    List<TblSystemInfo> selectSystemByCode();
    
    List<TblSystemInfo> selectSystemByOamAndNew(@Param("systemName") String systemName,
    		@Param("systemCode") String systemCode);

    List<TblSystemInfo> selectSystemByAgile(@Param("systemName") String systemName,
                                                @Param("systemCode") String systemCode);
    
    List<TblSystemInfo> selectSystemBySystemClass(@Param("systemClass")Integer systemClass);

    List<TblSystemInfo> selectSystemByDevelopmentMode(@Param("developmentMode")Integer developmentMode);

    /**
     * 批量修改系统类型
     * @param systemIdStr
     * @param systemClass
     */
    void batchUpdateSystemClass(@Param("systemIdStr") String systemIdStr,@Param("systemClass")Integer systemClass);

    /**
     * 批量修改开发模式（1:敏捷;2:非敏捷）
     * @param systemIdStr
     * @param developmentMode
     */
    void batchUpdateDevelopmentMode(@Param("systemIdStr") String systemIdStr,@Param("developmentMode")Integer developmentMode);


    /**
     * 根据系统类型查询系统
     * @param systemClass
     * @return
     */
    String selectSystemByClass(@Param("systemClass")Integer systemClass);
}
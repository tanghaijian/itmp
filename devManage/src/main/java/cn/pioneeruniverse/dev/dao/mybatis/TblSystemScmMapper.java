package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblDataDic;
import cn.pioneeruniverse.dev.entity.TblSystemScm;
import org.apache.ibatis.annotations.Param;
/**
 *
 * @ClassName:TblSystemScmMapper
 * @Description:系统源码mapper
 * @author author
 * @date 2020年8月19日
 *
 */
public interface TblSystemScmMapper extends BaseMapper<TblSystemScm> {
    /**
     * 删除
     * @param id
     * @return id
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入
     * @param record
     * @return
     */

    int insertSelective(TblSystemScm record);

    /**查询
     *
     * @param id
     * @return TblSystemScm
     */

    TblSystemScm selectByPrimaryKey(Long id);

    /**
     * 更新
     * @param record
     * @return int
     */

    int updateByPrimaryKeySelective(TblSystemScm record);

    int updateByPrimaryKey(TblSystemScm record);

    /**
     * 通过系统id获取源码表
     * @param id
     * @return  List<TblSystemScm>
     */

    List<TblSystemScm> getBySystemId(Long id);

    List<TblSystemScm> getScmByParam(Map<String, Object> map);

    /**
     * 删除
     * @param id
     */

    void deleteBySystemId(Long id);

    /**
     * 插入
     * @param scm
     */

    void insertSystemScm(TblSystemScm scm);

    /**
     * 删除
     * @param id
     */

    void delete(Long id);

    List<TblSystemScm> selectBuildingBySystemid(Long id);

    List<TblSystemScm> judgeScmBuildStatus(@Param("systemIds")String systemIds);

    int countScmBuildStatus(Map<String, Object> map);

    List<TblSystemScm> findStatus2(Long id);

	void deleteBySystemIds(List<Long> deleteIds);

	List<Map<String, Object>> getSystemVersionBranch(Long systemId);

	List<TblSystemScm> selectBuildingBySystemidDeploy(Long id);

    /**
     * 获取该系统下的环境
     * @param systemId
     * @return List<String>
     */

	List<String> getEnvTypes(Long systemId);

	List<Long> getEnvTypesBySyetemId(Long id);

	void updateSystemScm(Map<String, Object> map);

    List<TblSystemScm> selectBreakName(Long systemId);



    List<TblSystemScm> getScmBySystemId(Long systemId);
    void deleteScmById(TblSystemScm scm);
    TblSystemScm findScmDoesItExist(@Param("systemId")Long systemId,@Param("envType")Integer envType);

    /**
     * 新增
     * @param scm
     */
    void insertScm(TblSystemScm scm);
    void updateScm(TblSystemScm scm);
}
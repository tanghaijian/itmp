package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblTestSet;

public interface TblTestSetMapper extends BaseMapper<TblTestSet>{
    int deleteByPrimaryKey(Long id);

    Integer insert(TblTestSet record);

    int insertSelective(TblTestSet record);

    TblTestSet selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TblTestSet record);

    int updateByPrimaryKey(TblTestSet record);
    
    List<Map<String,Object>> selectTestSet(TblTestSet record);
    
    String findMaxId();
    
    List<TblTestSet> getTestSetByFeatureId(@Param("featureId") Long featureId);

    /**
    *@author liushan
    *@Description 根据工作任务查询测试集
    *@Date 2020/3/17
    *@Param [testTaskId]
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblTestSet>
    **/
    List<Map<String, Object>> getTestSetByTestTaskId(Map<String, Object> param);
    
    List<Map<String, Object>> getTestTree(Map<String, Object> map);
    
    List<Map<String, Object>> getTaskTree(Map<String, Object> map);
    
    Map<String, Object> getRelateSystem(@Param("testSetId") Long testSetId);
    
    List<Map<String, Object>> selectTestByCon(@Param("nameOrNumber") String nameOrNumber,@Param("createBy") List<Long> createBy,@Param("testTaskId") Long testTaskId);

	Map<String, Object> selectOneById(@Param("id")Long testSetId);

	List<Map<String, Object>> selectTestSetByCon(TblTestSet testSet);

    /**
     *  根据测试集id删除测试集案例
     * @param testSetId
     * @return
     */
	int updateTestSetCaseByTestSetId(Long testSetId);
}
package cn.pioneeruniverse.dev.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pioneeruniverse.common.annotion.DataSource;
import cn.pioneeruniverse.dev.vo.ExportParameters;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.pioneeruniverse.dev.entity.TblTestSet;
import cn.pioneeruniverse.dev.entity.TblTestSetCase;
import cn.pioneeruniverse.dev.entity.TblTestSetCaseVo;

public interface TblTestSetCaseMapper extends BaseMapper<TblTestSetCase>{


    /**
     *  获取数据字典中执行结果的code
     * @param valueName
     * @return
     */
    @DataSource(name = "itmpDataSource")
    Integer getValueCode(@Param("valueName")String valueName);


	List<TblTestSetCaseVo> selectByTestSetId(Map<String, Object> map);

	Integer insert(TblTestSetCase testSetCase);

    /**
     * 批量修改
     * @param result
     * @return
     */
    int updateResult(Map<String, Object> result);
    /**
     * 批量执行查询
     * @param map
     * @return
     */
    List<Map<String, Object>> selectBatchPass(Map<String, Object> map);

    int countSelectBatchPass(Map<String,Object> params);

    String getStepDescription(String testSetCaseId);
    /**
     * 修改案例查询
     * @param id
     * @return
     */
    Map<String, Object> getTestSetCaseId(Long id);
    /**
     * 修改案例编辑
     * @param map
     * @return
     */
    int updatetestSetCase(Map<String, Object> map);
    /**
     * 根据案例ID查询数据
     * @param testSetCase
     * @return
     */

    List<Map<String, Object>> selectByCon(TblTestSetCase testSetCase);

    void updateCase(TblTestSetCase testSetCase);

    void updateManyStatus(Map<String, Object> map);

    void batchInsert(@Param("list") List<TblTestSetCase> list);

    Long judgeTestSetCase(@Param("testSetId") Long testSetId,@Param("caseNumber") String caseNumber);

    void updateExecuteResult(Map<String, Object> map);

    List<Map<String, Object>> selectCaseTree(@Param("testSetId") Long testSetId,@Param("executeRound") Integer executeRound,@Param("executeResultCode") Long executeResultCode);

	Long findCaseCount(Long id);

	Long getCaseExecuteResult(HashMap<String, Object> map);

	Long getCaseCount(Long devID);

	/**
	*@author liushan
	*@Description 获取测试案例数据
	*@Date 2020/3/20
	*@Param [testCaseId]
	*@return cn.pioneeruniverse.dev.entity.TblTestSetCaseVo
	**/
    Map<String,Object> getTestCaseById(@Param("testCaseId") Long testCaseId);

    /**
    *@author liushan
    *@Description 根据测试案例id添加案例
    *@Date 2020/3/23
    *@Param [testCaseId, currentUserId]
    *@return void
    **/
    void insertById(TblTestSetCase setCase);

    /**
    *@author liushan
    *@Description 更新测试集案例排序数值
    *@Date 2020/3/24
    *@Param [testSetCaseId, currentUserId, i]
    *@return void
    **/
    void updateOrderByTestSetCaseId(@Param("testSetCaseId") Long testSetCaseId,@Param("currentUserId") Long currentUserId,@Param("orderNum") Integer orderNum);

    /**
    *@author liushan
    *@Description 更新测试集下测试案例一定范围排序数据
    *@Date 2020/3/24
    *@Param [testSetId, currentUserId, movePlaceOrderNum]
    *@return void
    **/
    void updateOrderByTestSetId(@Param("testSetId") Long testSetId,@Param("currentUserId")  Long currentUserId,@Param("movePlaceOrderNum")  Integer movePlaceOrderNum,@Param("moveNum") Integer moveNum);

    /**
    *@author liushan
    *@Description 最大的排序值+1
    *@Date 2020/3/25
    *@Param [testSetId]
    *@return java.lang.Integer
    **/
    Integer selectMaxOrder(@Param("testSetId") Long testSetId);

	List<TblTestSetCase> getOtherTestSetCase(HashMap<String, Object> paramMap);

    /**
    *@author liushan
    *@Description 更新测试集案例执行状态
    *@Date 2020/3/25
    *@Param [param]
    *@return void
    **/
    void updateExecuteResultById(Map<String,Object> param);

    /**
    *@author liushan
    *@Description 批量修改测试集案例排序
    *@Date 2020/3/26
    *@Param [map]
    *@return void
     **/
    void updateBatchOrder(Map<String, Object> map);

    /**
    *@author liushan
    *@Description 列表查询+步骤
    *@Date 2020/4/7
    *@Param [params]
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblTestSetCaseVo>
    **/
    List<TblTestSetCaseVo> selectBatchPassWithStep(Map<String,Object> params);

    /**
     *  根据测试集id获取测试集信息
     * @return
     */
    ExportParameters getTestSetInfo(Integer id);

    /**
    *@author liushan
    *@Description ORDER_CASE在一定的范围增加+1
    *@Date 2020/4/2
    *@Param [testSetId, currentUserId, laterIndex, i]
    *@return void
    **/
    void updateAddOrderByTestSetId(Map<String,Object> params);

    /**
    *@author liushan
    *@Description 排除已存在的ids
    *@Date 2020/4/3
    *@Param [param]
    *@return java.util.List<cn.pioneeruniverse.dev.entity.TblTestSetCase>
    **/
    List<TblTestSetCase> findNoChooseCaseId(Map<String,Object> param);

    /**
    *@author liushan
    *@Description 更新移除之后的排序号
    *@Date 2020/4/7
    *@Param [currentUserId]
    *@return void
    **/
    void updateOrderCase(Map<String,Object> param);
}
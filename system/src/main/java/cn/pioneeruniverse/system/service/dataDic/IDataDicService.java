package cn.pioneeruniverse.system.service.dataDic;

import java.util.List;

import cn.pioneeruniverse.common.dto.TblDataDicDTO;
import cn.pioneeruniverse.common.entity.JqGridPage;

import cn.pioneeruniverse.system.entity.TblDataDic;

import javax.servlet.http.HttpServletRequest;

public interface IDataDicService {


    /**
     * @param termCode
     * @return java.util.List<cn.pioneeruniverse.common.dto.TblDataDicDTO>
     * @Description 通过数据类型编码获取数据字典列表
     * @MethodName getDataDicByTerm
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/12/26 10:42
     */
    List<TblDataDic> getDataDicByTerm(String termCode);

    List<TblDataDic> selectList(TblDataDic dic);

    /**
     * @param page
     * @param tblDataDicDTO
     * @return cn.pioneeruniverse.common.entity.JqGridPage<cn.pioneeruniverse.common.dto.TblDataDicDTO>
     * @Description 分页查询数据字典列表
     * @MethodName getDataDictPage
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/12/25 16:36
     */
    JqGridPage<TblDataDicDTO> getDataDictPage(JqGridPage<TblDataDicDTO> page, TblDataDicDTO tblDataDicDTO) throws Exception;


    /**
     * @param dataDics
     * @param delDataDicts
     * @param dataDicMapForRedis
     * @param request
     * @return void
     * @Description 保存数据字典修改操作(和redis中的数据字典联动，保证redis中的数据字典最新)
     * @MethodName saveDataDictSubmit
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/12/28 16:21
     */
    void saveDataDictSubmit(String dataDics, String dataDicMapForRedis, HttpServletRequest request);


    /**
     * @param tblDataDic
     * @return void
     * @Description 数据字典状态设置(同步更新redis)
     * @MethodName updateDataDictStatus
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/12/29 13:42
     */
    void updateDataDictStatus(TblDataDic tblDataDic, HttpServletRequest request);

	void saveDataDict(String dataDics, String dataDicMapForRedis, HttpServletRequest request);

	void updateStatus(TblDataDic tblDataDic, HttpServletRequest request);


}

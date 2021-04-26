package cn.pioneeruniverse.system.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.pioneeruniverse.common.dto.TblDataDicDTO;
import cn.pioneeruniverse.common.entity.AjaxModel;
import cn.pioneeruniverse.common.entity.JqGridPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import cn.pioneeruniverse.system.entity.TblDataDic;
import cn.pioneeruniverse.system.service.dataDic.IDataDicService;


/**
 * 
* @ClassName: DataDicController
* @Description: 数据字典维护controller
* @author author
* @date 2020年8月4日 下午10:18:07
*
 */
@RestController
@RequestMapping("dataDic")
public class DataDicController {

    private final static Logger logger = LoggerFactory.getLogger(DataDicController.class);

    @Autowired
    private IDataDicService iDataDicService;

    /**
     * @param request
     * @param response
     * @param tblDataDicDTO 封装的数据字典查询条件
     * @return cn.pioneeruniverse.common.entity.JqGridPage<cn.pioneeruniverse.common.dto.TblDataDicDTO> 以jqgrid形式封装返回数据字典列表
     * @Description 获取数字典表分页数据
     * @MethodName getDataDictPage
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/12/28 15:24
     */
    @RequestMapping(value = "getDataDictPage", method = RequestMethod.POST)
    public JqGridPage<TblDataDicDTO> getDataDictPage(HttpServletRequest request, HttpServletResponse response, TblDataDicDTO tblDataDicDTO) {
        JqGridPage<TblDataDicDTO> jqGridPage = null;
        try {
            jqGridPage = iDataDicService.getDataDictPage(new JqGridPage<>(request, response), tblDataDicDTO);
            return jqGridPage;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }

    }

    /**
     * 
    * @Title: saveDataDict
    * @Description: 保存数据字典
    * @author author
    * @param dataDics 需要保存的数据字典json字符串
    * @param dataDicMapForRedis 需要保存到redis的数据字典json字符串
    * 举例：
    * dataDics: [{"id":"383","termName":"任务类型","termCode":"JOB_TYPE","valueName":"构建","valueSeq":"1","valueCode":"1","status":"1"},{"id":"458","termName":"任务类型","termCode":"JOB_TYPE","valueName":"构建","valueSeq":"1","valueCode":"1","status":"1"},{"id":"384","termName":"任务类型","termCode":"JOB_TYPE","valueName":"部署","valueSeq":"2","valueCode":"2","status":"1"}]
      dataDicMapForRedis: {"JOB_TYPE":{"1":"构建","2":"部署"}}
    * @param request
    * @return
    * @throws
     */
    @RequestMapping(value = "saveDataDict", method = RequestMethod.POST)
    public AjaxModel saveDataDict(String dataDics, String dataDicMapForRedis, HttpServletRequest request) {
        try {
            iDataDicService.saveDataDictSubmit(dataDics, dataDicMapForRedis, request);
            return AjaxModel.SUCCESS();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return AjaxModel.FAIL(e);
        }
    }

    /**
     * @param tblDataDic 需要更新的数据字典
     * @param request
     * @return cn.pioneeruniverse.common.entity.AjaxModel
     * @Description 更新数据字典状态请求
     * @MethodName updateStatus
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/12/29 13:54
     */
    @RequestMapping(value = "updateStatus", method = RequestMethod.POST)
    public AjaxModel updateStatus(TblDataDic tblDataDic, HttpServletRequest request) {
        try {
            iDataDicService.updateDataDictStatus(tblDataDic, request);
            return AjaxModel.SUCCESS();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return AjaxModel.FAIL(e);
        }
    }

    /**
     * @param termCode 数据字典编码
     * @return java.util.List<cn.pioneeruniverse.common.dto.TblDataDicDTO>
     * @Description 通过数据类型编码获取数据字典列表
     * @MethodName getDataDicByTermCode
     * @author yaojiaxin [yaojiaxin@pioneerservice.cn]
     * @Date 2018/12/26 10:36
     */
    @RequestMapping(value = "getDataDicByTermCode", method = RequestMethod.POST)
    public List<TblDataDic> getDataDicByTermCode(String termCode) {
        return iDataDicService.getDataDicByTerm(termCode);
    }


}

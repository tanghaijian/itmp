package cn.pioneeruniverse.dev.service.defect.dev;

/**
 * Description:
 * Author:liushan
 * Date: 2018/12/10 下午 1:44
 */
public interface TestDefectService {
    /**
    *@author author
    *@Description 同步缺陷
    *@Date 2020/8/20
     * @param objectJson
    *@return void
    **/
    void syncDefect(String objectJson) throws Exception;

    /**
    *@author author
    *@Description 同步缺陷附件
    *@Date 2020/8/20
     * @param objectJson
    *@return void
    **/
    void syncDefectAtt(String objectJson) throws Exception;

    /**
    *@author author
    *@Description 添加缺陷附件
    *@Date 2020/8/20
     * @param objectJson
    *@return void
    **/
    void insertDefectAttachement(String objectJson);
}

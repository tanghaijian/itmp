package cn.pioneeruniverse.common.dto;

import java.io.Serializable;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description: 接口请求数据返回类
 * @Date: Created in 13:46 2019/1/4
 * @Modified By:
 */
public class ResultDataDTO implements Serializable {

    private static final long serialVersionUID = 9062226946763636728L;

    //返回的Code
    private String resCode;
    //返回的描述
    private String resDesc;
    //返回的数据
    private Object data;

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResDesc() {
        return resDesc;
    }

    public void setResDesc(String resDesc) {
        this.resDesc = resDesc;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


    public ResultDataDTO() {

    }

    public ResultDataDTO(String resCode, String resDesc, Object data) {
        this.resCode = resCode;
        this.resDesc = resDesc;
        this.data = data;
    }

    public ResultDataDTO(String resCode, String resDesc) {
        this.resCode = resCode;
        this.resDesc = resDesc;
    }

    public static ResultDataDTO SUCCESSWITHDATA(String resDesc, Object data) {
        return new ResultDataDTO(DEFAULT_SUCCESS_CODE, resDesc, data);
    }

    public static ResultDataDTO FAILUREWITHDATA(String resDesc, Object data) {
        return new ResultDataDTO(DEFAULT_FAILURE_CODE, resDesc, data);
    }

    public static ResultDataDTO ABNORMALWITHDATA(String resDesc, Object data) {
        return new ResultDataDTO(DEFAULT_ABNORMAL_CODE, resDesc, data);
    }

    public static ResultDataDTO SUCCESS(String resDesc) {
        return new ResultDataDTO(DEFAULT_SUCCESS_CODE, resDesc);
    }

    public static ResultDataDTO FAILURE(String resDesc) {
        return new ResultDataDTO(DEFAULT_FAILURE_CODE, resDesc);
    }

    public static ResultDataDTO ABNORMAL(String resDesc) {
        return new ResultDataDTO(DEFAULT_ABNORMAL_CODE, resDesc);
    }

    public static ResultDataDTO SUCCESS(String resCode, String resDesc) {
        return new ResultDataDTO(resCode, resDesc);
    }

    public static ResultDataDTO FAILURE(String resCode, String resDesc) {
        return new ResultDataDTO(resCode, resDesc);
    }

    public static ResultDataDTO ABNORMAL(String resCode, String resDesc) {
        return new ResultDataDTO(resCode, resDesc);
    }

    public static ResultDataDTO SUCCESS(String resCode, String resDesc, Object data) {
        return new ResultDataDTO(resCode, resDesc, data);
    }

    public static ResultDataDTO FAILURE(String resCode, String resDesc, Object data) {
        return new ResultDataDTO(resCode, resDesc, data);
    }

    public static ResultDataDTO ABNORMAL(String resCode, String resDesc, Object data) {
        return new ResultDataDTO(resCode, resDesc, data);
    }

    public static final String DEFAULT_SUCCESS_CODE = "0000";
    public static final String DEFAULT_FAILURE_CODE = "9999";
    public static final String DEFAULT_ABNORMAL_CODE = "0009";

}

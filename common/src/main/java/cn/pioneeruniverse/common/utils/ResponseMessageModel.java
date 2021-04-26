package cn.pioneeruniverse.common.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息模型
 */
public class ResponseMessageModel implements Serializable {
    private static final long serialVersionUID = -6496067455862633155L;
    private Integer code;  //消息编码
    private String msg;     //消息结果
    private Map<String,Object> result = new HashMap<>();    //内容

    public ResponseMessageModel() {
    }

    public ResponseMessageModel(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseMessageModel(Integer code, String msg, Map<String, Object> result) {
        this.code = code;
        this.msg = msg;
        this.result = result;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(Integer msg) {
        if (msg == 0){
            this.msg = "error";
        }else{
            this.msg = "success";
        }

    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }
}

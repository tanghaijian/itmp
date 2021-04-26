package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description:
 * @Date: Created in 14:47 2019/6/12
 * @Modified By:
 */
@TableName("tbl_system_scm_submit_log")
public class TblSystemScmSubmitLog extends BaseEntity {

    private Long systemId;//系统ID
    private Long scmSubmitId;//TblSystemScmSubmit的id
    private String oldData;//修改之前的数据，提交人ID，以,隔开
    private List<String> oldDataList; //修改之前的数据列表，提交人ID，以List表示
    private String oldDataName; //修改之前的数据，提交人姓名，以,隔开
    private String newData;//修改之后的数据，提交人ID，以,隔开
    private List<String> newDataList;//修改之后的数据，提交人ID，以List表示
    private String newDataName;//修改之后的数据，提交人姓名，以,隔开
    private String operation;//操作人员ID
    private String operationName;//操作人姓名
    private String submitStatus;//提交状态修改记录，由是变否，或否变是

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public Long getScmSubmitId() {
        return scmSubmitId;
    }

    public void setScmSubmitId(Long scmSubmitId) {
        this.scmSubmitId = scmSubmitId;
    }

    public String getOldData() {
        return oldData;
    }

    public void setOldData(String oldData) {
        this.oldData = oldData;
        if(StringUtils.isBlank(oldData)){
            return;
        }
        this.oldDataList = Arrays.asList(oldData.split(","));
    }

    public String getOldDataName() {
        return oldDataName;
    }

    public void setOldDataName(String oldDataName) {
        this.oldDataName = oldDataName;
    }

    public String getNewData() {
        return newData;
    }

    public void setNewData(String newData) {
        this.newData = newData;
        if(StringUtils.isBlank(newData)){
            return;
        }
        this.newDataList = Arrays.asList(newData.split(","));
    }

    public String getNewDataName() {
        return newDataName;
    }

    public void setNewDataName(String newDataName) {
        this.newDataName = newDataName;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public List<String> getOldDataList() {
        return oldDataList;
    }

    public List<String> getNewDataList() {
        return newDataList;
    }

    public String getSubmitStatus() {
        return submitStatus;
    }

    public void setSubmitStatus(String submitStatus) {
        this.submitStatus = submitStatus;
    }

}

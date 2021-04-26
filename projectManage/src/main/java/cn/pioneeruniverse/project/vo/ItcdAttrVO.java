package cn.pioneeruniverse.project.vo;

public class ItcdAttrVO {

    /**
     * 文档名称，带后缀
     **/
    private String docName;     //文档名称，带后缀

    /**
     * 文档的mongoKey
     **/
    private String docKey;      //文档的mongoKey

    /**
     * 文档分类
     **/
    private String docType;     //文档分类

    /**
     * 文档状态，1:正常，2:删除
     **/
    private String docStatus;   //文档状态，1:正常，2:删除

    /**
     * 上传用户账号
     **/
    private String userAccount; //上传用户账号

    /**
     * 任务编号
     **/
    private String taskCode;    //任务编号

    private String reqCode;

    public String getDocName() {
        return docName;
    }
    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDocKey() {
        return docKey;
    }
    public void setDocKey(String docKey) {
        this.docKey = docKey;
    }

    public String getDocType() {
        return docType;
    }
    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getDocStatus() {
        return docStatus;
    }
    public void setDocStatus(String docStatus) {
        this.docStatus = docStatus;
    }

    public String getUserAccount() {
        return userAccount;
    }
    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getTaskCode() {
        return taskCode;
    }
    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public String getReqCode() {
        return reqCode;
    }
    public void setReqCode(String reqCode) {
        this.reqCode = reqCode;
    }
}

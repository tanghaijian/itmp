package cn.pioneeruniverse.common.databus;

/**
 * 
* @ClassName: DisposeDetail 
* @Description: databus系统生产发布结果
* @author author
* @date 2020年8月17日 下午5:52:33
*
 */
public class DisposeDetail {
	//实例名
    private String instanceName;
    //实例IP
    private String instanceIp;

    public String getInstanceName() {
        return instanceName;
    }
    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getInstanceIp() {
        return instanceIp;
    }
    public void setInstanceIp(String instanceIp) {
        this.instanceIp = instanceIp;
    }
}

package cn.pioneeruniverse.common.databus;

import java.util.List;

/**
 * 
* @ClassName: SubDisposeDetail
* @Description: 系统生产发布结果报
* @author author
* @date 2020年8月17日 下午5:54:08
*
 */
public class SubDisposeDetail {
	//子系统编码
    private String subSystemCode;
    //子系统测试返回结果
    private List<DisposeDetail> subDisposeDetails;

    public String getSubSystemCode() {
        return subSystemCode;
    }
    public void setSubSystemCode(String subSystemCode) {
        this.subSystemCode = subSystemCode;
    }

    public List<DisposeDetail> getSubDisposeDetails() {
        return subDisposeDetails;
    }
    public void setSubDisposeDetails(List<DisposeDetail> subDisposeDetails) {
        this.subDisposeDetails = subDisposeDetails;
    }
}

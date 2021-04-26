package cn.pioneeruniverse.common.databus;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author:
 * @Description: 大地接口RequestHead字段
 * @Date: Created in 2020年9月7日 16:48:56
 * @Modified By:
 */
@Component
public class DataBusRequestHead {

    private static String consumerSeqNo;
    private static String classCode;
    private static String regionCode;
    private static String seqNo;
    private static String consumerID;
    private static String providerID;
    private static String riskCode;
    private static String version;

    @Value("${requestHead.consumerSeqNo}")
    public void setConsumerSeqNo(String param) {
        consumerSeqNo = param;
    }
    @Value("${requestHead.classCode}")
    public void setClassCode(String param) {
        classCode = param;
    }
    @Value("${requestHead.regionCode}")
    public void setRegionCode(String param) {
        regionCode = param;
    }
    @Value("${requestHead.seqNo}")
    public void setSeqNo(String param) {
        seqNo = param;
    }
    @Value("${requestHead.consumerID}")
    public void setConsumerID(String param) {
        consumerID = param;
    }
    @Value("${requestHead.providerID}")
    public void setProviderID(String param) {
        providerID = param;
    }
    @Value("${requestHead.riskCode}")
    public void setRiskCode(String param) {
        riskCode = param;
    }
    @Value("${requestHead.version}")
    public void setVersion(String param) {
        version = param;
    }

    public static Map<String,Object> getRequestHead(){
        Map<String, Object> mapHead = new HashMap<>();
        mapHead.put("seqNo", seqNo);
        mapHead.put("consumerSeqNo", consumerSeqNo);
        mapHead.put("consumerID", consumerID);
        mapHead.put("providerID", providerID);
        mapHead.put("classCode", classCode);
        mapHead.put("riskCode", riskCode);
        mapHead.put("regionCode", regionCode);
        mapHead.put("version", version);
        return mapHead;
    }
    public static void setConsumerSeqNo1(String param) {
        consumerSeqNo = param;
    }
    public static void setClassCode1(String param) {
        classCode = param;
    }
    public static void setRegionCode1(String param) {
        regionCode = param;
    }
    public static void setSeqNo1(String param) {
        seqNo = param;
    }
    public static void setConsumerID1(String param) {
        consumerID = param;
    }
    public static void setProviderID1(String param) {
        providerID = param;
    }
    public static void setRiskCode1(String param) {
        riskCode = param;
    }
    public static void setVersion1(String param) {
        version = param;
    }
}

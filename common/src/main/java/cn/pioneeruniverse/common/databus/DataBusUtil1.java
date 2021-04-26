package cn.pioneeruniverse.common.databus;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
/**
 *
 * @ClassName:DataBusUtil1
 * @Description DataBus工具类2
 * @author author
 * @date 2020年8月26日
 *
 */
@Component
public class DataBusUtil1 {
    @Value("${dataBus.ipaddr}")
    private  String dataBusIpaddr;

    private final static Logger log = LoggerFactory.getLogger(DataBusUtil.class);
    public  HttpResponse postDataBus(String dataName, String param) throws IOException {
        try {
            log.info("http调用开始 param"+param);
            HttpClient client = HttpClients.createDefault();
            String url=dataBusIpaddr+dataName;
            log.info("dataBusIpaddr 地址"+url);
            HttpPost post = new HttpPost(url);
            post.setEntity(new StringEntity(param,"UTF-8"));
            HttpResponse response = client.execute(post);
            log.info("http调用结束");
            log.info("response"+response.toString());
            return response;
        }catch (Exception e){
            this.handleException(e);
            throw e;
        }


    }
    public void handleException(Exception e) {
        e.printStackTrace();
        log.error(e.getMessage(), e);
    }
}

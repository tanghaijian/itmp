package cn.pioneeruniverse.common.sonar.client;


import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import cn.pioneeruniverse.common.sonar.bean.SonarQubeException;
import cn.pioneeruniverse.common.sonar.constants.SonarAPIConstants;

/**
 *
 * @ClassName:SourceApi
 * @Description:SourceApi
 * @author author
 * @date 2020年8月19日
 *
 */
 
public class SourceApi extends AbstractApi {

    public SourceApi(String url, HTTPBasicAuthFilter auth) {
        super(url, auth);
    }
    /**
     * 查询 详细代码 返回代码html json
     * @param  key my_project:/src/foo/test.java
     * @return String
     * @throws SonarQubeException 
     */
    public String  search(String key) throws SonarQubeException{
    	
         MultivaluedMap<String, String> params = new MultivaluedMapImpl();
         params.add("key", key);
       /*  params.add("s", "FILE_LINE");//可取值CREATION_DATE,UPDATE_DATE等
         params.add("types", types);
         params.add("additionalFields", "_all");*/
        /* params.add("from", "1");///起始行与结束行
         params.add("to", "800");*/
       /*  params.add("p", "2");*///页数
         /*params.add("metricKeys",metricKeys);*/
         ClientResponse clientResponse = RestApi.get(SonarAPIConstants.SONAR_URL,
            		 SonarAPIConstants.SONAR_SOURCES_LINES,getAuth(),params);
         String result = clientResponse.getEntity(String.class);
           System.out.println(result);
         
         return result;
    	
    }

}

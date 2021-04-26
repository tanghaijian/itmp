package cn.pioneeruniverse.common.sonar.client;


import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import cn.pioneeruniverse.common.sonar.bean.SonarQubeException;
import cn.pioneeruniverse.common.sonar.constants.SonarAPIConstants;


/**
 *
 * @ClassName:ComponentApi
 * @Description:查询度量信息
 * @author author
 * @date 2020年8月19日
 *
 */
public class ComponentApi extends AbstractApi {

    public ComponentApi(String url, HTTPBasicAuthFilter auth) {
        super(url, auth);
    }
    /**
     * 查询 项目主要度量详细信息 如 代码bug详细位置
     * @param  componentKeys,types(types可选BUG, VULNERABILITY,CODE_SMELL)
     * @return String
     * @throws SonarQubeException 
     */
    public String  search(String componentKeys,String types) throws SonarQubeException{
    	
         MultivaluedMap<String, String> params = new MultivaluedMapImpl();
         params.add("componentKeys", componentKeys);
         params.add("s", "FILE_LINE");//可取值CREATION_DATE,UPDATE_DATE等
         params.add("types", types);
         params.add("additionalFields", "_all");
         params.add("resolved", "false");///已解决的
       /*  params.add("p", "2");*///页数
         /*params.add("metricKeys",metricKeys);*/
         ClientResponse clientResponse = RestApi.get(SonarAPIConstants.SONAR_URL,
            		 SonarAPIConstants.SONAR_ISSUES_SEARCH,getAuth(),params);
         String result = clientResponse.getEntity(String.class);
           
         
         return result;
    	
    }
    
    
    /**
     * 查询 项目主要度量详细信息 如 代码bug详细位置
     * @param  key 显示条数
     * @return String
     * @throws SonarQubeException 
     */
    public String  Search_projects(String ps) throws SonarQubeException{
    	
        MultivaluedMap<String, String> params = new MultivaluedMapImpl();

        params.add("ps", "10");
     
        ClientResponse clientResponse = RestApi.get(SonarAPIConstants.SONAR_URL,
           		 SonarAPIConstants.SONAR_SEARCH_PEORJECT,getAuth(),params);
        String result = clientResponse.getEntity(String.class);
          
        
        return result;
   	
   }

}

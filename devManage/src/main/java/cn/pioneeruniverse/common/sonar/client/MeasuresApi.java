package cn.pioneeruniverse.common.sonar.client;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import cn.pioneeruniverse.common.sonar.bean.Component;
import cn.pioneeruniverse.common.sonar.bean.SonarQubeException;
import cn.pioneeruniverse.common.sonar.constants.SonarAPIConstants;


/**
 * @author weiji
 *
 */


public class MeasuresApi extends AbstractApi{

    public MeasuresApi(String url, HTTPBasicAuthFilter auth) {
        super(url, auth);
    }
    
    
    /**
     * 查询 项目度量信息
     * @param  projectkey,metricKeys(如metricKeys取值alert_status,bugs等)
     * @return String
     * @throws SonarQubeException 
     */
    public String  search(String projectKeys,String metricKeys) throws SonarQubeException{
    	
         MultivaluedMap<String, String> params = new MultivaluedMapImpl();
         params.add("projectKeys", projectKeys);
         params.add("metricKeys",metricKeys);
         ClientResponse clientResponse = RestApi.get(SonarAPIConstants.SONAR_URL,
            		 SonarAPIConstants.SONAR_MEASURES_SEARCH,getAuth(),params);
         String output = clientResponse.getEntity(String.class);
           
         
         return output;
    	
    }
    
    
    
    /**
     * 查询 项目度量信息
     * @param  projectkey,metricKeys(如metricKeys取值alert_status,bugs等)
     * @return String
     * @throws SonarQubeException 
     */
    public String  searchByUrl(String projectKeys,String metricKeys,String url) throws SonarQubeException{
    	
         MultivaluedMap<String, String> params = new MultivaluedMapImpl();
         params.add("projectKeys", projectKeys);
         params.add("metricKeys",metricKeys);
         ClientResponse clientResponse = RestApi.get(url,
            		 SonarAPIConstants.SONAR_MEASURES_SEARCH,getAuth(),params);
         String output = clientResponse.getEntity(String.class);
           
         
         return output;
    	
    }
    
    
    /**
     * 查询 项目度量信息
     * @param  projectkey,metricKeys(如metricKeys取值alert_status,bugs等)
     * @return String
     * @throws SonarQubeException 
     */
    public String  componenttreeSerarch(String baseComponentKey,String metricKeys) throws SonarQubeException{
    	
         MultivaluedMap<String, String> params = new MultivaluedMapImpl();
         params.add("baseComponentKey", baseComponentKey);
         params.add("metricKeys",metricKeys);
         /*params.add("ps","1");*/
         params.add("strategy","children");
         params.add("s","qualifier,name");
         ClientResponse clientResponse = RestApi.get(SonarAPIConstants.SONAR_URL,
            		 SonarAPIConstants.SONAR_COMPONET_TREE,getAuth(),params);
         String output = clientResponse.getEntity(String.class);
           
         
         return output;
    	
    }
    
    /**
     *   查询 单个项目指标信息
     * @param  projectkey
     * @return Map<String, String>
     * @throws SonarQubeException 
     */
    
    
    public Map<String, String> searchProjectByKey(String projectKey) throws SonarQubeException{
    	Map<String, String> map=new HashMap<String, String>();
    	 List<Component> components = new ArrayList<>();
        

            MultivaluedMap<String, String> params = new MultivaluedMapImpl();
            params.add("componentKey", projectKey);
            params.add("additionalFields", "metrics");
           
            
            params.add("metricKeys","alert_status,quality_gate_details,bugs,new_bugs,reliability_rating,new_reliability_rating,vulnerabilities,new_vulnerabilities,security_rating,new_security_rating,code_smells,new_code_smells,sqale_rating,new_maintainability_rating,sqale_index,new_technical_debt,coverage,new_coverage,new_lines_to_cover,tests,duplicated_lines_density,new_duplicated_lines_density,duplicated_blocks,ncloc,ncloc_language_distribution,projects,new_lines");
            
            ClientResponse clientResponse = RestApi.get(SonarAPIConstants.SONAR_URL,
           		 SonarAPIConstants.SONAR_MEASURES_COMPONET,getAuth(),params);
            String output = clientResponse.getEntity(String.class);
            //解析

            JSONObject jsonObject = JSONObject.parseObject(output);
           String component= jsonObject.getString("component");
           JSONObject componentObject = JSONObject.parseObject(component);
           String measures=componentObject.getString("measures");
           JSONArray jsonArray = JSONArray.parseArray(measures);
           for (int i = 0; i < jsonArray.size(); i++) {

               JSONObject jobject = jsonArray.getJSONObject(i);
               
              String metric= jobject.getString("metric");
            
              String value=jobject.getString("value");
              map.put(metric, value);
               
           }
           
            
          
       
        return map;
   	
   }
    
    


}

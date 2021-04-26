package cn.pioneeruniverse.common.sonar.client;


import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import com.google.gson.Gson;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import cn.pioneeruniverse.common.sonar.bean.Component;
import cn.pioneeruniverse.common.sonar.bean.ProjectSearchModel;
import cn.pioneeruniverse.common.sonar.bean.SonarQubeException;
import cn.pioneeruniverse.common.sonar.constants.SonarAPIConstants;


/**
 *
 * @ClassName:ProjectsApi
 * @Description:查询sonar project信息
 * @author author
 * @date 2020年8月19日
 *
 */

public class ProjectsApi extends AbstractApi{

    public ProjectsApi(String url, HTTPBasicAuthFilter auth) {
        super(url, auth);
    }
    
    
    /**
     * 查询所有sonar project信息
     * @param 
     * @return
     * @throws SonarQubeException 
     */
    public List<Component> search() throws SonarQubeException{
    	 List<Component> components = new ArrayList<>();
         Gson gson = new Gson();
         boolean isNotLast;
         int page = 1;
         int ps = 100;
         do{
             MultivaluedMap<String, String> params = new MultivaluedMapImpl();
             params.add("p", Integer.toString(page));
             ClientResponse clientResponse = RestApi.get(SonarAPIConstants.SONAR_URL,
            		 SonarAPIConstants.SONAR_PROJECT_SEARCH,getAuth(),params);
             String output = clientResponse.getEntity(String.class);
             ProjectSearchModel projects = gson.fromJson(output, ProjectSearchModel.class);
             components.addAll(projects.getComponents());
             isNotLast = page * ps < projects.getPaging().getTotal();
             page = projects.getPaging().getTotal();
         }while(isNotLast);

         return components;
    	
    }
    public void create(String name ,String projectkey) throws SonarQubeException {
    	 MultivaluedMap<String, String> params = new MultivaluedMapImpl();
         params.add("name",name);
         params.add("project",projectkey);
         ClientResponse clientResponse = RestApi.post(SonarAPIConstants.SONAR_URL,
        		 SonarAPIConstants.SONAR_PROJECT_CREATE,getAuth(),params);
         String output = clientResponse.getEntity(String.class);
         System.out.println(output);
    }
    


}

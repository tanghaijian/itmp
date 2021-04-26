package cn.pioneeruniverse.common.sonar.client;

import com.google.gson.Gson;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

import cn.pioneeruniverse.common.sonar.bean.Errors;
import cn.pioneeruniverse.common.sonar.bean.SonarQubeException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.util.stream.IntStream;



public class RestApi {

   
    public static ClientResponse get(String url, String path, HTTPBasicAuthFilter auth, MultivaluedMap<String, String> queryParams) throws SonarQubeException {
        Client client = Client.create();
        client.addFilter(auth);
        WebResource webResource = client.resource(url).path(path).queryParams(queryParams);
        ClientResponse clientResponse = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        checkResponse(clientResponse);
        return clientResponse;
    }

   
    public static ClientResponse post(String url, String path, HTTPBasicAuthFilter auth, MultivaluedMap<String, String> data) throws SonarQubeException {
        Client client = Client.create();
        client.addFilter(auth);
        WebResource webResource = client.resource(url).path(path);
        ClientResponse clientResponse = webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, data);
        checkResponse(clientResponse);
        return clientResponse;
    }

   
    private static void checkResponse(ClientResponse clientResponse) throws SonarQubeException {
        int[] errorStatuses = {400, 401, 403, 404};

        int status = clientResponse.getStatus();

        if(IntStream.of(errorStatuses).anyMatch(x -> x == status)){
            String output = clientResponse.getEntity(String.class);
            Errors errors = new Gson().fromJson(output, Errors.class);
            StringBuilder message = new StringBuilder();
            for (Errors.Error error : errors.getErrors()) {
                message.append(error.getMsg()).append("|");
            }
            throw new SonarQubeException(message.substring(0, message.length()-1));
        }
    }

}

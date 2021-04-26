package cn.pioneeruniverse.common.config;


import org.springframework.context.annotation.Configuration;

import com.netflix.hystrix.exception.HystrixBadRequestException;
import feign.codec.ErrorDecoder;
import feign.Response;

@Configuration
public class FeignErrorConfig implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        if(response.status() >= 400 && response.status() <= 499){
            return new HystrixBadRequestException("接口调用异常");
        }
        return feign.FeignException.errorStatus(methodKey, response);
    }
    }
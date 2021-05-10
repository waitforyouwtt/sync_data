package com.yh.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
public class HttpUtils {

    public static String shenshuUrl = "http://o2o-support-dev.o2o-support-idaas-gateway.devgw.yonghui.cn";

    @Autowired
    RestTemplate restTemplate;

    public Result postJson(String json,String remoteUrl){
        String url = shenshuUrl.concat(remoteUrl);
        //设置请求头
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> formEntity = new HttpEntity<String>(json, headers);
        //调用服务接口，此时的请求参数存放于body中。服务端接受参数解析body即可。
        String response = restTemplate.postForObject(url, formEntity, String.class);
        return getResult( response );
    }

    private Result getResult(String response) {
        if (response == null){
            return new Result(500000,"远程调用返回结果为null");
        }
        Map<String,Object> maps = (Map) JSON.parse( response );
        if (!maps.get("code").equals(200000)){
            log.warn("调用神荼平台接口出错，{}", maps.get("message"));
            return new Result(500000,maps.get("message").toString());
        }
        return new Result(200000,maps.get("message").toString(),maps.get("result"));
    }

}

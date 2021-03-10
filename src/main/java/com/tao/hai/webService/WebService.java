package com.tao.hai.webService;

import javax.jws.WebMethod;
import javax.xml.ws.Endpoint;

@javax.jws.WebService
public class WebService {
    @WebMethod
    public String getUser(String userId){
        return "Response:"+userId;
    }

    public static void main(String[] args) {
        Endpoint.publish("http://localhost:8090/service/webService",new WebService());
        System.out.println("服务发布成功！");
        /**
         *
         * http://localhost:8090/service/webService?wsdl 访问
         * */
    }
}

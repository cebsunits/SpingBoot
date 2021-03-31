package com.tao.hai.webservice;

import com.tao.webservice.client.WebService;
import com.tao.webservice.client.WebServiceService;
/***
 * idea 点击项目中的webService,选择Generate java ocde from wsdl生成客户端
 * */
public class WebServiceDemo {
    public static void main(String[] args) {
        WebServiceService webService=new WebServiceService();
        WebService service=webService.getWebServicePort();
        String user=service.getUser("AAAA");
        System.out.println("user:="+user);
    }
}
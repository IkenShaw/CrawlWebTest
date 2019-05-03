package org.iken.main.controller;


import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.iken.main.page.Page;

import java.io.IOException;

public class RequestAndResponseTool {

    public static Page sendRequestAndGetResponse(String url) {
        Page page = null;
        HttpClient httpClient = new HttpClient();
        // 设置http连接超时时间
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
        GetMethod getMethod = new GetMethod(url);
        // 设置get请求超时时间
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
        // 设置请求重试处理
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        try {
            int statusCode = httpClient.executeMethod(getMethod);
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed：" + getMethod.getStatusLine());
            }
            // 处理http响应内容
            byte[] responseBody = getMethod.getResponseBody();
            String contentType = getMethod.getRequestHeader("Content-Type").getValue();
            page = new Page(responseBody, url, contentType);
        }catch (IOException e) {
            System.err.println("Please check your provided http address!");
            e.printStackTrace();
        } finally {
            getMethod.releaseConnection();
        }
        return page;
    }

}

package com.demon;

import org.springframework.util.StreamUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
//https需要认证，所以暂时用http
//Url:https://way.jd.com/jisuapi/query4?shouji=13456755448&appkey=74b78d03f913a65ac5275be3e9a77e4b
public class HttpTest {
    public static void main(String[] args)throws Exception {
        //定义需要访问的地址
        URL url = new URL("http://way.jd.com/jisuapi/query4");
        //建立连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        //设置请求方式
        connection.setRequestMethod("POST");
        //设置允许携带参数
        connection.setDoOutput(true);
        StringBuilder sb = new StringBuilder();
        sb.append("shouji=").append("18379977051")
                .append("&appkey=").append("74b78d03f913a65ac5275be3e9a77e4b");
        connection.getOutputStream().write(sb.toString().getBytes("UTF-8"));
        //发起请求
        connection.connect();
        //接受返回值
        String response = StreamUtils.copyToString(connection.getInputStream(), Charset.forName("UTF-8"));

        System.out.println(response);
    }

}

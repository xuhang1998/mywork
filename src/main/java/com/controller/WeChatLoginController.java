package com.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

/**
 * @author smileyan
 */
@RestController
@RequestMapping("/wechat")
public class WeChatLoginController {

    @Value("${wechat.appid}")
    private String appid;
    @Value("${wechat.appsecret}")
    private String appsecret;

    private String openid;
    private String session_key;
    private  String WX_USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo";// 获取用户信息 url

    @GetMapping("/index")
    private String login(String code) {
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String resultString = "";
        CloseableHttpResponse response = null;
        code = "003mW7000eMe8K1BAN3002hZIc3mW704";
        String url="https://api.weixin.qq.com/sns/jscode2session?appid="+appid+"&secret="+appsecret+"&js_code="+code+"&grant_type=authorization_code";
        // 获取用户 openid 和access——toke 的 URL
        String ACCESSTOKE_OPENID_URL =
                "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appid+"&secret="+appsecret+"&code="+code+"&grant_type=authorization_code";
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(ACCESSTOKE_OPENID_URL);
            URI uri = builder.build();

            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);

            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 解析json
        JSONObject jsonObject = (JSONObject) JSONObject.parse(resultString);
        session_key = jsonObject.get("session_key")+"";
        openid = jsonObject.get("openid")+"";
        String access_token = jsonObject.get("access_token")+"";

        System.out.println("session_key=="+session_key);
        System.out.println("openid=="+openid);

        StringBuffer userUrl = new StringBuffer();
        userUrl.append(WX_USERINFO_URL).append("?access_token=").append(access_token).append("&openid=").append(openid);
//		String userRet = this.get(userUrl.toString());

        String userRet = this.httpRequest(userUrl.toString(),"GET",null);

        System.out.println("-----获取得到的用户信息------"+userRet);
        return resultString;
    }

    //处理http请求  requestUrl为请求地址  requestMethod请求方式，值为"GET"或"POST"

    public  String httpRequest(String requestUrl,String requestMethod,String outputStr){

        StringBuffer buffer=null;

        try{

            URL url=new URL(requestUrl);

            HttpURLConnection conn=(HttpURLConnection)url.openConnection();

            conn.setDoOutput(true);

            conn.setDoInput(true);

            conn.setRequestMethod(requestMethod);

            conn.connect();

            //往服务器端写内容 也就是发起http请求需要带的参数

            if(null!=outputStr){

                OutputStream os=conn.getOutputStream();

                os.write(outputStr.getBytes("utf-8"));

                os.close();

            }



            //读取服务器端返回的内容

            InputStream is=conn.getInputStream();

            InputStreamReader isr=new InputStreamReader(is,"utf-8");

            BufferedReader br=new BufferedReader(isr);

            buffer=new StringBuffer();

            String line=null;

            while((line=br.readLine())!=null){

                buffer.append(line);

            }

            System.out.println(url);
            System.out.println( buffer.toString());
        }catch(Exception e){

            e.printStackTrace();

        }

        return buffer.toString();

    }

}


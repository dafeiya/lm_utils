package httprequest;

import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class HttpUtils {

    private static final int HTTP_TIME_OUT = 3000;

    /**
     * 发送post json请求
     *
     * @param jsonObj
     * @param url
     * @return
     */
    public static JSONObject httpPost(JSONObject jsonObj, String url) {
        System.out.println("url:" + url);
        HttpPost post = null;
        JSONObject responJson = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            post = initHttpPost(httpClient, url, jsonObj);
            //发送请求
            HttpResponse response = httpClient.execute(post);
            //处理数据
            responJson = handlerResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (post != null) {
                try {
                    post.releaseConnection();
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return responJson;
    }

    private static HttpPost initHttpPost(HttpClient httpClient, String url, JSONObject jsonObj) {
        HttpPost post = null;
        post = new HttpPost(url);
        post.setHeader("Content-type", "application/json; charset=utf-8");
        post.setHeader("Connection", "closed");

        StringEntity entity = new StringEntity(jsonObj.toString(), Charset.forName("UTF-8"));
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        post.setEntity(entity);

        //设置超时
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, HTTP_TIME_OUT);
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, HTTP_TIME_OUT);
        return post;
    }

    //将返回数据处理为json
    private static JSONObject handlerResponse(HttpResponse response) throws IOException {
        BufferedReader bReader = null;
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("statusCode: " + statusCode);
        String line = null;
        bReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = bReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        bReader.close();
        System.out.println("Response Content:" + stringBuilder);
        return JSONObject.fromObject(stringBuilder.toString());
    }

    public static void main(String[] args) {
        JSONObject json = new JSONObject();
        json.put("test", "123");
        httpPost(json, "http://hb.dev.ebidding.net.cn:8600/ebidding/outer/tbclient/validateVerison.htm?sysCode=hbpt&clientVersion=2.0.0.0");
//         httpPost(json,"http://baidu.com");
    }
}

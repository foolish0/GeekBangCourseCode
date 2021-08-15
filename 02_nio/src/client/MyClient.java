package client;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author lizhenjiang
 */
public class MyClient {
    public static void main(String[] args) throws IOException {
        String url = "http://localhost:8881";
        String responseBody = getMethodWithHttpClient(url);
        System.out.println("response:\n" + responseBody);
    }

    /**
     * 使用HttpClient的GET方法
     * @param url 请求URL
     * @return 响应体
     * @throws IOException
     */
    public static String getMethodWithHttpClient(String url) throws IOException{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity, "UTF-8");
        } finally {
            if (response != null) {
                response.close();
            }
            httpClient.close();
        }
    }



}

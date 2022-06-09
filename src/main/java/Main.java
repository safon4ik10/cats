import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(
                        RequestConfig.custom()
                                .setConnectTimeout(5000)
                                .setSocketTimeout(30000)
                                .setRedirectsEnabled(false)
                                .build()
                ).build();

        HttpGet httpGet = new HttpGet("https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats");
        httpGet.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());

        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            String body = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
            //System.out.println(body);

            List<Cat> cats = new ObjectMapper()
                    .readValue(body, new TypeReference<>() {});

            cats.stream()
                    .filter(cat -> cat.getUpvotes() != null && cat.getUpvotes() > 0)
                    .sorted(Comparator.comparing(Cat::getUpvotes))
                    .forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

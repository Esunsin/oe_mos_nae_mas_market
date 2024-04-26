package cheolppochwippo.oe_mos_nae_mas_market.global.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.TransportUtils;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ElasticConfig {

    @Value("${elasticsearch.host}")
    private String host;
    @Value("${elasticsearch.port}")
    private int port;
    @Value("${elasticsearch.username}")
    private String username;
    @Value("${elasticsearch.password}")
    private String password;
    @Value("${elasticsearch.encodedApiKey}")
    private String encodedApiKey;
    @Value("${elasticsearch.fingerprint}")
    private String fingerPrint;

    /*
    yml 또는 properties에서 @Value로 지정해둔 값이 제대로 넘어오는지 log로 확인하는 방법
    @PostConstruct
    public void logConfig() {
        log.info("Elasticsearch host: {}", host);
        log.info("Elasticsearch port: {}", port);
        log.info("Elasticsearch username: {}", username);
        log.info("Elasticsearch password: {}", password);
        log.info("Elasticsearch encodeApiKey: {}", encodedApiKey);
        log.info("Elasticsearch fingerprint: {}", fingerPrint);
    }
     */

    @Bean
    public RestClient getRestClient() {
        CredentialsProvider credentials = new BasicCredentialsProvider();
        credentials.setCredentials(
            AuthScope.ANY, new UsernamePasswordCredentials(username, password));

        return RestClient.builder(
                new HttpHost(host, port, "https"))
            .setDefaultHeaders(new Header[]{
                new BasicHeader("Authorization", "ApiKey " + encodedApiKey)
            })
            .setHttpClientConfigCallback(httpAsyncClientBuilder -> httpAsyncClientBuilder
                .setSSLContext(TransportUtils.sslContextFromCaFingerprint(fingerPrint))
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .setDefaultCredentialsProvider(credentials))
            .build();
    }

    @Bean
    public ElasticsearchTransport getElasticsearchTransport() {
        return new RestClientTransport(getRestClient(), new JacksonJsonpMapper());
    }

    @Bean
    public ElasticsearchClient getElasticsearchClient() {
        return new ElasticsearchClient(getElasticsearchTransport());
    }

}

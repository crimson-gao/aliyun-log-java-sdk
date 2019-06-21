package com.aliyun.openservices.log.http.comm;

import com.aliyun.openservices.log.http.client.ClientException;
import com.aliyun.openservices.log.http.client.HttpMethod;
import com.aliyun.openservices.log.http.utils.HttpHeaders;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;

import java.util.Map.Entry;

class HttpRequestFactory {

    public HttpRequestBase createHttpRequest(ServiceClient.Request request) {

        String uri = request.getUri();

        HttpRequestBase httpRequest;
        HttpMethod method = request.getMethod();
        if (method == HttpMethod.POST) {
            HttpPost postMethod = new HttpPost(uri);

            if (request.getContent() != null) {
                postMethod.setEntity(new RepeatableInputStreamEntity(request));
            }

            httpRequest = postMethod;
        } else if (method == HttpMethod.PUT) {
            HttpPut putMethod = new HttpPut(uri);

            if (request.getContent() != null) {
                putMethod.setEntity(new RepeatableInputStreamEntity(request));
            }

            httpRequest = putMethod;
        } else if (method == HttpMethod.GET) {
            httpRequest = new HttpGet(uri);
        } else if (method == HttpMethod.DELETE) {
            httpRequest = new HttpDelete(uri);
        } else if (method == HttpMethod.HEAD) {
            httpRequest = new HttpHead(uri);
        } else if (method == HttpMethod.OPTIONS) {
            httpRequest = new HttpOptions(uri);
        } else {
            throw new ClientException("Unknown HTTP method name: " + method.toString());
        }
        configureRequestHeaders(request, httpRequest);
        return httpRequest;
    }

    private void configureRequestHeaders(ServiceClient.Request request,
                                         HttpRequestBase httpRequest) {
        for (Entry<String, String> entry : request.getHeaders().entrySet()) {
            if (entry.getKey().equalsIgnoreCase(HttpHeaders.CONTENT_LENGTH)
                    || entry.getKey().equalsIgnoreCase(HttpHeaders.HOST)) {
                continue;
            }

            httpRequest.addHeader(entry.getKey(), entry.getValue());
        }
    }
}

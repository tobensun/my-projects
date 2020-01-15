package com.example.jsonparam;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.Charset;

public class JsonPathArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String JSONBODYATTRIBUTE = "JSON_REQUEST_BODY";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(JsonArg.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        JsonArg methodAnnotation = parameter.getParameterAnnotation(JsonArg.class);
        if (methodAnnotation != null) {
            String body = getRequestBody(webRequest);
            String jsonName = methodAnnotation.value();
            JSONObject jsonObject = JSONObject.parseObject(body);
            return jsonObject.get(jsonName);
        }
        return null;
    }

    private String getRequestBody(NativeWebRequest webRequest) {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        String jsonBody = (String) servletRequest.getAttribute(JSONBODYATTRIBUTE);
        if (jsonBody == null) {
            try {
                String body = IOUtils.toString(servletRequest.getInputStream(), Charset.defaultCharset());
                servletRequest.setAttribute(JSONBODYATTRIBUTE, body);
                jsonBody = body;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return jsonBody;
    }
}
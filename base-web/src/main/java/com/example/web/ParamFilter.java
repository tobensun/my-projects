package com.example.web;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


/**
 * 处理前端请求为request payload 方式为 form-data
 */
@Component
@WebFilter(urlPatterns = "/*")
public class ParamFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        ParameterRequestWrapper requestWrapper = new ParameterRequestWrapper((HttpServletRequest)request);
        JSONObject jsonObject = getStringFromStream(request);
        if(jsonObject != null){
            jsonObject.keySet().stream().forEach(key -> {
                requestWrapper.addParameter(key,jsonObject.get(key));
            });
            filterChain.doFilter(requestWrapper, response);
        }else{
            filterChain.doFilter(request, response);
        }
    }

    private JSONObject getStringFromStream(ServletRequest req) {
        ServletInputStream is;
        try {
            is = req.getInputStream();
            int nRead = 1;
            int nTotalRead = 0;
            byte[] bytes = new byte[512];
            while (nRead > 0) {
                nRead = is.read(bytes, nTotalRead, bytes.length - nTotalRead);
                if (nRead > 0)
                    nTotalRead = nTotalRead + nRead;
            }
            String str = new String(bytes, 0, nTotalRead, "utf-8");
            return JSONObject.parseObject(str);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

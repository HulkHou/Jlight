package com.lew.jlight.web.filter;

import com.google.common.base.Strings;

import com.lew.jlight.web.util.ServletUtil;

import org.springframework.core.env.Environment;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(filterName="preparedFilter",urlPatterns="/*")
public class PreparedFilter implements Filter {

    @Resource
    private Environment environment;

    private String contextPath;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        contextPath = environment.getProperty("server.contextPath");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpServletResponse httpResp = (HttpServletResponse) resp;
        if(!Strings.isNullOrEmpty(contextPath)){
            req.setAttribute( "ctx", contextPath );
        }
        String requestUrl = ServletUtil.getRequestUrl(httpReq);
        if (!ServletUtil.endsWithAny(requestUrl)) {
            ServletUtil.setRequest(httpReq);
            ServletUtil.setResponse(httpResp);
            try {
                chain.doFilter(req, resp);
            } finally {
                ServletUtil.clearServletContext();
            }
        }else{
            chain.doFilter(req, resp);
        }
    }

    @Override
    public void destroy() {
    }
}

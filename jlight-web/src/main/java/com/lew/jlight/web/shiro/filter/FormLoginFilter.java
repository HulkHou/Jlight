package com.lew.jlight.web.shiro.filter;

import com.lew.jlight.core.Response;
import com.lew.jlight.web.util.ServletUtil;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class FormLoginFilter extends AuthorizationFilter{
	
	private String unauthorizedUrl = "/login";

	public String getUnauthorizedUrl() {
		return unauthorizedUrl;
	}

	public void setUnauthorizedUrl(String unauthorizedUrl) {
		this.unauthorizedUrl = unauthorizedUrl;
	}

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		Subject subject = getSubject(request, response);
		return subject.getPrincipal() != null;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws IOException {

        Subject subject = getSubject(request, response);
        // If the subject isn't identified, redirect to login URL
        if (subject.getPrincipal() == null) {
        	// if request ajax
        	if(ServletUtil.isAjax(WebUtils.toHttp(request))){
        		Map<String, Object> retMap = new HashMap<>();
    			retMap.put("status", Response.ERROR);
    			retMap.put("msg", "您还没有登录");
        		ServletUtil.write(WebUtils.toHttp(response), retMap);
        	}else{
        		saveRequestAndRedirectToLogin(request, response);
        	}
        } else {
            // If subject is known but not authorized, redirect to the unauthorized URL if there is one
            // If no unauthorized URL is specified, just return an unauthorized HTTP status code
            String unauthorizedUrl = getUnauthorizedUrl();
            //SHIRO-142 - ensure that redirect _or_ error code occurs - both cannot happen due to response commit:
            if (StringUtils.hasText(unauthorizedUrl)) {
                WebUtils.issueRedirect(request, response, unauthorizedUrl);
            } else {
                WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
        return false;
    }
}

package com.lew.jlight.web.shiro.filter;

import com.lew.jlight.core.Response;
import com.lew.jlight.web.util.ServletUtil;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;


public class ResourceCheckFilter extends AccessControlFilter {

	private Logger logger = LoggerFactory.getLogger(ResourceCheckFilter.class);

	private String errorUrl;

	public void setErrorUrl(String errorUrl) {
		this.errorUrl = errorUrl;
	}

	@Override
	protected boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object mappedValue) throws Exception {
		Subject subject = getSubject(request, response);
		String url = getPathWithinApplication(request);
		logger.debug("request url is ："+url);
		return "admin".equals(subject.getPrincipal()) || subject.isPermitted(url);
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws Exception {
		HttpServletResponse resp = (HttpServletResponse)response;
		if(ServletUtil.isAjax(WebUtils.toHttp(request))){
			Map<String, Object> retMap = new HashMap<>();
			retMap.put("status", Response.ERROR);
			retMap.put("msg", "no permission");
    		ServletUtil.write(WebUtils.toHttp(response), retMap);
		}else{
			resp.sendRedirect(this.errorUrl);
		}
		return false;
	}

}

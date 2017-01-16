package com.lew.jlight.web.shiro.permission;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.util.AntPathMatcher;
import org.apache.shiro.util.PatternMatcher;

class UrlPermission implements Permission{

	private String url;
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	UrlPermission(String url){
		setUrl(url);
	}

	public boolean implies(Permission p) {
		if( !(p instanceof UrlPermission) ){
			return false;
		}
		UrlPermission up = (UrlPermission) p;
		PatternMatcher patternMatcher = new AntPathMatcher();
		return patternMatcher.matches(this.getUrl(), up.getUrl());
	}

}

package com.lew.jlight.web.shiro.config;

import java.util.Map;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.google.common.collect.Maps;
import com.lew.jlight.web.shiro.UserRealm;
import com.lew.jlight.web.shiro.credentials.RetryLimitHashedCredentialsMatcher;
import com.lew.jlight.web.shiro.filter.FormLoginFilter;
import com.lew.jlight.web.shiro.filter.ResourceCheckFilter;
import com.lew.jlight.web.shiro.permission.UrlPermissionResolver;
import com.lew.jlight.web.util.Constants;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;


@Configuration
public class ShiroConfig{

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        filterRegistration.setEnabled(false);
        filterRegistration.addUrlPatterns("/*");
        filterRegistration.setDispatcherTypes(DispatcherType.REQUEST,DispatcherType.FORWARD);
        return filterRegistration;
    }
    
    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    } 
    
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter() {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager());
        bean.setLoginUrl("/login");
        bean.setSuccessUrl("/index");
        bean.setUnauthorizedUrl("/unauthor");

        Map<String, Filter> filters = Maps.newLinkedHashMap();
        
        ResourceCheckFilter resourceCheckFilter = new ResourceCheckFilter();
        resourceCheckFilter.setErrorUrl("/403.html");
        FormLoginFilter formLoginFilter = new FormLoginFilter();
        filters.put("formLoginFilter", formLoginFilter);
        filters.put("resourceCheckFilter", resourceCheckFilter);

        bean.setFilters(filters);

        Map<String, String> chains = Maps.newLinkedHashMap();
        chains.put("/login", "anon");
        chains.put("/index", "anon,formLoginFilter");
        chains.put("/home", "anon,formLoginFilter");
        chains.put("/doLogin", "anon");
        chains.put("/unauthor", "anon");
        chains.put("/logout", "anon");
        //静态资源过滤
        chains.put("/assets/**", "anon");
        chains.put("/css/**", "anon");
        chains.put("/images/**", "anon");
        chains.put("/js/**", "anon");
        chains.put("/favicon.ico", "anon");

        //错误页面过滤
        chains.put("/401.html", "anon");
        chains.put("/403.html", "anon");
        chains.put("/404.html", "anon");
        chains.put("/error", "anon");
        
        chains.put("/**", "formLoginFilter,resourceCheckFilter");

        bean.setFilterChainDefinitionMap(chains);
        return bean;
    }

    @Bean(name="securityManager")
    public DefaultWebSecurityManager securityManager() {
    	DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(userRealm());
        manager.setSessionManager(sessionManager());
        manager.setCacheManager(cacheManager());
        return manager;
    }
    
    @Bean
    public SessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setGlobalSessionTimeout(1800000);
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setDeleteInvalidSessions(true);
        return sessionManager;
    }

    @Bean
    public UserRealm userRealm() {
    	UserRealm userRealm = new UserRealm();
    	userRealm.setPermissionResolver(urlPermissionResolver());
        return userRealm;
    }
    
    @Bean
    public CacheManager cacheManager(){
    	EhCacheManager cacheManager = new EhCacheManager();
    	cacheManager.setCacheManagerConfigFile("classpath:ehcache-shiro.xml"); 
    	return cacheManager;
    }
		
    @Bean
    public UrlPermissionResolver urlPermissionResolver(){
    	return new UrlPermissionResolver();
    }
    
    @Bean
    public ShiroDialect shiroDialect(){
    	return new ShiroDialect();
    }
    
   /* //若返回会导致注解事务失效
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }*/
}

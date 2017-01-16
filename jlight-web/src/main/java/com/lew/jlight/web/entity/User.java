package com.lew.jlight.web.entity;

import com.lew.jlight.core.BaseEntity;

import java.util.Date;

public class User extends BaseEntity {
	
	private static final long serialVersionUID = 3428451338994126059L;

	private String userId;
    
    private String account;
    
    private String trueName;
    
    private String password;
    
    private Date birth;
    
    private Integer sex;
    
    private String email;
    
    private String mobile;
    
    private Integer errorCount;
    
    private Boolean isLock;
    
    private Date loginTime;
    
    private String loginIp;

	public String getUserId( ) {
		return userId;
	}

	public void setUserId( String userId ) {
		this.userId = userId;
	}

	public String getAccount( ) {
		return account;
	}

	public void setAccount( String account ) {
		this.account = account;
	}

	public String getTrueName( ) {
		return trueName;
	}

	public void setTrueName( String trueName ) {
		this.trueName = trueName;
	}

	public String getPassword( ) {
		return password;
	}

	public void setPassword( String password ) {
		this.password = password;
	}

	public Date getBirth( ) {
		return birth;
	}

	public void setBirth( Date birth ) {
		this.birth = birth;
	}

	public Integer getSex( ) {
		return sex;
	}

	public void setSex( Integer sex ) {
		this.sex = sex;
	}

	public String getEmail( ) {
		return email;
	}

	public void setEmail( String email ) {
		this.email = email;
	}

	public String getMobile( ) {
		return mobile;
	}

	public void setMobile( String mobile ) {
		this.mobile = mobile;
	}

	public Integer getErrorCount( ) {
		return errorCount;
	}

	public void setErrorCount( Integer errorCount ) {
		this.errorCount = errorCount;
	}
	public Boolean getIsLock( ) {
		return isLock;
	}

	public void setIsLock( Boolean isLock ) {
		this.isLock = isLock;
	}

	public Date getLoginTime( ) {
		return loginTime;
	}

	public void setLoginTime( Date loginTime ) {
		this.loginTime = loginTime;
	}

	public String getLoginIp( ) {
		return loginIp;
	}

	public void setLoginIp( String loginIp ) {
		this.loginIp = loginIp;
	}
}

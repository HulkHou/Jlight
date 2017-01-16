package com.lew.jlight.web.entity;

import com.lew.jlight.core.BaseEntity;

public class UserRole extends BaseEntity {
	private static final long serialVersionUID = 267861377230595751L;

	private String userId;
	
	private String roleId;

	public String getUserId( ) {
		return userId;
	}

	public void setUserId( String userId ) {
		this.userId = userId;
	}

	public String getRoleId( ) {
		return roleId;
	}

	public void setRoleId( String roleId ) {
		this.roleId = roleId;
	}
	
}

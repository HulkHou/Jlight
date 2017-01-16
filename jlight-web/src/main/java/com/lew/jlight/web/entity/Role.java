package com.lew.jlight.web.entity;

import com.lew.jlight.core.BaseEntity;

public class Role extends BaseEntity {
	private String roleId;

	private String name;

	private String sign;

	public String getRoleId( ) {
		return roleId;
	}

	public void setRoleId( String roleId ) {
		this.roleId = roleId;
	}

	public String getName( ) {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public String getSign( ) {
		return sign;
	}

	public void setSign( String sign ) {
		this.sign = sign;
	}

}

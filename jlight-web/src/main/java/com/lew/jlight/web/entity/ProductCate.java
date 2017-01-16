package com.lew.jlight.web.entity;

import com.lew.jlight.core.BaseEntity;

import java.util.Date;

public class ProductCate extends BaseEntity {
	
	private String cateName;

	private String cateId;

	private String cateImg;

	private String cateIconImg;

	private String parentId;

	private String isShow;

	private String sortNum;

	private String status;

	public String getCateImg() {
		return cateImg;
	}

	public void setCateImg(String cateImg) {
		this.cateImg = cateImg;
	}

	public String getCateIconImg() {
		return cateIconImg;
	}

	public void setCateIconImg(String cateIconImg) {
		this.cateIconImg = cateIconImg;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	public String getSortNum() {
		return sortNum;
	}

	public void setSortNum(String sortNum) {
		this.sortNum = sortNum;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCateName() {
		return cateName;
	}

	public void setCateName(String cateName) {
		this.cateName = cateName;
	}

	public String getCateId() {
		return cateId;
	}

	public void setCateId(String cateId) {
		this.cateId = cateId;
	}
}

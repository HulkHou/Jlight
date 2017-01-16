package com.lew.jlight.web.service;

import com.lew.jlight.web.entity.User;

public interface LoginService {

	User doLogin(String account, String clientIp);
	
}

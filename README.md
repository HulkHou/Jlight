#Jlight
####项目简介
Jlight是一款基础权限管理系统，其目标主要是降低从零到一的开发成本、学习共同进步。
####功能简介
-  用户管理
-  角色管理
-  菜单管理
-  字典管理
-  登录日志
-  操作日志



####技术简介
#####后台
- spring boot 1.4.2.RELEASE
- apache shiro 1.3.2
- mybatis 3.4.1
- alibaba druid 1.0.26
- thymeleaf 2.1.5.RELEASE
- logback 1.1.7
- guava 19.0
- ehcache 2.5.3

#####前端
- AngularJS v1.4.6
- bootstrap.js v3.0.0
- Layer弹出层
- bootstrap datetimepicker
- JSTree
- select-mutiple

####部署步骤

######环境要求
JDK1.8+ 、MySQL5.5+、Maven3.3+

######本地部署
- 创建数据库jlight，并执行doc/jlight-init.sql
- 运行jlight-web/com/lew/jlight/web/Application的main函数即可
- 访问路径：http://localhost
- 帐号：admin | test  密码：123456

######打包部署
- 执行mvn clean package -Pdev(profile参数根据使用环境而定)，执行后在Jlight-web/target目录生成jlight-web-1.0-SNAPSHOT.zip，解压后内容：
config（Jlight配置文件）、lib（相关依赖包）、jlight.sh、start.bat
**（注意：Linux环境下脚本使用：./jlight.sh start|stop|restart|status|info）**

####相关界面
![输入图片说明](http://git.oschina.net/uploads/images/2016/1210/163213_a72d39e6_4226.jpeg "登录页面")

![输入图片说明](http://git.oschina.net/uploads/images/2016/1210/163223_675283f8_4226.jpeg "主页面")

![输入图片说明](http://git.oschina.net/uploads/images/2016/1210/163340_caa97f09_4226.jpeg "用户页面")

![输入图片说明](http://git.oschina.net/uploads/images/2016/1210/163356_afbbf240_4226.jpeg "角色页面")

![输入图片说明](http://git.oschina.net/uploads/images/2016/1210/163733_e8b146a4_4226.jpeg "在这里输入图片标题")



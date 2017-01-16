truncate table sys_login_log;
truncate table sys_web_log;
delete from sys_dict where parent_id!='#';
delete from sys_user where user_id!='1';
delete from sys_role where role_id!='1';
delete from sys_role_menu where role_id!='1';
delete from sys_user_role where user_id!='1';

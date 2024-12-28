package com.example.rest;

import org.eclipse.microprofile.auth.LoginConfig;

import jakarta.annotation.security.DeclareRoles;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;


/**
 * 声明应用程序中使用的角色。
 * 该注解定义了三个角色："admins"、"managers"和"users"。
 */
@DeclareRoles({ "admins", "managers", "users" })
/**
 * 配置应用程序的登录方式为MP-JWT（MicroProfile JSON Web Token）。
 * 该注解指定了认证方法为MP-JWT。
 */
@LoginConfig(authMethod = "MP-JWT")
/**
 * 定义应用程序的根路径为"/api"。
 * 该注解指定了RESTful服务的基本路径。
 */
@ApplicationPath("/api")
public class RestApplication extends Application {

}
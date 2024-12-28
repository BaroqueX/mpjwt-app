package com.example.rest;

import com.example.biz.UserService;
import com.example.rest.dto.RegInfo;

import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * 提供用户注册功能的REST资源。
 * <p>
 * 通过HTTP POST请求接收注册信息，调用UserService的registerUser方法完成用户注册。
 * 注册成功后返回HTTP状态码200。
 * </p>
 * 
 * @author [Your Name]
 * @version 1.0
 * @since [Date]
 */
@Path("/registration")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RegistrationResource {
    
    @EJB
    private UserService userService;

    @POST
    public Response register(RegInfo regInfo) {
        userService.registerUser(regInfo.username(), regInfo.password(), regInfo.email());
        return Response.ok().build();
    }
}

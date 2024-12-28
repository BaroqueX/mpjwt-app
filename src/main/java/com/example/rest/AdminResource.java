package com.example.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/admins")
public class AdminResource {

    /**
     * 处理HTTP GET请求，返回管理员信息。
     * 该方法使用@RolesAllowed注解限制只有具有"admins"角色的用户才能访问。
     * 如果用户具有相应权限，返回200 OK状态码和欢迎信息；否则返回403 Forbidden状态码。
     *
     * @return 包含欢迎信息的Response对象，如果用户没有权限则包含错误信息
     */
    @GET
    @RolesAllowed("admins")
    public Response getAdminInfo() {
        return Response.ok("Welcome, Admin!").build();
    }
}
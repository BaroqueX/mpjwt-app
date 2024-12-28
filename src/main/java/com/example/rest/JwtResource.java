package com.example.rest;

import com.example.biz.TokenUtil;
import com.example.rest.dto.LoginInfo;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/jwt")
public class JwtResource {

    @Inject
    private TokenUtil tokenUtil;

    /**
     * 处理HTTP POST请求，生成并返回JWT。
     * 该方法接收一个包含用户名和密码的LoginInfo对象，使用TokenUtil生成JWT。
     * 如果生成成功，返回200 OK状态码和JWT；如果认证失败，返回401 Unauthorized状态码和错误信息。
     *
     * @param loginInfo 包含用户名和密码的LoginInfo对象
     * @return 包含JWT的Response对象，如果认证失败则包含错误信息
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response generateJwt(LoginInfo loginInfo) {
        Response response = null;
        // 使用TokenUtil生成JWT
        String jwtToken = tokenUtil.generateJwt(loginInfo.username(), loginInfo.password());

        if (jwtToken == null) {
            // 如果为空，返回401 Unauthorized状态码和错误信息
            response = Response.status(Response.Status.UNAUTHORIZED).entity("认证失败！").build();
        } else {
            // 如果不为空，返回200 OK状态码和JWT
            response = Response.ok(jwtToken).build();
        }
        return response;
    }

}

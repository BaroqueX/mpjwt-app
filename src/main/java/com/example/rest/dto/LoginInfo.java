/**
 * 登录信息数据传输对象（DTO），用于封装用户的登录凭证。
 * 包含用户名和密码两个字段。
 * 
 * @author [Your Name]
 * @version 1.0
 * @since [Date]
 * 
 */
package com.example.rest.dto;

public record LoginInfo(String username, String password) {

}

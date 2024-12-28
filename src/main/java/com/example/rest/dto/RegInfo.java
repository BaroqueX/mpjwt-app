/**
 * RegInfo 类是一个记录类，用于封装用户注册信息。
 * 包含用户名、密码和电子邮件地址。
 * 这个类主要用于数据传输对象（DTO），在REST API中使用。
 * 
 * @author [Your Name]
 * @version 1.0
 * @since [Date]
 * 
 */
package com.example.rest.dto;

public record RegInfo(String username, String password, String email) {

}

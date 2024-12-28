   package com.example.biz;

import com.example.data.UserRepository;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

/**
 * 用户服务类，提供用户注册功能。
 * 使用Pbkdf2PasswordHash对用户密码进行哈希处理，并通过UserRepository将用户信息存储到数据库。
*/

@Stateless
public class UserService {
    @Inject
    private Pbkdf2PasswordHash pbkdf2PasswordHash;

    @EJB
    private UserRepository userRepository;

    /**
     * 注册一个新用户
     *
     * @param username 用户名
     * @param password 密码
     * @param email    电子邮件
     * @return 注册成功的用户对象
     */
    public User registerUser(String username, String password, String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(pbkdf2PasswordHash.generate(password.toCharArray()));
        user.setEmail(email);
        userRepository.add(user);
        return user;
    }
}

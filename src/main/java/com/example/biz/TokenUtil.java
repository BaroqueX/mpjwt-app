package com.example.biz;

import io.jsonwebtoken.Jwts;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import com.example.data.UserEntity;
import com.example.data.UserRepository;

import java.io.FileReader;
import java.security.PrivateKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Stateless
public class TokenUtil {

    @Inject
    private Pbkdf2PasswordHash pbkdf2PasswordHash;

    @Inject
    UserRepository userRepository;

    /**
     * 生成JWT令牌的方法。
     * 该方法接收用户名和密码作为参数，验证用户存在性和密码正确性后，生成并返回JWT令牌。
     * JWT令牌包含用户组信息和用户名（upn），并使用私钥进行签名。
     * 令牌有效期为10天。
     *
     * @param username 用户名
     * @param password 密码
     * @return 生成的JWT令牌，如果用户不存在或密码错误则返回null
     */
    public String generateJwt(String username, String password) {

        String jwtToken = null;

        // 读取私钥
        PrivateKey privateKey = readPrivateKey("/temp/privateKey.pem");

        Optional<UserEntity> user = userRepository.findByUsername(username);

        // 检查用户是否为空
        if (user.isEmpty()) {
            // 如果用户不存在，记录警告日志并返回null
            Logger.getLogger(TokenUtil.class.getName()).warning("用户不存在！");
            return null;
        } else {
            // 验证密码
            if (!pbkdf2PasswordHash.verify(password.toCharArray(), user.get().getPassword())) {
                // 如果密码错误，记录警告日志并返回null
                Logger.getLogger(TokenUtil.class.getName()).warning("密码错误！");
                return null;
            }

            // 初始化Token发行时间
            Date issuedAt = new Date();
            // 设置Token过期时间，有效期为10天
            Date expirationTime = new Date(System.currentTimeMillis() + 864_000_000); // Token有效期为10天

            // 创建存储JWT声明的Map
            Map<String, Object> claims = new HashMap<>();
            // 添加用户组信息到声明中
            claims.put("groups", userRepository.findGroupNamesByUsername(username));
            // 添加用户名到声明中
            claims.put("upn", username);

            // 生成JWT
            jwtToken = Jwts.builder().id(UUID.randomUUID().toString()).issuer("https://auth.example.com")
                    .issuedAt(issuedAt)
                    .expiration(expirationTime).claims(claims)
                    .signWith(privateKey).compact();
        }
        return jwtToken;
    }

    /**
     * 从指定文件路径读取私钥。
     *
     * @param filePath 私钥文件的路径
     * @return 返回读取到的私钥对象，如果读取失败则返回null
     * @throws Exception 如果读取或解析私钥过程中发生错误
     */
    private static PrivateKey readPrivateKey(String filePath) {

        PrivateKey privateKey = null; // 私钥

        try (
                FileReader keyReader = new FileReader(filePath);
                PEMParser pemParser = new PEMParser(keyReader)) {
            // 添加BouncyCastle提供者
            java.security.Security.addProvider(new BouncyCastleProvider());
            // 解析私钥信息
            PrivateKeyInfo privateKeyInfo = (PrivateKeyInfo) pemParser.readObject();
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
            privateKey = converter.getPrivateKey(privateKeyInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return privateKey;
    }

}

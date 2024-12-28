package com.example.data;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.biz.User;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

/**
 * UserRepository 类是一个无状态会话 Bean，用于处理 UserEntity 对象的持久化操作。
 * 它提供了根据 ID 查找用户、根据用户名查找用户以及添加新用户的方法。
 * 在添加用户时，会根据用户的角色（如管理员或经理）将其添加到相应的用户组中。
 * 所有数据库操作都通过 EntityManager 进行管理。
 */
@Stateless
public class UserRepository {

    @PersistenceContext
    EntityManager em;

    public Optional<UserEntity> find(Long id) {
        return Optional.ofNullable(em.find(UserEntity.class, id));
    }

    public Optional<UserEntity> findByUsername(String username) {
        UserEntity userEntity = null;
        try {
            userEntity = em.createQuery("SELECT u FROM UserEntity u WHERE u.username = :username", UserEntity.class)
                .setParameter("username", username)
                .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(userEntity);
    }

    public List<String> findGroupNamesByUsername(String username) {
        TypedQuery<String> query = em.createQuery(
                "SELECT ug.groupname FROM UserGroupEntity ug WHERE ug.username = :username", String.class);
        query.setParameter("username", username);
        return query.getResultList();
    }

    /**
     * 添加用户及其关联的用户组到数据库。
     * 对于每个新用户，首先创建一个UserEntity实例并设置其属性。
     * 然后，为用户创建一个默认的用户组'users'。
     * 如果用户名以'admin_'开头，则还会为用户创建一个'admins'用户组。
     * 如果用户名以'manager_'开头，则还会为用户创建一个'managers'用户组。
     * 所有实体都将通过EntityManager持久化到数据库。
     * 如果在此过程中发生任何异常，将记录错误日志。
     * 
     * @param user 包含用户名、密码和邮箱信息的User对象
     */
    public void add(User user) {

        // 创建UserEntity实例并设置用户信息
        UserEntity entity = new UserEntity();
        entity.setUsername(user.getUsername());
        entity.setPassword(user.getPassword());
        entity.setEmail(user.getEmail());

        try {
            // 创建UserGroupEntity实例，用于将用户添加到"users"组
            UserGroupEntity groupEntity = new UserGroupEntity();
            groupEntity.setUsername(user.getUsername());
            groupEntity.setGroupname("users");
            em.persist(entity);
            em.persist(groupEntity);

            // 判断用户名是否以"admin_"开头，如果是，则创建UserGroupEntity实例并将其添加到"admins"组
            if (user.getUsername().startsWith("admin_")) {
                UserGroupEntity adminGroupEntity = new UserGroupEntity();
                adminGroupEntity.setUsername(user.getUsername());
                adminGroupEntity.setGroupname("admins");
                em.persist(adminGroupEntity);
            }

            // 判断用户名是否以"manager_"开头，如果是，则创建UserGroupEntity实例并将其添加到"managers"组
            if (user.getUsername().startsWith("manager_")) {
                UserGroupEntity managerGroupEntity = new UserGroupEntity();
                managerGroupEntity.setUsername(user.getUsername());
                managerGroupEntity.setGroupname("managers");
                em.persist(managerGroupEntity);
            }

            // 提交事务，确保所有实体操作被执行
            em.flush();
        } catch (Exception e) {
            // 使用日志记录保存操作中的异常
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, "添加用户失败", e);
        }
    }

}

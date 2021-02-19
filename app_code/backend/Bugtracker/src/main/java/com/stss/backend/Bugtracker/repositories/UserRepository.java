package com.stss.backend.Bugtracker.repositories;

import com.stss.backend.Bugtracker.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByEmailAndPassword(String email, String password);
    User findByUserId(long userId);
    User findByName(String name);
    @Query(value="SELECT user.user_id as userId, user.name FROM users user",nativeQuery = true)
    List<GetUserIdAndName> getUserIdAndName();

    @Modifying
    @Query(value = "UPDATE users u SET u.failed_attempt = :failedAttempts WHERE u.email = :email", nativeQuery = true)
    public void updateFailedAttempts(@Param("failedAttempts") Integer failedAttempts, String email);

    @Modifying
    @Query(value = "UPDATE users u SET u.password = :password WHERE u.email = :email", nativeQuery = true)
    public void updatePassword(@Param("password") String password, String email);
    //"UPDATE `bugtracker`.`users` SET `failed_attempt` = '0' WHERE (`user_id` = '32')"
}

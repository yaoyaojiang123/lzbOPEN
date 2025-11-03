package com.ch.personmis.repository;
import com.ch.personmis.entity.UserEntity;
import org.springframework.stereotype.Repository;
@Repository
public interface AdminRepository {
    UserEntity login(UserEntity buser);
}

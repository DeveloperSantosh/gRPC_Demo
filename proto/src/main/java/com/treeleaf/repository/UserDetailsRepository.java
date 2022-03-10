package com.treeleaf.repository;

import com.treeleaf.model.UserDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetailsEntity, Integer> {
    List<UserDetailsEntity> findByGender(String gender);
}

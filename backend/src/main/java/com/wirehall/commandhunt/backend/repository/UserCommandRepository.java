package com.wirehall.commandhunt.backend.repository;

import com.wirehall.commandhunt.backend.model.UserCommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCommandRepository
    extends JpaRepository<UserCommandEntity, Long>, JpaSpecificationExecutor<UserCommandEntity> {

  UserCommandEntity findOneByIdAndUserEmail(Long userCommandId, String userEmail);

  void deleteByIdAndUserEmail(Long userCommandId, String userEmail);
}

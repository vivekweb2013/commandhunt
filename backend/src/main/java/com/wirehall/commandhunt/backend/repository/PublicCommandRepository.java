package com.wirehall.commandhunt.backend.repository;

import com.wirehall.commandhunt.backend.model.PublicCommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicCommandRepository extends JpaRepository<PublicCommandEntity, Long>,
        JpaSpecificationExecutor<PublicCommandEntity> {

    void deleteByIdAndUserEmail(Long id, String userEmail);
}

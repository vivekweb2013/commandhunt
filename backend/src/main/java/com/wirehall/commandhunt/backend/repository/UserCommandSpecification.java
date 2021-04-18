package com.wirehall.commandhunt.backend.repository;

import com.wirehall.commandhunt.backend.model.UserCommandEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class UserCommandSpecification {

  private UserCommandSpecification() {}

  public static Specification<UserCommandEntity> equalsCommandName(String commandName) {
    if (!StringUtils.hasLength(commandName)) {
      return null;
    }
    return (root, query, cb) -> cb.equal(root.get("commandName"), commandName);
  }

  public static Specification<UserCommandEntity> likeCommandText(String commandText) {
    if (!StringUtils.hasLength(commandText)) {
      return null;
    }
    return (root, query, cb) -> cb.like(root.get("commandText"), "%" + commandText + "%");
  }

  public static Specification<UserCommandEntity> equalsUserEmail(String userEmail) {
    return (root, query, cb) -> cb.equal(root.get("userEmail"), userEmail);
  }
}

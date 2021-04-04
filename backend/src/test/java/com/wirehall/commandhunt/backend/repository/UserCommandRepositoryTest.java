package com.wirehall.commandhunt.backend.repository;

import com.wirehall.commandhunt.backend.model.UserCommandEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.Persistence;
import javax.persistence.PersistenceUtil;
import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserCommandRepositoryTest {

  @Autowired
  private UserCommandRepository userCommandRepository;

  @Test
  void should_ReturnEmptyList_When_FindAllOnEmptyTable() {
    List<UserCommandEntity> list = userCommandRepository.findAll();
    assertTrue(list.isEmpty());
  }

  @Test
  @Sql(scripts = {"classpath:sql/InsertSingleUser.sql"})
  void should_InsertOne_When_NullableFieldsAreEmpty() {
    // Saved entity should match with retrieved entity.
    UserCommandEntity userCommandEntity = new UserCommandEntity();
    userCommandEntity.setModifiedOn(null);
    userCommandEntity.setOptions(null);
    userCommandEntity.setFlags(null);
    userCommandEntity.setCommandName("ls");
    userCommandEntity.setCommandText("ls -alt");
    userCommandEntity.setUserEmail("abc@xyz.com");
    userCommandEntity.setCreatedOn(new Timestamp(System.currentTimeMillis()));
    userCommandEntity.setOperatedOn(new Timestamp(System.currentTimeMillis()));

    userCommandRepository.save(userCommandEntity);
    List<UserCommandEntity> userCommandEntities = userCommandRepository.findAll();
    UserCommandEntity uceRetrieved = userCommandEntities.get(0);

    assertAll(
            () -> assertEquals(1, userCommandEntities.size()),
            () -> assertNotNull(userCommandEntity.getId()),
            () -> assertEquals(userCommandEntity.getId(), uceRetrieved.getId()),
            () -> assertEquals(userCommandEntity.getCommandName(), uceRetrieved.getCommandName()),
            () -> assertEquals(userCommandEntity.getCommandText(), uceRetrieved.getCommandText()),
            () -> assertEquals(userCommandEntity.getUserEmail(), uceRetrieved.getUserEmail()),
            () -> assertEquals(userCommandEntity.getCreatedOn(), uceRetrieved.getCreatedOn()),
            () -> assertEquals(userCommandEntity.getOperatedOn(), uceRetrieved.getOperatedOn()),
            () -> assertNull(userCommandEntity.getModifiedOn()),
            () -> assertEquals(userCommandEntity.getOptions(), uceRetrieved.getOptions()),
            () -> assertEquals(userCommandEntity.getFlags(), uceRetrieved.getFlags())
    );
  }

  @Test
  @Sql(scripts = {"classpath:sql/InsertSingleUser.sql"})
  void should_MatchWithRetrievedEntity_When_SameWasInserted() {
    // Saved entity should match with retrieved entity.
    UserCommandEntity userCommandEntity = generateNewUserCommandEntity();

    userCommandRepository.save(userCommandEntity);
    List<UserCommandEntity> userCommandEntities = userCommandRepository.findAll();
    UserCommandEntity uceRetrieved = userCommandEntities.get(0);

    assertAll(
            () -> assertEquals(1, userCommandEntities.size()),
            () -> assertNotNull(userCommandEntity.getId()),
            () -> assertEquals(userCommandEntity.getId(), uceRetrieved.getId()),
            () -> assertEquals(userCommandEntity.getCommandName(), uceRetrieved.getCommandName()),
            () -> assertEquals(userCommandEntity.getCommandText(), uceRetrieved.getCommandText()),
            () -> assertEquals(userCommandEntity.getUserEmail(), uceRetrieved.getUserEmail()),
            () -> assertEquals(userCommandEntity.getCreatedOn(), uceRetrieved.getCreatedOn()),
            () -> assertEquals(userCommandEntity.getModifiedOn(), uceRetrieved.getModifiedOn()),
            () -> assertEquals(userCommandEntity.getOperatedOn(), uceRetrieved.getOperatedOn()),
            () -> assertEquals(userCommandEntity.getOptions(), uceRetrieved.getOptions()),
            () -> assertEquals(userCommandEntity.getFlags(), uceRetrieved.getFlags())
    );
  }

  @Test
  void should_ThrowException_When_InsertedWithoutUserEmail() {
    // Inserting UserCommand without userEmail should fail
    UserCommandEntity userCommandEntity = generateNewUserCommandEntity();
    userCommandEntity.setUserEmail(null);

    Executable executable = () -> userCommandRepository.saveAndFlush(userCommandEntity);
    assertThrows(DataIntegrityViolationException.class, executable);
  }

  @Test
  @Sql(scripts = {"classpath:sql/InsertSingleUser.sql"})
  void should_ThrowException_When_InsertedWithoutCommandName() {
    UserCommandEntity userCommandEntity = generateNewUserCommandEntity();
    userCommandEntity.setCommandName(null);

    Executable executable = () -> userCommandRepository.saveAndFlush(userCommandEntity);
    assertThrows(DataIntegrityViolationException.class, executable);
  }

  @Test
  @Sql(scripts = {"classpath:sql/InsertSingleUser.sql"})
  void should_ThrowException_When_InsertedWithoutCommandText() {
    UserCommandEntity userCommandEntity = generateNewUserCommandEntity();
    userCommandEntity.setCommandText(null);

    Executable executable = () -> userCommandRepository.saveAndFlush(userCommandEntity);
    assertThrows(DataIntegrityViolationException.class, executable);
  }

  @Test
  @Sql(scripts = {"classpath:sql/InsertSingleUser.sql"})
  void should_ThrowException_When_InsertedWithoutCreatedOnField() {
    UserCommandEntity userCommandEntity = generateNewUserCommandEntity();
    userCommandEntity.setCreatedOn(null);

    Executable executable = () -> userCommandRepository.saveAndFlush(userCommandEntity);
    assertThrows(DataIntegrityViolationException.class, executable);
  }

  @Test
  void should_ThrowException_When_UserNotExistWithUserEmail() {
    // Inserting UserCommand with userEmail which does not exist should fail
    UserCommandEntity userCommandEntity = generateNewUserCommandEntity();

    Executable executable = () -> userCommandRepository.saveAndFlush(userCommandEntity);
    // Since we are not adding any user with abc@xyz.com with this test, It should fail
    assertThrows(DataIntegrityViolationException.class, executable);
  }

  private UserCommandEntity generateNewUserCommandEntity() {
    UserCommandEntity userCommandEntity = new UserCommandEntity();
    userCommandEntity.setCommandName("ls");
    userCommandEntity.setCommandText("ls -alt");
    userCommandEntity.setUserEmail("abc@xyz.com");
    userCommandEntity.setCreatedOn(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    userCommandEntity.setModifiedOn(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    userCommandEntity.setOperatedOn(new Timestamp(Calendar.getInstance().getTimeInMillis()));

    // Add flags
    Set<String> flags = new HashSet<>();
    flags.add("-t");
    flags.add("-a");
    flags.add("-l");
    userCommandEntity.setFlags(flags);

    // Add options
    Map<String, List<String>> options = new HashMap<>();
    // Add multivalued option
    List<String> files = new ArrayList<>();
    files.add("/yyy/yyy/yyy.txt");
    files.add("/ooo/ooo/ooo.jpg");
    options.put("FILE", files);
    // Add single values option
    List<String> dirs = new ArrayList<>();
    dirs.add("/zz/vvv/tt");
    options.put("DIRS", dirs);
    userCommandEntity.setOptions(options);

    return userCommandEntity;
  }
}
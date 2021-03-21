package com.wirehall.commandhunt.backend.repository;

import com.wirehall.commandhunt.backend.model.UserCommand;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@EnableTransactionManagement
@ActiveProfiles("dev")
public class UserCommandRepositoryJpaTest {

  @Autowired
  private UserCommandRepositoryJpa userCommandRepository;

  @Test
  @Transactional
  //@Rollback(false)
  public void testCreateUserCommand() {
    UserCommand userCommand = new UserCommand();
    userCommand.setCommandName("ls");
    userCommand.setCommandText("ls -alt");
    userCommand.setUserEmail("adsf@gsdfs.cc");
    userCommand.setTimestamp(new Timestamp(Calendar.getInstance().getTimeInMillis()));

    Map<String, Boolean> flags = new HashMap<>();
    flags.put("-t", true);
    flags.put("-a", true);
    flags.put("-l", true);
    userCommand.setFlags(flags);

    Map<String, List<String>> options = new HashMap<>();
    List<String> files = new ArrayList<>();
    files.add("/yyy/yyy/yyy.txt");
    files.add("/ooo/ooo/ooo.jpg");
    options.put("FILE", files);
    List<String> dirs = new ArrayList<>();
    dirs.add("/yyy/yyy/yyy.txt");
    dirs.add("/ooo/ooo/ooo.jpg");
    options.put("DIRS", dirs);
    userCommand.setOptions(options);
    userCommandRepository.save(userCommand);
    System.out
        .println("########################" + userCommandRepository.findById(userCommand.getId()));
    System.out.println(
        "########################" + userCommandRepository.findById(userCommand.getId()).get()
            .getFlags());
    System.out.println(
        "########################" + userCommandRepository.findById(userCommand.getId()).get()
            .getOptions());
    Assertions.assertNotNull(userCommandRepository.findById(userCommand.getId()));
  }
}
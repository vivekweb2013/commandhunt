package com.wirehall.commandhunt.backend.service;

import com.wirehall.commandhunt.backend.dto.UserCommand;
import com.wirehall.commandhunt.backend.dto.filter.Condition;
import com.wirehall.commandhunt.backend.dto.filter.Filter;
import com.wirehall.commandhunt.backend.dto.filter.PageResponse;
import com.wirehall.commandhunt.backend.dto.filter.Pagination;
import com.wirehall.commandhunt.backend.exception.BadRequestException;
import com.wirehall.commandhunt.backend.model.UserCommandEntity;
import com.wirehall.commandhunt.backend.repository.UserCommandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserCommandServiceTest {
    @Mock
    UserCommandRepository userCommandRepository;

    UserCommandService userCommandService;

    @BeforeEach
    void setUp() {
        userCommandService = new UserCommandService(userCommandRepository);
    }

    @Test
    void should_GetAllUserCommands_When_FilterAndUserEmailProvided() {
        // Given
        Filter filter = generateTestFilter();
        UserCommandEntity uce = new UserCommandEntity();
        Set<String> flags = new HashSet<>();
        flags.add("a");
        flags.add("l");
        uce.setFlags(flags);
        Map<String, List<String>> options = new HashMap<>();
        options.put("DIR", Arrays.asList("/root", "/user"));
        options.put("PATH", Arrays.asList("/a.txt", "/b.jpg"));
        uce.setOptions(options);

        Page<UserCommandEntity> ucePage = new PageImpl<>(Collections.singletonList(uce));
        when(userCommandRepository.findAll(isA(Specification.class), isA(Pageable.class))).thenReturn(ucePage);


        // When
        PageResponse<UserCommand> pageResponse = userCommandService.getAllUserCommands(filter, "abc@xyz.com");

        Pageable pageable = PageRequest.of(// page arg is a index so it starts with zero
                filter.getPagination().getPageNumber() - 1, filter.getPagination().getPageSize(),
                Sort.by(Sort.Direction.valueOf(filter.getPagination().getSort().getOrder().name()),
                        filter.getPagination().getSort().getBy()));

        // Then
        assertTrue(pageResponse.getRecords().get(0).getFlags().isEmpty());
        assertTrue(pageResponse.getRecords().get(0).getOptions().isEmpty());

        assertEquals(pageResponse.getPageNumber(), ucePage.getNumber());
        assertEquals(pageResponse.getPageSize(), filter.getPagination().getPageSize());
        assertEquals(pageResponse.getRecords().size(), ucePage.getTotalElements());

        verify(userCommandRepository).findAll(isA(Specification.class), eq(pageable));
        // Specification can not be easily tested thoroughly in unit test
        // So it needs to be tested with integration tests
    }

    @Test
    void should_GetSingleUserCommand_When_IdAndEmailProvided() {
        // Given
        UserCommandEntity uce = new UserCommandEntity();
        Set<String> flags = new HashSet<>();
        flags.add("a");
        flags.add("l");
        uce.setFlags(flags);
        Map<String, List<String>> options = new HashMap<>();
        options.put("DIR", Arrays.asList("/root", "/user"));
        options.put("PATH", Arrays.asList("/a.txt", "/b.jpg"));
        uce.setOptions(options);
        when(userCommandRepository.findOneByIdAndUserEmail(isA(Long.class), isA(String.class))).thenReturn(uce);

        // When
        UserCommand uc = userCommandService.getUserCommandById(anyLong(), anyString());

        // Then
        assertEquals(2, uc.getFlags().size());
        assertEquals(2, uc.getOptions().size());
        verify(userCommandRepository).findOneByIdAndUserEmail(isA(Long.class), isA(String.class));
    }

    @Test
    void should_AddSingleUserCommand_When_UserCommandAndEmailProvided() {
        // Given
        UserCommandEntity uce = new UserCommandEntity();
        when(userCommandRepository.save(isA(UserCommandEntity.class))).thenReturn(uce);

        // When
        String userEmail = "abc@xyz.com";
        userCommandService.addUserCommand(new UserCommand(), userEmail);

        // Then
        ArgumentCaptor<UserCommandEntity> argument = ArgumentCaptor.forClass(UserCommandEntity.class);
        verify(userCommandRepository).save(argument.capture());
        UserCommandEntity uceSaved = argument.getValue();
        assertEquals(uceSaved.getUserEmail(), userEmail);
        assertNotNull(uceSaved.getCreatedOn());
        assertNotNull(uceSaved.getOperatedOn());
        assertNull(uceSaved.getModifiedOn());
        assertEquals(uceSaved.getCreatedOn(), uceSaved.getOperatedOn());
    }

    @Test
    void should_ThrowExceptionOnAdd_When_UserCommandWithIdProvided() {
        // Given
        UserCommand uc = new UserCommand();
        uc.setId(101L);

        // When
        String userEmail = "abc@xyz.com";
        Executable e = () -> userCommandService.addUserCommand(uc, userEmail);

        // Then
        assertThrows(BadRequestException.class, e);
    }

    @Test
    void should_UpdateSingleUserCommands_When_UserCommandAndEmailProvided() {
        // Given
        UserCommand uc = new UserCommand();
        uc.setId(101L);
        UserCommandEntity uce = new UserCommandEntity();
        when(userCommandRepository.save(isA(UserCommandEntity.class))).thenReturn(uce);

        // When
        String userEmail = "abc@xyz.com";
        userCommandService.updateUserCommand(uc, userEmail);

        // Then
        ArgumentCaptor<UserCommandEntity> argument = ArgumentCaptor.forClass(UserCommandEntity.class);
        verify(userCommandRepository).save(argument.capture());
        UserCommandEntity uceSaved = argument.getValue();
        assertEquals(uceSaved.getUserEmail(), userEmail);
        assertNotNull(uceSaved.getModifiedOn());
        assertNotNull(uceSaved.getOperatedOn());
        assertEquals(uceSaved.getModifiedOn(), uceSaved.getOperatedOn());
    }

    @Test
    void should_ThrowExceptionOnUpdate_When_UserCommandWithoutIdProvided() {
        // Given
        UserCommand uc = new UserCommand();

        // When
        String userEmail = "abc@xyz.com";
        Executable e = () -> userCommandService.updateUserCommand(uc, userEmail);

        // Then
        assertThrows(BadRequestException.class, e);
    }

    @Test
    void should_DeleteSingleUserCommand_When_IdAndEmailProvided() {
        // When
        Executable e = () -> userCommandService.deleteUserCommand(anyLong(), anyString());

        // Then
        assertDoesNotThrow(e);
    }

    private Filter generateTestFilter() {
        Filter filter = new Filter();
        Pagination pagination = new Pagination();
        pagination.setPageNumber(1); // pageNumber field is not index in this dto class, so it starts with 1.
        pagination.setPageSize(10);
        Pagination.Sort sort = new Pagination.Sort();
        sort.setBy(new String[]{"commandName"});
        sort.setOrder(Pagination.Sort.SortOrder.DESC);
        pagination.setSort(sort);
        filter.setPagination(pagination);
        List<Condition> conditions = new ArrayList<>();
        Condition condition = new Condition();
        condition.setKey("commandText");
        condition.setOperator(Condition.Operator.CONTAINS);
        condition.setValue("ls");
        conditions.add(condition);
        filter.setConditions(conditions);
        return filter;
    }
}
package com.wirehall.commandhunt.backend.service;

import com.wirehall.commandhunt.backend.dto.UserCommand;
import com.wirehall.commandhunt.backend.dto.filter.Condition;
import com.wirehall.commandhunt.backend.dto.filter.Filter;
import com.wirehall.commandhunt.backend.dto.filter.PageResponse;
import com.wirehall.commandhunt.backend.dto.filter.Pagination;
import com.wirehall.commandhunt.backend.model.UserCommandEntity;
import com.wirehall.commandhunt.backend.repository.UserCommandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        Filter filter = generateTestFilter();
        Page<UserCommandEntity> userCommandEntityPage = new PageImpl<>(Collections.emptyList());
        when(userCommandRepository.findAll(isA(Specification.class), isA(Pageable.class))).thenReturn(userCommandEntityPage);

        PageResponse<UserCommand> rp = userCommandService.getAllUserCommands(filter, "abc@xyz.com");

        Pageable pageable = PageRequest.of(// page arg is a index so it starts with zero
                filter.getPagination().getPageNumber() - 1, filter.getPagination().getPageSize(),
                Sort.by(Sort.Direction.valueOf(filter.getPagination().getSort().getOrder().name()),
                        filter.getPagination().getSort().getBy()));

        verify(userCommandRepository).findAll(isA(Specification.class), eq(pageable));
        // Specification can not be easily tested thoroughly in unit test
        // so it needs to be tested with integration tests
    }

    private Filter generateTestFilter() {
        Filter filter = new Filter();
        Pagination pagination = new Pagination();
        pagination.setPageNumber(1); // pageNumber field is not index in this dto class, so it starts with 1.
        pagination.setPageSize(10);
        Pagination.Sort sort = new Pagination.Sort();
        sort.setBy("commandName");
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
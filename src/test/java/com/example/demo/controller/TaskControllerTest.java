package com.example.demo.controller;

import com.example.demo.domain.model.Task;
import com.example.demo.domain.model.TaskStatus;
import com.example.demo.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    @Test
    void listShouldReturnTasksOfAuthenticatedUser() throws Exception {
        UUID idUser = UUID.randomUUID();
        Task task = new Task();
        task.setTitle("Estudar Spring");
        task.setIdUser(idUser);
        task.setStatus(TaskStatus.PENDING);

        when(taskService.listTaskByUser(idUser)).thenReturn(List.of(task));

        mockMvc.perform(get("/atividade/list-task").requestAttr("idUser", idUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Estudar Spring"))
                .andExpect(jsonPath("$[0].status").value("PENDING"));

        verify(taskService).listTaskByUser(idUser);
    }

    @Test
    void listShouldReturnEmptyArrayWhenUserHasNoTasks() throws Exception {
        UUID idUser = UUID.randomUUID();
        when(taskService.listTaskByUser(idUser)).thenReturn(List.of());

        mockMvc.perform(get("/atividade/list-task").requestAttr("idUser", idUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}

package ru.job4j.accidents.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.service.AccidentService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccidentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccidentService accidentService;

    @Test
    @WithMockUser
    void whenGetAccidentsIsSuccess() throws Exception {
        this.mockMvc.perform(get("/accidents"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("accidents"));
    }

    @Test
    void whenGetAccidentsIsFailedBecauseNotLogIn() throws Exception {
        this.mockMvc.perform(get("/accidents"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser
    void whenGetEditPageAccidentIsSuccess() throws Exception {
        when(accidentService.findById(any(Integer.class ))).thenReturn(new Accident());

        this.mockMvc.perform(get("/editAccident").param("id", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("editAccident"));
    }

    @Test
    @WithMockUser
    void whenGetEditPageAccidentFailedByWrongId() throws Exception {
        when(accidentService.findById(any(Integer.class )))
                .thenThrow(new IllegalArgumentException("Авария с таким id не найдена"));

        this.mockMvc.perform(get("/editAccident").param("id", "-1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("errors/404"))
                .andExpect(model().attribute("error", "Авария с таким id не найдена"));
    }

    @Test
    @WithMockUser
    void whenGetEditPageAccidentFailedByException() throws Exception {
        when(accidentService.findById(any(Integer.class ))).thenThrow(new RuntimeException("ошибка"));

        this.mockMvc.perform(get("/editAccident").param("id", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("errors/404"))
                .andExpect(model().attribute("error", "Произошло исключение!"));
    }

    @Test
    @WithMockUser
    void whenGetCreatingAccidentPageIsSuccess() throws Exception {
        this.mockMvc.perform(get("/createAccident"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("createAccident"));
    }
}
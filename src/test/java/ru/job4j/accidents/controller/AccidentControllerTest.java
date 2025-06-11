package ru.job4j.accidents.controller;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.service.AccidentService;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        when(accidentService.findById(any(Integer.class))).thenReturn(new Accident());

        this.mockMvc.perform(get("/editAccident").param("id", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("editAccident"));
    }

    @Test
    @WithMockUser
    void whenGetEditPageAccidentFailedByWrongId() throws Exception {
        when(accidentService.findById(any(Integer.class)))
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
        when(accidentService.findById(any(Integer.class))).thenThrow(new RuntimeException("ошибка"));

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

    @Test
    @WithMockUser
    void whenSaveAccidentIsSuccessThenGetAccidentAndRedirect() throws Exception {
        this.mockMvc.perform(post("/saveAccident")
                        .with(csrf())
                        .param("id", "1")
                        .param("name", "name test")
                        .param("text", "text test")
                        .param("address", "address test")
                        .param("type", "1")
                        .param("rIds", "1", "2"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/index"));

        ArgumentCaptor<Accident> accidentArgumentCaptor = ArgumentCaptor.forClass(Accident.class);
        ArgumentCaptor<Set<Integer>> rIdsCaptor = ArgumentCaptor.forClass(Set.class);

        verify(accidentService).create(accidentArgumentCaptor.capture(), rIdsCaptor.capture());

        Accident captured = accidentArgumentCaptor.getValue();
        Set<Integer> rIdsCaptured = rIdsCaptor.getValue();

        assertThat(captured.getName()).isEqualTo("name test");
        assertThat(captured.getText()).isEqualTo("text test");
        assertThat(captured.getAddress()).isEqualTo("address test");

        assertThat(rIdsCaptured).hasSize(2);
        assertThat(rIdsCaptured).containsExactlyInAnyOrder(1, 2);
    }

    @Test
    @WithMockUser
    void whenSaveAccidentFailedByExceptionThenRedirect() throws Exception {
        when(accidentService.create(any(), any())).thenThrow(new RuntimeException("ошибка"));

        this.mockMvc.perform(post("/saveAccident")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("errors/404"))
                .andExpect(model().attribute("error", "Произошла ошибка при сохранении"));
    }

    @Test
    @WithMockUser
    void whenConfirmEditIsFailedByGlobalExceptionThenGetMessageAndErrorPage() throws Exception {
        doThrow(new RuntimeException("Ошибка"))
                .when(accidentService)
                .update(any(), any());

        this.mockMvc.perform(post("/confirmEdit")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("errors/404"))
                .andExpect(model().attribute("error", "Произошла ошибка при редактировании инцидента"));
    }

    @Test
    @WithMockUser
    void whenConfirmEditIsSuccessThenGetIndexPage() throws Exception {
        String errMsg = "Вы не заполнили необходимые поля инцидента";
        doThrow(new IllegalArgumentException(errMsg))
                .when(accidentService)
                .update(any(), any());

        this.mockMvc.perform(post("/confirmEdit")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("errors/404"))
                .andExpect(model().attribute("error", errMsg));
    }
}
package com.marcosviniciusdev.api.controller;

import com.marcosviniciusdev.api.domain.consulta.AgendaDeConsultas;
import com.marcosviniciusdev.api.domain.consulta.DadosAgendamentoConsulta;
import com.marcosviniciusdev.api.domain.consulta.DadosDetalhamentoConsulta;
import com.marcosviniciusdev.api.domain.medico.Especialidade;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ConsultaControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DadosAgendamentoConsulta> dadosAgendamentoConsultaJson;

    @Autowired
    private JacksonTester<DadosDetalhamentoConsulta> dadosDetalhamentoConsultaJson;

    @MockBean
    private AgendaDeConsultas agendaDeConsultas;

    // Teste de erro 404
    @Test
    @DisplayName("Deveria devolver o codigo http 400 quando informacoes estao invalidas.")
    @WithMockUser
    void agendarCenario1() throws Exception {
        var response = mvc.perform(post("/consultas")).andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    // Teste de erro 200
    @Test
    @DisplayName("Deveria devolver o codigo http 200 quando informacoes estao validas.")
    @WithMockUser
    void agendarCenario2() throws Exception {
        var data = LocalDateTime.now().plusHours(1);
        var especialidade = Especialidade.CARDIOLOGIA;
        var dadosDetalhamento = new DadosDetalhamentoConsulta(null, 2l, 1l, data);
        var jsonEsperado = dadosDetalhamentoConsultaJson.write(dadosDetalhamento).getJson();

        when(agendaDeConsultas.agendar(any())).thenReturn(dadosDetalhamento);

        var response = mvc.perform(
                post("/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosAgendamentoConsultaJson.write(new DadosAgendamentoConsulta(2l, 1l, data, especialidade)).getJson())).andReturn().getResponse();

        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }
}
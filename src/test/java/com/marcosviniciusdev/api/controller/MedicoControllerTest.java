package com.marcosviniciusdev.api.controller;

import com.marcosviniciusdev.api.domain.endereco.DadosEndereco;
import com.marcosviniciusdev.api.domain.endereco.Endereco;
import com.marcosviniciusdev.api.domain.medico.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class MedicoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DadosCadastroMedico> dadosCadastroMedicoJacksonTester;

    @Autowired
    private JacksonTester<DadosDetalhamentoMedico> dadosDetalhamentoMedicoJacksonTester;

    @Autowired
    private MedicoRepository repository;

    @Test
    @DisplayName("Deveria devolver o codigo http 400 quando informacoes estao invalidas.")
    @WithMockUser
    void cadastrarCenario1() throws Exception {
        var res = mvc.perform(post("/medicos")).andReturn().getResponse();

        Assertions.assertThat(res.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver o codigo http 400 quando informacoes estao invalidas.")
    @WithMockUser
    void cadastrarCenario2() throws Exception {
        var especialidade = Especialidade.CARDIOLOGIA;
        var endereco = new Endereco("", "", "", "", "", "", "");
        var dadosCadastro = new DadosCadastroMedico(
                "Medico",
                "medico@voll.med",
                "61999999999",
                "123456",
                Especialidade.CARDIOLOGIA,
                dadosEndereco());
        var dadosDetalhamento = new DadosDetalhamentoMedico(
                null,
                dadosCadastro.nome(),
                dadosCadastro.email(),
                dadosCadastro.crm(),
                dadosCadastro.telefone(),
                dadosCadastro.especialidade(),
                new Endereco(dadosCadastro.endereco())
        );

        when(repository.save(any())).thenReturn(new Medico(dadosCadastro));

        var res = mvc.perform(post("/medicos").contentType(MediaType.APPLICATION_JSON).content(dadosCadastroMedicoJacksonTester.write(dadosCadastro).getJson())).andReturn().getResponse();
        var jsonEsperado = dadosDetalhamentoMedicoJacksonTester.write(dadosDetalhamento).getJson();

        Assertions.assertThat(res.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        Assertions.assertThat(res.getContentAsString()).isEqualTo(jsonEsperado);
    }

    private DadosEndereco dadosEndereco() {
        return new DadosEndereco(
                "rua xpto",
                "bairro",
                "00000000",
                "Brasilia",
                "DF",
                null,
                null
        );
    }
}
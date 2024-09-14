package com.marcosviniciusdev.api.domain.consulta.validacoes;

import com.marcosviniciusdev.api.domain.ValidacaoException;
import com.marcosviniciusdev.api.domain.consulta.DadosAgendamentoConsulta;
import com.marcosviniciusdev.api.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorPacienteAtivo implements ValidadorAgendamentoDeConsulta {

    @Autowired
    private PacienteRepository repository;

    public void validar(DadosAgendamentoConsulta dados) {
        var pacienteEstaAtivo = repository.findAtivoById(dados.idPaciente());

        if (!pacienteEstaAtivo) {
            throw new ValidacaoException("O paciente está com cadastro inativo.");
        }
    }
}

package com.marcosviniciusdev.api.domain.consulta.validacoes;

import com.marcosviniciusdev.api.domain.ValidacaoException;
import com.marcosviniciusdev.api.domain.consulta.DadosAgendamentoConsulta;
import com.marcosviniciusdev.api.domain.medico.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorMedicoAtivo implements ValidadorAgendamentoDeConsulta {

    @Autowired
    private MedicoRepository repository;

    public void validar(DadosAgendamentoConsulta dados) {
        var medicoEstaAtivo = repository.findAtivoById(dados.idMedico());

        if (dados.idMedico() == null) {
            return;
        }

        if (!medicoEstaAtivo) {
            throw new ValidacaoException("Consulta não pode ser agendada com médico inativo.");
        }
    }

}

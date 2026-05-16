package com.odontotrack.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.odontotrack.api.model.AgendamentoConsulta;
import com.odontotrack.api.model.StatusConsulta;

@Repository
public interface AgendamentoRepository extends JpaRepository<AgendamentoConsulta, Long> {

    // Agora traz o histórico ordenado da consulta mais recente para a mais antiga
    List<AgendamentoConsulta> findAllByPacienteIdOrderByDataInicioDesc(Long pacienteId);

    List<AgendamentoConsulta> findAllByProfissionalId(Long profissionalId);

    List<AgendamentoConsulta> findAllByStatusConsulta(StatusConsulta statusConsulta);

    // Conta a quantidade de consultas por status para um dentista específico
    long countByProfissionalIdAndStatusConsulta(Long profissionalId, StatusConsulta status);
}

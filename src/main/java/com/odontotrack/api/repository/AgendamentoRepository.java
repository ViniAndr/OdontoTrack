package com.odontotrack.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.odontotrack.api.model.AgendamentoConsulta;
import com.odontotrack.api.model.StatusConsulta;

@Repository
public interface AgendamentoRepository extends JpaRepository<AgendamentoConsulta, Long> {

    List<AgendamentoConsulta> findAllByPacienteId(Long pacienteId);

    List<AgendamentoConsulta> findAllByProfissionalId(Long profissionalId);

    List<AgendamentoConsulta> findAllByStatusConsulta(StatusConsulta statusConsulta);
}

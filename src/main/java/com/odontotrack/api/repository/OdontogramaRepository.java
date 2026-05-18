package com.odontotrack.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.odontotrack.api.model.Odontograma;

@Repository
public interface OdontogramaRepository extends JpaRepository<Odontograma, Long> {

    List<Odontograma> findAllByPacienteId(Long pacienteId);

    Optional<Odontograma> findByAgendamentoId(Long agendamentoId);

    boolean existsByAgendamentoId(Long agendamentoId);
}

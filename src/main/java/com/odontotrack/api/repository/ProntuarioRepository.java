package com.odontotrack.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.odontotrack.api.model.ProntuarioClinicos;

@Repository
public interface ProntuarioRepository extends JpaRepository<ProntuarioClinicos, Long> {

    List<ProntuarioClinicos> findAllByPacienteId(Long pacienteId);
}

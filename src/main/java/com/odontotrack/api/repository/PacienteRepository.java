package com.odontotrack.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.odontotrack.api.model.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    List<Paciente> findAllByAtivoTrue();

    Optional<Paciente> findByCpf(String cpf);

    Optional<Paciente> findByEmail(String email);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);
}

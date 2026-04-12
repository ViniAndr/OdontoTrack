package com.odontotrack.api.repository;

import com.odontotrack.api.model.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {

    Optional<Profissional> findByCpf(String cpf);

    List<Profissional> findAllByAtivoTrue();

    Optional<Profissional> findByEmail(String email);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

}

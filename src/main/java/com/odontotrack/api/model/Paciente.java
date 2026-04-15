package com.odontotrack.api.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pacientes")
@Getter
@Setter
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @NotBlank
    @Column(unique = true, nullable = false, length = 11)
    private String cpf;

    @NotBlank
    @Column(nullable = false, length = 14)
    private String telefone;

    @NotBlank
    @Column(nullable = false)
    private String email;

    @NotNull
    @Column(nullable = false)
    private String endereco;

    @NotNull
    @Column(nullable = false)
    private LocalDate dataNascimento;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime dataCadastro;

    @Column(nullable = false)
    private Boolean ativo = true;

}

package com.odontotrack.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "profissionais")
@Data
public class Profissional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @NotBlank
    @Column(unique = true, nullable = false, length = 11)
    private String cpf;

    private String registroProfissional; // Ex: CRO

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Cargo cargo;

    // Lista de especialidades
    @ElementCollection
    @CollectionTable(name = "profissional_especialidades", joinColumns = @JoinColumn(name = "profissional_id"))
    @Column(name = "especialidade")
    private List<String> especialidades;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String telefone;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String email;

    // --- CAMPOS DE AUDITORIA ---

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    private LocalDateTime dataAtualizacao;

    private LocalDateTime dataDeletado; // Se for null, o usuário está ativo

    private Boolean ativo = true;
}

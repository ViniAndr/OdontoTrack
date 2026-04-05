package com.odontotrack.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    // Sistema de Permissões de Acesso
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "profissional_perfis", joinColumns = @JoinColumn(name = "profissional_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "perfil")
    private Set<Perfil> perfis = new HashSet<>();

    // Especialidades Odontológicas (Ortodontia, etc)
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

    @NotBlank
    @Column(nullable = false)
    private String senha;

    // --- CAMPOS DE AUDITORIA ---

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    private LocalDateTime dataAtualizacao;

    private LocalDateTime dataDeletado;

    private Boolean ativo = true;
}
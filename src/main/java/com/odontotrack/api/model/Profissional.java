package com.odontotrack.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "profissionais")
@Getter
@Setter
public class Profissional implements UserDetails {

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



    // Ensina o Spring a ler os nossos Cargos (ROLE_ADMIN, ROLE_DENTISTA)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.perfis.stream()
                .map(perfil -> new SimpleGrantedAuthority(perfil.name()))
                .collect(Collectors.toList());
    }

    // Ensina o Spring onde está a senha criptografada
    @Override
    public String getPassword() {
        return this.senha;
    }

    // Ensina o Spring que o nosso "Username" na verdade é o E-mail
    @Override
    public String getUsername() {
        return this.email;
    }

    // Conta não expirada?
    @Override
    public boolean isAccountNonExpired() {
        return true; // Vamos simplificar e dizer que as contas nunca expiram
    }

    // Conta não bloqueada?
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Credenciais não expiradas?
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Usuário está ativo? (Aqui usamos o nosso próprio campo 'ativo')
    @Override
    public boolean isEnabled() {
        return this.ativo;
    }
}
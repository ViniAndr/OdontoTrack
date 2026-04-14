package com.odontotrack.api.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "prontuarios_clinicos")
@Getter
@Setter
public class ProntuarioClinicos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String alergias_historico;

    @Column(columnDefinition = "JSON", nullable = true)
    private String estado_odontograma;

    @Column(nullable = true)
    private LocalDateTime data_ultima_atualizacao;
}

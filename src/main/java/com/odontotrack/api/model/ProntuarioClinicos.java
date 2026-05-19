package com.odontotrack.api.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
    private Long id;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @OneToOne
    @JoinColumn(name = "agendamento_id", nullable = false, unique = true)
    private AgendamentoConsulta agendamento;

    @OneToOne
    @JoinColumn(name = "odontogram_id", nullable = true)
    private Odontograma odontograma;

    @Column(columnDefinition = "TEXT")
    private String alergiasHistorico;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime dataUltimaAtualizacao;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String queixaPrincipal;
    
    @Column(columnDefinition = "TEXT", nullable = true)
    private String achadoClinico;
    
    @Column(columnDefinition = "TEXT", nullable = true)
    private String materialUsado;
    
    @Column(columnDefinition = "TEXT", nullable = true)
    private String orientacoesPaciente;

}

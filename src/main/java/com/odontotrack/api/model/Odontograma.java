package com.odontotrack.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "odontogramas")
@Getter
@Setter
public class Odontograma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agendamento_id", nullable = false)
    private AgendamentoConsulta agendamento;

    @OneToMany(mappedBy = "odontograma", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OdontogramaItem> itens = new ArrayList<>();
}
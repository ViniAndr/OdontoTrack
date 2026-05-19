package com.odontotrack.api.model;

import com.odontotrack.api.Types.Dentes;
import com.odontotrack.api.Types.StatusDente;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "odontogramas_itens")
@Getter
@Setter
public class OdontogramaItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "odontograma_id", nullable = false)
    private Odontograma odontograma;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Dentes dente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusDente statusDente;
}

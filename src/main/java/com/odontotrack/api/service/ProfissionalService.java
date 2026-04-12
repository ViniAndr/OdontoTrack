package com.odontotrack.api.service;

import com.odontotrack.api.model.Perfil;
import com.odontotrack.api.model.Profissional;
import com.odontotrack.api.repository.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfissionalService {

    @Autowired
    private ProfissionalRepository repository;

    public Profissional salvar(Profissional profissional) {
        if (repository.findByCpf(profissional.getCpf()).isPresent()) {
            throw new RuntimeException("Já existe um profissional cadastrado com este CPF.");
        }

        // Verifica se é DENTISTA e se o CRO está preenchido
        if (profissional.getPerfis().contains(Perfil.ROLE_DENTISTA) &&
                (profissional.getRegistroProfissional() == null || profissional.getRegistroProfissional().isBlank())) {
            throw new RuntimeException("O registro profissional (CRO) é obrigatório para dentistas.");
        }

        return repository.save(profissional);
    }

    public List<Profissional> listarTodosAtivos() {
        return repository.findAllByAtivoTrue();
    }
}

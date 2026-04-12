package com.odontotrack.api.config;

import com.odontotrack.api.model.Perfil;
import com.odontotrack.api.model.Profissional;
import com.odontotrack.api.repository.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements CommandLineRunner {

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... arg) throws Exception{
        // Verifica se o banco já possui algum registro. Se estiver vazio, cria o Admin.
        if (profissionalRepository.count() == 0) {
            Profissional admin = new Profissional();
            admin.setNome("Administrador do Sistema");
            admin.setCpf("00000000000"); // 11 dígitos
            admin.setTelefone("00000000000");
            admin.setEmail("admin@odontotrack.com");
            admin.setSenha(passwordEncoder.encode("123456"));

            // Adiciona o perfil de Administrador
            admin.getPerfis().add(Perfil.ROLE_ADMIN);

            profissionalRepository.save(admin);
            System.out.println("✅ Usuário Admin padrão criado com sucesso!");
        } else {
            System.out.println("⚡ Banco de dados já populado. Nenhuma ação necessária no Seeder.");
        }
    }
}

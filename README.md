# 🦷 OdontoTrack - Sistema de Gestão Odontológica

O **OdontoTrack** é um sistema de gerenciamento para clínicas odontológicas desenvolvido como projeto acadêmico. O objetivo é facilitar o controle de pacientes, agendamentos e prontuários digitais.

## 🚀 Tecnologias Utilizadas
* **Java 17/21** (Linguagem principal)
* **Spring Boot 3** (Framework para o Backend)
* **Spring Data JPA** (Persistência de dados)
* **MySQL** (Banco de dados relacional)
* **Lombok** (Produtividade no código)
* **Spring Security** (Autenticação e Autorização)

## 📁 Estrutura do Projeto (Camadas)
O projeto segue o padrão de arquitetura em camadas para melhor organização e manutenção:
* `controller`: Responsável por receber as requisições HTTP (API REST).
* `service`: Contém as regras de negócio do sistema.
* `repository`: Interface de comunicação direta com o banco de dados.
* `model/domain`: Representação das entidades do banco de dados (ex: Pacientes, Consultas).
* `dto`: Objetos de transferência de dados para segurança da API.

## 🛠️ Como rodar o projeto
1. Clone o repositório: `git clone https://github.com/seu-usuario/odontotrack.git`
2. Certifique-se de ter o **MySQL** instalado e rodando.
3. Crie um banco de dados chamado `odontotrack_db`.
4. Configure suas credenciais de banco no arquivo `src/main/resources/application.properties`.
5. Execute o projeto via IntelliJ ou através do terminal com `./mvnw spring-boot:run`.

## 📌 Funcionalidades Planejadas
- [ ] Cadastro e Gestão de Pacientes
- [ ] Sistema de Login (JWT)
- [ ] Agenda de Consultas
- [ ] Prontuário Digital (Odontograma)
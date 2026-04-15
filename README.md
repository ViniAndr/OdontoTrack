# 🦷 OdontoTrack - Sistema de Gestão Odontológica

O **OdontoTrack** é uma API REST desenvolvida para modernizar a gestão de clínicas odontológicas. O sistema permite o controle centralizado de pacientes, profissionais, agendamentos e prontuários clínicos, utilizando práticas modernas de segurança e arquitetura de software.

## 🚀 Tecnologias Utilizadas
* **Java 21**: Linguagem principal do ecossistema.
* **Spring Boot 3/4**: Framework para agilidade no desenvolvimento do backend.
* **Spring Security & JWT**: Autenticação e autorização baseada em tokens para segurança *stateless*.
* **Spring Data JPA**: Abstração para persistência de dados.
* **MySQL**: Banco de dados relacional para produção.
* **H2 Database**: Banco em memória utilizado para desenvolvimento e testes rápidos.
* **Flyway**: Gerenciamento de migrações do banco de dados.
* **Lombok**: Redução de código boilerplate em modelos e DTOs.

## 📋 Pré-requisitos
* Java JDK 21 ou superior.
* Maven (ou utilizar o wrapper `./mvnw`).
* MySQL instalado (opcional, se usar o perfil H2).

## 🛠️ Como Rodar o Projeto

1. **Clonar o repositório:**
   ```bash
   git clone https://github.com/viniandr/odontotrack.git
   cd odontotrack
   ```

2. **Configuração do Banco de Dados:**
   * Por padrão, o projeto está configurado para usar o banco em memória **H2**.
   * Para usar MySQL, altere as credenciais no arquivo `src/main/resources/application.properties`.

3. **Execução:**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Acesso Inicial:**
   O sistema possui um **Seeder** automático que cria um administrador inicial se o banco estiver vazio:
   * **Login:** `admin@odontotrack.com`
   * **Senha:** `123456`

---

## 📡 Exemplos de Requisições (Endpoints)

### 1. Autenticação (Login)
Para acessar as rotas protegidas, você deve primeiro obter um Token JWT.

* **POST** `/profissionais/login`
* **Body:**
```json
{
  "email": "admin@odontotrack.com",
  "senha": "123456"
}
```

### 2. Cadastro de Paciente
* **POST** `/pacientes`
* **Header:** `Authorization: Bearer {seu_token_aqui}`
* **Body:**
```json
{
  "nome": "João Exemplo",
  "cpf": "12345678901",
  "telefone": "82999999999",
  "email": "joao@exemplo.com",
  "endereco": "Rua Central, 123",
  "dataNascimento": "1990-01-01"
}
```

### 3. Agendamento de Consulta
* **POST** `/agendamentos`
* **Body:**
```json
{
  "pacienteId": 1,
  "profissionalId": 1,
  "dataInicio": "2026-05-10T14:00:00",
  "dataFim": "2026-05-10T15:00:00",
  "statusConsulta": "AGENDADO"
}
```

### 4. Registro de Prontuário
* **POST** `/prontuarios`
* **Body:**
```json
{
  "pacienteId": 1,
  "alergiasHistorico": "Alergia a iodo.",
  "estadoOdontograma": "{\"dente_11\": \"restaurado\"}"
}
```

---

## 🔐 Segurança e Perfis de Acesso
A API utiliza controle de acesso baseado em cargos (Roles):
* **ROLE_ADMIN**: Acesso total ao sistema, incluindo gestão de profissionais.
* **ROLE_DENTISTA**: Gestão de pacientes, prontuários e visualização de agenda.
* **ROLE_RECEPCIONISTA**: Gestão de agendamentos e cadastros básicos.

## 📁 Estrutura do Projeto
* `controller`: Camada de entrada das requisições HTTP.
* `service`: Concentra as regras de negócio.
* `repository`: Interfaces para operações no banco de dados.
* `model`: Entidades de domínio do sistema.
* `dto`: Objetos de transferência para comunicação segura.
* `security`: Filtros e configurações de segurança JWT.

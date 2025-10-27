# Fintech API - Sistema de Gestão Financeira

API RESTful para gestão financeira pessoal e empresarial desenvolvida como projeto acadêmico.

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-green)
![Oracle](https://img.shields.io/badge/Oracle-Database-red)
![Status](https://img.shields.io/badge/Status-Em%20Desenvolvimento-yellow)

---

## Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Tecnologias](#tecnologias)
- [Pré-requisitos](#pré-requisitos)
- [Instalação e Execução](#instalação-e-execução)

---

## Sobre o Projeto

Este projeto é uma API REST para gestão financeira que permite:

- ✅ Cadastro e autenticação de usuários (PF e PJ)
- 🔜 Gerenciamento de receitas
- 🔜 Gerenciamento de despesas
- 🔜 Controle de contas bancárias
- 🔜 Categorização de transações
- 🔜 Relatórios financeiros

---

## Tecnologias

### Backend

- **Java 21**
- **Spring Boot 3.5.7**
  - Spring Data JPA
  - Spring Security
  - Spring Web
  - Spring Validation
- **Oracle JDBC Driver**
- **BCrypt** (hash de senhas)

### Banco de Dados

- **Oracle Database** (Docker para desenvolvimento)

### Build & Deploy

- **Maven**
- **Docker** (banco de dados)

---

## Pré-requisitos

Antes de começar, você precisa ter instalado:

- **Java 21** ou superior
- **Maven 3.8+**
- **Docker** e **Docker Compose**
- **Git**
- **Postman** ou **Insomnia** (para testes)

---

## Instalação e Execução

### 1. Clonar o Repositório

```bash
git clone https://github.com/seu-usuario/fintech-api.git
cd fintech-api
```

### 2. Subir o Banco de Dados Oracle (Docker)

```bash
docker-compose up -d
```

Aguarde alguns minutos para o Oracle inicializar completamente.

### 3. Configurar o Banco de Dados

As configurações estão em `src/main/resources/application.properties`:

```properties
# Banco de dados local (Docker)
spring.datasource.url=jdbc:oracle:thin:@//localhost:1521/FREEPDB1
spring.datasource.username=fitechapp
spring.datasource.password=201125
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
```

### 4. Compilar o Projeto

```bash
./mvnw clean compile
```

### 5. Executar a Aplicação

```bash
./mvnw spring-boot:run
```

A aplicação estará disponível em: **http://localhost:8080**

### 6. Testar a API

```bash
curl http://localhost:8080/health
```

Resposta esperada:

```json
{
  "environment": "local",
  "application": "Fintech API",
  "message": "Sistema de autenticação operacional",
  "version": "0.0.1-SNAPSHOT",
  "status": "OK",
  "timestamp": "2025-10-26T22:45:05.056374173"
}
```

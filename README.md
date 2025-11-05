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
- ✅ Gerenciamento de categorias
- ✅ Gerenciamento de subcategorias
- ✅ Gerenciamento de receitas
- ✅ Gerenciamento de despesas

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
- **Docker** (banco de dados Oracle)

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

### 2. Configurar o Banco de Dados Oracle

As configurações estão em `src/main/resources/application.properties`:

```properties
# Banco de dados local (Docker)
spring.datasource.url=jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl
spring.datasource.username=RMxxxxxx
spring.datasource.password=ddmmyy
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
```

### 3. Compilar o Projeto

```bash
mvn clean compile
```

### 4. Executar a Aplicação

```bash
mvn spring-boot:run
```

A aplicação estará disponível em: **http://localhost:8080**

### 5. Testar a API

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

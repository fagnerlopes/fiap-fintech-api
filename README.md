# 💰 Fintech API - Sistema de Gestão Financeira

API RESTful para gestão financeira pessoal e empresarial desenvolvida como projeto acadêmico.

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-green)
![Oracle](https://img.shields.io/badge/Oracle-Database-red)
![Status](https://img.shields.io/badge/Status-Em%20Desenvolvimento-yellow)

---

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Tecnologias](#tecnologias)
- [Pré-requisitos](#pré-requisitos)
- [Instalação e Execução](#instalação-e-execução)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Fases de Desenvolvimento](#fases-de-desenvolvimento)
- [Endpoints da API](#endpoints-da-api)
- [Testes](#testes)
- [Documentação](#documentação)

---

## 📖 Sobre o Projeto

Este projeto é uma API REST para gestão financeira que permite:

- ✅ Cadastro e autenticação de usuários (PF e PJ)
- 🔜 Gerenciamento de receitas
- 🔜 Gerenciamento de despesas
- 🔜 Controle de contas bancárias
- 🔜 Categorização de transações
- 🔜 Relatórios financeiros

### Requisitos Acadêmicos

- ✅ Mínimo 3 entidades completas (Model, Repository, Service, Controller)
- ✅ Todos os verbos HTTP (GET, POST, PUT, DELETE)
- ✅ Status HTTP corretos para cada operação
- ✅ Conexão com Oracle Database
- ✅ Spring Security para autenticação

---

## 🚀 Tecnologias

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

## 📦 Pré-requisitos

Antes de começar, você precisa ter instalado:

- **Java 21** ou superior
- **Maven 3.8+**
- **Docker** e **Docker Compose**
- **Git**
- **Postman** ou **Insomnia** (para testes)

---

## 🔧 Instalação e Execução

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
  "status": "OK",
  "message": "Fintech API está funcionando",
  "fase": "FASE 2 - Sistema de Autenticação completo"
}
```

---

## 📁 Estrutura do Projeto

```
fintech-api/
├── docs/                           # Documentação
│   ├── DDL_FINTECH.sql            # Script do banco de dados
│   ├── ESCOPO_PROJETO.md          # Escopo do projeto
│   └── FASE2_IMPLEMENTADA.md      # Documentação da FASE 2
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── br/com/fintech/fintechapi/
│   │   │       ├── config/        # Configurações (Security, CORS)
│   │   │       ├── controller/    # REST Controllers
│   │   │       ├── exception/     # Exceções e handlers
│   │   │       ├── model/         # Entidades JPA
│   │   │       ├── repository/    # Repositories JPA
│   │   │       └── service/       # Serviços (regras de negócio)
│   │   └── resources/
│   │       └── application.properties
│   └── test/                       # Testes
├── .cursor/
│   └── rules/                      # Regras e checklists
├── docker-compose.yml              # Docker para Oracle
├── pom.xml                         # Dependências Maven
├── POSTMAN_COLLECTION.md           # Collection para testes
└── README.md                       # Este arquivo
```

---

## 🎯 Fases de Desenvolvimento

### ✅ FASE 1: Configuração Inicial

- [x] Configuração do projeto Spring Boot
- [x] Configuração do banco de dados Oracle (Docker)
- [x] Estrutura de pacotes

### ✅ FASE 2: Sistema de Autenticação

- [x] Entidades: Usuario, PessoaFisica, PessoaJuridica
- [x] Repositories e Services
- [x] Spring Security configurado
- [x] Controllers de autenticação
- [x] Exception handling global
- [x] **Compilação bem-sucedida**

📄 **Ver documentação completa:** [docs/FASE2_IMPLEMENTADA.md](docs/FASE2_IMPLEMENTADA.md)

### 🔜 FASE 3: Entidade Receita (CRUD Completo)

- [ ] Model: Receita, Categoria, Subcategoria
- [ ] Repository, Service e Controller
- [ ] Validações de negócio

### 🔜 FASE 4: Entidade Despesa (CRUD Completo)

- [ ] Model: Despesa
- [ ] Repository, Service e Controller
- [ ] Validações de negócio

### 🔜 FASE 5: Entidade Conta Bancária

- [ ] Model: ContaBancaria
- [ ] Repository, Service e Controller
- [ ] Regra de conta padrão

### 🔜 FASE 6: Testes e Documentação

- [ ] Testes unitários
- [ ] Testes de integração
- [ ] Collection Postman completa
- [ ] Swagger/OpenAPI

### 🔜 FASE 7: Deploy para Produção

- [ ] Migração para Oracle FIAP
- [ ] Configurações de produção
- [ ] Validação final

---

## 🔌 Endpoints da API

### 📌 Endpoints Públicos (Sem Autenticação)

| Método | Endpoint             | Descrição              | Status      |
| ------ | -------------------- | ---------------------- | ----------- |
| GET    | `/health`            | Health check da API    | 200 OK      |
| GET    | `/`                  | Home da API            | 200 OK      |
| POST   | `/api/auth/registro` | Registrar novo usuário | 201 Created |
| POST   | `/api/auth/login`    | Login de usuário       | 200 OK      |

### 🔒 Endpoints Protegidos (Requerem Autenticação)

| Método | Endpoint             | Descrição                  | Status         |
| ------ | -------------------- | -------------------------- | -------------- |
| GET    | `/api/usuarios/me`   | Buscar usuário autenticado | 200 OK         |
| GET    | `/api/usuarios`      | Listar todos os usuários   | 200 OK         |
| GET    | `/api/usuarios/{id}` | Buscar usuário por ID      | 200 OK         |
| PUT    | `/api/usuarios/{id}` | Atualizar usuário          | 200 OK         |
| DELETE | `/api/usuarios/{id}` | Deletar usuário            | 204 No Content |

📄 **Ver exemplos de uso:** [POSTMAN_COLLECTION.md](POSTMAN_COLLECTION.md)

---

## 🧪 Testes

### Testar com cURL

**Health Check:**

```bash
curl http://localhost:8080/health
```

**Registrar Usuário:**

```bash
curl -X POST http://localhost:8080/api/auth/registro \
  -H "Content-Type: application/json" \
  -d '{
    "email": "teste@email.com",
    "senha": "senha123",
    "tipoUsuario": "PF",
    "pessoaFisica": {
      "nome": "Teste User",
      "cpf": "123.456.789-00",
      "dataNasc": "1990-01-01"
    }
  }'
```

**Login:**

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "teste@email.com",
    "senha": "senha123"
  }'
```

**Buscar Usuário Autenticado:**

```bash
curl -X GET http://localhost:8080/api/usuarios/me \
  -u teste@email.com:senha123
```

### Testar com Postman

Importe a collection disponível em [POSTMAN_COLLECTION.md](POSTMAN_COLLECTION.md)

---

## 📚 Documentação

### Documentos Disponíveis

- **[FASE2_IMPLEMENTADA.md](docs/FASE2_IMPLEMENTADA.md)** - Documentação completa da FASE 2
- **[POSTMAN_COLLECTION.md](POSTMAN_COLLECTION.md)** - Collection para testes
- **[DDL_FINTECH.sql](docs/DDL_FINTECH.sql)** - Script do banco de dados
- **[ESCOPO_PROJETO.md](docs/ESCOPO_PROJETO.md)** - Escopo do projeto acadêmico

### Status HTTP Utilizados

| Código | Significado           | Quando Usar            |
| ------ | --------------------- | ---------------------- |
| 200    | OK                    | GET, PUT bem-sucedidos |
| 201    | Created               | POST bem-sucedido      |
| 204    | No Content            | DELETE bem-sucedido    |
| 400    | Bad Request           | Dados inválidos        |
| 401    | Unauthorized          | Não autenticado        |
| 403    | Forbidden             | Sem permissão          |
| 404    | Not Found             | Recurso não encontrado |
| 409    | Conflict              | Dados duplicados       |
| 500    | Internal Server Error | Erro no servidor       |

---

## 🔒 Segurança

- ✅ Senhas hasheadas com **BCrypt**
- ✅ Autenticação **HTTP Basic** (Spring Security)
- ✅ Senhas **nunca expostas** em respostas JSON
- ✅ **CORS** configurado para desenvolvimento
- ✅ Validações de **email, CPF e CNPJ únicos**
- ✅ **Exception handling** global

---

## 👥 Autores

**Equipe de Desenvolvimento**

- Desenvolvimento: [Seu Nome]
- Orientação: Prof. [Nome do Professor]
- Instituição: FIAP

---

## 📝 Licença

Este projeto é um trabalho acadêmico desenvolvido para fins educacionais.

---

## 🤝 Contribuindo

Este é um projeto acadêmico, mas sugestões são bem-vindas!

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

---

## 📞 Contato

Para dúvidas ou sugestões, entre em contato:

- Email: [seu-email@example.com]
- GitHub: [@seu-usuario](https://github.com/seu-usuario)

---

## 🎉 Agradecimentos

- FIAP pela orientação e infraestrutura
- Comunidade Spring Boot
- Equipe de desenvolvimento

---

**Última atualização:** 26/10/2025 - 19:50  
**Versão:** 0.0.1-SNAPSHOT  
**Status:** ✅ FASE 2 Concluída - Sistema de Autenticação Funcionando

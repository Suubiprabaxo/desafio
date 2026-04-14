# 📊 API de Gerenciamento de Portfólio de Projetos

API REST desenvolvida em **Java + Spring Boot** para gerenciamento de projetos, membros e geração de relatórios de portfólio.

---

## 🚀 Tecnologias utilizadas

* Java 8+
* Spring Boot
* Spring Data JPA
* Hibernate
* Maven
* JUnit 5 + Mockito
* Swagger (OpenAPI)

---

## 📁 Estrutura do projeto

```
com.desafio.lucas
├── controller        # Camada de entrada (REST)
├── service           # Regras de negócio
├── repository        # Acesso a dados (JPA)
├── model             # Entidades
├── dto               # Objetos de transporte
├── mapper            # Conversão Entity <-> DTO
├── exception         # Tratamento de erros
└── test              # Testes unitários
```

---

## ⚙️ Como executar o projeto

### 🔧 Pré-requisitos

* Java 8+
* Maven instalado

### ▶️ Rodando a aplicação

```bash
mvn spring-boot:run
```

A aplicação estará disponível em:

```
http://localhost:8080
```

---

## 📌 Documentação da API

Swagger disponível em:

```
http://localhost:8080/swagger-ui.html
```

---

## 📌 Endpoints principais

### 🔍 Listar projetos (com filtro opcional)

```
GET /api/projetos?status=EM_ANALISE
```

---

### ➕ Criar projeto

```
POST /api/projetos
```

**Body:**

```json
{
  "nome": "Projeto X",
  "dataInicio": "2024-01-01",
  "previsaoTermino": "2024-06-01",
  "orcamentoTotal": 100000,
  "descricao": "Projeto importante",
  "idGerente": 1
}
```

---

### ❌ Excluir projeto

```
DELETE /api/projetos/{id}
```

📌 Regra:

* Não permite excluir projetos:

    * INICIADO
    * EM_ANDAMENTO
    * ENCERRADO

---

### 🔄 Alterar status

```
PATCH /api/projetos/{id}/status?novoStatus=PLANEJADO
```

📌 Regras:

* Deve respeitar fluxo de status (`podeTransitarPara`)
* Para ENCERRADO:

    * Data de término deve estar preenchida
    * Data não pode ser anterior ao início

---

### 👥 Associar membro

```
POST /api/projetos/{projetoId}/membros/{membroId}
```

📌 Regras:

* Apenas membros com atribuição "funcionário"
* Máximo de 10 membros por projeto
* Máximo de 3 projetos ativos por membro

---

### 📊 Relatório de portfólio

```
GET /api/projetos/relatorio
```

Retorna:

* Quantidade de projetos por status
* Orçamento total por status
* Média de duração dos projetos encerrados
* Quantidade de membros únicos

---

## 🧠 Regras de negócio implementadas

* Controle de transição de status
* Validação de encerramento de projeto
* Limite de membros por projeto
* Limite de projetos ativos por membro
* Cálculo de métricas de portfólio

---

## 🧪 Testes

### ▶️ Executar testes

```bash
mvn test
```

### ✔ Cobertura

* Testes unitários na camada **Service**
* Validação de regras de negócio
* Simulação com Mockito

---

## 👨‍💻 Autor

Lucas França

---

# TodoList API

API REST de estudo para gerenciamento de tarefas com Spring Boot.

## Visão geral

Este projeto foi feito para praticar uma arquitetura em camadas, validação de entrada, tratamento centralizado de erros e testes unitários de service com cobertura mínima de 80%.

## Stack

- Java 21
- Spring Boot 4.1.0
- Spring Web
- Spring Data JPA
- Bean Validation
- PostgreSQL 16
- Docker e Docker Compose
- JUnit 5 e Mockito
- JaCoCo

## Arquitetura

O projeto segue a separação por responsabilidades:

- `controller`: expõe os endpoints e traduz HTTP para chamadas de aplicação.
- `service`: concentra as regras de negócio.
- `repository`: acessa o banco com Spring Data JPA.
- `entity`: representa a tabela persistida.
- `dto`: carrega dados de entrada e saída da API.
- `exception`: centraliza erros e respostas de falha.

## Endpoints

- `POST /tarefas` - criar tarefa
- `GET /tarefas` - listar tarefas
- `GET /tarefas/{id}` - buscar tarefa por id
- `PUT /tarefas/{id}` - atualizar tarefa completa
- `PATCH /tarefas/{id}/status` - atualizar apenas o status
- `DELETE /tarefas/{id}` - remover tarefa

## Como rodar o projeto

### 1. Subir o banco

No diretório do projeto, execute:

```powershell
docker compose up -d
```

O PostgreSQL do container sobe na porta `5433` para evitar conflito com uma possível instalação local do PostgreSQL na `5432`.

### 2. Subir a aplicação

```powershell
 ./mvnw.cmd spring-boot:run
```

Se preferir, rode primeiro dentro da pasta `todolist`.

## Como testar

### Testes unitários

```powershell
 ./mvnw.cmd test
```

O projeto usa JaCoCo com meta mínima de 80% de cobertura de linhas no bundle e testes unitários focados no service.

### Postman

Importe a collection em `postman/todolist.postman_collection.json`.

Variables usadas pela collection:

- `baseUrl`: `http://localhost:8080`
- `tarefaId`: preenchido automaticamente após o `POST`

Ordem sugerida de execução:

1. `POST /tarefas`
2. `GET /tarefas`
3. `GET /tarefas/{id}`
4. `PUT /tarefas/{id}`
5. `PATCH /tarefas/{id}/status`
6. `DELETE /tarefas/{id}`

## Banco de dados

Configuração principal em `src/main/resources/application.yml`:

- URL: `jdbc:postgresql://localhost:5433/todolist_db`
- Usuário: `todolist_user`
- Senha: `todolist_pass`

## Observações de desenvolvimento

- Os testes rodam com H2 em memória para não depender de Docker durante o `mvn test`.
- A aplicação principal continua usando PostgreSQL local via Docker.
- O `README` e a collection do Postman fazem parte da entrega final do projeto.

## Estrutura principal

```text
src/main/java/com/todolist/todolist
├── controller
├── dto
├── entity
├── exception
├── repository
└── service
```

## Próximos passos de estudo

- Revisar os códigos de status HTTP usados em cada endpoint.
- Exercitar os cenários de erro no Postman.
- Adicionar mais testes de serviço se quiser aumentar a cobertura além do mínimo.
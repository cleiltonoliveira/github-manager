# 🧩 GitHub Manager API

Uma API desenvolvida em **Spring Boot** para gerenciar usuários e perfis (roles) do **GitHub**, com sincronização via API pública e persistência em banco de dados **MySQL**.  
A aplicação utiliza **autenticação via token** no cabeçalho `Authorization`.

---

## 🚀 Tecnologias

- **Java 17**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **Spring Security** (token-based auth)
- **Flyway** (migrações do banco de dados)
- **ModelMapper** (mapeamento de objetos)
- **Lombok**
- **MySQL** (via Docker Compose)
- **JUnit 5 & Mockito** (testes unitários)

---

## 🐳 Subindo o Banco de Dados

O projeto inclui um `docker-compose.yml` configurado para o **MySQL**.

Execute o comando abaixo para subir o banco de dados:

```bash
docker-compose up -d
```

---

## 🔑 Autenticação

Todas as requisições à API devem incluir o cabeçalho de autenticação:

```
Authorization: Bearer my-secret-token
```

O token pode ser configurado no arquivo `application.properties` através da propriedade `security.api-token`.

---

## 🌐 Endpoints da API

### Usuários

#### Sincronizar usuários do GitHub
```
POST /api/v1/users/sync
```
Sincroniza usuários do GitHub com o banco de dados local.

**Resposta de Sucesso**:
```json
Status: 200 OK
"Usuários sincronizados com sucesso!"
```

#### Listar todos os usuários
```
GET /api/v1/users
```
Retorna a lista de todos os usuários armazenados.

**Resposta de Sucesso**:
```json
Status: 200 OK
[
  {
    "id": 1,
    "login": "octocat",
    "url": "https://api.github.com/users/octocat",
    "roles": []
  }
]
```

#### Atribuir um perfil a um usuário
```
POST /api/v1/users/{userId}/roles/{roleId}
```
Associa um perfil específico a um usuário.

**Resposta de Sucesso**:
```json
Status: 200 OK
{
  "message": "Perfil atribuído com sucesso",
  "userId": 1,
  "roleId": 2
}
```

### Perfis (Roles)

#### Criar novo perfil
```
POST /api/v1/roles
```

**Requisição**:
```json
{
  "name": "ADMIN"
}
```

**Resposta de Sucesso**:
```json
Status: 201 Created
{
  "id": 1,
  "name": "ADMIN"
}
```

---

## 🧪 Executando Testes

Para executar os testes unitários do projeto:

```bash
mvn test
```

---

## 🚀 Executando a Aplicação

Após subir o banco de dados, você pode iniciar a aplicação:

```bash
mvn spring-boot:run
```

A API estará disponível em `http://localhost:8080`

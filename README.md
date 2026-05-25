# Sistema de Controle de Estoque

Sistema web para gerenciar inventário de produtos com API REST.

## Funcionalidades

- ✅ CRUD completo de produtos
- ✅ Busca por nome, categoria, SKU, termo
- ✅ Alertas de estoque mínimo
- ✅ Paginação
- ✅ Validação de dados
- ✅ Tratamento de erros centralizado
- ✅ Testes unitários

## Requisitos

- Java 17+
- MySQL 8.0+
- Maven 3.8+

## Instalação

1. **Clone o repositório**
   ```bash
   git clone <repo-url>
   cd controle-estoque
   ```

2. **Configure o banco de dados**
   ```sql
   CREATE DATABASE controle_estoque;
   ```
   
   Atualize `application.properties` com suas credenciais MySQL:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/controle_estoque
   spring.datasource.username=seu_usuario
   spring.datasource.password=sua_senha
   ```

3. **Instale dependências**
   ```bash
   mvn clean install
   ```

4. **Execute a aplicação**
   ```bash
   mvn spring-boot:run
   ```

   A API estará disponível em `http://localhost:8080`

## Endpoints da API

### Produtos

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/produtos` | Criar produto |
| GET | `/api/produtos` | Listar produtos (paginado) |
| GET | `/api/produtos/{id}` | Obter produto por ID |
| GET | `/api/produtos/sku/{sku}` | Obter produto por SKU |
| GET | `/api/produtos/buscar/nome?nome=X` | Buscar por nome |
| GET | `/api/produtos/buscar/categoria?categoria=X` | Buscar por categoria |
| GET | `/api/produtos/buscar/termo?termo=X` | Buscar por termo genérico |
| PUT | `/api/produtos/{id}` | Atualizar produto |
| PATCH | `/api/produtos/{id}/estoque?quantidade=X` | Atualizar quantidade |
| DELETE | `/api/produtos/{id}` | Deletar produto |
| GET | `/api/produtos/alertas/estoque-minimo` | Produtos com estoque baixo |

## Exemplo de Requisição

**Criar Produto**
```bash
curl -X POST http://localhost:8080/api/produtos \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Teclado Mecânico",
    "descricao": "Teclado com switches mecânicos RGB",
    "preco": 299.99,
    "quantidade": 50,
    "quantidadeMinima": 10,
    "sku": "TEC-001",
    "categoria": "Periféricos"
  }'
```

## Testes

```bash
mvn test
```

## Estrutura do Projeto

```
src/
├── main/java/com/estoque/sistemacontroleestoque/
│   ├── controller/      # Controladores REST
│   ├── service/         # Lógica de negócio
│   ├── repository/      # Acesso a dados
│   ├── model/           # Entidades JPA
│   ├── dto/             # Data Transfer Objects
│   └── exception/       # Exceções customizadas
├── resources/
│   └── application.properties
└── test/                # Testes unitários
```

## Tecnologias

- Spring Boot 3.2.5
- Spring Data JPA
- MySQL 8.0
- Lombok
- Jakarta EE
- JUnit 5
- Mockito

## Licença

MIT

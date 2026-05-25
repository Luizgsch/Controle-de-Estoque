# Testes da API com cURL

Base URL: `http://localhost:8080/api/produtos`

## 1. Criar Produto
```bash
curl -X POST http://localhost:8080/api/produtos \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Mousepad Grande",
    "descricao": "Mousepad resistente de 80x40cm",
    "preco": 79.99,
    "quantidade": 100,
    "quantidadeMinima": 20,
    "sku": "PAD-002",
    "categoria": "Acessórios"
  }'
```

## 2. Listar Produtos (Paginado)
```bash
curl -X GET "http://localhost:8080/api/produtos?page=0&size=10&sort=nome"
```

## 3. Obter Produto por ID
```bash
curl -X GET http://localhost:8080/api/produtos/1
```

## 4. Obter Produto por SKU
```bash
curl -X GET http://localhost:8080/api/produtos/sku/TEC-001
```

## 5. Buscar por Nome
```bash
curl -X GET "http://localhost:8080/api/produtos/buscar/nome?nome=Teclado"
```

## 6. Buscar por Categoria
```bash
curl -X GET "http://localhost:8080/api/produtos/buscar/categoria?categoria=Periféricos"
```

## 7. Buscar por Termo Genérico
```bash
curl -X GET "http://localhost:8080/api/produtos/buscar/termo?termo=gamer"
```

## 8. Atualizar Produto
```bash
curl -X PUT http://localhost:8080/api/produtos/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Teclado Mecânico RGB Pro",
    "descricao": "Teclado com switches mecânicos e iluminação RGB avançada",
    "preco": 349.99,
    "quantidade": 50,
    "quantidadeMinima": 10,
    "sku": "TEC-001-PRO",
    "categoria": "Periféricos"
  }'
```

## 9. Atualizar Estoque
```bash
curl -X PATCH "http://localhost:8080/api/produtos/1/estoque?quantidade=75"
```

## 10. Obter Alertas de Estoque Mínimo
```bash
curl -X GET http://localhost:8080/api/produtos/alertas/estoque-minimo
```

## 11. Deletar Produto
```bash
curl -X DELETE http://localhost:8080/api/produtos/1
```

## Códigos de Status Esperados

| Ação | Status | Descrição |
|------|--------|-----------|
| Criar | 201 | Created |
| Listar/Obter | 200 | OK |
| Atualizar | 200 | OK |
| Deletar | 204 | No Content |
| Não Encontrado | 404 | Not Found |
| Duplicado | 409 | Conflict |
| Erro Validação | 400 | Bad Request |

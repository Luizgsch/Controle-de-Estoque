# Guia de Deploy

## Desenvolvimento Local

### Com Docker Compose
```bash
docker-compose up -d
```
Acesso:
- App: http://localhost:8080
- API Docs: http://localhost:8080/swagger-ui.html
- MySQL: localhost:3306

### Sem Docker (MySQL local)
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

## Testes

```bash
# Todos os testes
mvn test

# Cobertura
mvn jacoco:report
# Report em: target/site/jacoco/index.html
```

## Build para Produção

### JAR
```bash
mvn clean package -DskipTests
java -jar target/sistema-controle-estoque-1.0.0.jar \
  --spring.profiles.active=prod \
  --server.port=8080
```

### Docker
```bash
docker build -t sistema-controle-estoque:1.0.0 .
docker run -d \
  -e DB_USERNAME=root \
  -e DB_PASSWORD=senha_segura \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://db-host:3306/controle_estoque \
  -p 8080:8080 \
  sistema-controle-estoque:1.0.0
```

## Variáveis de Ambiente (Produção)

| Variável | Padrão | Descrição |
|----------|--------|-----------|
| `SPRING_PROFILES_ACTIVE` | `prod` | Profile ativo |
| `DB_USERNAME` | `root` | Usuário BD |
| `DB_PASSWORD` | `root` | Senha BD |
| `SPRING_DATASOURCE_URL` | Veja application-prod.properties | URL do BD |
| `SERVER_PORT` | `8080` | Porta da aplicação |

## Health Check

```bash
curl http://localhost:8080/actuator/health
```

## Logs

Desenvolvimento:
```bash
tail -f logs/*.log
```

Docker:
```bash
docker logs -f controle-estoque-app
```

## Backup do Banco

```bash
mysqldump -u root -p controle_estoque > backup.sql
```

Restore:
```bash
mysql -u root -p controle_estoque < backup.sql
```

## Monitoramento

Dashboard Prometheus/Grafana (via `management.endpoints.web.exposure.include`):
- Métricas: http://localhost:8080/actuator/metrics

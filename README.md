# Licitação API

## Descrição

Este projeto é uma API Java utilizando Spring Boot, que gerencia licitações e integrações. A API fornece dois principais endpoints: `/licitacoes` e `/integracoes`.

- O endpoint `/licitacoes` permite buscar, atualizar obtendo dados do comprasnet.gov.br e marcar licitações como lidas.
- O endpoint `/integracoes` lista todas as integrações e seus status.

## Tecnologias Utilizadas

- Java 17
- Spring Boot 3.3.0
  - spring-boot-starter-web
  - spring-boot-starter-data-jpa
  - spring-boot-starter-test
- H2 Database
- Jsoup 1.13.1

## Configuração do Projeto

### Clonar o Repositório

```sh
git clone https://github.com/RaizaFeitoza/licitacao.git
```

###  Build com Maven
```
./mvnw clean install
./mvnw spring-boot:run
```

## Endpoints

### /licitacoes

Atualizar licitações obtendo dados do comprasnet.gov.br
```
curl -X GET http://localhost:8080/licitacoes/update
```

Buscar Todas as Licitações
```
curl http://localhost:8080/licitacoes
```

Buscar Licitação por ID
```
curl http://localhost:8080/licitacoes/{id}
```

Marcar Licitação como Lida
```
curl -X PUT http://localhost:8080/licitacoes/{id}/marcar-como-lida
```

### /integracoes
Listar Todas as Integrações
```
curl http://localhost:8080/integracoes
```

# C4 — Nível 2: Containers

Este diagrama abre o SRM Credit Engine e apresenta suas unidades executáveis e seus relacionamentos.

```mermaid
C4Container
    title Diagrama de Containers — SRM Credit Engine

    Person(operator, "Operador de crédito", "Opera simulações, liquidações e consultas.")

    System_Boundary(srm, "SRM Credit Engine") {
        Container(web, "Frontend Web", "React, TypeScript, React Query, Nginx", "Entrega a interface, realiza simulações em tempo real, apresenta o extrato e encaminha /api para o backend.")
        Container(api, "API de Crédito", "Java 21, Spring Boot, Spring Data JPA", "Valida entradas, executa estratégias de precificação, garante idempotência e expõe a API REST.")
        ContainerDb(database, "Banco de Dados", "PostgreSQL 17, Flyway", "Persiste moedas, cedentes, produtos, taxas de câmbio e liquidações.")
    }

    Rel(operator, web, "Utiliza", "HTTPS")
    Rel(web, api, "Consome e encaminha /api", "HTTP/JSON")
    Rel(api, database, "Lê e grava", "JDBC/SQL")
```

## Responsabilidades

| Container | Responsabilidade principal | Porta no Docker Compose |
|---|---|---:|
| Aplicação Web / Nginx | Interface, estado remoto, arquivos estáticos e proxy reverso | `3000` |
| API de Crédito | Regras de negócio, validação, precificação e liquidação | `8080` |
| PostgreSQL | Persistência relacional e integridade referencial | `5432` |

O Docker Compose constrói e orquestra os três serviços implantáveis: `frontend` (React compilado e servido pelo Nginx), `backend` e `postgres`. O Vite é utilizado no desenvolvimento e no processo de build; não é um container separado em produção.

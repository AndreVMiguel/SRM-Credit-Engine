# Uso de IA

## Uso nesta versão

A IA auxiliou no scaffolding do projeto Spring Boot, na organização em camadas, na criação dos DTOs, no desenho inicial do schema SQL e na geração dos testes do motor de precificação.

## Validação humana necessária

- Revisar a convenção financeira do prazo: esta implementação considera mês comercial de 30 dias e capitalização fracionária.
- Confirmar se a taxa base e os spreads são nominais ou efetivos.
- Confirmar fonte, validade e política de fechamento da taxa de câmbio.
- Executar análise de segurança, testes de integração e carga antes de produção.

## Análise crítica

A IA acelerou a estrutura inicial e a documentação, mas decisões de domínio financeiro — arredondamento, calendário, prazo, câmbio e consistência — não devem ser aceitas sem validação de especialistas. O código usa `BigDecimal` para valores armazenados; a exponenciação fracionária usa `double`, uma simplificação explícita que deve ser substituída por uma biblioteca decimal ou convenção financeira homologada caso a precisão exigida não permita essa aproximação.

# Akimob Software de Gestão Imobiliária

- **Multi-tenancy**: Cada cliente com seus dados isolados
- **Autorização**: Autenticação e autorização centralizada com OAuth2
- **Gestão Financeira**: Contas a pagar e receber, com hierarquia de categorias (ex. Manutenções: Elétrica, Hidráulica, ...)
- **Gerenciamento de Propriedades**: Controle de propriedades, manutenções, inquilinos, receitas e despesas.
- **Dashboard Customizado**: Informações e dados principais com gráficos e relatórios.

## Tecnologias

- **Frontend**: Angular
- **Backend**: Spring Boot (Separado em dois módulos: Autorização e Recursos)
- **Database**: PostgreSQL com clientes separados por schema
- **Security**: OAuth 2.0, JWT, Session-based client (BFF)
- **Containerization**: Docker

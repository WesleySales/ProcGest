# Sistema de Gestão de Procurações

Este é um sistema completo para gerenciamento de procurações, desenvolvido com foco em automação de processos, segurança e escalabilidade. Ele permite o cadastro, upload e extração de dados de procurações a partir de arquivos PDF, envio automático de e-mails com relatórios, e uma interface amigável para consulta.

## 🛠️ Tecnologias utilizadas
- Java 17
- Spring Boot
- Spring Security (com autenticação baseada em roles)
- JPA / Hibernate
- MySQL
- Thymeleaf (Frontend)
- JavaMailSender (Envio de e-mails)
- Apache PDFBox (leitura de PDFs)
- Maven

## 🔐 Funcionalidades
- Cadastro manual de procurações via formulário
- Upload de arquivos PDF e extração automática de dados
- Armazenamento e gerenciamento de procurações com status
- Envio automático de e-mails com estatísticas e alertas
- Listagem de procurações próximas ao vencimento
- Geração de relatórios em PDF
- Autenticação e autorização com perfis de acesso
- Estatísticas do sistema: procurações pendentes, concluídas, expiradas

## 🔄 Próximas melhorias
- Integração com IA para extração inteligente de dados dos PDFs
- Deploy com Docker
- Testes automatizados
- Interface gráfica aprimorada

## 📦 Como rodar localmente
1. Clone o repositório
2. Configure o banco de dados MySQL
3. Execute a aplicação com `mvn spring-boot:run` ou diretamente no IDE
4. Acesse `http://localhost:8080`

## 🤝 Contribuições
Aberto a sugestões e colaborações. Entre em contato pelo LinkedIn!


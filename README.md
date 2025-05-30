# 🏥 Censo HGP - Sistema de Gestão de Pacientes

Migração de Sistema **Spring Boot** para **Quarkus** utilizando java 21, desenvolvido para gerenciar informações do censo hospitalar no Hospital Geral de Palmas (HGP), incluindo pacientes, departamentos, procedimentos e controle de acesso de usuários.

## 📦 Tecnologias Utilizadas

- Java 21
- Quarkus 3.22.x
- Hibernate ORM with Panache 
- PostgreSQL
- Jakarta Validation
- API RESTful
- Swagger/OpenAPI

## 📁 Estrutura do Projeto

```text

br.com.unitins.censohgp
│
├── configs # Configurações de segurança e filtros JWT
├── controllers # Endpoints REST
├── dtos # DTOs (entrada e saída de dados)
├── exceptions # Exceções personalizadas
├── models # Entidades JPA
├── repositories # Interfaces do Spring Data JPA
├── services # Regras de negócio
└── utils # Utilitários

```

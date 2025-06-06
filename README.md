# 🚀 EchoReport API (Quarkus)

![Java](https://img.shields.io/badge/Java-17+-orange?style=for-the-badge&logo=java)
![Quarkus](https://img.shields.io/badge/Quarkus-3.x-blueviolet?style=for-the-badge&logo=quarkus)
![Maven](https://img.shields.io/badge/Maven-3.8+-red?style=for-the-badge&logo=apache-maven)
![Oracle](https://img.shields.io/badge/Oracle-DB-red?style=for-the-badge&logo=oracle)

## 🌟 Sobre o Projeto

A **EchoReport API** é o coração do sistema EchoReport, uma plataforma colaborativa de alertas e informações sobre riscos urbanos e climáticos. Desenvolvida em **Java com o framework Quarkus**, esta API é responsável por toda a lógica de negócio, gerenciamento de dados, processamento de uploads de imagens e persistência em um banco de dados Oracle.

Este projeto foi desenvolvido como parte da **Global Solution 2025 da FIAP**, com o objetivo de criar uma solução tecnológica robusta e escalável para aumentar a segurança e a resiliência das comunidades urbanas.

Link: https://echo-report-eight.vercel.app (O carregamento dos dados das páginas pode demorar devido o Render, por favor, tenha paciência 🙏)
---

## ✨ Funcionalidades Principais

A API oferece um conjunto completo de endpoints RESTful para gerenciar as entidades centrais do sistema:

* **👤 Carrega dados nas páginas via GET**
    * Carrega dados de usuários, reportes, alertas, abrigos e zonas de risco.
    * Suporte a preferências (localização, alertas inscritos, nome).

* **👤 Gerenciamento de Usuários:**
    * Registro de novos usuários com hashing de senha.
    * Autenticação via login.
    * CRUD completo (Criar, Ler, Atualizar, Deletar) para administradores.
    * Atualização de preferências de usuário (localização, alertas inscritos, nome).

* **📑 Gerenciamento de Reportes:**
    * Criação de reportes por usuários (logados ou anônimos).
    * Suporte a upload de imagens (`multipart/form-data`).
    * Moderação de reportes por administradores, incluindo atualização de:
        * **Status:** (novo, verificado, em atendimento, resolvido, falso_positivo).
        * **Severidade:** (baixa, media, alta, nao_definida).
        * **Notas do Admin.**

* **🔔 Gerenciamento de Alertas**
    * CRUD completo para alertas emitidos por administradores.
    * Gerenciamento de status (rascunho, agendado, ativo, expirado).

* **🏘️ Gerenciamento de Abrigos:**
    * CRUD completo para abrigos e pontos de apoio.

* **🗺️ Gerenciamento de Zonas de Risco:**
    * CRUD completo para zonas de risco oficiais exibidas no mapa.

* **🔐 Segurança:**
    * Endpoint protegido por uma chave de API estática (`X-API-Key`) para garantir que apenas clientes autorizados (como o frontend Next.js) possam acessar a API.

---

## 💻 Tecnologias Utilizadas

* **Java 17+:** Linguagem de programação principal.
* **Quarkus 3.x:** Framework Java Supersonic e Subatômico, otimizado para a nuvem e Kubernetes.
* **RESTEasy Reactive:** Implementação de JAX-RS (API para web services RESTful) usada pelo Quarkus.
* **Maven:** Ferramenta de automação de build e gerenciamento de dependências.
* **JDBC (Java Database Connectivity):** Para comunicação direta com o banco de dados.
* **Oracle Database:** Sistema de Gerenciamento de Banco de Dados relacional para persistência de dados.
* **Log4j2 / JBoss Logging:** Para registro de logs da aplicação.
* **Jackson:** Para serialização e desserialização de JSON.

---

## 🔗 Endpoints da API

A seguir, uma visão geral dos principais endpoints disponíveis.

### `/usuarios`
| Método HTTP | Endpoint             | Descrição                                         |
| :---------- | :------------------- | :------------------------------------------------ |
| `POST`      | `/registrar`         | Registra um novo usuário.                         |
| `POST`      | `/login`             | Autentica um usuário e retorna seus dados.        |
| `GET`       | `/`                  | **[Admin]** Lista todos os usuários.                    |
| `GET`       | `/{id}`              | **[Admin]** Busca um usuário por ID.                    |
| `PUT`       | `/{id}`              | **[Admin/User]** Atualiza dados de um usuário.          |
| `DELETE`    | `/{id}`              | **[Admin]** Remove um usuário.                          |

### `/reportes`
| Método HTTP | Endpoint             | Descrição                                         |
| :---------- | :------------------- | :------------------------------------------------ |
| `POST`      | `/`                  | Cria um novo reporte (espera `multipart/form-data`). |
| `GET`       | `/`                  | Lista todos os reportes.                          |
| `GET`       | `/{id}`              | Busca um reporte por ID.                          |
| `PUT`       | `/{id}`              | **[Admin]** Atualiza um reporte (status, severidade, etc.). |
| `DELETE`    | `/{id}`              | **[Admin]** Remove um reporte.                          |

### `/alertas`, `/abrigos`, `/mapas`
Estes endpoints seguem um padrão de CRUD RESTful similar ao de `/usuarios` e `/reportes`, permitindo que administradores gerenciem seus respectivos conteúdos.

---

## 🗃️ Esquema do Banco de Dados

A persistência de dados é organizada nas seguintes tabelas principais no Oracle:

* **`ER_USUARIOS`**: Armazena informações dos usuários registrados.
* **`ER_REPORTES`**: Contém os reportes enviados pela comunidade.
* **`ER_ALERTAS`**: Guarda os alertas oficiais criados por administradores.
* **`ER_ABRIGOS`**: Contém informações sobre abrigos e pontos de apoio.
* **`ER_RISK_AREAS`**: Armazena as zonas de risco oficiais e persistentes exibidas no mapa.

---

## 📋 Pré-requisitos para Rodar Localmente

* **JDK (Java Development Kit)**: Versão 17 ou superior.
* **Apache Maven**: Versão 3.8.x ou superior.
* **Acesso a um Banco de Dados Oracle**: Um banco de dados Oracle (pode ser local, em nuvem ou o Oracle XE) com um usuário e senha configurados.
* **IDE (Opcional, mas Recomendado)**: IntelliJ IDEA, Eclipse ou VS Code com as extensões Java e Quarkus.

---

## 🛠️ Configuração do Ambiente

1.  **Clone o Repositório:**
    ```bash
    git clone <https://github.com/spectrumfiap/echoreport-api>
    cd <apiecho>
    ```

2.  **Configure a Conexão com o Banco de Dados:**
    Abra o arquivo `src/main/resources/application.properties` e certifique-se de que as propriedades de conexão com seu banco de dados Oracle estão corretas:

    ```properties
    # Quarkus Datasource Configuration
    quarkus.datasource.db-kind=oracle
    quarkus.datasource.username=seu_usuario_oracle
    quarkus.datasource.password=sua_senha_oracle
    quarkus.datasource.jdbc.url=jdbc:oracle:thin:@//seu_host:sua_porta/seu_sid_ou_service_name
    quarkus.datasource.jdbc.driver=oracle.jdbc.OracleDriver
    ```
    Substitua `seu_usuario_oracle`, `sua_senha_oracle`, `seu_host`, `sua_porta` e `seu_sid_ou_service_name` pelos dados corretos do seu ambiente.

3.  **Crie as Tabelas no Banco:**
    Execute os scripts SQL DDL que criamos (para `ER_USUARIOS`, `ER_ALERTAS`, `ER_ABRIGOS`, `ER_REPORTES`, `ER_RISK_AREAS`) no seu banco de dados Oracle para criar a estrutura de tabelas necessária.

---

## 🚀 Como Rodar o Projeto

1.  **Abra um terminal** na pasta raiz do seu projeto backend.

2.  **Execute o Quarkus em modo de desenvolvimento:**
    Este comando compilará o projeto e o iniciará em modo de desenvolvimento, com hot-reload ativado.
    ```bash
    mvn compile quarkus:dev
    ```

3.  **A API estará disponível** na URL base (geralmente `http://localhost:8080`).

4.  Você pode agora acessar os endpoints usando uma ferramenta como Postman, Insomnia, ou diretamente através do seu frontend Echo Report.

---

## 🧪 Como Rodar os Testes

Para executar os testes unitários e de integração do seu projeto, use o comando Maven:
```bash
mvn test
```

🧑‍💻 Autor

* **Arthur Thomas** - RM: 561061 - [GitHub](https://github.com/athomasmariano) - [LinkedIn](https://www.linkedin.com/in/arthur-thomas-941a97234/)

*(Turma 1TDSPA FIAP)*
# Projeto Blog Pessoal - Backend com Spring Boot

<br />

<div align="center">
    <img src="https://i.imgur.com/w8tTOuT.png" title="source: imgur.com" /> 
</div>

<br />

<div align="center">
  <img src="https://img.shields.io/github/languages/top/rafaelq80/aulas_java_t82?style=flat-square" />
  <img src="https://img.shields.io/github/repo-size/rafaelq80/aulas_java_t82?style=flat-square" />
  <img src="https://img.shields.io/github/languages/count/rafaelq80/aulas_java_t82?style=flat-square" />
  <img src="https://img.shields.io/github/last-commit/rafaelq80/aulas_java_t82?style=flat-square" />
  <img src="https://img.shields.io/github/issues/rafaelq80/aulas_java_t82?style=flat-square" />
  <img src="https://img.shields.io/github/issues-pr/rafaelq80/aulas_java_t82?style=flat-square" />
  <img src="https://img.shields.io/badge/status-construção-yellow" alt="Status: Em Construção">

</div>

<br />

## 1. Descrição

<br />

O **Blog Pessoal** é uma aplicação que permite que usuários publiquem, editem e visualizem postagens relacionadas a temas variados, de forma organizada e segura. Este projeto foi desenvolvido com fins educacionais, simulando uma aplicação real de blog para praticar conceitos de API REST com Java e Spring Boot.

Entre os principais recursos que um blog pessoal oferece, destacam-se:

1. Criação, edição e exclusão de postagens
2. Associação de postagens a temas específicos
3. Cadastro e autenticação de usuários
4. Visualização de postagens por tema ou usuário
5. Controle de acesso a operações sensíveis

<br />

## 2. Sobre esta API

<br />

A API do Blog Pessoal foi desenvolvida utilizando **Java** e o **framework Spring**, seguindo os princípios da Arquitetura MVC e REST. Ela oferece endpoints para o gerenciamento dos recursos **Usuário**, **Postagem** e **Tema**, permitindo a interação entre os usuários e os conteúdos publicados.

<br />

### 2.1. Principais funcionalidades da API:

<br />

1. Consulta, cadastro, login e atualização dos dados de usuários
2. Consulta, criação e gerenciamento de temas para classificar postagens
3. Criação, edição, listagem e remoção de postagens
4. Associação de postagens a temas e autores
5. Autenticação via token JWT para segurança nas requisições

<br />

## 3. Diagrama de Classes

<br />

O **Diagrama de Classes** é um modelo visual usado na programação orientada a objetos para representar a estrutura de um sistema. Ele exibe classes, atributos, métodos e os relacionamentos entre elas, como associações, heranças e dependências.

Esse diagrama ajuda a planejar e entender a arquitetura do sistema, mostrando como as entidades interagem e se conectam. É amplamente utilizado nas fases de design e documentação de projetos.

<br />

```mermaid
classDiagram
class Postagem {
  - id : Long
  - titulo : String
  - texto : String
  - data : LocalDateTime
  - tema : Tema
  - usuario : Usuario
}
class Tema {
  - id : Long
  - descricao : String
  - postagens : List<Postagem>
}
class Usuario {
  - id : Long
  - nome : String
  - usuario : String
  - senha : String
  - foto : String
  - postagens : List<Postagem>
}
Tema "1" --> "0..*" Postagem : classifica
Usuario "1" --> "0..*" Postagem : cria

```

<br />

## 4. Diagrama Entidade-Relacionamento (DER)

<br />

O **DER (Diagrama Entidade-Relacionamento)** do projeto **Blog Pessoal** representa de forma visual como os dados estão organizados no banco de dados relacional e como as entidades se relacionam entre si.

<br />

```mermaid
erDiagram
    tb_usuarios ||--o{ tb_postagens : escreve
    tb_temas ||--o{ tb_postagens : classifica
    tb_usuarios {
        bigint id PK
        varchar(255) nome
        varchar(255) usuario
        varchar(255) senha
        varchar(5000) foto
    }
    tb_temas {
        bigint id PK
        varchar(255) descricao
    }
    tb_postagens {
        bigint id PK
        varchar(255) titulo
        varchar(255) texto
        datetime data
        bigint tema_id FK
        bigint usuario_id FK
    }
```

<br />

## 5. Tecnologias utilizadas

<br />

| Item                          | Descrição       |
| ----------------------------- | --------------- |
| **Servidor**                  | Tomcat          |
| **Linguagem de programação**  | Java            |
| **Framework**                 | Spring Boot     |
| **ORM**                       | JPA + Hibernate |
| **Banco de dados Relacional** | MySQL           |
| **Segurança**                 | Spring Security |
| **Autenticação**              | JWT             |
| **Testes automatizados**      | JUnit           |
| **Documentação**              | SpringDoc       |

<br />

## 6. Requisitos

<br />

Para executar os códigos localmente, você precisará:

- [Java JDK 17+](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- Banco de dados [MySQL](https://dev.mysql.com/downloads/)
- [STS](https://spring.io/tools)
- [Insomnia](https://insomnia.rest/download) ou [Postman](https://www.postman.com/)

<br />

## 7. Como Executar o projeto no STS

<br />

### 7.1. Importando o Projeto

1. Clone o repositório do Projeto [Blog Pessoal](https://github.com/rafaelq80/blogpessoal_spring_t82) dentro da pasta do *Workspace* do STS

```bash
git clone https://github.com/phcarneiro9/Blog_Pessoal
```

2. **Abra o STS** e selecione a pasta do *Workspace* onde você clonou o repositório do projeto
3. No menu superior do STS, clique na opção: **File 🡲 Import...**
4. Na janela **Import**, selecione a opção: **General 🡲 Existing Projects into Workspace** e clique no botão **Next**
5. Na janela **Import Projects**, no item **Select root directory**, clique no botão **Browse...** e selecione a pasta do Workspace onde você clonou o repositório do projeto
6. O STS reconhecerá o projeto automaticamente
7. Marque o Projeto Blog Pessoal no item **Projects** e clique no botão **Finish** para concluir a importação

<br />

### 7.2. Executando o projeto

1. Na Guia **Boot Dashboard**, localize o  **Projeto Blog Pessoal**
2. Selecione o **Projeto Blog Pessoal**
3. Clique no botão **Start or Restart** <img src="https://i.imgur.com/wdoZqWP.png" title="source: imgur.com" width="4%"/> para iniciar a aplicação
4. Caso seja perguntado se você deseja autorizar o acesso ao projeto via rede, clique no botão **Permitir Acesso**
5. Acompanhe a inicialização do projeto no console do STS
6. Verifique se o banco de dados `db_blogpessoal` foi criado corretamente e se as tabelas foram geradas automaticamente.
7. Utilize o [Insomnia](https://insomnia.rest/) ou o [Postman](https://www.postman.com/) para testar os endpoints.

<br />

> [!TIP]
>
> Ao acessar a URL `http://localhost:8080` em seu navegador, a interface do Swagger será carregada automaticamente, permitindo a visualização e a interação com os endpoints da API, bem como a consulta dos modelos de dados utilizados.

<br />

## 8. Como Executar os Testes no STS

### 8.1. **Localizando as Classes de Teste**

- Na **Package Explorer**, navegue até a Source Folder `src/test/java`
- Localize as classes que contém os testes (classes cujo nome terminam com a palavra **Test**)

<br />

### 8.2. **Executando os Testes**

Você pode executar os testes de duas formas:

#### 👉 Opção 1: Executar uma classe de teste específica

- Clique com o botão direito sobre a classe de teste
- Selecione a opção `Run As > JUnit Test`

#### 👉 Opção 2: Executar todos os testes do projeto

- Clique com o botão direito sobre a pasta do projeto
- Selecione: `Run As > JUnit test` 

<br />

### 8.3. **Verificando os Resultados**

- Ao executar os testes, na **Package Explorer**, será exibida a guia **JUnit**  mostrando os resultados dos testes
- Os testes que falharem serão destacados em vermelho, e os bem-sucedidos em verde
- Clique nos testes para visualizar os detalhes ou mensagens de erro no item **Failure Trace**

<br />

## 9. Contribuição

<br />

Este repositório é parte de um projeto educacional, mas contribuições são sempre bem-vindas! Caso tenha sugestões, correções ou melhorias, fique à vontade para:

- Criar uma **issue**
- Enviar um **pull request**
- Compartilhar com colegas que estejam aprendendo Java!

<br />

##  10. Contato

<br />

Desenvolvido por [**Patrick Carneiro**](https://github.com/phcarneiro9)
Para dúvidas, sugestões ou colaborações, entre em contato via GitHub ou abra uma issue!

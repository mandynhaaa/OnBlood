# OnBlood



## Instalação

### Recomendações
**1. IDE**
- Recomendamos fortemente o uso da IDE [Eclipse](https://www.eclipse.org/downloads/packages/), especificamente na versão [2025-03](https://www.eclipse.org/downloads/download.php?file=/oomph/epp/2025-03/R/eclipse-inst-jre-win64.exe) para windows que já contempla os requisitos 1 e 2 listados abaixo.


### Requisitos
**1. Java JDK 21**
- Necessário para compilar e executar o projeto;
- Download: https://www.oracle.com/br/java/technologies/downloads/


**2. Apache Maven**
- Utilizado para gerenciamento de dependências e build do projeto;
- Instalação: https://maven.apache.org/install.html

**3. Banco de Dados**

**_MongoDB - NoSQL_**

Deixamos o projeto configurado para acessar a base de dados no servidor. Com isso, além de clonar o repositório, não é necessário realizar mais configurações.

Caso deseje, pode instalar o [MongoDB Compass](https://www.mongodb.com/try/download/compass) para visualizar e gerir a base de dados MongoDB.

O arquivo [DataSeeder.java](https://github.com/mandynhaaa/OnBlood/blob/main/src/main/java/Main/DataSeeder.java) é o script de criação e população.

**_MySQL - Banco Relacional_**

```diff
- O projeto foi ajustado para utilizar banco de dados NoSQL. Apesar disso, ainda é possível visualizar os arquivos de conexão, 
- geração de SQL e _dumps_ que foram utilizados na versão com banco de dados relacional.
```

- Necessário para persistir os dados.
- Versão recomendada: MySQL 8.0+
- Download: https://dev.mysql.com/downloads/

Criar uma conexão local e importar o script [dbOnBlood-MySQLWorkbench.sql](https://github.com/mandynhaaa/OnBlood/blob/main/src/main/resources/dbOnBlood-MySQLWorkbench.sql).

Garantir que a conexão tenha os seguintes dados de acesso:

```bash
Connection Name: "dbOnBlood";
Hostname: 127.0.0.1;
Port: 3306;
username = "root";
password = "admin";
```

OU altere os dados no arquivo [ConnectionSQL.java](https://github.com/mandynhaaa/OnBlood/blob/main/src/main/java/Connection/ConnectionSQL.java).

## Clonagem do Projeto
A seguir serão listados os passos para rodar o projeto OnBlood na sua máquina utilizando a IDE recomendada.

Após abrir o Eclipse, acesse o menu File -> Import -> Git.
É possível utilizar qualquer uma das opções abaixo.
![image](https://github.com/user-attachments/assets/64c32c8d-736a-471e-a8b5-10f208300daf)


**1. Buscando do GitHub**

![image](https://github.com/user-attachments/assets/7e6bd02e-9e25-4179-b47f-a45d10bfed02)


**2. Clonando URI**

Obtenha o link do repositório pelo GitHub:

![image](https://github.com/user-attachments/assets/91c3d1c2-8373-42d3-8777-d2104278995f)

Informe a URI do projeto e já serão preenchidos os campos de Host e Repository Path.

Para Authentication será necessário informar seu usuário do GitHub e um token gerado na sua do GitHub.

![image](https://github.com/user-attachments/assets/044eba8f-87ac-4838-a2ca-e49f9d8b3065)

## Rodando o Projeto
Para rodar o projeto, basta acessar a branch main e rodar o arquivo Login.java ou RegisterUser.Java localizados em src/main/java/View/Access.

![image](https://github.com/user-attachments/assets/41dc149b-9535-45cd-a618-d511805ac356)

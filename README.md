# OnBlood



## Instalação

### Recomendações
**1. IDE**
- Recomendamos fortemente o uso da IDE [Eclipse](https://www.eclipse.org/downloads/packages/), especificamente na versão [2025-03](https://www.eclipse.org/downloads/download.php?file=/oomph/epp/2025-03/R/eclipse-inst-jre-win64.exe) para windows que já contempla os requisitos 1 e 2 listados abaixo.

### Requisitos
**1. Java JDK 21**
- Necessário para compilar e executar o projeto;


**2. Apache Maven**
- Utilizado para gerenciamento de dependências e build do projeto;


**3. MySQL Server**
- Necessário para persistir os dados.
- Versão recomendada: MySQL 8.0+
- Download: https://dev.mysql.com/downloads/

Criar uma conexão local e importar o script [dbOnBlood-MySQLWorkbench.sql](./OnBlood/src/Connection/dbOnBlood-MySQLWorkbench.sql).
Garantir que a conexão tenha os seguintes dados de acesso:

```bash
Connection Name: "dbOnBlood";
Hostname: 127.0.0.1;
Port: 3306;
username = "root";
password = "admin";
```

OU altere os dados no arquivo [ConnectionSQL.java](./OnBlood/src/Connection/ConnectionSQL.java).

CREATE TABLE tipo_Usuario (
    id_Tipo_Usuario SERIAL PRIMARY KEY,
    descricao VARCHAR(20)
);

CREATE TABLE endereco (
    id_Endereco SERIAL PRIMARY KEY,
    descricao VARCHAR(20),
    cep INT,
    pais VARCHAR(50),
    estado VARCHAR(50),
    cidade VARCHAR(50),
    bairro VARCHAR(50),
    rua VARCHAR(50),
    numero INT,
    complemento VARCHAR(50)
);

CREATE TABLE telefone (
    id_Telefone SERIAL PRIMARY KEY,
    descricao VARCHAR(20),
    ddd INT,
    numero INT
);

CREATE TABLE usuario (
    id_Usuario SERIAL PRIMARY KEY,
    nome VARCHAR(50),
    email VARCHAR(50),
    senha VARCHAR(200),
    data_Criacao TIMESTAMP,
    id_Endereco INT,
    id_Telefone INT,
    id_Tipo_Usuario INT,
    FOREIGN KEY (id_Endereco)
        REFERENCES endereco (id_Endereco),
    FOREIGN KEY (id_Telefone)
        REFERENCES telefone (id_Telefone),
    FOREIGN KEY (id_Tipo_Usuario)
        REFERENCES tipo_Usuario (id_Tipo_Usuario)
);

CREATE TABLE tipo_Sanguineo (
    id_Tipo_Sanguineo SERIAL PRIMARY KEY,
    descricao VARCHAR(20)
);

CREATE TABLE doador (
    id_Doador SERIAL PRIMARY KEY,
    data_Nascimento DATE,
    cpf INT,
    id_Usuario INT,
    id_Tipo_Sanguineo INT,
    FOREIGN KEY (id_Usuario)
        REFERENCES usuario (id_Usuario),
    FOREIGN KEY (id_Tipo_Sanguineo)
        REFERENCES tipo_Sanguineo (id_Tipo_Sanguineo)
);

CREATE TABLE hemocentro (
    id_Hemocentro SERIAL PRIMARY KEY,
    razao_Social VARCHAR(50),
    cnpj INT,
    id_Usuario INT,
    FOREIGN KEY (id_Usuario)
        REFERENCES usuario (id_Usuario)
);

CREATE TABLE doacao (
    id_Doacao SERIAL PRIMARY KEY,
    status VARCHAR(20),
    volume FLOAT,
    data_Hora TIMESTAMP,
    id_Doador INT,
    id_Hemocentro INT,
    FOREIGN KEY (id_Doador)
        REFERENCES doador (id_Doador),
    FOREIGN KEY (id_Hemocentro)
        REFERENCES hemocentro (id_Hemocentro)
);

CREATE TABLE solicitacao (
    id_Solicitacao SERIAL PRIMARY KEY,
    status VARCHAR(20),
    volume FLOAT,
    data_Hora TIMESTAMP,
    id_Hemocentro INT,
    id_Tipo_Sanguineo INT,
    FOREIGN KEY (id_Hemocentro)
        REFERENCES hemocentro (id_Hemocentro),
    FOREIGN KEY (id_Tipo_Sanguineo)
        REFERENCES tipo_Sanguineo (id_Tipo_Sanguineo)
);

CREATE TABLE estoque (
    id_Estoque SERIAL PRIMARY KEY,
    data_Atualizacao TIMESTAMP,
    volume FLOAT,
    id_Hemocentro INT,
    id_Tipo_Sanguineo INT,
    FOREIGN KEY (id_Hemocentro)
        REFERENCES hemocentro (id_Hemocentro),
    FOREIGN KEY (id_Tipo_Sanguineo)
        REFERENCES tipo_Sanguineo (id_Tipo_Sanguineo)
);
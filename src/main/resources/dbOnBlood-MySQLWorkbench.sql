DROP DATABASE IF EXISTS dbOnBlood;
CREATE DATABASE dbOnBlood;
USE dbOnBlood;

CREATE TABLE tipo_Usuario (
    id_Tipo_Usuario INT PRIMARY KEY AUTO_INCREMENT,
    descricao VARCHAR(20)
);

CREATE TABLE usuario (
    id_Usuario INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(50),
    email VARCHAR(50),
    senha VARCHAR(200),
    data_Criacao DATETIME,
    id_Tipo_Usuario INT,
    FOREIGN KEY (id_Tipo_Usuario)
        REFERENCES tipo_Usuario (id_Tipo_Usuario)
);

CREATE TABLE endereco (
    id_Endereco INT PRIMARY KEY AUTO_INCREMENT,
    descricao VARCHAR(20),
    cep VARCHAR(10),
    pais VARCHAR(50),
    estado VARCHAR(50),
    cidade VARCHAR(50),
    bairro VARCHAR(50),
    rua VARCHAR(50),
    numero INT,
    complemento VARCHAR(50),
	id_Usuario INT,
    FOREIGN KEY (id_Usuario)
        REFERENCES usuario (id_Usuario)
);

CREATE TABLE telefone (
    id_Telefone INT PRIMARY KEY AUTO_INCREMENT,
    descricao VARCHAR(20),
    ddd INT,
    numero INT,
	id_Usuario INT,
    FOREIGN KEY (id_Usuario)
        REFERENCES usuario (id_Usuario)
);

CREATE TABLE tipo_Sanguineo (
    id_Tipo_Sanguineo INT PRIMARY KEY AUTO_INCREMENT,
    descricao VARCHAR(20)
);

CREATE TABLE doador (
    id_Doador INT PRIMARY KEY AUTO_INCREMENT,
    data_Nascimento DATE,
    cpf VARCHAR(20) UNIQUE,
    id_Usuario INT,
    id_Tipo_Sanguineo INT,
    FOREIGN KEY (id_Usuario)
        REFERENCES usuario (id_Usuario),
    FOREIGN KEY (id_Tipo_Sanguineo)
        REFERENCES tipo_Sanguineo (id_Tipo_Sanguineo)
);

CREATE TABLE hemocentro (
    id_Hemocentro INT PRIMARY KEY AUTO_INCREMENT,
    razao_Social VARCHAR(50),
    cnpj VARCHAR(20) UNIQUE,
    id_Usuario INT,
    FOREIGN KEY (id_Usuario)
        REFERENCES usuario (id_Usuario)
);

CREATE TABLE doacao (
    id_Doacao INT PRIMARY KEY AUTO_INCREMENT,
    status VARCHAR(20),
    volume FLOAT,
    data_Hora DATETIME,
    id_Doador INT,
    id_Hemocentro INT,
    FOREIGN KEY (id_Doador)
        REFERENCES doador (id_Doador),
    FOREIGN KEY (id_Hemocentro)
        REFERENCES hemocentro (id_Hemocentro)
);

CREATE TABLE solicitacao (
    id_Solicitacao INT PRIMARY KEY AUTO_INCREMENT,
    status VARCHAR(20),
    volume FLOAT,
    data_Hora DATETIME,
    id_Hemocentro INT,
    id_Tipo_Sanguineo INT,
    FOREIGN KEY (id_Hemocentro)
        REFERENCES hemocentro (id_Hemocentro),
    FOREIGN KEY (id_Tipo_Sanguineo)
        REFERENCES tipo_Sanguineo (id_Tipo_Sanguineo)
);

CREATE TABLE estoque (
    id_Estoque INT PRIMARY KEY AUTO_INCREMENT,
    data_Atualizacao DATETIME,
    volume FLOAT,
    id_Hemocentro INT,
    id_Tipo_Sanguineo INT,
    FOREIGN KEY (id_Hemocentro)
        REFERENCES hemocentro (id_Hemocentro),
    FOREIGN KEY (id_Tipo_Sanguineo)
        REFERENCES tipo_Sanguineo (id_Tipo_Sanguineo)
);

-- Valores padrão

INSERT INTO tipo_Usuario (id_Tipo_Usuario, descricao) VALUES 
(1, "Administrador"),
(2, "Doador"),
(3, "Hemocentro");

INSERT INTO tipo_Sanguineo (id_Tipo_Sanguineo, descricao) VALUES 
(1, "A+"),
(2, "A-"),
(3, "B+"),
(4, "B-"),
(5, "AB+"),
(6, "AB-"),
(7, "O+"),
(8, "O-");

-- Registros de exemplo

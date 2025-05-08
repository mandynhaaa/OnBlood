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
    ddd VARCHAR(5),
    numero VARCHAR(15),
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
insert into usuario (nome, email, senha, data_Criacao, id_Tipo_Usuario) values 
('João', 'joao@gmail.com', '$2a$10$7xR8gNYOoYQZ/S85fvRIfuSWWKp50WtDn4uSYpG2QM7SUFIyUw5y.', '2025-05-07 22:17:43', '2'),
('hemocentro', 'hemocentro@gmail.com', '$2a$10$.vkiJUEjFNMtD85mUV1Nge28M8fUJw7zCYEBN6hM9OrSPRGZ9lqoK', '2025-05-07 22:18:22', '3'),
('Maria', 'maria@gmail.com', '$2a$10$YcOx5UxqsCsFEChWEMs1FOV4sJcycWa58ts21ja.Q1xzAxXlGLTUG', '2025-05-07 22:30:22', '2'),
('hemocentro sangue', 'doesangue@gmail.com', '$2a$10$b6TcLABCZHBZuh9NcBUFbuDoVmmW6d.6/Yej9NpUWFGUWfSOFCO6q', '2025-05-07 22:38:09', '3');

insert into doador (data_Nascimento, cpf, id_Usuario, id_Tipo_Sanguineo) values 
 ('2000-01-01', '123.456.789-10', '1', '7'),
 ('2000-12-20', '456.123.789-71', '3', '3');
 
 insert into hemocentro (razao_Social, cnpj, id_Usuario) values
('hemocentro', '987654/0001-23', '2'),
('hemocentro sangue', '741852/0001-25', '4');

insert into doacao (status, volume, data_Hora, id_Doador, id_Hemocentro) values
('Pendente', '250.5', '2025-05-07 10:10:10', '1', '1'),
('Realizada', '250.5', '2025-06-01 10:00:00', '1', '2'),
('Pendente', '500', '2025-06-01 10:30:00', '2', '2');

insert into solicitacao (status, volume, data_Hora, id_Hemocentro, id_Tipo_Sanguineo) values 
('Realizada', '100', '2025-06-01 10:00:00', '2', '7');

insert into estoque (data_Atualizacao, volume, id_Hemocentro, id_Tipo_Sanguineo) values
('2025-05-07 22:42:31', '150.5', '2', '7');

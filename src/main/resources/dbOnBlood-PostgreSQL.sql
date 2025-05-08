CREATE TABLE tipo_Usuario (
    id_Tipo_Usuario SERIAL PRIMARY KEY,
    descricao VARCHAR(20)
);

CREATE TABLE usuario (
    id_Usuario SERIAL PRIMARY KEY,
    nome VARCHAR(50),
    email VARCHAR(50),
    senha VARCHAR(200),
    data_Criacao TIMESTAMP,
    id_Tipo_Usuario INT REFERENCES tipo_Usuario(id_Tipo_Usuario)
);

CREATE TABLE endereco (
    id_Endereco SERIAL PRIMARY KEY,
    descricao VARCHAR(20),
    cep VARCHAR(10),
    pais VARCHAR(50),
    estado VARCHAR(50),
    cidade VARCHAR(50),
    bairro VARCHAR(50),
    rua VARCHAR(50),
    numero INT,
    complemento VARCHAR(50),
    id_Usuario INT REFERENCES usuario(id_Usuario)
);

CREATE TABLE telefone (
    id_Telefone SERIAL PRIMARY KEY,
    descricao VARCHAR(20),
    ddd VARCHAR(5),
    numero VARCHAR(15),
    id_Usuario INT REFERENCES usuario(id_Usuario)
);

CREATE TABLE tipo_Sanguineo (
    id_Tipo_Sanguineo SERIAL PRIMARY KEY,
    descricao VARCHAR(20)
);

CREATE TABLE doador (
    id_Doador SERIAL PRIMARY KEY,
    data_Nascimento DATE,
    cpf VARCHAR(20) UNIQUE,
    id_Usuario INT REFERENCES usuario(id_Usuario),
    id_Tipo_Sanguineo INT REFERENCES tipo_Sanguineo(id_Tipo_Sanguineo)
);

CREATE TABLE hemocentro (
    id_Hemocentro SERIAL PRIMARY KEY,
    razao_Social VARCHAR(50),
    cnpj VARCHAR(20) UNIQUE,
    id_Usuario INT REFERENCES usuario(id_Usuario)
);

CREATE TABLE doacao (
    id_Doacao SERIAL PRIMARY KEY,
    status VARCHAR(20),
    volume FLOAT,
    data_Hora TIMESTAMP,
    id_Doador INT REFERENCES doador(id_Doador),
    id_Hemocentro INT REFERENCES hemocentro(id_Hemocentro)
);

CREATE TABLE solicitacao (
    id_Solicitacao SERIAL PRIMARY KEY,
    status VARCHAR(20),
    volume FLOAT,
    data_Hora TIMESTAMP,
    id_Hemocentro INT REFERENCES hemocentro(id_Hemocentro),
    id_Tipo_Sanguineo INT REFERENCES tipo_Sanguineo(id_Tipo_Sanguineo)
);

CREATE TABLE estoque (
    id_Estoque SERIAL PRIMARY KEY,
    data_Atualizacao TIMESTAMP,
    volume FLOAT,
    id_Hemocentro INT REFERENCES hemocentro(id_Hemocentro),
    id_Tipo_Sanguineo INT REFERENCES tipo_Sanguineo(id_Tipo_Sanguineo)
);

-- Inserção de dados

INSERT INTO tipo_Usuario (id_Tipo_Usuario, descricao) VALUES 
(1, 'Administrador'),
(2, 'Doador'),
(3, 'Hemocentro');

INSERT INTO tipo_Sanguineo (id_Tipo_Sanguineo, descricao) VALUES 
(1, 'A+'),
(2, 'A-'),
(3, 'B+'),
(4, 'B-'),
(5, 'AB+'),
(6, 'AB-'),
(7, 'O+'),
(8, 'O-');

INSERT INTO usuario (id_Usuario, nome, email, senha, data_Criacao, id_Tipo_Usuario) VALUES 
(1, 'João', 'joao@gmail.com', '$2a$10$7xR8gNYOoYQZ/S85fvRIfuSWWKp50WtDn4uSYpG2QM7SUFIyUw5y.', '2025-05-07 22:17:43', 2),
(2, 'hemocentro', 'hemocentro@gmail.com', '$2a$10$.vkiJUEjFNMtD85mUV1Nge28M8fUJw7zCYEBN6hM9OrSPRGZ9lqoK', '2025-05-07 22:18:22', 3),
(3, 'Maria', 'maria@gmail.com', '$2a$10$YcOx5UxqsCsFEChWEMs1FOV4sJcycWa58ts21ja.Q1xzAxXlGLTUG', '2025-05-07 22:30:22', 2),
(4, 'hemocentro sangue', 'doesangue@gmail.com', '$2a$10$b6TcLABCZHBZuh9NcBUFbuDoVmmW6d.6/Yej9NpUWFGUWfSOFCO6q', '2025-05-07 22:38:09', 3);

INSERT INTO doador (data_Nascimento, cpf, id_Usuario, id_Tipo_Sanguineo) VALUES 
('2000-01-01', '123.456.789-10', 1, 7),
('2000-12-20', '456.123.789-71', 3, 3);

INSERT INTO hemocentro (razao_Social, cnpj, id_Usuario) VALUES
('hemocentro', '987654/0001-23', 2),
('hemocentro sangue', '741852/0001-25', 4);

INSERT INTO doacao (status, volume, data_Hora, id_Doador, id_Hemocentro) VALUES
('Pendente', 250.5, '2025-05-07 10:10:10', 1, 1),
('Realizada', 250.5, '2025-06-01 10:00:00', 1, 2),
('Pendente', 500.0, '2025-06-01 10:30:00', 2, 2);

INSERT INTO solicitacao (status, volume, data_Hora, id_Hemocentro, id_Tipo_Sanguineo) VALUES 
('Realizada', 100.0, '2025-06-01 10:00:00', 2, 7);

INSERT INTO estoque (data_Atualizacao, volume, id_Hemocentro, id_Tipo_Sanguineo) VALUES
('2025-05-07 22:42:31', 150.5, 2, 7);
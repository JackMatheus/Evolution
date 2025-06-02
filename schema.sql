CREATE TABLE alunos_responsaveis (
    cpf_aluno VARCHAR(11) NOT NULL,
    nome_aluno VARCHAR(100) NOT NULL,
    sobrenome_aluno VARCHAR(100) NOT NULL,
    telefone_responsavel VARCHAR(20) NOT NULL,
    email_responsavel VARCHAR(100) NULL,
    PRIMARY KEY (cpf_aluno)
);

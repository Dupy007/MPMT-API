-- Création de la base de donnée
CREATE DATABASE MPMT;
USE MPMT;

-- Création des tables
CREATE TABLE IF NOT EXISTS users (
    id INT  NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(250) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS projects (
    id INT  NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS tasks (
    id INT  NOT NULL AUTO_INCREMENT PRIMARY KEY ,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    due_date TIMESTAMP,
    priority INT,
    fk_project INT NOT NULL,
    statut INT,
    end_date TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS project_members (
    id INT  NOT NULL AUTO_INCREMENT PRIMARY KEY,
    fk_project INT REFERENCES projects(id),
    fk_user INT REFERENCES users(id),
    role INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS task_history (
    id INT  NOT NULL AUTO_INCREMENT PRIMARY KEY,
    fk_task INT REFERENCES tasks(id),
    action VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insertion des données de test
INSERT INTO users (username, email, password) VALUES
('john_doe', 'john@example.com', 'password123'),
('jane_doe', 'jane@example.com', 'password456'),
('bob_smith', 'bob@example.com', 'password789');

INSERT INTO projects (name, description, start_date, end_date) VALUES
('Project Alpha', 'Description of Project Alpha', '2024-01-01', '2024-06-30'),
('Project Beta', 'Description of Project Beta', '2024-02-01', NULL);

INSERT INTO tasks (name, description, due_date, priority, statut,fk_project) VALUES
('Task 1', 'Description of Task 1', '2024-03-01', 1, 1,1),
('Task 2', 'Description of Task 2', '2024-04-01', 2, 1,1);

INSERT INTO project_members (fk_project, fk_user, role) VALUES
(1, 1, 1),
(1, 2, 2),
(2, 3, 3);

INSERT INTO task_history (fk_task, action, description) VALUES
(1, 'create', 'Task 1 created'),
(2, 'update', 'Task 2 updated to in progress');

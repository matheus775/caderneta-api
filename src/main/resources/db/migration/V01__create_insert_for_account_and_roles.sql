CREATE TABLE accounts (
	user_id serial PRIMARY KEY,
	username VARCHAR ( 50 ) UNIQUE NOT NULL,
	password VARCHAR ( 50 ) NOT NULL,
	email VARCHAR ( 255 ) UNIQUE NOT NULL
);   

CREATE TABLE roles(
   role_id serial PRIMARY KEY,
   role_name VARCHAR (255) UNIQUE NOT NULL
);

CREATE TABLE accounts_roles (
  user_id INT NOT NULL,
  role_id INT NOT NULL,
  PRIMARY KEY (user_id, role_id),
  FOREIGN KEY (role_id)
      REFERENCES roles (role_id),
  FOREIGN KEY (user_id)
      REFERENCES accounts (user_id)
);

INSERT INTO roles 
  (role_name)
VALUES
  ('Administrador'),
  ('Gerente'),
  ('Caixa');

INSERT INTO accounts
  (username,password,email)
VALUES
  ('Raiden','raio','raiden@em.com'),
  ('Kung Lao','chapeu','Lao@em.com'),
  ('Liu Kang','fogo','Liu@em.com');

INSERT INTO accounts_roles
  (user_id,role_id)
VALUES
  ('1','1'),
  ('1','2'),
  ('2','2'),
  ('2','3'),
  ('3','3');
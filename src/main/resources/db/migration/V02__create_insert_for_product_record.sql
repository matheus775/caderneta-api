CREATE TABLE customer(
	customer_id SERIAL PRIMARY KEY,
	customer_name VARCHAR (50) NOT NULL,
	customer_email VARCHAR (50),
	customer_adress VARCHAR (100),
	customer_cpf CHAR (11) UNIQUE
);



CREATE TABLE product (
	product_id serial PRIMARY KEY,
	product_name VARCHAR ( 50 ) NOT NULL,
	value DECIMAL NOT NULL,
	created_on DATE NOT NULL 
);   

CREATE TABLE record (
	record_id serial PRIMARY KEY,
	user_id INT NOT NULL,
	customer_id INT NOT NULL,
	total_value DECIMAL NOT NULL,
	created_on DATE NOT NULL,
	FOREIGN KEY (user_id)
            REFERENCES accounts (user_id),
        FOREIGN KEY (customer_id)
            REFERENCES customer (customer_id)
);


CREATE TABLE product_record (
  product_id INT NOT NULL,
  record_id INT NOT NULL,
  PRIMARY KEY (product_id, record_id),
  FOREIGN KEY (product_id)
      REFERENCES product (product_id),
  FOREIGN KEY (record_id)
      REFERENCES record (record_id)
);



-------------------------------------------

INSERT INTO customer
  (customer_name,customer_email,customer_adress,customer_cpf)
VALUES
  ('Vega','vega@gmail.com','Rua do Ryu 339, Sion','78916212313'),
  ('Sagat','saga@gmail.com','Rua do Sagat 338,Sion','78916234353'),
  ('Ryu','ryu@gmail.com','Rua do Ryu 54, Olo','23916212313'),
  ('Ken','ken@gmail.com','Rua do ken 34 , Centro','37891625123'),
  ('Honda','honda@gmail.com','Rua do Honda 89, Simao','55516212313');


INSERT INTO product 
  (product_name,value,created_on)
VALUES
  ('batata','2','2019-02-07 00:00:00'),
  ('arroz','13','2019-02-07 00:00:00'),
  ('feijao','6','2019-02-07 00:00:00');

INSERT INTO record
  (user_id,customer_id,total_value,created_on)
VALUES
 ('3','2','23','2019-02-07 00:00:00'),
 ('3','1','43','2019-02-07 00:00:00'),
 ('3','4','66','2019-02-07 00:00:00');

INSERT INTO product_record
  (product_id,record_id)
VALUES
  ('1','1'),
  ('1','2'),
  ('2','2'),
  ('2','3'),
  ('3','3');




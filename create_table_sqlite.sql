CREATE TABLE directors (
    director_id INTEGER NOT NULL CONSTRAINT director_pk PRIMARY key autoincrement,
    surname VARCHAR,
    first_name VARCHAR NOT NULL
);

CREATE UNIQUE index director_director_id_uindex ON directors (director_id);

CREATE TABLE supply_centers (
    supply_center VARCHAR NOT NULL CONSTRAINT supply_centers_pk PRIMARY key,
    director_id INTEGER REFERENCES directors
);

CREATE UNIQUE index supply_centers_supply_center_uindex ON supply_centers (supply_center);

CREATE TABLE salesmen (
    salesman_id INTEGER NOT NULL CONSTRAINT salesmen_pk PRIMARY key autoincrement,
    surname VARCHAR,
    first_name VARCHAR NOT NULL,
    salesman_number VARCHAR NOT NULL,
    gender VARCHAR NOT NULL,
    age INTEGER NOT NULL,
    supply_center VARCHAR NOT NULL REFERENCES supply_centers
);

CREATE UNIQUE index salesmen_salesman_id_uindex ON salesmen (salesman_id);

CREATE UNIQUE index salesmen_salesman_number_uindex ON salesmen (salesman_number);

CREATE TABLE mobile_phones (
    salesman_id INTEGER NOT NULL REFERENCES salesmen,
    phone_number VARCHAR NOT NULL,
    CONSTRAINT mobile_phones_pk PRIMARY key (salesman_id, phone_number)
);

CREATE UNIQUE index mobile_phone_phone_number_uindex ON mobile_phones (phone_number);

CREATE TABLE client_enterprises (
    client_enterprise_id INTEGER NOT NULL CONSTRAINT client_enterprises_pk PRIMARY key autoincrement,
    name VARCHAR NOT NULL,
    industry VARCHAR NOT NULL,
    city VARCHAR,
    country VARCHAR NOT NULL,
    supply_center VARCHAR NOT NULL REFERENCES supply_centers
);

CREATE UNIQUE index client_enterprises_client_enterprise_id_uindex ON client_enterprises (client_enterprise_id);

CREATE UNIQUE index client_enterprises_name_uindex ON client_enterprises (name);

CREATE TABLE contracts (
    contract_number VARCHAR(20) NOT NULL CONSTRAINT contracts_pk PRIMARY key,
    contract_date DATE NOT NULL,
    client_enterprise_id INTEGER NOT NULL REFERENCES client_enterprises
);

CREATE UNIQUE index contracts_contract_number_uindex ON contracts (contract_number);

CREATE TABLE products (
    product_code VARCHAR(20) NOT NULL CONSTRAINT products_pk PRIMARY key,
    product_name VARCHAR NOT NULL
);

CREATE TABLE models (
    model_id INTEGER NOT NULL CONSTRAINT models_pk PRIMARY key autoincrement,
    product_model VARCHAR NOT NULL,
    unit_price INTEGER NOT NULL,
    product_code VARCHAR NOT NULL REFERENCES products
);

CREATE UNIQUE index models_model_id_uindex ON models (model_id);

CREATE UNIQUE index models_product_model_uindex ON models (product_model);

CREATE TABLE orders (
    order_id INTEGER NOT NULL CONSTRAINT orders_pk PRIMARY key autoincrement,
    estimated_delivery_date DATE NOT NULL,
    lodgement_date DATE NOT NULL,
    quantity INTEGER NOT NULL,
    salesman_id INTEGER NOT NULL REFERENCES salesmen,
    model_id INTEGER NOT NULL REFERENCES models,
    contract_number VARCHAR NOT NULL REFERENCES contracts
);

CREATE UNIQUE index orders_order_id_uindex ON orders (order_id);
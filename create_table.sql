CREATE TABLE directors (
    director_id serial PRIMARY key,
    surname VARCHAR,
    first_name VARCHAR NOT NULL
);

CREATE TABLE supply_centers (
    supply_center VARCHAR NOT NULL PRIMARY key,
    director_id INTEGER NOT NULL CONSTRAINT fk_sc_drc REFERENCES directors
);

CREATE TABLE salesmen (
    salesman_id serial PRIMARY key,
    surname VARCHAR,
    first_name VARCHAR NOT NULL,
    salesman_number VARCHAR NOT NULL UNIQUE,
    gender VARCHAR NOT NULL,
    age INTEGER NOT NULL,
    supply_center VARCHAR NOT NULL REFERENCES supply_centers
);

CREATE TABLE mobile_phones (
    salesman_id INTEGER NOT NULL CONSTRAINT fk_pn_slm REFERENCES salesmen,
    phone_number VARCHAR NOT NULL UNIQUE,
    PRIMARY key (salesman_id, phone_number)
);

CREATE TABLE client_enterprises (
    client_enterprise_id serial PRIMARY key,
    name VARCHAR NOT NULL UNIQUE,
    industry VARCHAR NOT NULL,
    city VARCHAR,
    country VARCHAR NOT NULL,
    supply_center VARCHAR NOT NULL CONSTRAINT fk_ce_sc REFERENCES supply_centers
);

CREATE TABLE contracts (
    contract_number VARCHAR(20) NOT NULL PRIMARY key,
    contract_date DATE NOT NULL,
    client_enterprise_id INTEGER NOT NULL CONSTRAINT fk_ctr_ce REFERENCES client_enterprises
);

CREATE TABLE products (
    product_code VARCHAR(20) NOT NULL PRIMARY key,
    product_name VARCHAR NOT NULL
);

CREATE TABLE models (
    model_id serial PRIMARY key,
    product_model VARCHAR NOT NULL UNIQUE,
    unit_price INTEGER NOT NULL,
    product_code VARCHAR NOT NULL CONSTRAINT fk_mdl_pdc REFERENCES products
);

CREATE TABLE orders (
    order_id serial PRIMARY key,
    estimated_delivery_date DATE NOT NULL,
    lodgement_date DATE,
    quantity INTEGER NOT NULL,
    salesman_id INTEGER NOT NULL REFERENCES salesmen,
    model_id INTEGER NOT NULL REFERENCES models,
    contract_number VARCHAR NOT NULL REFERENCES contracts
);
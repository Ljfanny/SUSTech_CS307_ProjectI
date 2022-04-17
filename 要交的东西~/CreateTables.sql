create table directors
(
    director_id serial primary key,
    surname     varchar,
    first_name  varchar not null
);

create table if not exists products
(
    product_code varchar(20) not null
        primary key,
    product_name varchar     not null
);

create table supply_centers
(
    supply_center varchar primary key,
    director_id   integer not null,
    constraint fk_sc_drc foreign key (director_id) references directors (director_id)
);

create table if not exists salesmen
(
    salesman_id     serial primary key,
    surname         varchar,
    first_name      varchar        not null,
    salesman_number varchar unique not null,
    gender          varchar        not null,
    age             integer        not null,
    supply_center   varchar        not null references supply_centers (supply_center)
);

create table if not exists mobile_phones
(
    salesman_id  integer        not null,
    phone_number varchar unique not null,
    constraint fk_pn_slm foreign key (salesman_id) references salesmen (salesman_id),
    primary key (salesman_id, phone_number)
);

create table if not exists models
(
    model_id      serial primary key,
    product_model varchar unique not null,
    unit_price    integer        not null,
    product_code  varchar        not null,
    constraint fk_mdl_pdc foreign key (product_code) references products (product_code)
);

create table client_enterprises
(
    client_enterprise_id serial primary key,
    name                 varchar unique not null,
    industry             varchar        not null,
    city                 varchar,
    country              varchar        not null,
    supply_center        varchar        not null,
    constraint fk_ce_sc foreign key (supply_center) references supply_centers (supply_center)
);

create table contracts
(
    contract_number      varchar(20) primary key,
    contract_date        date    not null,
    client_enterprise_id integer not null,
    constraint fk_ctr_ce foreign key (client_enterprise_id) references
        client_enterprises (client_enterprise_id)
);

create table orders
(
    order_id                serial primary key,
    estimated_delivery_date date    not null,
    lodgement_date          date,
    quantity                integer not null,
    salesman_id             integer not null references salesmen (salesman_id),
    model_id                integer not null references models (model_id),
    contract_number         varchar not null references contracts (contract_number)
);
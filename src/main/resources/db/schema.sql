DROP TABLE IF EXISTS employee;
DROP TABLE IF EXISTS electric_meter;
DROP TABLE IF EXISTS payment_details;
DROP TABLE IF EXISTS feedback;
DROP TABLE IF EXISTS billing;
DROP TABLE IF EXISTS invoice;
DROP TABLE IF EXISTS tariff;
DROP TABLE IF EXISTS customer_role;
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS address;
DROP table IF EXISTS checks;
DROP TABLE IF EXISTS debit_card;
DROP TABLE IF EXISTS credit_card;
DROP TABLE IF EXISTS paypal;
DROP TABLE IF EXISTS roles;

CREATE TABLE IF NOT EXISTS address
(
    address_id integer primary key generated always as identity,
    city varchar(255) NOT NULL,
    street varchar(255) NOT NULL,
    house_number varchar(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS roles
(
    role_id integer primary key generated always as identity,
    name varchar(255) not null
    );

CREATE TABLE IF NOT EXISTS customer
(
    customer_id integer primary key generated always as identity,
    name varchar(255) NOT NULL,
    surname varchar(255) NOT NULL,
    login varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    address_id integer NOT NULL,
    role_id integer NOT NULL,
    foreign key (address_id) references address(address_id) on delete cascade on update cascade,
    foreign key (role_id) references roles(role_id) on delete cascade on update cascade
    );

CREATE TABLE IF NOT EXISTS tariff
(
    tariff_id integer primary key generated always as identity,
    pay_rate double precision NOT NULL,
    penalty_percentage double precision NOT NULL,
    description varchar(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS invoice
(
    invoice_id integer primary key generated always as identity,
    capacity double precision NOT NULL,
    debt double precision NOT NULL,
    excess_paid double precision NOT NULL,
    surcharge double precision NOT NULL,
    to_pay double precision NOT NULL,
    tariff_id integer NOT NULL,
    customer_id integer NOT NULL,
    paid boolean NOT NULL,
    foreign key (customer_id) references customer(customer_id) on delete cascade on update cascade,
    foreign key (tariff_id) references tariff(tariff_id) on delete cascade on update cascade
    );

CREATE TABLE IF NOT EXISTS billing
(
    billing_id integer primary key generated always as identity,
    payment_date date NOT NULL,
    bill_amount double precision NOT NULL,
    paid double precision NOT NULL,
    excess_paid double precision NOT NULL,
    customer_id integer NOT NULL,
    invoice_id integer NOT NULL,
    foreign key (customer_id) references customer(customer_id) on delete cascade on update cascade,
    foreign key (invoice_id) references invoice(invoice_id) on delete cascade on update cascade
    );

CREATE TABLE IF NOT EXISTS credit_card
(
    credit_card_id integer primary key generated always as identity,
    card_number bigint NOT NULL,
    cardholder_name varchar(255) NOT NULL,
    exp_date date NOT NULL,
    cvv integer NOT NULL,
    cash double precision NOT NULL,
    valid boolean NOT NULL
    );

CREATE TABLE IF NOT EXISTS debit_card
(
    debit_card_id integer primary key generated always as identity,
    card_number bigint NOT NULL,
    cardholder_name varchar(255) NOT NULL,
    exp_date date NOT NULL,
    cvv integer NOT NULL,
    cash double precision NOT NULL,
    valid boolean NOT NULL
    );

CREATE TABLE IF NOT EXISTS electric_meter
(
    electric_meter_id integer primary key generated always as identity,
    capacity double precision NOT NULL,
    company_name varchar(255),
    customer_id integer NOT NULL,
    foreign key (customer_id) references customer(customer_id) on delete cascade on update cascade
    );

CREATE TABLE IF NOT EXISTS employee
(
    employee_id integer primary key generated always as identity,
    name varchar(255) NOT NULL,
    surname varchar(255) NOT NULL,
    login varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    salary double precision NOT NULL
    );

CREATE TABLE IF NOT EXISTS feedback
(
    feedback_id integer primary key generated always as identity,
    feedback_text varchar(255) NOT NULL,
    feedback_date date NOT NULL,
    customer_id integer NOT NULL,
    foreign key (customer_id) references customer(customer_id) on delete cascade on update cascade
    );

CREATE TABLE IF NOT EXISTS paypal
(
    paypal_id integer primary key generated always as identity,
    paypal_email varchar(255) NOT NULL,
    paypal_password varchar(255) NOT NULL,
    cash double precision NOT NULL,
    valid boolean NOT NULL
    );

CREATE TABLE IF NOT EXISTS payment_details
(
    payment_details_id integer primary key generated always as identity,
    debit_card_id integer,
    credit_card_id integer,
    paypal_id integer,
    customer_id integer NOT NULL,
    foreign key (credit_card_id) references credit_card(credit_card_id) on delete cascade on update cascade,
    foreign key (customer_id) references customer(customer_id) on delete cascade on update cascade,
    foreign key (debit_card_id) references debit_card(debit_card_id) on delete cascade on update cascade,
    foreign key (paypal_id) references paypal(paypal_id) on delete cascade on update cascade
    );

CREATE TABLE IF NOT EXISTS checks
(
    check_id integer primary key generated always as identity,
    creation_date date NOT NULL,
    cash_added double precision NOT NULL,
    paypal_id integer,
    credit_card_id integer,
    debit_card_id integer,
    foreign key (paypal_id) references paypal(paypal_id) on delete cascade on update cascade,
    foreign key (credit_card_id) references credit_card(credit_card_id) on delete cascade on update cascade,
    foreign key (debit_card_id) references debit_card(debit_card_id) on delete cascade on update cascade
    );

-- CREATE TABLE IF NOT EXISTS customer_role
-- (
--     customers_id integer NOT NULL,
--     roles_id integer NOT NULL,
--     foreign key (customers_id) references customer(customer_id) on delete cascade on update cascade,
--     foreign key (roles_id) references roles(roles_id) on delete cascade on update cascade
--     );
INSERT INTO paypal(paypal_email, paypal_password, cash, valid)
VALUES ('paypalEmail', 'qwerty', 123456,true),
       ('paypalEmail2', 'qwerty', 543201000, true);

INSERT INTO credit_card(card_number, cardholder_name, exp_date, cvv, cash, valid)
VALUES (4000123456789010, 'RAMAZAN BOLAT', '2024-12-01', 123, 123456, true),
       (5355901212345678, 'MARAT ZHUMARTOV', '2023-10-01', 141, 654321, true);

INSERT INTO debit_card(card_number, cardholder_name, exp_date, cvv, cash, valid)
VALUES (6229468888886666, 'RAMAZAN BOLAT', '2024-01-01', 541, 612521, true),
       (5412755678901234, 'MARAT ZHUMARTOV', '2024-09-01', 321, 123456, true);


INSERT INTO address(city, street, house_number)
VALUES ('Almaty', 'Saina', '2'),
       ('Almaty', 'Saina', '3'),
       ('Almaty', 'Saina', '4');

INSERT INTO roles(name)
VALUES ('ROLE_USER'),
       ('ROLE_ADMIN');

INSERT INTO customer(name, surname, login, password, address_id, role_id)
VALUES ('Ramazan', 'Bolat', 'bolat', '$2a$12$k5.OalXsx04TrJJEhtYUdeCu.NoyWz.jjlU9ywacY5mFIqX3N.2gi', 1, 2),
       ('Marat', 'Zhumartov', 'zhumartov', '$2a$12$k5.OalXsx04TrJJEhtYUdeCu.NoyWz.jjlU9ywacY5mFIqX3N.2gi', 2, 1),
       ('Alina', 'Rozamukhamedova', 'rozamukhamedova', '$2a$12$k5.OalXsx04TrJJEhtYUdeCu.NoyWz.jjlU9ywacY5mFIqX3N.2gi', 3, 1);

INSERT INTO tariff(pay_rate, penalty_percentage, description)
VALUES (17.1, 5, 'Standard tariff for all customers');

INSERT INTO invoice(capacity, debt, excess_paid, surcharge, to_pay, tariff_id, customer_id, paid)
VALUES (10, 0, 0, 0, 171, 1, 1, true),
       (0, 0, 0, 0, 0, 1, 2, true),
       (0, 0, 0, 0, 0, 1, 3, true);

INSERT INTO billing(payment_date, bill_amount, paid, excess_paid, customer_id, invoice_id)
VALUES ('2022-04-01', 171, 171, 0, 1, 1),
       ('2022-04-01', 0, 0 , 0, 2, 2),
       ('2022-04-01', 0, 0, 0., 3, 3);

INSERT INTO feedback(feedback_text, feedback_date, customer_id)
VALUES ('Test', '2022-04-02', 1),
       ('The system is working properly, everything is fine!', '2022-04-02', 1);

INSERT INTO payment_details(debit_card_id, credit_card_id, paypal_id, customer_id)
VALUES (1, 1, 1, 1);

INSERT INTO payment_details(debit_card_id, credit_card_id, customer_id)
VALUES (2, 2, 2);

INSERT INTO payment_details(paypal_id, customer_id)
VALUES (1, 3);

INSERT INTO electric_meter(capacity, company_name, customer_id)
VALUES (100, 'Company', 1),
       (0, '', 2),
       (0, 'Company', 3);

INSERT INTO employee(name, surname, login, password, salary)
VALUES ('Aldiyar', 'Tagaibekov', 'tagaibekov', 'qwerty', 123456);

INSERT INTO checks(creation_date, cash_added, debit_card_id)
VALUES ('2022-05-18', 521, 1);

-- INSERT INTO customer_role(customers_id, roles_id)
-- VALUES (1, 2),
--        (2, 1),
--        (3, 1);

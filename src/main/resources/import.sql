insert into customer (id, uuid, change_date, create_date, change_user, create_user, version, document, firstname, lastname ) values ('1', 'aasa11131a', current_timestamp, current_timestamp, 'admin', 'admin', 0, '47996307091', 'Jair', 'Domingues');
insert into address(id, uuid, change_date, create_date, change_user, create_user, version, city, complement, country, defaults, lat, lon, neighborhood, "number", province, reference_point, street, zip, idpessoa)	values ('1', 'aasa11131fb', current_timestamp, current_timestamp, 'admin', 'admin', 0, 'Santa Cruz do Sul', 'casa', 'Brasil', true, '', '', 'Centro', '12', 'RS', '', 'Manoel Elias', '91240260', 1);
insert into wallet_of_customer(id, uuid, change_date, create_date, change_user, create_user, version, password, wallet_of_customer_id) values ('1', 'aasa11131fc', current_timestamp, current_timestamp, 'admin', 'admin', 0, '123456', 1);

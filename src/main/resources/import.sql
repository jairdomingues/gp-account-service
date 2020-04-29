--insert into customer (id, uuid, change_date, create_date, change_user, create_user, version, document, firstname, lastname ) values ('1', 'aasa11131a', current_timestamp, current_timestamp, 'admin', 'admin', 0, '47996307091', 'Jair', 'Domingues');
--insert into address(id, uuid, change_date, create_date, change_user, create_user, version, city, complement, country, defaults, lat, lon, neighborhood, "number", province, reference_point, street, zip, idpessoa)	values ('1', 'aasa11131fb', current_timestamp, current_timestamp, 'admin', 'admin', 0, 'Santa Cruz do Sul', 'casa', 'Brasil', true, '', '', 'Centro', '12', 'RS', '', 'Manoel Elias', '91240260', 1);
--insert into wallet_of_customer(id, uuid, change_date, create_date, change_user, create_user, version, password, wallet_of_customer_id) values ('1', 'aasa11131fc', current_timestamp, current_timestamp, 'admin', 'admin', 0, '123456', 1);

INSERT INTO public.partner(id, change_date, change_user, create_date, create_user, uuid, version, document, firstname, lastname, user_id, account_id) VALUES (1000, current_timestamp, 'admin', current_timestamp, 'admin', 'artraa1xz', 0, '1234567890', 'XZ Eletrônica', 'XZ', 1000, null);
INSERT INTO public.partner_address(id, change_date, change_user, create_date, create_user, uuid, version, city, complement, country, defaults, lat, lng, neighborhood, "number", province, reference_point, street, zip, partner_id) VALUES (1000, current_timestamp, 'admin', current_timestamp, 'admin', 'aazzassa1x', 0, 'Santa Cruz do Sul', 'casa', 'Brasil', true, -30.0356898, -51.1213425, 'Centro', '12', 'RS', '', 'Manoel Elias', '91240260', 1000);
INSERT INTO public.partner_account(id, change_date, change_user, create_date, create_user, uuid, version, active, last_balance, name, partner_id) VALUES (1000, current_timestamp, 'admin', current_timestamp, 'admin', 'aasaaa1h', 0, 'true', 0.00, 'minha conta', 1000);
update public.partner set account_id=1000 where id=1000;

INSERT INTO public.partner(id, change_date, change_user, create_date, create_user, uuid, version, document, firstname, lastname, user_id, account_id) VALUES (1, current_timestamp, 'admin', current_timestamp, 'admin', 'artraa1', 0, '1234567890', 'XZ Eletrônica', 'XZ', null, null);
INSERT INTO public.partner_address(id, change_date, change_user, create_date, create_user, uuid, version, city, complement, country, defaults, lat, lng, neighborhood, "number", province, reference_point, street, zip, partner_id) VALUES (1, current_timestamp, 'admin', current_timestamp, 'admin', 'aazzassa1', 0, 'Santa Cruz do Sul', 'casa', 'Brasil', true, -30.0356898, -51.1213425, 'Centro', '12', 'RS', '', 'Manoel Elias', '91240260', 1);
INSERT INTO public.partner_account(id, change_date, change_user, create_date, create_user, uuid, version, active, last_balance, name, partner_id) VALUES (1, current_timestamp, 'admin', current_timestamp, 'admin', 'aasaaa1', 0, 'true', 0.00, 'minha conta', 1);

INSERT INTO public.partner(id, change_date, change_user, create_date, create_user, uuid, version, document, firstname, lastname, user_id, account_id) VALUES (2, current_timestamp, 'admin', current_timestamp, 'admin', 'aaxcca2', 0, '1234567890', 'New Salão de Beleza', 'NEW', null, null);
INSERT INTO public.partner_address(id, change_date, change_user, create_date, create_user, uuid, version, city, complement, country, defaults, lat, lng, neighborhood, "number", province, reference_point, street, zip, partner_id) VALUES (2, current_timestamp, 'admin', current_timestamp, 'admin', 'aadxxxa13', 0, 'Santa Cruz do Sul', 'casa', 'Brasil', true, -30.0158674, -51.1190112, 'Centro', '12', 'RS', '', 'Manoel Elias', '91240260', 2);
INSERT INTO public.partner_account(id, change_date, change_user, create_date, create_user, uuid, version, active, last_balance, name, partner_id) VALUES (2, current_timestamp, 'admin', current_timestamp, 'admin', 'aaaaaea1', 0, 'true', 0.00, 'minha conta', 2);

INSERT INTO public.tag(id, change_date, change_user, create_date, create_user, uuid, version, text)	VALUES (1, current_timestamp, 'admin', current_timestamp, 'admin', 'aaa1', 0, 'sunt');
INSERT INTO public.rating(id, change_date, change_user, create_date, create_user, uuid, version, rating, rating_count) VALUES (1, current_timestamp, 'admin', current_timestamp, 'admin', 'aaa1', 0, 3.86, 26);
INSERT INTO public.feature(id, change_date, change_user, create_date, create_user, uuid, version, text)	VALUES (1, current_timestamp, 'admin', current_timestamp, 'admin', 'aaa1', 0, 'aliquio');
INSERT INTO public.gallery(id, change_date, change_user, create_date, create_user, uuid, version, photo) VALUES (1, current_timestamp, 'admin', current_timestamp, 'admin', 'aaa1', 0, '../../../../assets/images/products/speaker-1.jpg');
INSERT INTO public.badge(id, change_date, change_user, create_date, create_user, uuid, version, color, text) VALUES (1, current_timestamp, 'admin', current_timestamp, 'admin', 'aaa1', 0, '#0D47A1', '20% TPK' );
INSERT INTO public.price(id, change_date, change_user, create_date, create_user, uuid, version, previous, sale)	VALUES (1, current_timestamp, 'admin', current_timestamp, 'admin', 'aaa1', 0, 32, 54);
INSERT INTO public.product(id, change_date, change_user, create_date, create_user, uuid, version, _id, category, description, name, photo, subtitle, badge_id, price_id, ratings_id, partner_id) VALUES (1, current_timestamp, 'admin', current_timestamp, 'admin', 'aaa1', 0, '1234567890', 'speaker', 'Lorem ipsum dolor sit amet, dum assentior ad duo. Pri ad sapientem ocurreret incorrupte', 'Wireless Bluetooth V4.0 Portable Speaker with HD Sound and Bass', '../../../../assets/images/products/speaker-1.jpg', 'Estabelecimento Numero Um', 1, 1, 1, 1);
INSERT INTO public.product_features(product_id, features_id) VALUES (1, 1);
INSERT INTO public.product_gallery(product_id, gallery_id) VALUES (1, 1);
INSERT INTO public.product_tags(product_id, tags_id) VALUES (1, 1);


INSERT INTO public.tag(id, change_date, change_user, create_date, create_user, uuid, version, text)	VALUES (2, current_timestamp, 'admin', current_timestamp, 'admin', 'aaa1a', 0, 'sunt');
INSERT INTO public.rating(id, change_date, change_user, create_date, create_user, uuid, version, rating, rating_count) VALUES (2, current_timestamp, 'admin', current_timestamp, 'admin', 'aaa1b', 0, 3.86, 26);
INSERT INTO public.feature(id, change_date, change_user, create_date, create_user, uuid, version, text)	VALUES (2, current_timestamp, 'admin', current_timestamp, 'admin', 'aaa1c', 0, 'aliquio');
INSERT INTO public.gallery(id, change_date, change_user, create_date, create_user, uuid, version, photo) VALUES (2, current_timestamp, 'admin', current_timestamp, 'admin', 'aaa1d', 0, '../../../../assets/images/products/speaker-1.jpg');
INSERT INTO public.badge(id, change_date, change_user, create_date, create_user, uuid, version, color, text) VALUES (2, current_timestamp, 'admin', current_timestamp, 'admin', 'aaa1x', 0, '#0D47A1', '20% TPK' );
INSERT INTO public.price(id, change_date, change_user, create_date, create_user, uuid, version, previous, sale)	VALUES (2, current_timestamp, 'admin', current_timestamp, 'admin', 'aaa1h', 0, 32, 54);
INSERT INTO public.product(id, change_date, change_user, create_date, create_user, uuid, version, _id, category, description, name, photo, subtitle, badge_id, price_id, ratings_id, partner_id) VALUES (2, current_timestamp, 'admin', current_timestamp, 'admin', 'aaa1ssf', 0, '1234567890', 'speaker', 'Lorem ipsum dolor sit amet, dum assentior ad duo. Pri ad sapientem ocurreret incorrupte', 'Corte de cabelo', '../../../../assets/images/products/speaker-1.jpg', 'New Salão de Beleza', 1, 1, 1, 2);
INSERT INTO public.product_features(product_id, features_id) VALUES (2, 2);
INSERT INTO public.product_gallery(product_id, gallery_id) VALUES (2, 2);
INSERT INTO public.product_tags(product_id, tags_id) VALUES (2, 2);

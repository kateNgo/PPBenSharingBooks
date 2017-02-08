-- insert username/password : phuong@yahoo.com/phuong
/*
insert into account (email, password, fullname,accountType ) values ('phuong@yahoo.com','ae4743afb621676d91fbd995954400c5d55a016474b815ada8c5ade55c1b340f','Thi bich Phuong Ngo','ADMIN');
create view jdbcrealm_user (email, password) as
select email, password
from account;

create view jdbcrealm_group (email, groupname) as
select email, accountType
from account;
*/

insert into type (typeid, typename) values (1, 'Information Technology');
insert into type (typeid,typename) values (2,'Cooking');
insert into subtype (subtypeid,subtypename, type_typeid) values (1,'System Security', 1);
insert into subtype (subtypeid,subtypename, type_typeid) values (2,'Java Programming', 1);
insert into subtype (subtypeid,subtypename, type_typeid) values (3,'Networking', 1);
insert into subtype (subtypeid,subtypename, type_typeid) values (4,'Database', 1);
insert into subtype (subtypeid,subtypename, type_typeid) values (5,'Vietnamese Cook book', 2);
insert into subtype (subtypeid,subtypename, type_typeid) values (6,'Western Cook Book', 2);
insert into subtype (subtypeid,subtypename, type_typeid) values (7,'Thai Cook Book', 2);
insert into subtype (subtypeid,subtypename, type_typeid) values (8,'Eastern Midle Cook Book', 2);



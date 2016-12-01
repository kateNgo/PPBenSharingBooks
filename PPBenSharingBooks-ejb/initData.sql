-- insert username/password : phuong/phuong
insert into account (email, password, fullname,accountType ) values ('phuong@yahoo.com','ae4743afb621676d91fbd995954400c5d55a016474b815ada8c5ade55c1b340f','Thi bich Phuong Ngo','ADMIN');
create view jdbcrealm_user (email, password) as
select email, password
from account;

create view jdbcrealm_group (email, groupname) as
select email, accountType
from account;

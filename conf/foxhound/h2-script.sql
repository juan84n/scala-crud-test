
drop table if exists tbl_account ;

create table tbl_account(  id varchar, dateOfOpen varchar, balance varchar ) ;

insert into tbl_account ( id, dateOfOpen, balance ) values
  ( '1001', '2018-09-19' , 50000 ),
  ( '1110', '2018-09-19' , 10000 ),
  ( '10010', '2018-09-19' , 20000 ),
  ( '4999', '2018-09-19' , 30000 ),
;
create table tbl_users (
    email varchar(100) NOT NULL,
    first_name varchar(50) NOT NULL,
    last_name varchar(50) not null,
    locale varchar(50),
    primary key (email)
)

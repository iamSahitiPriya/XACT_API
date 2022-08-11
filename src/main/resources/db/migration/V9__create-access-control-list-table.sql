create table tbl_access_control (
                                         email varchar(500) NOT NULL PRIMARY KEY,
                                         role varchar(50) CHECK(role='Admin')
);
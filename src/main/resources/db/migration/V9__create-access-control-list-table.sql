create table tbl_access_control (
                                         acl_id INT PRIMARY KEY,
                                         email varchar(500) NOT NULL,
                                         role varchar(50) CHECK(role='Admin')
);
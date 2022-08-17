create table tbl_access_control (
                                         email varchar(500) NOT NULL PRIMARY KEY,
                                         role varchar(50) CHECK(role='Admin')
);

INSERT INTO tbl_access_control VALUES ('vaishnavi.narayanan@thoughtworks.com','Admin');
INSERT INTO tbl_access_control VALUES ('ankit.mehta@thoughtworks.com','Admin');

INSERT INTO tbl_access_control VALUES ('shashank.mishra@thoughtworks.com','Admin');
INSERT INTO tbl_access_control VALUES ('ruchika.modgil@thoughtworks.com','Admin');
INSERT INTO tbl_access_control VALUES ('sahitipriya.a@thoughtworks.com','Admin');
INSERT INTO tbl_access_control VALUES ('brindha.e@thoughtworks.com','Admin');
INSERT INTO tbl_access_control VALUES ('jathin.meduri@thoughtworks.com','Admin');
INSERT INTO tbl_access_control VALUES ('ramprakash.pg@thoughtworks.com','Admin');






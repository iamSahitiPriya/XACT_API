/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */



Alter table tbl_access_control
    drop constraint tbl_access_control_role_check ;

update tbl_access_control set role='PRIMARY_ADMIN' where role='Admin';

alter table tbl_access_control add  constraint role_check check ( role='PRIMARY_ADMIN' or role='SECONDARY_ADMIN' ) ;


create table tbl_notification (
                                  notification_id SERIAL PRIMARY KEY,
                                  template_name varchar(20) NOT NULL ,
                                  user_email text NOT NULL,
                                  payload text ,
                                  status varchar(20) CHECK (status='Y' OR status='N'),
                                  retries int NOT NULL DEFAULT 0,
                                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP

);

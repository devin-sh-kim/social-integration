CREATE SEQUENCE SEQ;

CREATE TABLE user (
    id bigint NOT NULL,
    email varchar(320) NOT NULL,
    password varchar(2000),
    name varchar(200) NOT NULL,
    gender varchar(1),
    age varchar(2),
    mobile varchar(20),
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    CONSTRAINT user_pk PRIMARY KEY (id),
    CONSTRAINT user_email_uc UNIQUE (email)
);

CREATE TABLE user_social (
    user_id bigint NOT NULL,
    social VARCHAR(20) NOT NULL,
    social_account_id VARCHAR(320) NOT NULL,
    access_token VARCHAR(256),
    refresh_token VARCHAR(256),
    created_at timestamp,
    created_by bigint,
    updated_at timestamp,
    updated_by bigint,
    CONSTRAINT user_social_pk PRIMARY KEY (user_id, social)
);
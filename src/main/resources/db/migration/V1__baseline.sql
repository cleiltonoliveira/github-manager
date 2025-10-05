CREATE TABLE user (
    id        BIGINT     PRIMARY KEY,
    login     TEXT       NOT NULL,
    url       TEXT       NOT NULL
);

CREATE TABLE role (
    id        BIGINT     PRIMARY KEY,
    name      TEXT       NOT NULL
);

CREATE TABLE user_role (
    id        BIGINT     PRIMARY KEY,
    user_id   BIGINT     NOT NULL,
    role_id   BIGINT     NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (role_id) REFERENCES role (id)
);
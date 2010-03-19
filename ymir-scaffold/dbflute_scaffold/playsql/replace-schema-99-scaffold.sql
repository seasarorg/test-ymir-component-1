CREATE TABLE ys_user
(
    id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    display_name VARCHAR(200) NOT NULL,
    password VARCHAR(200) NOT NULL,
    mail_address VARCHAR(200),
    created_date DATETIME NOT NULL,
    modified_date DATETIME NOT NULL,
    version_no BIGINT NOT NULL DEFAULT 1
)  ;

COMMENT ON TABLE ys_user IS 'ユーザ:';
COMMENT ON COLUMN ys_user.id IS 'ID:';
COMMENT ON COLUMN ys_user.name IS 'ユーザ名:';
COMMENT ON COLUMN ys_user.display_name IS '表示名:';
COMMENT ON COLUMN ys_user.password IS 'パスワード:';
COMMENT ON COLUMN ys_user.mail_address IS 'メールアドレス:';
COMMENT ON COLUMN ys_user.created_date IS '作成日時:';
COMMENT ON COLUMN ys_user.modified_date IS '更新日時:';
COMMENT ON COLUMN ys_user.version_no IS 'バージョン番号:';

ALTER TABLE ys_user ADD CONSTRAINT uk_ys_user_name
    UNIQUE (name);

INSERT INTO ys_user (
    id, name, display_name, password,
    created_date, modified_date
) VALUES (
    1, 'administrator', '管理者', '',
    SYSDATE, SYSDATE
);

CREATE TABLE ys_group
(
    id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    display_name VARCHAR(200) NOT NULL,
    created_date DATETIME NOT NULL,
    modified_date DATETIME NOT NULL,
    version_no BIGINT NOT NULL DEFAULT 1
)  ;

COMMENT ON TABLE ys_group IS 'グループ:';
COMMENT ON COLUMN ys_group.id IS 'ID:';
COMMENT ON COLUMN ys_group.name IS 'グループ名:';
COMMENT ON COLUMN ys_group.display_name IS '表示名:';
COMMENT ON COLUMN ys_group.created_date IS '作成日時:';
COMMENT ON COLUMN ys_group.modified_date IS '更新日時:';
COMMENT ON COLUMN ys_group.version_no IS 'バージョン番号:';

ALTER TABLE ys_group ADD CONSTRAINT uk_ys_group_name
    UNIQUE (name);

CREATE TABLE ys_group_user
(
    id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    group_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    created_date DATETIME NOT NULL,
    modified_date DATETIME NOT NULL,
    version_no BIGINT NOT NULL DEFAULT 1
)  ;

ALTER TABLE ys_group_user ADD CONSTRAINT uk_ys_group_user_group_id_user_id
    UNIQUE (group_id, user_id);
ALTER TABLE ys_group_user ADD CONSTRAINT fk_ys_group_user_group_id 
    FOREIGN KEY (group_id) REFERENCES ys_group (id);
ALTER TABLE ys_group_user ADD CONSTRAINT fk_ys_group_user_user_id 
    FOREIGN KEY (user_id) REFERENCES ys_user (id);

CREATE TABLE ys_role
(
    id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    display_name VARCHAR(200) NOT NULL,
    created_date DATETIME NOT NULL,
    modified_date DATETIME NOT NULL,
    version_no BIGINT NOT NULL DEFAULT 1
)  ;

COMMENT ON TABLE ys_role IS 'ロール:';
COMMENT ON COLUMN ys_role.id IS 'ID:';
COMMENT ON COLUMN ys_role.name IS 'ロール名:';
COMMENT ON COLUMN ys_role.display_name IS '表示名:';
COMMENT ON COLUMN ys_role.created_date IS '作成日時:';
COMMENT ON COLUMN ys_role.modified_date IS '更新日時:';
COMMENT ON COLUMN ys_role.version_no IS 'バージョン番号:';

ALTER TABLE ys_role ADD CONSTRAINT uk_ys_role_name
    UNIQUE (name);

INSERT INTO ys_role (
    id, name, display_name,
    created_date, modified_date
) VALUES (
    1, 'administrator', '管理者',
    SYSDATE, SYSDATE
);

CREATE TABLE ys_role_group_user
(
    id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    role_id BIGINT NOT NULL,
    group_id BIGINT,
    user_id BIGINT,
    created_date DATETIME NOT NULL,
    modified_date DATETIME NOT NULL,
    version_no BIGINT NOT NULL DEFAULT 1
)  ;

ALTER TABLE ys_role_group_user ADD CONSTRAINT uk_ys_role_group_user_role_id_group_id_user_id
    UNIQUE (role_id, group_id, user_id);
ALTER TABLE ys_role_group_user ADD CONSTRAINT fk_ys_role_group_user_role_id 
    FOREIGN KEY (role_id) REFERENCES ys_role (id);
ALTER TABLE ys_role_group_user ADD CONSTRAINT fk_ys_role_group_user_group_id 
    FOREIGN KEY (group_id) REFERENCES ys_group (id);
ALTER TABLE ys_role_group_user ADD CONSTRAINT fk_ys_role_group_user_user_id 
    FOREIGN KEY (user_id) REFERENCES ys_user (id);

INSERT INTO ys_role_group_user (
    role_id, user_id,
    created_date, modified_date
) VALUES (
    1, 1,
    SYSDATE, SYSDATE
);

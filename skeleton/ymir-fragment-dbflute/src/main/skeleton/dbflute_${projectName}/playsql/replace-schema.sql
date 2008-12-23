CREATE TABLE PURCHASE
(
    PURCHASE_ID NUMBER(16) NOT NULL PRIMARY KEY,
    MEMBER_ID INTEGER NOT NULL,
    PRODUCT_ID INTEGER NOT NULL,
    PURCHASE_DATETIME DATE NOT NULL,
    PURCHASE_COUNT INTEGER NOT NULL,
    PURCHASE_PRICE INTEGER NOT NULL,
    PAYMENT_COMPLETE_FLG INTEGER NOT NULL,
    REGISTER_DATETIME DATE NOT NULL,
    REGISTER_USER VARCHAR(200) NOT NULL,
    REGISTER_PROCESS VARCHAR(200) NOT NULL,
    UPDATE_DATETIME DATE NOT NULL,
    UPDATE_USER VARCHAR(200) NOT NULL,
    UPDATE_PROCESS VARCHAR(200) NOT NULL,
    VERSION_NO NUMBER(38) NOT NULL
)  ;


CREATE TABLE MEMBER_WITHDRAWAL
(
    MEMBER_ID INTEGER NOT NULL PRIMARY KEY,
    WITHDRAWAL_REASON_CODE CHAR(3),
    WITHDRAWAL_REASON_INPUT_TEXT CLOB,
    WITHDRAWAL_DATETIME DATE NOT NULL,
    REGISTER_DATETIME DATE NOT NULL,
    REGISTER_PROCESS VARCHAR(200) NOT NULL,
    REGISTER_USER VARCHAR(200) NOT NULL,
    UPDATE_DATETIME DATE NOT NULL,
    UPDATE_PROCESS VARCHAR(200) NOT NULL,
    UPDATE_USER VARCHAR(200) NOT NULL,
    VERSION_NO NUMBER(38) NOT NULL
)  ;


CREATE TABLE MEMBER_SECURITY
(
    MEMBER_ID INTEGER NOT NULL PRIMARY KEY,
    LOGIN_PASSWORD VARCHAR(50) NOT NULL,
    REMINDER_QUESTION VARCHAR(50) NOT NULL,
    REMINDER_ANSWER VARCHAR(50) NOT NULL,
    REGISTER_DATETIME DATE NOT NULL,
    REGISTER_PROCESS VARCHAR(200) NOT NULL,
    REGISTER_USER VARCHAR(200) NOT NULL,
    UPDATE_DATETIME DATE NOT NULL,
    UPDATE_PROCESS VARCHAR(200) NOT NULL,
    UPDATE_USER VARCHAR(200) NOT NULL,
    VERSION_NO NUMBER(38) NOT NULL
)  ;


CREATE TABLE PRODUCT
(
    PRODUCT_ID INTEGER NOT NULL PRIMARY KEY,
    PRODUCT_NAME VARCHAR(50) NOT NULL,
    PRODUCT_HANDLE_CODE VARCHAR(100) NOT NULL,
    PRODUCT_STATUS_CODE CHAR(3) NOT NULL,
    REGISTER_DATETIME DATE NOT NULL,
    REGISTER_USER VARCHAR(200) NOT NULL,
    REGISTER_PROCESS VARCHAR(200) NOT NULL,
    UPDATE_DATETIME DATE NOT NULL,
    UPDATE_USER VARCHAR(200) NOT NULL,
    UPDATE_PROCESS VARCHAR(200) NOT NULL,
    VERSION_NO NUMBER(38) NOT NULL
)  ;


CREATE TABLE MEMBER
(
    MEMBER_ID INTEGER NOT NULL PRIMARY KEY,
    MEMBER_NAME VARCHAR(200) NOT NULL,
    MEMBER_ACCOUNT VARCHAR(50) NOT NULL,
    MEMBER_STATUS_CODE CHAR(3) NOT NULL,
    MEMBER_FORMALIZED_DATETIME DATE,
    MEMBER_BIRTHDAY DATE,
    REGISTER_DATETIME DATE NOT NULL,
    REGISTER_USER VARCHAR(200) NOT NULL,
    REGISTER_PROCESS VARCHAR(200) NOT NULL,
    UPDATE_DATETIME DATE NOT NULL,
    UPDATE_USER VARCHAR(200) NOT NULL,
    UPDATE_PROCESS VARCHAR(200) NOT NULL,
    VERSION_NO NUMBER(38)  NOT NULL
)  ;


CREATE TABLE MEMBER_LOGIN
(
    MEMBER_LOGIN_ID NUMBER(16) NOT NULL PRIMARY KEY,
    MEMBER_ID INTEGER NOT NULL,
    LOGIN_DATETIME DATE NOT NULL,
    LOGIN_MOBILE_FLG INTEGER NOT NULL,
    LOGIN_MEMBER_STATUS_CODE CHAR(3) NOT NULL
)  ;


CREATE TABLE WITHDRAWAL_REASON
(
    WITHDRAWAL_REASON_CODE CHAR(3) NOT NULL PRIMARY KEY,
    WITHDRAWAL_REASON_TEXT CLOB NOT NULL,
    DISPLAY_ORDER INTEGER NOT NULL
)  ;


CREATE TABLE PRODUCT_STATUS
(
    PRODUCT_STATUS_CODE CHAR(3) NOT NULL PRIMARY KEY,
    PRODUCT_STATUS_NAME VARCHAR(50) NOT NULL
)  ;


CREATE TABLE MEMBER_STATUS
(
    MEMBER_STATUS_CODE CHAR(3) NOT NULL PRIMARY KEY,
    MEMBER_STATUS_NAME VARCHAR(50) NOT NULL,
    DISPLAY_ORDER INTEGER NOT NULL
)  ;





ALTER TABLE PURCHASE ADD CONSTRAINT FK_PURCHASE_MEMBER 
    FOREIGN KEY (MEMBER_ID) REFERENCES MEMBER (MEMBER_ID) ;

ALTER TABLE PURCHASE ADD CONSTRAINT FK_PURCHASE_PRODUCT 
    FOREIGN KEY (PRODUCT_ID) REFERENCES PRODUCT (PRODUCT_ID) ;

ALTER TABLE MEMBER_WITHDRAWAL ADD CONSTRAINT FK_MEMBER_WITHDRAWAL_MEMBER 
    FOREIGN KEY (MEMBER_ID) REFERENCES MEMBER (MEMBER_ID) ;

ALTER TABLE MEMBER_WITHDRAWAL ADD CONSTRAINT FK_MEMBER_WITHDRAWAL_REASON 
    FOREIGN KEY (WITHDRAWAL_REASON_CODE) REFERENCES WITHDRAWAL_REASON (WITHDRAWAL_REASON_CODE) ;

ALTER TABLE MEMBER_SECURITY ADD CONSTRAINT FK_MEMBER_SECURITY_MEMBER 
    FOREIGN KEY (MEMBER_ID) REFERENCES MEMBER (MEMBER_ID) ;

ALTER TABLE PRODUCT ADD CONSTRAINT FK_PRODUCT_PRODUCT_STATUS 
    FOREIGN KEY (PRODUCT_STATUS_CODE) REFERENCES PRODUCT_STATUS (PRODUCT_STATUS_CODE) ;

ALTER TABLE MEMBER ADD CONSTRAINT FK_MEMBER_MEMBER_STATUS 
    FOREIGN KEY (MEMBER_STATUS_CODE) REFERENCES MEMBER_STATUS (MEMBER_STATUS_CODE) ;

ALTER TABLE MEMBER_LOGIN ADD CONSTRAINT FK_MEMBER_LOGIN_MEMBER_STATUS 
    FOREIGN KEY (LOGIN_MEMBER_STATUS_CODE) REFERENCES MEMBER_STATUS (MEMBER_STATUS_CODE) ;

ALTER TABLE MEMBER_LOGIN ADD CONSTRAINT FK_MEMBER_LOGIN_MEMBER 
    FOREIGN KEY (MEMBER_ID) REFERENCES MEMBER (MEMBER_ID) ;


ALTER TABLE PURCHASE ADD CONSTRAINT UQ_PURCHASE UNIQUE (MEMBER_ID, PRODUCT_ID, PURCHASE_DATETIME) ;
ALTER TABLE PRODUCT ADD CONSTRAINT UQ_PRODUCT_PRODUCT_HANDLE_CODE UNIQUE (PRODUCT_HANDLE_CODE) ;
ALTER TABLE WITHDRAWAL_REASON ADD CONSTRAINT UQ_WITHDRAWAL_R_D_ORDER UNIQUE (DISPLAY_ORDER) ;
ALTER TABLE PRODUCT_STATUS ADD CONSTRAINT UQ_PRODUCT_STATUS_P_S_NAME UNIQUE (PRODUCT_STATUS_NAME) ;
ALTER TABLE MEMBER ADD CONSTRAINT UQ_MEMBER_MEMBER_ACCOUNT UNIQUE (MEMBER_ACCOUNT) ;
ALTER TABLE MEMBER_LOGIN ADD CONSTRAINT UQ_MEMBER_LOGIN UNIQUE (MEMBER_ID, LOGIN_DATETIME) ;
ALTER TABLE MEMBER_STATUS ADD CONSTRAINT UQ_MEMBER_STATUS_M_S_NAME UNIQUE (MEMBER_STATUS_NAME) ;
ALTER TABLE MEMBER_STATUS ADD CONSTRAINT UQ_MEMBER_STATUS_DISPLAY_ORDER UNIQUE (DISPLAY_ORDER) ;


CREATE INDEX IX_MEMBER_MEMBER_NAME ON MEMBER(MEMBER_NAME) ;
CREATE INDEX IX_MEMBER_FORMALIZED_DATETIME ON MEMBER(MEMBER_FORMALIZED_DATETIME) ;
CREATE INDEX IX_MEMBER_LOGIN_DATETIME ON MEMBER_LOGIN(LOGIN_DATETIME) ;
CREATE INDEX IX_PURCHASE_PRODUCT_DATETIME ON PURCHASE(PRODUCT_ID, PURCHASE_DATETIME) ;
CREATE INDEX IX_PURCHASE_DATETIME_MEMBER ON PURCHASE(PURCHASE_DATETIME, MEMBER_ID) ;
CREATE INDEX IX_PURCHASE_PRICE ON PURCHASE(PURCHASE_PRICE) ;
CREATE INDEX IX_PRODUCT_PRODUCT_NAME ON PRODUCT(PRODUCT_NAME) ;

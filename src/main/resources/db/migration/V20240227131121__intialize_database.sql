CREATE SCHEMA IF NOT EXISTS GWBACKEND;
CREATE TABLE IF NOT EXISTS GWBACKEND.FACULTY
(
    ID              BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL UNIQUE,
    FACULTY_NAME    VARCHAR(255) NOT NULL,
    CREATED_AT      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT PK_FACULTY PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS GWBACKEND.USERS
(
    ID              BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL UNIQUE,
    USERNAME        VARCHAR(255) NOT NULL,
    PASSWORD        VARCHAR(255) NOT NULL,
    EMAIL           VARCHAR(255) NOT NULL UNIQUE,
    USER_ROLE       VARCHAR(255) NOT NULL,
    FACULTY_ID      BIGINT,
    CREATED_AT      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT PK_USER PRIMARY KEY (ID),
    CONSTRAINT FK_FACULTY FOREIGN KEY (FACULTY_ID) REFERENCES GWBACKEND.FACULTY(ID)
);

CREATE TABLE IF NOT EXISTS GWBACKEND.CONTRIBUTION
(
    ID              BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL UNIQUE,
    UPLOAD_USER_ID  BIGINT NOT NULL,
    APPROVED_COORDINATOR_ID BIGINT NOT NULL,
    STUDENT_ID      VARCHAR(255) NOT NULL,
    STUDENT_NAME    VARCHAR(255) NOT NULL,
    TITLE           VARCHAR(255) NOT NULL,
    CONTENT         VARCHAR(255) NOT NULL
    SUBMISSION_PERIOD_ID BIGINT NOT NULL,
    STATUS          VARCHAR(255) NOT NULL,
    CREATED_AT      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT PK_CONTRIBUTION PRIMARY KEY (ID),
    CONSTRAINT FK_UPLOAD_USER_ID FOREIGN KEY (UPLOAD_USER_ID) REFERENCES GWBACKEND.USERS(ID),
    CONSTRAINT FK_APPROVED_COORDINATOR_ID FOREIGN KEY (APPROVED_COORDINATOR_ID) REFERENCES GWBACKEND.USERS(ID),
    CONSTRAINT FK_IMAGE_ID FOREIGN KEY (IMAGE_ID) REFERENCES GWBACKEND.IMAGE(ID)
    CONSTRAINT FK_DOCUMENT_ID FOREIGN KEY (DOCUMENT_ID) REFERENCES GWBACKEND.DOCUMENT(ID)
    CONSTRAINT FK_COORDINATOR FOREIGN KEY (ID) REFERENCES GWBACKEND.USERS(ID)
);

CREATE TABLE IF NOT EXISTS GWBACKEND.SUBMISSION_PERIOD
(
    ID              BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL UNIQUE,
    NAME            VARCHAR(255) NOT NULL,
    START_DATE      TIMESTAMP NOT NULL,
    CLOSURE_DATE    TIMESTAMP NOT NULL,
    FINAL_CLOSURE_DATE TIMESTAMP NOT NULL,
    CREATED_AT      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT PK_SUBMISSION_PERIOD PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS GWBACKEND.COMMENTS
(
    ID              BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL UNIQUE,
    USER_ID         BIGINT NOT NULL,
    CONTRIBUTION_ID BIGINT NOT NULL,
    CONTENT         TEXT NOT NULL,
    CREATED_AT      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT PK_COMMENTS PRIMARY KEY (ID),
    CONSTRAINT FK_USER FOREIGN KEY (USER_ID) REFERENCES GWBACKEND.USERS(ID),
    CONSTRAINT FK_CONTRIBUTION FOREIGN KEY (CONTRIBUTION_ID) REFERENCES GWBACKEND.CONTRIBUTION(ID),

);

CREATE TABLE IF NOT EXISTS GWBACKEND.IMAGE
(
    ID              BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL UNIQUE,
    CONTRIBUTION_ID BIGINT NOT NULL,
    PATH            VARCHAR(255) NOT NULL,
    CREATED_AT      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT PK_IMAGE PRIMARY KEY (ID),
    CONSTRAINT FK_CONTRIBUTION FOREIGN KEY (CONTRIBUTION_ID) REFERENCES GWBACKEND.CONTRIBUTION(ID)
);

CREATE TABLE IF NOT EXISTS GWBACKEND.DOCUMENT
(
    ID              BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL UNIQUE,
    CONTRIBUTION_ID BIGINT NOT NULL,
    PATH            VARCHAR(255) NOT NULL,
    CREATED_AT      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT PK_DOCUMENT PRIMARY KEY (ID),
    CONSTRAINT FK_CONTRIBUTION FOREIGN KEY (CONTRIBUTION_ID) REFERENCES GWBACKEND.CONTRIBUTION(ID)
);

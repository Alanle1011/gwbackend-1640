CREATE SCHEMA IF NOT EXISTS GWBACKEND;

CREATE TABLE IF NOT EXISTS GWBACKEND.FACULTIES
(
    ID              BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL UNIQUE,
    FACULTY_NAME    VARCHAR(255) NOT NULL,
    COORDINATOR_ID  BIGINT NOT NULL,
    CREATED_AT      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT PK_FACULTY PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS GWBACKEND.USERS
(
    ID              BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL UNIQUE,
    NAME        VARCHAR(255) NOT NULL,
    PASSWORD        VARCHAR(255) NOT NULL,
    EMAIL           VARCHAR(255) NOT NULL UNIQUE,
    USER_ROLE       VARCHAR(255) NOT NULL,
    FACULTY_ID      BIGINT,
    CREATED_AT      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT PK_USER PRIMARY KEY (ID),
    CONSTRAINT FK_FACULTY FOREIGN KEY (FACULTY_ID) REFERENCES GWBACKEND.FACULTIES(ID)
);

ALTER TABLE GWBACKEND.FACULTIES
ADD CONSTRAINT FK_COORDINATOR FOREIGN KEY (COORDINATOR_ID) REFERENCES GWBACKEND.USERS(ID);

CREATE TABLE IF NOT EXISTS GWBACKEND.SUBMISSION_PERIODS
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

CREATE TABLE IF NOT EXISTS GWBACKEND.CONTRIBUTIONS
(
    ID              BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL UNIQUE,
    UPLOADED_USER_ID  BIGINT NOT NULL,
    APPROVED_COORDINATOR_ID BIGINT NOT NULL,
    TITLE           VARCHAR(255) NOT NULL,
    CONTENT         VARCHAR(255) NOT NULL,
    SUBMISSION_PERIOD_ID BIGINT NOT NULL,
    STATUS          VARCHAR(255) NOT NULL,
    CREATED_AT      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT PK_CONTRIBUTION PRIMARY KEY (ID),
    CONSTRAINT FK_UPLOADED_USER_ID FOREIGN KEY (UPLOADED_USER_ID) REFERENCES GWBACKEND.USERS(ID),
    CONSTRAINT FK_APPROVED_COORDINATOR_ID FOREIGN KEY (APPROVED_COORDINATOR_ID) REFERENCES GWBACKEND.USERS(ID),
    CONSTRAINT FK_SUBMISSION_PERIOD_ID FOREIGN KEY (SUBMISSION_PERIOD_ID) REFERENCES GWBACKEND.SUBMISSION_PERIODS(ID)
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
    CONSTRAINT FK_CONTRIBUTION FOREIGN KEY (CONTRIBUTION_ID) REFERENCES GWBACKEND.CONTRIBUTIONS(ID)
);

CREATE TABLE IF NOT EXISTS GWBACKEND.IMAGES
(
    ID              BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL UNIQUE,
    CONTRIBUTION_ID BIGINT NULL,
    USER_ID         BIGINT NULL,
    NAME            VARCHAR(255) NOT NULL,
    TYPE            VARCHAR(255) NOT NULL,
    DATA            OID NOT NULL,
    CREATED_AT      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT PK_IMAGE PRIMARY KEY (ID),
    CONSTRAINT FK_CONTRIBUTION FOREIGN KEY (CONTRIBUTION_ID) REFERENCES GWBACKEND.CONTRIBUTIONS(ID),
    CONSTRAINT FK_USER_IMAGE FOREIGN KEY (USER_ID) REFERENCES GWBACKEND.USERS(ID)
);

CREATE TABLE IF NOT EXISTS GWBACKEND.DOCUMENTS
(
    ID              BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL UNIQUE,
    CONTRIBUTION_ID BIGINT NULL, --Not yet figure out how to link this to the contribution
    NAME            VARCHAR(255) NOT NULL,
    TYPE            VARCHAR(255) NOT NULL,
    DATA            OID NOT NULL,
    CREATED_AT      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT PK_DOCUMENT PRIMARY KEY (ID),
    CONSTRAINT FK_CONTRIBUTION FOREIGN KEY (CONTRIBUTION_ID) REFERENCES GWBACKEND.CONTRIBUTIONS(ID)
);

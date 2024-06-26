DROP TABLE IF EXISTS GWBACKEND.COMMENTS;

CREATE TABLE IF NOT EXISTS GWBACKEND.COMMENTS
(
    ID              BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL UNIQUE,
    USER_ID         BIGINT,
    CONTRIBUTION_ID BIGINT,
    IS_PUBLISHED_CONTRIBUTION BOOLEAN DEFAULT FALSE,
    CONTENT         TEXT NOT NULL,
    CREATED_AT      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT PK_COMMENTS PRIMARY KEY (ID),
    CONSTRAINT FK_USER FOREIGN KEY (USER_ID) REFERENCES GWBACKEND.USERS(ID),
    CONSTRAINT FK_CONTRIBUTION FOREIGN KEY (CONTRIBUTION_ID) REFERENCES GWBACKEND.CONTRIBUTIONS(ID)
);
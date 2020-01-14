SHOW DATABASES;

DROP DATABASE IF EXISTS socialSite;
CREATE DATABASE socialSite;
USE socialSite;

CREATE TABLE user (

	ID 	   		INT UNSIGNED NOT NULL AUTO_INCREMENT,
    email 		VARCHAR(50) NOT NULL UNIQUE,
    password	VARCHAR(70) NOT NULL,
    salt 		VARCHAR(50) NOT NULL,
    name 		VARCHAR(50) NOT NULL,
    phone 		VARCHAR(13) NOT NULL,
    isMale 		BOOL NOT NULL,
    dateOfBirth DATE,
    join_date	DATE NOT NULL,
    
    CONSTRAINT user_PK PRIMARY KEY (ID)
    
);


CREATE TABLE verification (

	userEmail 		VARCHAR(50) NOT NULL UNIQUE,
    code 			CHAR(5) NOT NULL,
    created_date	TIMESTAMP NOT NULL,
    
			-- CHECK CONSTRAINT TO PROTECT AGAINST NOT VALID SALT 
    CONSTRAINT verificationCode_check CHECK (LENGTH(REPLACE(code,' ',''))  = 5), 
    CONSTRAINT	verification_FK FOREIGN KEY (userEmail) REFERENCES user (email) ON DELETE CASCADE
    
    
);

CREATE TABLE resetPassword (
	user_email   VARCHAR(50) NOT NULL UNIQUE,
    token		 VARCHAR(70) NOT NULL UNIQUE,
    expire_date  DATETIME NOT NULL,
    
    CONSTRAINT resetPassword_FK FOREIGN KEY (user_email) REFERENCES user (email) ON DELETE CASCADE
);

-- TESTING COMMANDS
SELECT * FROM user;
SELECT * FROM verification;
SELECT * FROM resetPassword;


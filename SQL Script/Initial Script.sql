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

CREATE TABLE friendRequest (
	senderID   INT UNSIGNED NOT NULL,
    receiverID INT UNSIGNED NOT NULL,
    state	   ENUM('PENDING','ACCEPTED','REFUSED') NOT NULL,
    created_date DATE NOT NULL,
    
    CONSTRAINT friendRequest_PK PRIMARY KEY (senderID,receiverID),
    CONSTRAINT friendRequest_Sender_FK FOREIGN KEY (senderID) REFERENCES user (ID) ON DELETE CASCADE,
    CONSTRAINT friendRequest_Receiver_FK FOREIGN KEY (receiverID) REFERENCES user (ID) ON DELETE CASCADE
);

-- TESTING COMMANDS
USE socialSite;
SELECT * FROM user;
SELECT * FROM verification;
SELECT * FROM resetPassword;
SELECT * FROM friendRequest;

update friendRequest set state = 'ACCEPTED' Where senderID = 4 AND receiverID = 8;
delete from friendRequest where (senderID = 4 AND receiverID = 6) OR (senderID = 4 AND receiverID = 6);
SELECT  ID , name , email FROM user JOIN friendRequest ON (senderID = user.ID) WHERE receiverID = 4 AND state = 'PENDING';
Drop table friendRequest;
Delete From friendRequest;
Delete From user;

INSERT INTO user VALUES (default,'aaa5@gmail.com','00000000','0000000000','MIDO',01023839260,1,null,current_date());
INSERT INTO user VALUES (default,'bbb5@gmail.com','00000000','0000000000','Ali',01023839260,1,null,current_date());
INSERT INTO user VALUES (default,'ccc5@gmail.com','00000000','0000000000','MAZEN',01023839260,1,null,current_date());
INSERT INTO user VALUES (default,'dddd5@gmail.com','00000000','0000000000','ADEL',01023839260,1,null,current_date());
INSERT INTO user VALUES (default,'GG5@gmail.com','00000000','0000000000','Mohamed',01023839260,1,null,current_date());
INSERT INTO user VALUES (default,'FF5@gmail.com','00000000','0000000000','Mohaned',01023839260,1,null,current_date());
INSERT INTO user VALUES (default,'nn5@gmail.com','00000000','0000000000','Noor',01023839260,0,null,current_date());

INSERT INTO friendRequest VALUES (4,6,'ACCEPTED',CURRENT_DATE());
INSERT INTO friendRequest VALUES (4,8,default,CURRENT_DATE());
INSERT INTO friendRequest VALUES (9,4,'ACCEPTED',CURRENT_DATE());
INSERT INTO friendRequest VALUES (7,5,default,CURRENT_DATE());
INSERT INTO friendRequest VALUES (7,8,default,CURRENT_DATE());
INSERT INTO friendRequest VALUES (10,4,default,CURRENT_DATE());
INSERT INTO friendRequest VALUES (11,9,default,CURRENT_DATE());
INSERT INTO friendRequest VALUES (4,7,default,CURRENT_DATE());


SELECT distinct ID,email,name,state FROM user left join friendRequest On  (senderID = ID  OR receiverID = ID) where  ID != 2;

SELECT user.ID , user.name ,  email ,CASE WHEN state IS NULL THEN 'NOT FRIEND' Else state END AS State FROM user
 left join
 (
 SELECT u2.ID , CASE WHEN state = 'PENDING' AND receiverID = 4 THEN 'REPLY' ELSE state END AS state
 FROM user as u2 join friendRequest ON (senderID = u2.ID  OR receiverID = u2.ID) Where (senderID = 4  OR receiverID = 4) AND u2.ID != 4
 ) As requests ON user.ID = requests.ID where user.ID != 4 AND (name LIKE "%a%" OR email LIKE "%a%") ;
 
 SELECT u2.ID , Case when state = 'PENDING' AND receiverID = 2 THEN 'REPLY' ELSE state END As state
 FROM user as u2 join friendRequest ON (senderID = u2.ID  OR receiverID = u2.ID) Where (senderID = 2  OR receiverID = 2) AND u2.ID != 2;
 
 SELECT  ID , name ,email , phone FROM user JOIN friendRequest ON (user.ID = senderID OR user.ID = receiverID)
WHERE user.ID != 4 AND (senderID = 4 OR receiverID = 4 ) AND state = 'ACCEPTED';
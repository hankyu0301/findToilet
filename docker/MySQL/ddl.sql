DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS review;
DROP TABLE IF EXISTS toilet;

CREATE TABLE member (
                        member_id BIGINT NOT NULL AUTO_INCREMENT,
                        created_at DATETIME(6),
                        modified_at DATETIME(6),
                        email VARCHAR(30) NOT NULL,
                        nickname VARCHAR(20) NOT NULL,
                        password VARCHAR(255),
                        role VARCHAR(32) DEFAULT 'ROLE_USER',
                        PRIMARY KEY (member_id)
) ENGINE=InnoDB;

-- review 테이블 생성
CREATE TABLE review (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        created_at DATETIME(6),
                        modified_at DATETIME(6),
                        content VARCHAR(255) NOT NULL,
                        score DOUBLE PRECISION NOT NULL,
                        member_id BIGINT NOT NULL,
                        toilet_id BIGINT NOT NULL,
                        PRIMARY KEY (id)
) ENGINE=InnoDB;

-- toilet 테이블 생성
CREATE TABLE toilet (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        created_at DATETIME(6),
                        modified_at DATETIME(6),
                        address VARCHAR(255) NOT NULL,
                        diaper BIT NOT NULL,
                        female_disabled BIT NOT NULL,
                        female_kids BIT NOT NULL,
                        latitude DOUBLE PRECISION NOT NULL,
                        longitude DOUBLE PRECISION NOT NULL,
                        male_disabled BIT NOT NULL,
                        male_kids BIT NOT NULL,
                        name VARCHAR(255) NOT NULL,
                        operation_time VARCHAR(255),
                        road_address VARCHAR(255) NOT NULL,
                        PRIMARY KEY (id)
) ENGINE=InnoDB;
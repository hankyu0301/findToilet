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
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        road_address VARCHAR(255) NOT NULL,
                        address VARCHAR(255) NOT NULL,
                        location POINT NOT NULL SRID 4326,
                        male_disabled BOOLEAN NOT NULL,
                        female_disabled BOOLEAN NOT NULL,
                        male_kids BOOLEAN NOT NULL,
                        female_kids BOOLEAN NOT NULL,
                        diaper BOOLEAN NOT NULL,
                        operation_time VARCHAR(100),
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        SPATIAL INDEX(location)
) ENGINE=InnoDB;
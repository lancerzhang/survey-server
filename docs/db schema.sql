CREATE TABLE delegates (
    id INT PRIMARY KEY AUTO_INCREMENT,
    delegator_id INT NOT NULL,
    delegate_id INT NOT NULL,
    FOREIGN KEY (delegate_id) REFERENCES users(id)
);

CREATE TABLE options (
    id INT PRIMARY KEY AUTO_INCREMENT,
    option_text VARCHAR(4000) NOT NULL,
    question_id INT,
    FOREIGN KEY (question_id) REFERENCES questions(id)
);

CREATE TABLE option_replies (
    id INT PRIMARY KEY AUTO_INCREMENT,
    questionReply_id INT,
    option_id INT NOT NULL,
    selected BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (questionReply_id) REFERENCES question_replies(id)
);

CREATE TABLE prizes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    survey_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(4000),
    quantity INT NOT NULL,
    created_at TIMESTAMP,
    last_modified TIMESTAMP
);

CREATE TABLE questions (
    id INT PRIMARY KEY AUTO_INCREMENT,
    question_type VARCHAR(255) NOT NULL,
    question_text VARCHAR(4000) NOT NULL,
    is_mandatory BOOLEAN DEFAULT FALSE,
    min_selection INT,
    max_selection INT,
    survey_id INT,
    FOREIGN KEY (survey_id) REFERENCES surveys(id)
);

CREATE TABLE question_replies (
    id INT PRIMARY KEY AUTO_INCREMENT,
    surveyReply_id INT,
    question_id INT NOT NULL,
    reply_text VARCHAR(4000),
    FOREIGN KEY (surveyReply_id) REFERENCES survey_replies(id)
);

CREATE TABLE surveys (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(8000),
    is_anonymous BOOLEAN DEFAULT FALSE,
    allow_resubmit BOOLEAN DEFAULT FALSE,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    max_replies INT,
    is_deleted BOOLEAN DEFAULT FALSE,
    is_template BOOLEAN NOT NULL,
    created_at TIMESTAMP,
    last_modified TIMESTAMP
);

CREATE TABLE survey_replies (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    survey_id INT,
    is_anonymous BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP,
    last_modified TIMESTAMP,
    FOREIGN KEY (survey_id) REFERENCES surveys(id)
);

CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    display_name VARCHAR(255) NOT NULL,
    employee_id VARCHAR(255),
    public_key VARCHAR(1000),
    email VARCHAR(255),
    created_at TIMESTAMP,
    last_modified TIMESTAMP
);

CREATE TABLE winners (
    id INT PRIMARY KEY AUTO_INCREMENT,
    prize_id INT,
    survey_id INT NOT NULL,
    user_id INT,
    won_at TIMESTAMP,
    FOREIGN KEY (prize_id) REFERENCES prizes(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE survey_access (
    id INT PRIMARY KEY AUTO_INCREMENT,
    survey_id INT NOT NULL,
    user_id INT NOT NULL,
    granted_at TIMESTAMP,
    FOREIGN KEY (survey_id) REFERENCES surveys(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

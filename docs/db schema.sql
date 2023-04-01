CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    is_anonymous BOOLEAN NOT NULL DEFAULT false,
    public_key VARCHAR(255) NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    last_modified TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE surveys (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    user_id INT NOT NULL,
    allow_anonymous_reply BOOLEAN NOT NULL DEFAULT FALSE,
    allow_resubmit BOOLEAN NOT NULL DEFAULT FALSE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    last_modified TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    start_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    end_time TIMESTAMP,
    max_replies INT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE questions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    survey_id INT NOT NULL,
    question_text TEXT NOT NULL,
    question_type ENUM('TEXT', 'RADIO', 'CHECKBOX') NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (survey_id) REFERENCES surveys(id)
);

CREATE TABLE options (
    id INT AUTO_INCREMENT PRIMARY KEY,
    question_id INT NOT NULL,
    option_text VARCHAR(255) NOT NULL,
    FOREIGN KEY (question_id) REFERENCES questions(id)
);

CREATE TABLE survey_reply (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    survey_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (survey_id) REFERENCES surveys(id),
    UNIQUE (user_id, survey_id)
);

CREATE TABLE question_reply (
    id INT AUTO_INCREMENT PRIMARY KEY,
    survey_reply_id INT NOT NULL,
    question_id INT NOT NULL,
    reply_text TEXT,
    FOREIGN KEY (survey_reply_id) REFERENCES survey_reply(id),
    FOREIGN KEY (question_id) REFERENCES questions(id),
    UNIQUE (survey_reply_id, question_id)
);

CREATE TABLE option_reply (
    id INT AUTO_INCREMENT PRIMARY KEY,
    question_reply_id INT NOT NULL,
    option_id INT NOT NULL,
    selected BOOLEAN NOT NULL DEFAULT false,
    FOREIGN KEY (question_reply_id) REFERENCES question_reply(id),
    FOREIGN KEY (option_id) REFERENCES options(id),
    UNIQUE (question_reply_id, option_id)
);

CREATE TABLE prizes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    survey_id INT NOT NULL,
    prize_name VARCHAR(255) NOT NULL,
    prize_description TEXT,
    total_quantity INT NOT NULL,
    remaining_quantity INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (survey_id) REFERENCES surveys(id)
);

CREATE TABLE winners (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    survey_id INT NOT NULL,
    prize_id INT NOT NULL,
    won_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (survey_id) REFERENCES surveys(id),
    FOREIGN KEY (prize_id) REFERENCES prizes(id)
);

CREATE TABLE delegate (
    id INT AUTO_INCREMENT PRIMARY KEY,
    delegator_id INT NOT NULL,
    delegate_id INT NOT NULL,
    FOREIGN KEY (delegator_id) REFERENCES users(id),
    FOREIGN KEY (delegate_id) REFERENCES users(id),
    UNIQUE (delegator_id, delegate_id)
);

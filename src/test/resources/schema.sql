CREATE TABLE IF NOT EXISTS students
(
 id INT AUTO_INCREMENT PRIMARY KEY,
 full_name VARCHAR(100) NOT NULL,
 furigana VARCHAR(100) NOT NULL,
 nickname VARCHAR(100),
 email_address VARCHAR(100) NOT NULL,
 address VARCHAR(200) NOT NULL,
 age INT,
 sex VARCHAR(30) NOT NULL,
 remark VARCHAR(500),
 is_deleted BOOLEAN DEFAULT FALSE

);

CREATE TABLE IF NOT EXISTS students_courses
(
 id INT AUTO_INCREMENT PRIMARY KEY,
 students_id INT,
 course_name VARCHAR(100),
 start_date DATE,
 end_date DATE,
 FOREIGN KEY (students_id) REFERENCES students(id) ON DELETE CASCADE
 );

CREATE TABLE IF NOT EXISTS application_status
(
 id INT AUTO_INCREMENT PRIMARY KEY,
 course_id INT,
 status ENUM('仮申込', '本申込', '受講中', '受講終了'),
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 FOREIGN KEY (course_id) REFERENCES students_courses(id) ON DELETE CASCADE
 );




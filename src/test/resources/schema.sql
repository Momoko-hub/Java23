CREATE TABLE IF NOT EXISTS students
(
 id INT PRIMARY KEY,
 full_name VARCHAR(100),
 furigana VARCHAR(100),
 nickname VARCHAR(100),
 email_address VARCHAR(100)
 address VARCHAR(200)
 age INT,
 sex VARCHAR(30),
 remark VARCHAR(500),
 is_deleted TINYINT(1)

);

CREATE TABLE IF NOT EXISTS students_courses
(
 id INT PRIMARY KEY,
 student_id INT,
 course_name VARCHAR(100)
 start_date DATE,
 end_date DATE

 );


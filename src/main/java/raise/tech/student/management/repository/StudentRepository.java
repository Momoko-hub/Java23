package raise.tech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import raise.tech.student.management.data.Student;
import raise.tech.student.management.data.StudentsCourses;
import raise.tech.student.management.domain.StudentDetail;

/**
 * 受講生情報を扱うリポジトリ
 * 全件検索や単質条件での検索、コース情報の検索が行えるクラスです。
 */

@Mapper
public interface StudentRepository {

    /**
     * 全件検索します。
     *
     * * @return 全件検索した受講生情報の一覧。
     */

    @Select("SELECT * FROM students")
    List<Student> searchStudents();

    @Select("SELECT * FROM students_courses")
    List<StudentsCourses> searchCourse();

    /**
     * 新規受講生の名前を保存します。
     * @param fullName　受講生のフルネーム
     */

    @Insert("INSERT INTO students (full_name, furigana, nickname, email_address, address, age, sex, remark, is_deleted) " +
            "VALUES (#{fullName}, #{furigana}, #{nickname}, #{emailAddress}, #{address}, #{age}, #{sex}, #{remark}, #{isDeleted})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertStudent(Student student);

    /**
     * 新規受講生のコース情報を保存します。
     * @param studentsCourses　受講生のコース情報
     * @param studentId　受講生のID
     */


    @Insert("INSERT INTO students_courses (students_id, course_name, start_date, end_date)" +
        "VALUES (#{studentsId}, #{courseName}, #{startDate}, #{endDate})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void  insertStudentsCourses(StudentsCourses studentsCourses);
}


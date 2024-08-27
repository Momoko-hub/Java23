package raise.tech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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
    List<StudentsCourses> searchStudentCoursesList();

    /**
     * 新規受講生の名前を保存します。
     * @param student　受講生のフルネーム
     */

    @Insert("INSERT INTO students (full_name, furigana, nickname, email_address, address, age, sex, remark, is_deleted) " +
            "VALUES (#{fullName}, #{furigana}, #{nickname}, #{emailAddress}, #{address}, #{age}, #{sex}, #{remark}, #{isDeleted})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertStudent(Student student);

    /**
     * 新規受講生のコース情報を保存します。
     * @param studentsCourses　受講生のコース情報
     */


    @Insert("INSERT INTO students_courses (students_id, course_name, start_date, end_date)" +
        "VALUES (#{studentsId}, #{courseName}, #{startDate}, #{endDate})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void  insertStudentsCourses(StudentsCourses studentsCourses);

    /**
     * 受講生情報を更新します。
     * @param student 受講生情報
     */

    @Update("UPDATE students SET full_name = #{fullName}, furigana = #{furigana}, nickname = #{nickname}, " +
            "email_address = #{emailAddress}, address = #{address}, age = #{age}, sex = #{sex}, remark = #{remark}, " +
            "is_deleted = #{isDeleted} WHERE id = #{id}")
    void updateStudent(Student student);

    @Update("UPDATE students_courses SET course_name = #{courseName} WHERE id = #{id}")
    void updateStudentsCourses(StudentsCourses studentsCourses);

    /**
     * IDを基に学生情報を取得します。
     * @param id 学生ID
     * @return 学生情報
     */

    @Select("SELECT * FROM students WHERE id = #{id}")
    Student searchStudent(Long id);

    @Select("SELECT * FROM students_courses WHERE students_id = #{studentsId}")
    List<StudentsCourses> searchStudentCourses(Long studentId);


}


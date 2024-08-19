package raise.tech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import raise.tech.student.management.data.Student;
import raise.tech.student.management.data.StudentsCourses;

/**
 * 受講生情報を扱うリポジトリ
 * 全件検索や単質条件での検索、コース情報の検索が行えるクラスです。
 */
@Mapper
public interface StudentRepository {

    /**
     * 全件検索します。
     *
     * @return 全件検索した受講生情報の一覧。
     */

    @Select("SELECT * FROM students")
    List<Student> searchStudents();

    @Select("SELECT * FROM students_courses")
    List<StudentsCourses> searchCourse();

  }

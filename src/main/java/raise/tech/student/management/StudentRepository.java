package raise.tech.student.management;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface StudentRepository {

    List<Student> studentsList();

    @Select("SELECT * FROM students")
    List<Student> searchStudents();

    @Select("SELECT * FROM students_courses")
    List<StudentsCourses> searchCourse();

  }

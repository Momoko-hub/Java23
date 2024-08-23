package raise.tech.student.management.domain;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import raise.tech.student.management.data.Student;
import raise.tech.student.management.data.StudentsCourses;

@Getter
@Setter
public class StudentDetail {

  private Student student;
  private List<StudentsCourses> studentsCourses;



}

package raise.tech.student.management.controller.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raise.tech.student.management.data.Student;
import raise.tech.student.management.data.StudentsCourses;
import raise.tech.student.management.domain.StudentDetail;

@Component
public class StudentConverter {

  public List<StudentDetail> convertStudentDetails(List<Student> students,
      List<StudentsCourses> studentsCourses) {
    List<StudentDetail> studentDetails = new ArrayList<>();
    students.forEach(student -> {
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student);

      List<StudentsCourses> convertStudentCourses = studentsCourses.stream()
          .filter(studentsCourse -> student.getId().equals(studentsCourse.getStudentsId()))
          .collect(Collectors.toList());

      studentDetail.setStudentsCourses(convertStudentCourses);
      studentDetails.add(studentDetail);
    });
    return studentDetails;
  }

}
package raise.tech.student.management.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raise.tech.student.management.data.Student;
import raise.tech.student.management.data.StudentsCourses;
import raise.tech.student.management.domain.StudentDetail;
import raise.tech.student.management.repository.StudentRepository;

@Service
public class StudentService {

  private final StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentsList() {
    return repository.searchStudents();
  }

  public List<StudentsCourses> searchCourseList() {
    return repository.searchCourse();
  }

  @Transactional
  public void saveStudent(StudentDetail studentDetail){
    repository.insertStudent(studentDetail.getStudent());
  }


}



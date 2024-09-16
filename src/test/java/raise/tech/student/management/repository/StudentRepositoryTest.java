package raise.tech.student.management.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raise.tech.student.management.data.Student;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void 受講生の全件検索が行われること() {
    List<Student> actual = sut.searchStudents();
    assertThat(actual.size()).isEqualTo(5);
  }

  @Test
  void 受講生の登録が行えること() {
    Student student = new Student();

    student.setFullName("大田　桃子");
    student.setFurigana("オオタ　モモコ");
    student.setNickname("もも");
    student.setEmailAddress("momo@email.com");
    student.setAddress("東京都");
    student.setAge(29);
    student.setSex("女性");

    sut.insertStudent(student);
    
    List<Student> actual = sut.searchStudents();

    assertThat(actual.size()).isEqualTo(6);
  }
}
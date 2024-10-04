package raise.tech.student.management.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import raise.tech.student.management.controller.converter.StudentConverter;
import raise.tech.student.management.data.Student;
import raise.tech.student.management.repository.StudentRepository;
import raise.tech.student.management.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private StudentService service;

  @MockBean
  private StudentRepository repository;

  @MockBean
  private StudentConverter converter;


  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  void 受講生詳細の一覧検索が実行できて空のリストが返ってくること() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/studentsList"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    verify(service, times(1)).searchAllStudentList();
  }

  @Test
  void IDに紐づく受講生詳細が実行できて空のリストが返ってくること() throws Exception {
    int id = 12345;

    mockMvc.perform(MockMvcRequestBuilders.get("/student/{id}", id))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudent(id);
  }

  @Test
  void 受講生詳細の登録が実行されているか() throws Exception {
    mockMvc.perform(post("/registerStudent")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                        {
                            "student": {
                      "fullName": "森田恵麻",
                      "furigana": "モリタエマ",
                      "nickname": "Emma",
                      "emailAddress": "emma@email.jp",
                      "address": "埼玉県大宮市",
                      "age": 30,
                      "sex": "女性",
                      "remark": ""
                },
                "studentCourseList": [
                {
                
                    "courseName": "Webデザイン"
                
                    }
                  ]
                }
                """))
        .andExpect(status().isOk());

    verify(service, times(1)).registerStudent(any());


  }

  @Test
  void 受講生詳細の変更が実行されているか() throws Exception {
    mockMvc.perform(put("/updateStudent")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                       "student": {
                           "id": 197555,
                           "fullName": "森田恵麻",
                           "furigana": "モリタエマ",
                           "nickname": "Emma",
                           "emailAddress": "emma@email.jp",
                           "address": "埼玉県大宮市",
                           "age": 30,
                           "sex": "女性",
                           "remark": null,
                           "isDeleted": false
                       },
                       "studentCourseList": [
                           {
                               "id": 996284,
                               "studentsId": 197555,
                               "courseName": "Webデザイン",
                               "startDate": "2024-09-01T00:00:00",
                               "endDate": "2025-09-01T00:00:00"
                           },
                           {
                               "id": 996285,
                               "studentsId": 197555,
                               "courseName": "AWS",
                               "startDate": "2023-09-01T00:00:00",
                               "endDate": "2024-09-01T00:00:00"
                           }
                       ]
                   }
                """))
        .andExpect(status().isOk());

    verify(service, times(1)).updateStudent(any());
  }

  @Test
  void 受講生詳細の例外APIが実行できてステータスが400で返ってくること() throws Exception {
    mockMvc.perform(get("/students"))
        .andExpect(status().is4xxClientError())
        .andExpect(content().string(
            "現在このAPIは使用できません。URLは「students』ではなく「studentsList」を利用してください。"));
  }

  @Test
  void 受講生詳細の受講生で適切な値を入力した時に異常が発生しないこと() {
    Student student = new Student();
    student.setId(12345);
    student.setFullName("大田　桃子");
    student.setFurigana("オオタ　モモコ");
    student.setNickname("もも");
    student.setEmailAddress("momo@email.com");
    student.setAddress("東京都");
    student.setAge(29);
    student.setSex("女性");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);
    assertThat(violations.size()).isEqualTo(0);
  }

  @Test
  void 受講生詳細の受講生で名前が空欄だった時に入力チェックが掛かること() {
    Student student = new Student();
    student.setId(12345);
    student.setFullName("");
    student.setFurigana("オオタ　モモコ");
    student.setNickname("もも");
    student.setEmailAddress("momo@email.com");
    student.setAddress("東京都");
    student.setAge(29);
    student.setSex("女性");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);
    assertThat(violations.size()).isEqualTo(1);
  }
}

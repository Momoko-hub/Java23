package raise.tech.student.management.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import raise.tech.student.management.controller.converter.StudentConverter;
import raise.tech.student.management.data.ApplicationStatus;
import raise.tech.student.management.data.Student;
import raise.tech.student.management.data.StudentCourse;
import raise.tech.student.management.domain.Status;
import raise.tech.student.management.domain.StudentDetail;
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

  private List<StudentDetail> studentDetailList;

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  void 受講生詳細の一覧検索が実行できて空のリストが返ってくること＿論理削除含む() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/students"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    verify(service, times(1)).searchStudentList();
  }

  @Test
  void 受講生詳細の一覧検索が実行できて空のリストが返ってくること＿論理削除除く() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/students")
            .param("includeDeleted", "true"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    verify(service, times(1)).searchAllStudentList();
  }


  @Test
  void IDに紐づく受講生詳細が実行できて空のリストが返ってくること() throws Exception {
    int id = 12345;

    mockMvc.perform(MockMvcRequestBuilders.get("/students/{id}", id))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudent(id);
  }

  @Test
  void 名前検索で受講生詳細を取得できるか() {

    Student student = new Student();
    student.setFullName("テスト花子");

    StudentCourse course1 = new StudentCourse();
    course1.setCourseName("Java");

    StudentCourse course2 = new StudentCourse();
    course2.setCourseName("AWS");

    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentCourseList(Arrays.asList(course1, course2));

    when(service.searchStudentsByConditions("テスト花子", null, null, null, null, null, null))
        .thenReturn(Arrays.asList(studentDetail));

    List<StudentDetail> result = service.searchStudentsByConditions("テスト花子", null, null, null,
        null, null, null);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getStudent().getFullName()).isEqualTo("テスト花子");
    assertThat(result.get(0).getStudentCourseList()).hasSize(2);
  }


  @Test
  void 受講生詳細の登録が実行されているか() throws Exception {
    mockMvc.perform(post("/students")
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
    mockMvc.perform(put("/students")
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
  void 申込状況が適切に変更されているか() throws Exception {
    Integer testId = 123;
    ApplicationStatus status1 = new ApplicationStatus();
    status1.setId(testId);
    status1.setStatus(Status.仮申込);

    List<ApplicationStatus> mockApplicationStatus = Arrays.asList(status1);

    when(service.findStatusById(testId)).thenReturn(mockApplicationStatus);
    doAnswer(invocation -> {
      List<ApplicationStatus> statuses = invocation.getArgument(0);
      statuses.forEach(status -> status.setStatus(Status.本申込));
      return null;
    }).when(service).updateApplicationStatus(mockApplicationStatus, Status.本申込);

    mockMvc.perform(put("/students/request-status")
            .param("id", String.valueOf(testId))
            .param("status", Status.仮申込.name()))
        .andExpect(MockMvcResultMatchers.status().isOk());

    verify(service, times(1)).findStatusById(testId);
    verify(service, times(1)).updateApplicationStatus(mockApplicationStatus, Status.本申込);
    assertThat(mockApplicationStatus.get(0).getStatus()).isEqualTo(Status.本申込);

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

  @Test
  void 受講生詳細の例外APIが実行できてステータスが400で返ってくること() throws Exception {
    mockMvc.perform(get("/students/descriptions"))
        .andExpect(status().is4xxClientError())
        .andExpect(content().string(
            "現在このAPIは使用できません。URLは「students』ではなく「studentsList」を利用してください。"));
  }
}

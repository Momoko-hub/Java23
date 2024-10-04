package raise.tech.student.management.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raise.tech.student.management.controller.converter.StudentConverter;
import raise.tech.student.management.data.ApplicationStatus;
import raise.tech.student.management.data.Student;
import raise.tech.student.management.data.StudentCourse;
import raise.tech.student.management.domain.Status;
import raise.tech.student.management.domain.StudentDetail;
import raise.tech.student.management.exception.TestException;
import raise.tech.student.management.repository.StudentRepository;

/**
 * 受講生情報を取り扱うサービスです。 受講生の検索や登録・更新処理を行います。
 */
@Service
public class StudentService {

  private final StudentRepository repository;
  private final StudentConverter converter;


  @Autowired
  public StudentService(StudentRepository repository, StudentConverter converter) {
    this.repository = repository;
    this.converter = converter;

  }

  /**
   * 受講生詳細の一覧検索です。 全件検索を行うので、条件指定は行わないものになります。
   *
   * @return 受講生詳細一覧（全件）
   */
  public List<StudentDetail> searchAllStudentList() {
    List<Student> studentList = repository.searchAllStudents();

    List<StudentCourse> updatedCourses = studentList.stream().flatMap(student -> {

      List<StudentCourse> studentCourseList = repository.searchStudentCourseByStudentId(
          student.getId());

      return studentCourseList.stream().map(course -> {
        List<ApplicationStatus> applicationStatuses = repository.searchApplicationStatusByCourseId(
            course.getId());

        if (!applicationStatuses.isEmpty()) {
          course.setStatus(applicationStatuses.getFirst().getStatus());
        }
        return course;
      });
    }).collect(Collectors.toList());

    List<ApplicationStatus> applicationStatusList = updatedCourses.stream()
        .flatMap(course -> repository.searchApplicationStatusByCourseId(course.getId()).stream())
        .collect(Collectors.toList());

    return converter.convertStudentDetails(studentList, updatedCourses, applicationStatusList);
  }

  /**
   * 登録された受講生詳細の一覧検索です。 全件検索を行うので、条件指定は行わないものになります。 論理削除された受講生は表示されません。
   *
   * @return 受講生詳細一覧（全件）
   */
  public List<StudentDetail> searchStudentList() {
    List<Student> studentList = repository.searchStudents();

    List<StudentCourse> updatedCourses = studentList.stream().flatMap(student -> {

      List<StudentCourse> studentCourseList = repository.searchStudentCourseByStudentId(
          student.getId());

      return studentCourseList.stream().map(course -> {
        List<ApplicationStatus> applicationStatuses = repository.searchApplicationStatusByCourseId(
            course.getId());

        if (!applicationStatuses.isEmpty()) {
          course.setStatus(applicationStatuses.getFirst().getStatus());
        }
        return course;
      });
    }).collect(Collectors.toList());

    List<ApplicationStatus> applicationStatusList = updatedCourses.stream()
        .flatMap(course -> repository.searchApplicationStatusByCourseId(course.getId()).stream())
        .collect(Collectors.toList());

    return converter.convertStudentDetails(studentList, updatedCourses, applicationStatusList);
  }


  /**
   * 受講生詳細検索です。IDに紐づく受講生情報を取得した後、その受講生に紐づく受講生コース情報、申込状況を取得して設定します。
   *
   * @param id 受講生ID
   * @return 受講生詳細
   */
  public StudentDetail searchStudent(Integer id) {
    Student student = repository.searchStudentById(id);
    List<StudentCourse> studentCourseList = repository.searchStudentCourseByStudentId(
            student.getId()).stream()
        .peek(course -> {
          List<ApplicationStatus> applicationStatuses = repository.searchApplicationStatusByCourseId(
              course.getId());

          if (!applicationStatuses.isEmpty()) {
            course.setStatus(applicationStatuses.getFirst().getStatus());
          }
        })
        .toList();

    List<ApplicationStatus> applicationStatuses = studentCourseList.stream()
        .flatMap(course -> repository.searchApplicationStatusByCourseId(course.getId()).stream())
        .collect(Collectors.toList());

    return new StudentDetail(student, studentCourseList, applicationStatuses);
  }

  /**
   * 受講生の名前検索です。名前が一致する（部分一致含む）受講生情報を取得した後、その受講生に紐づく受講生コース情報、申込状況を取得して設定します。
   *
   * @param fullName 受講生の名前
   * @return 受講生詳細
   */
  public List<StudentDetail> searchStudentByName(String fullName) throws TestException {
    List<Student> students = repository.searchStudentByName(fullName);

    if (students.isEmpty()) {
      throw new TestException(fullName + "と一致する受講生が見つかりませんでした");
    }

    return students.stream().map(student -> {

      List<StudentCourse> studentCourses = repository.searchStudentCourseByStudentId(
          student.getId());

      List<StudentCourse> updatesCourses = studentCourses.stream().peek(course -> {
            List<ApplicationStatus> applicationStatuses = repository.searchApplicationStatusByCourseId(
                course.getId());

            if (!applicationStatuses.isEmpty()) {
              course.setStatus(applicationStatuses.getFirst().getStatus());
            }
          })
          .toList();

      List<ApplicationStatus> applicationStatuses = studentCourses.stream()
          .flatMap(course -> repository.searchApplicationStatusByCourseId(course.getId()).stream())
          .collect(Collectors.toList());

      return new StudentDetail(student, studentCourses, applicationStatuses);
    }).collect(Collectors.toList());
  }

  /**
   * 受講生のフリガナ検索です。フリガナが一致する（部分一致含む）受講生情報を取得した後、その受講生に紐づく受講生コース情報、申込状況を取得して設定します。
   *
   * @param furigana 受講生のフリガナ
   * @return 受講生詳細
   */
  public List<StudentDetail> searchStudentByFurigana(String furigana) throws TestException {
    List<Student> students = repository.searchStudentByFurigana(furigana);

    if (students.isEmpty()) {
      throw new TestException(furigana + "と一致する受講生が見つかりませんでした");
    }
    return students.stream().map(student -> {

      List<StudentCourse> studentCourses = repository.searchStudentCourseByStudentId(
          student.getId());

      List<StudentCourse> updatesCourses = studentCourses.stream().peek(course -> {
            List<ApplicationStatus> applicationStatuses = repository.searchApplicationStatusByCourseId(
                course.getId());

            if (!applicationStatuses.isEmpty()) {
              course.setStatus(applicationStatuses.getFirst().getStatus());
            }
          })
          .toList();
      List<ApplicationStatus> applicationStatuses = studentCourses.stream()
          .flatMap(course -> repository.searchApplicationStatusByCourseId(course.getId()).stream())
          .collect(Collectors.toList());

      return new StudentDetail(student, studentCourses, applicationStatuses);
    }).collect(Collectors.toList());
  }

  /**
   * 受講生の居住地域（市区町村）検索です。居住地域が一致する（部分一致含む）受講生情報を取得した後、その受講生に紐づく受講生コース情報、申込状況を取得して設定します。
   *
   * @param address 受講生の居住地域（市区町村）
   * @return 受講生詳細
   */
  public List<StudentDetail> searchStudentByAddress(String address) throws TestException {
    List<Student> students = repository.searchStudentByAddress(address);

    if (students.isEmpty()) {
      throw new TestException("条件と一致する受講生が見つかりませんでした。居住地域：" + address);
    }

    return students.stream().map(student -> {

      List<StudentCourse> studentCourses = repository.searchStudentCourseByStudentId(
          student.getId());

      List<StudentCourse> updatesCourses = studentCourses.stream().peek(course -> {
            List<ApplicationStatus> applicationStatuses = repository.searchApplicationStatusByCourseId(
                course.getId());

            if (!applicationStatuses.isEmpty()) {
              course.setStatus(applicationStatuses.getFirst().getStatus());
            }
          })
          .toList();

      List<ApplicationStatus> applicationStatuses = studentCourses.stream()
          .flatMap(course -> repository.searchApplicationStatusByCourseId(course.getId()).stream())
          .collect(Collectors.toList());

      return new StudentDetail(student, studentCourses, applicationStatuses);
    }).collect(Collectors.toList());
  }

  /**
   * 受講生の年齢検索です。年齢が一致する受講生情報を取得した後、その受講生に紐づく受講生コース情報、申込状況を取得して設定します。
   *
   * @param age 受講生の年齢
   * @return 受講生詳細
   */
  public List<StudentDetail> searchStudentByAge(Integer age) throws TestException {
    List<Student> students = repository.searchStudentByAge(age);

    if (students.isEmpty()) {
      throw new TestException("条件と一致する受講生が見つかりませんでした。年齢：" + age);
    }
    return students.stream().map(student -> {

      List<StudentCourse> studentCourses = repository.searchStudentCourseByStudentId(
          student.getId());

      List<StudentCourse> updatesCourses = studentCourses.stream().peek(course -> {
            List<ApplicationStatus> applicationStatuses = repository.searchApplicationStatusByCourseId(
                course.getId());

            if (!applicationStatuses.isEmpty()) {
              course.setStatus(applicationStatuses.getFirst().getStatus());
            }
          })
          .toList();

      List<ApplicationStatus> applicationStatuses = studentCourses.stream()
          .flatMap(course -> repository.searchApplicationStatusByCourseId(course.getId()).stream())
          .collect(Collectors.toList());

      return new StudentDetail(student, studentCourses, applicationStatuses);
    }).collect(Collectors.toList());
  }

  /**
   * 受講生の性別検索です。性別が一致する受講生情報を取得した後、その受講生に紐づく受講生コース情報、申込状況を取得して設定します。
   *
   * @param sex 受講生の名前
   * @return 受講生詳細
   */
  public List<StudentDetail> searchStudentBySex(String sex) {
    List<Student> students = repository.searchStudentBySex(sex);

    return students.stream().map(student -> {

      List<StudentCourse> studentCourses = repository.searchStudentCourseByStudentId(
          student.getId());

      List<StudentCourse> updatesCourses = studentCourses.stream().peek(course -> {
            List<ApplicationStatus> applicationStatuses = repository.searchApplicationStatusByCourseId(
                course.getId());

            if (!applicationStatuses.isEmpty()) {
              course.setStatus(applicationStatuses.getFirst().getStatus());
            }
          })
          .toList();

      List<ApplicationStatus> applicationStatuses = studentCourses.stream()
          .flatMap(course -> repository.searchApplicationStatusByCourseId(course.getId()).stream())
          .collect(Collectors.toList());

      return new StudentDetail(student, studentCourses, applicationStatuses);
    }).collect(Collectors.toList());
  }

  /**
   * 受講コースの名前検索です。受講コースが一致する受講生情報を取得した後、その受講生に紐づく受講生コース情報、申込状況を取得して設定します。
   * 複数のコースを受講している場合でも検索したコースのみが表示されます。
   *
   * @param courseName 受講生の名前
   * @return 受講生詳細
   */
  public List<StudentDetail> searchStudentByCourseName(String courseName) {
    List<StudentCourse> studentCourses = repository.searchStudentByCourseName(courseName);

    return studentCourses.stream().map(course -> {

      List<Student> students = repository.searchStudentsById(course.getStudentsId());

      List<ApplicationStatus> applicationStatuses = repository.searchApplicationStatusByCourseId(
          course.getId());

      if (!applicationStatuses.isEmpty()) {
        course.setStatus(applicationStatuses.getFirst().getStatus());
      }
      return new StudentDetail(students.get(0), List.of(course), applicationStatuses);
    }).collect(Collectors.toList());
  }

  /**
   * 受講生コース情報を登録する際の初期情報を設定する。
   *
   * @param studentCourses 受講生コース情報
   */
  private void initStudentCourse(StudentCourse studentCourses, Student student) {
    LocalDateTime now = LocalDateTime.now();

    studentCourses.setStudentsId(student.getId());
    studentCourses.setStartDate(now);
    studentCourses.setEndDate(now.plusYears(1));
  }

  public void registerApplicationStatus(ApplicationStatus applicationStatus) {
    LocalDateTime now = LocalDateTime.now();

    applicationStatus.setCreatedAt(now);
    applicationStatus.setUpdatedAt(now);
    repository.insertApplicationStatus(applicationStatus);
  }

  /**
   * 受講生詳細の登録を行います。 受講生と受講生コース情報、各コースの申込状況を個別に登録します。 受講生コース情報には受講生情報を紐付ける値やコース開始日、コース終了日を設定します。
   * 申込状況には受講生コース情を紐付ける値やコース開始日、更新日時を設定します。
   *
   * @param studentDetail 受講生詳細
   * @return 登録情報を付与した受講生詳細
   */
  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();

    repository.insertStudent(student);

    studentDetail.getStudentCourseList().forEach(studentCourses -> {
      initStudentCourse(studentCourses, student);
      repository.insertStudentsCourse(studentCourses);

      ApplicationStatus applicationStatus = new ApplicationStatus();
      applicationStatus.setCourseId(studentCourses.getId());
      applicationStatus.setStatus(Status.仮申込);
      applicationStatus.setCreatedAt(LocalDateTime.now());
      applicationStatus.setUpdatedAt(LocalDateTime.now());

      registerApplicationStatus(applicationStatus);

    });
    return studentDetail;
  }

  /**
   * 受講生詳細の更新を行います。受講生と受講生コース情報をそれぞれ更新します。
   *
   * @param studentDetail 受講生詳細
   */
  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());
    studentDetail.getStudentCourseList()
        .forEach(repository::updateStudentCourse);
  }

  /**
   * 申込状況のID検索です。
   *
   * @param id 申込状況のid
   * @return 申込状況
   */
  public List<ApplicationStatus> findStatusById(Integer id) {
    return repository.searchApplicationStatusById(id);
  }

  /**
   * 申込状況の更新です。仮申込から本申込に更新します。
   *
   * @param applicationStatus（本申込）
   */
  public void updateStatusToMainApplication(List<ApplicationStatus> applicationStatus) {
    for (ApplicationStatus status : applicationStatus) {
      status.setStatus(Status.本申込);

      status.setUpdatedAt(LocalDateTime.now());
      repository.updateStatus(status);
    }
  }

  /**
   * 申込状況の更新です。本申込から受講中に更新します。
   *
   * @param applicationStatus（受講中）
   */
  public void updateStatusToTakingTheCourse(List<ApplicationStatus> applicationStatus) {
    for (ApplicationStatus status : applicationStatus) {
      status.setStatus(Status.受講中);
      status.setUpdatedAt(LocalDateTime.now());
      repository.updateStatus(status);
    }
  }

  /**
   * 申込状況の更新です。受講中から受講終了に更新します。
   *
   * @param applicationStatus（受講終了）
   */
  public void updateStatusToCourseCompleted(List<ApplicationStatus> applicationStatus) {
    for (ApplicationStatus status : applicationStatus) {
      status.setStatus(Status.受講終了);
      status.setUpdatedAt(LocalDateTime.now());
      repository.updateStatus(status);
    }
  }

  /**
   * 申込状況の検索です。検索した申込状況に該当する受講生情報を表示します。 コースが複数ある場合は該当するコースのみ表示されます。
   *
   * @param status
   * @return 受講生詳細
   */
  public List<StudentDetail> searchStudentByStatus(Status status) {

    List<ApplicationStatus> applicationStatuses = repository.searchApplicationStatusByStatus(
        status);

    return applicationStatuses.stream().flatMap(applicationStatus -> {

      Integer id = applicationStatus.getCourseId();
      List<StudentCourse> studentCourses = repository.searchStudentCourseById(id);

      return studentCourses.stream().map(studentCourse -> {

        Integer studentId = studentCourse.getStudentsId();

        studentCourse.setStatus(applicationStatus.getStatus());

        Student student = repository.searchStudentById(studentId);

        return new StudentDetail(student, List.of(studentCourse), List.of(applicationStatus));
      });
    }).collect(Collectors.toList());


  }
}
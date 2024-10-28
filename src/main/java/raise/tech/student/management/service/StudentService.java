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

    List<StudentCourse> studentCourseList = studentList.stream()
        .flatMap(student -> repository.searchStudentCourseByStudentId(student.getId()).stream())
        .collect(Collectors.toList());

    List<ApplicationStatus> applicationStatusList = studentCourseList.stream()
        .flatMap(
            studentCourse -> repository.searchApplicationStatusByCourseId(studentCourse.getId())
                .stream())
        .collect(Collectors.toList());

    return converter.convertStudentDetails(studentList, studentCourseList, applicationStatusList);
  }

  /**
   * 登録された受講生詳細の一覧検索です。 全件検索を行うので、条件指定は行わないものになります。 論理削除された受講生は表示されません。
   *
   * @return 受講生詳細一覧（全件）
   */
  public List<StudentDetail> searchStudentList() {
    List<Student> studentList = repository.searchStudents();

    List<StudentCourse> studentCourseList = studentList.stream()
        .flatMap(student -> repository.searchStudentCourseByStudentId(student.getId()).stream())
        .collect(Collectors.toList());

    List<ApplicationStatus> applicationStatusList = studentCourseList.stream()
        .flatMap(
            studentCourse -> repository.searchApplicationStatusByCourseId(studentCourse.getId())
                .stream())
        .collect(Collectors.toList());

    return converter.convertStudentDetails(studentList, studentCourseList, applicationStatusList);
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
   * 受講生詳細検索です。条件と一致する受講生情報を取得した後、その受講生に紐づく受講生コース情報、申込状況を取得して設定します。
   * 検索可能条件（名前、フリガナ、居住地域、年齢、性別、受講コース、申込状況）
   *
   * @return 受講生詳細
   */
  public List<StudentDetail> searchStudentsByConditions(String fullName, String furigana,
      String address, Integer age, String sex, String courseName, Status status) {

    List<Student> students = repository.findStudentsByConditions(fullName, furigana, address, age,
        sex);
    List<StudentCourse> studentCourses = repository.findCoursesByConditions(courseName);
    List<ApplicationStatus> applicationStatuses = repository.findApplicationStatusByConditions(
        status);

    return converter.convertStudentDetails(students, studentCourses, applicationStatuses);
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

      repository.insertApplicationStatus(applicationStatus);

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
  public void updateApplicationStatus(List<ApplicationStatus> applicationStatus, Status newStatus) {
    for (ApplicationStatus status : applicationStatus) {
      status.setStatus(newStatus);
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

    return applicationStatuses.stream()
        .flatMap(applicationStatus -> {

          Integer courseId = applicationStatus.getCourseId();

          List<StudentCourse> studentCourses = repository.searchStudentCourseById(courseId);

          return studentCourses.stream().map(studentCourse -> {
            Integer studentId = studentCourse.getStudentsId();

            studentCourse.setStatus(applicationStatus.getStatus());

            Student student = repository.searchStudentById(studentId);

            return new StudentDetail(student, List.of(studentCourse), List.of(applicationStatus));
          });
        })
        .collect(Collectors.toList());
  }

}

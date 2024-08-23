package raise.tech.student.management.data;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter


public class StudentsCourses {

  private Long id;
  private Long studentsId;
  private String courseName;
  private LocalDateTime startDate;
  private LocalDateTime endDate;

}

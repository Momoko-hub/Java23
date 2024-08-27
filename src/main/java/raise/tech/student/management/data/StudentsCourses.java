package raise.tech.student.management.data;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter


public class StudentsCourses {

  private Long id;
  private Long studentsId;
  private String courseName;
  private LocalDateTime startDate;
  private LocalDateTime endDate;

}

package raise.tech.student.management;

import java.time.LocalDate;
import lombok.Getter;

@Getter


public class StudentsCourses {

  private long id;
  private long studentsId;
  private String courseName;
  private LocalDate startDate;
  private LocalDate endDate;

}

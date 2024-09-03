package raise.tech.student.management.data;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter


public class StudentCourse {

  private Integer id;

  private Integer studentsId;

  @NotBlank
  private String courseName;

  private LocalDateTime startDate;

  private LocalDateTime endDate;

}

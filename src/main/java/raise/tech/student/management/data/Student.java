package raise.tech.student.management.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Student {

  private String id;
  private String fullName;
  private String furigana;
  private String nickname;
  private String emailAddress;
  private String address;
  private int age;
  private String sex;
  private String remark;
  private boolean isDeleted;


}


package raise.tech.student.management.controller;

import java.util.Arrays;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

public class demo {

  @Controller
  public class MyFirstApp {

    @RequestMapping("/home")
    public String index(Model model) {
      model.addAttribute("message", "Hello Thymeleaf");
      model.addAttribute("items", Arrays.asList("Item 1", "Item 2", "Item 3"));
      return "start_page";
    }
  }
}

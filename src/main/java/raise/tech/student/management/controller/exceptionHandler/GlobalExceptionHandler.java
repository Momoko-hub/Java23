package raise.tech.student.management.controller.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import raise.tech.student.management.exception.TestException;

@ControllerAdvice
public class GlobalExceptionHandler {


    /**
     * TestExceptionが発生した場合の処理を行う。
     *
     * @param ex 発生した例外
     * @return エラーメッセージを含むレスポンス
     */

    @ExceptionHandler(TestException.class)
    public ResponseEntity<String> handleTestException(TestException ex){
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
  }



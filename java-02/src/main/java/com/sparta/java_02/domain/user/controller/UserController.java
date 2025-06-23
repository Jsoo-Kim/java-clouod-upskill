package com.sparta.java_02.domain.user.controller;

import com.sparta.java_02.domain.user.dto.UserRequest;
import com.sparta.java_02.domain.user.dto.UserSearchResponse;
import com.sparta.java_02.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//@Controller
@RestController // @Controller와 @ResponseBody가 합쳐진 어노테이션이라고 생각하면 됨 (들어가보면 있음)
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

//  @GetMapping("/{userId}")
//  public ResponseEntity<Map<String, String>> findAll(  // 당연히 이렇게 HashMap 쓰면 안 됨
//      @RequestParam(name = "email", required = false) String email, /** Query String **/
//      @PathVariable(name = "userId") Long userId /** {userId}에 들어오는 부분 **/
//  ) { // name은 인자값과 같으면 생략 가능
//    // GET /api/users?email="asd@naver.com"
//
//    return ResponseEntity.status(200)
//        .body(new HashMap<String, String>() {{
//          put("name", "홍길동");
//        }}); // ResponseEntity에 들어가면 Builder 패턴을 씀
//  }

  @GetMapping("/{userId}")
  public ResponseEntity<UserSearchResponse> findAll(
      @RequestParam(name = "email", required = false) String email, /** Query String **/
      @PathVariable(name = "userId") Long userId /** {userId}에 들어오는 부분 **/
  ) { // name은 인자값과 같으면 생략 가능
    // GET /api/users?email="asd@naver.com"

    return ResponseEntity.ok()
        .body(UserSearchResponse.builder().build()); // ResponseEntity에 들어가면 Builder 패턴을 씀
  } // status 코드도 이렇게 쓰지 말고 ENUM이나 static으로 명시적으로 빼 주는 게 좋음

//  @PostMapping
//  public void save(@RequestBody HashMap<String, Object> request) {
//    // @RequestBody에 HashMap<String, Object> 이런 식으로 비명시적인 객체 쓰는 건 매우 비추!!
//    userService.save();
//  }

  @PostMapping
  public ResponseEntity<Void> save(@RequestBody UserRequest request) {
//    userService.save();
    return ResponseEntity.badRequest().build();
  }

  // put과 post는 dto를 함께 쓸 가능성이 높다(기획과 협의)
  // patch 까지는 따로 해야 restful함
  @PutMapping("{userId}")
  public ResponseEntity<Void> update(@RequestBody UserRequest request) {

  }

  @PatchMapping("{userId}") // 부분 업데이트
  public ResponseEntity<Void> updateStatus() {

  }

  @DeleteMapping("{userId}")
  public ResponseEntity<Void> delete() {

  }

}

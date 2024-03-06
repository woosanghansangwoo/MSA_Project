package fisa.club.userservice.controller;
import fisa.club.userservice.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")


public class userController {
    private final List<User> users = new ArrayList<>();


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserCreateDTO {
        private String username;
        private String password;
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserCreateDTO user) {

        User newUser = new User();

        // 아이디 또는 패스워드가 비어있을 경우
        if (user.getUsername().isEmpty() || user.getPassword().isEmpty() || user.getUsername().isBlank() || user.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body("아이디 또는 패스워드는 비어있을 수 없습니다");
        }

        // 아이디가 4자 이하일 경우
        if (user.getUsername().length() < 4) {
            return ResponseEntity.badRequest().body("아이디는 4자 이상이어야 합니다");
        }

        // 패스워드가 4자 이하일 경우
        if (user.getPassword().length() < 4) {
            return ResponseEntity.badRequest().body("패스워드는 4자 이상이어야 합니다");
        }

        // 아이디에 특수문자가 포함되어 있을 경우
        if (user.getUsername().matches(".*[^a-zA-Z0-9].*")) {
            return ResponseEntity.badRequest().body("아이디에 허용되지 않는 문자가 포함되어 있습니다");
        }





        // 아이디가 이미 존재할 경우
        if (users.stream().anyMatch(existingUser -> existingUser.getUsername().equals(user.getUsername()))) {
            return ResponseEntity.badRequest().body("아이디가 이미 존재합니다");
        }

        newUser.setId(UUID.randomUUID().toString());
        newUser.setRole("USER");
        newUser.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));

        //try-catch문을 사용하여 예외처리를 해준다.
        try {
            // 사용자 추가
            users.add(newUser);
            return ResponseEntity.ok("사용자가 생성되었습니다");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("사용자 생성에 실패했습니다");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable String id, @RequestBody User newUser) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findAny()
                .map(user -> {
                    user.setUsername(newUser.getUsername());
                    user.setPassword(newUser.getPassword());
                    user.setRole(newUser.getRole());
                    return ResponseEntity.ok("사용자 정보 업데이트됨");
                })
                .orElseGet(() -> ResponseEntity.badRequest().body("사용자를 찾을 수 없습니다"));
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        return users.removeIf(user -> user.getId().equals(id)) ?
                ResponseEntity.ok("사용자가 삭제되었습니다") :
                ResponseEntity.badRequest().body("사용자를 찾을 수 없습니다");
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable String id) {
       return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseGet(() -> {
                   ResponseEntity.badRequest().body("사용자를 찾을 수 없습니다");
                   return null;
                });
    }


    @GetMapping
    public List<User> getUsers() {
        // 모든 유저 가져오기
        return users;
    }
}




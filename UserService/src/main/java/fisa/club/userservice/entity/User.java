package fisa.club.userservice.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@ToString
public class User {
    private String id;

    private String username;

    private String password;

    private String role;
}



package fisa.club.commentservice.entity;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString

public class Comment {
    private String id;
    private String articleId;
    private String author;
    private String content;
    private String date;
}

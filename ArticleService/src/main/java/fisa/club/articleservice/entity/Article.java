package fisa.club.articleservice.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Article {
    private String id;
    private String author;
    private String title;
    private String content;
    private String date;
}


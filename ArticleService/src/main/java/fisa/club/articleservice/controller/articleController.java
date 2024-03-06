package fisa.club.articleservice.controller;

import fisa.club.articleservice.entity.Article;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/articles")
public class articleController {

    private final List<Article> Articles = new ArrayList<>();

    // article 생성
    @PostMapping
    public ResponseEntity<String> createArticle(@RequestBody Article article) {

        return Optional.of(article)
                .filter(a -> a.getTitle() != null && a.getContent() != null)
                .map(a -> {
                    a.setTitle(article.getTitle());
                    a.setContent(article.getContent());
                    a.setAuthor(article.getAuthor());
                    a.setId(UUID.randomUUID().toString());
                    a.setDate(String.valueOf(System.currentTimeMillis()));
                    Articles.add(a);
                    return ResponseEntity.ok("게시글이 등록되었습니다");
                })
                .orElse(ResponseEntity.badRequest().body("제목 또는 내용이 비어있을 수 없습니다"));
    }

    // article 수정
    @PutMapping("/{id}")
    public ResponseEntity<String> updateArticle(@PathVariable String id, @RequestBody Article newArticle) {
        return Articles.stream()
                .filter(article -> article.getId().equals(id))
                .findFirst()
                .map(article -> {
                    article.setAuthor(newArticle.getAuthor());
                    article.setTitle(newArticle.getTitle());
                    article.setContent(newArticle.getContent());
                    article.setDate(newArticle.getDate());
                    return ResponseEntity.ok("게시글이 수정되었습니다");
                })
                .orElse(ResponseEntity.badRequest().body("게시글이 존재하지 않습니다"));
    }

    // article 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteArticle(@PathVariable String id) {
        return Articles.removeIf(article -> article.getId().equals(id)) ?
                ResponseEntity.ok("게시글이 삭제 되었습니다") :
                ResponseEntity.badRequest().body("게시글을 찾을 수 없습니다");
    }

    // article 조회
    @GetMapping("/{id}")
    public Article getArticle(@PathVariable String id) {
        return Articles.stream()
                .filter(article -> id.equals(article.getId())) // null-safe equals
                .findFirst()
                .orElseGet(() -> {
                    ResponseEntity.badRequest().body("게시글이 존재하지 않습니다");
                    return null;
                });
    }

    // article 목록 조회
    @GetMapping
    public List<Article> getArticles() {
        return Articles;
    }
}

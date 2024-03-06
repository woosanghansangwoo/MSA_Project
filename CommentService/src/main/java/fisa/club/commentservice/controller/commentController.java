package fisa.club.commentservice.controller;

import fisa.club.commentservice.entity.Comment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/comments")
public class commentController {
    private final List<Comment> comments = new ArrayList<>();

    // 댓글 작성
    @PostMapping
    public ResponseEntity<String> createComment(@RequestBody Comment comment){
       return Optional.of(comment)
               .filter(c -> c.getContent() != null)
               .map(c -> {
                   c.setId(UUID.randomUUID().toString());
                   c.setArticleId(comment.getArticleId());
                   c.setAuthor(comment.getAuthor());
                   c.setDate(String.valueOf(System.currentTimeMillis()));
                   comments.add(c);
                   return ResponseEntity.ok("댓글 작성 완료");
               })
               .orElse(ResponseEntity.badRequest().body("댓글 내용이 비어있을 수 없습니다"));
    }

    // 댓글 수정
    @PutMapping("/{id}")
    public ResponseEntity<String> updateComment(String id, Comment newComment){
        return comments.stream()
                .filter(comment -> comment.getId().equals(id))
                .findFirst()
                .map(comment -> {
                    comment.setAuthor(newComment.getAuthor());
                    comment.setContent(newComment.getContent());
                    comment.setDate(newComment.getDate());
                    return ResponseEntity.ok("댓글이 수정되었습니다");
                })
                .orElse(ResponseEntity.badRequest().body("댓글이 존재하지 않습니다"));
    }

    // 댓글 조회
    @GetMapping("/{id}")
    public ResponseEntity<Comment> getComment(String id){
        return comments.stream()
                .filter(comment -> comment.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    // 댓글 목록 조회
    @GetMapping
    public List<Comment> getComments(){
        return comments;
    }

    // 댓글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(String id){
        return comments.removeIf(comment -> comment.getId().equals(id)) ?
                ResponseEntity.ok("댓글이 삭제되었습니다") :
                ResponseEntity.badRequest().body("댓글을 찾을 수 없습니다");
    }
}

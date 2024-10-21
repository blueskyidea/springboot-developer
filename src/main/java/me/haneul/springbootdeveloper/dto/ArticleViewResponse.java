package me.haneul.springbootdeveloper.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.haneul.springbootdeveloper.domain.Article;
import me.haneul.springbootdeveloper.domain.Comment;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
public class ArticleViewResponse {
    private Long id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private List<Comment> comments;

    public ArticleViewResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.author = article.getAuthor();
        this.createdAt = article.getCreatedAt();
        this.comments = article.getComments();
    }
}

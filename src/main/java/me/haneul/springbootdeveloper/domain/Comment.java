package me.haneul.springbootdeveloper.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Table(name = "comments")
@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "content", nullable = false)
    private String content;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    //참조가 되는 뒷부분을 의미, 직렬화를 수행하지 않음(무한루프 스택오버플로우 해결 방법)
    @JsonBackReference
    //다대일 관계(여러 Comment가 하나의 Article을 가지도록 설정)
    @ManyToOne
    private Article article;

    @Builder
    public Comment(Article article, String author, String content) {
        this.article = article;
        this.author = author;
        this.content = content;
    }

    public void update(String content) {
        this.content = content;
    }
}

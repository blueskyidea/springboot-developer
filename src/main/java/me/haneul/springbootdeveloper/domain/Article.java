package me.haneul.springbootdeveloper.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)  //생성 및 수정 시간 자동으로 감시하고 기록
@Entity  //엔티티로 지정
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //기본키를 자동으로 1씩 증가
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "author", nullable = false)
    private String author;

    @CreatedDate  //엔티티가 생성될 때 생성 시간 저장
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate  //엔티티가 수정될 때 수정 시간 저장
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    //참조가 되는 앞부분을 의미, 정상적으로 직렬화 수행(무한루프 스택오버플로우 해결 방법)
    @JsonManagedReference
    //mappedBy: 자식 엔티티가 부모 엔티티를 참조할 때 사용
    //CascadeType.REMOVE: 블로그 글 엔티티가 삭제되면 댓글 엔티티를 모두 삭제
    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
    @OrderBy("createdAt DESC")  //댓글을 생성일 기준으로 내림차순 정렬
    private List<Comment> comments;

    @Builder  //빌더 패턴으로 객체 생성
    public Article(String author, String title, String content) {
        this.author = author;
        this.title = title;
        this.content = content;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}

package me.haneul.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.haneul.springbootdeveloper.config.error.exception.ArticleNotFoundException;
import me.haneul.springbootdeveloper.domain.Article;
import me.haneul.springbootdeveloper.domain.Comment;
import me.haneul.springbootdeveloper.dto.AddArticleRequest;
import me.haneul.springbootdeveloper.dto.AddCommentRequest;
import me.haneul.springbootdeveloper.dto.UpdateArticleRequest;
import me.haneul.springbootdeveloper.repository.BlogRepository;
import me.haneul.springbootdeveloper.repository.CommentRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor  //final이 붙거나 @NotNull이 붙은 필드의 생성자 추가
@Service  //빈으로 등록
public class BlogService {
    private final BlogRepository blogRepository;
    private final CommentRepository commentRepository;

    //블로그 글 추가 메서드
    public Article save(AddArticleRequest request, String userName) {
        return blogRepository.save(request.toEntity(userName));
    }

    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    public Article findById(Long id) {
        return blogRepository.findById(id)
                //.orElseThrow(() -> new IllegalArgumentException("not found: " + id));
                .orElseThrow(ArticleNotFoundException::new);
    }

    public void delete(Long id) {
        //blogRepository.deleteById(id);

        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));

        authorizeArticleAuthor(article.getAuthor());
        blogRepository.delete(article);
    }

    @Transactional  //트랜잭션 메서드
    public Article update(Long id, UpdateArticleRequest request) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));

        authorizeArticleAuthor(article.getAuthor());
        article.update(request.getTitle(), request.getContent());

        return article;
    }

    //게시글을 작성한 유저인지 확인
    private static void authorizeArticleAuthor(String name) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!name.equals(userName)) {
            throw new IllegalArgumentException("not authorized");
        }
    }

    //댓글 추가
    public Comment addComment(AddCommentRequest request, String userName) {
        Article article = blogRepository.findById(request.getArticleId())
                .orElseThrow(() -> new IllegalArgumentException("not found: " + request.getArticleId()));

        return commentRepository.save(request.toEntity(userName, article));
    }

    //댓글 삭제
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));

        authorizeArticleAuthor(comment.getAuthor());
        commentRepository.delete(comment);
    }

    //댓글 수정
    @Transactional  //트랜잭션 메서드
    public Comment updateComment(Long id, UpdateArticleRequest request) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));

        authorizeArticleAuthor(comment.getAuthor());
        comment.update(request.getContent());

        return comment;
    }
}

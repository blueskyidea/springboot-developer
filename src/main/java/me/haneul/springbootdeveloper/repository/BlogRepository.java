package me.haneul.springbootdeveloper.repository;

import me.haneul.springbootdeveloper.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlogRepository extends JpaRepository<Article, Long> {
}

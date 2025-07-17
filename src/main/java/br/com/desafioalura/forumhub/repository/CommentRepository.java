package br.com.desafioalura.forumhub.repository;

import br.com.desafioalura.forumhub.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
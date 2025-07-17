package br.com.desafioalura.forumhub.repository;

import br.com.desafioalura.forumhub.model.Like;
import br.com.desafioalura.forumhub.model.Post;
import br.com.desafioalura.forumhub.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndPost(User user, Post post);
}

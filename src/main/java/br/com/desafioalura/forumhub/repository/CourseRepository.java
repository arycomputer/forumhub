package br.com.desafioalura.forumhub.repository;

import br.com.desafioalura.forumhub.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}

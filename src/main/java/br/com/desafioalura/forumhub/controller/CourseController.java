package br.com.desafioalura.forumhub.controller;

import br.com.desafioalura.forumhub.model.Course;
import br.com.desafioalura.forumhub.repository.CourseRepository;
import br.com.desafioalura.forumhub.web.CourseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/coursess")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        List<CourseResponse> courseResponses = courses.stream()
                .map(course -> new CourseResponse(course.getId(), course.getName()))
                .toList();
        return ResponseEntity.ok(courseResponses);
    }
}

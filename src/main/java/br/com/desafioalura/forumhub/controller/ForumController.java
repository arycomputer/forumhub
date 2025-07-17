package br.com.desafioalura.forumhub.controller;

import br.com.desafioalura.forumhub.model.*;
import br.com.desafioalura.forumhub.repository.*;
import br.com.desafioalura.forumhub.web.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/forum")
public class ForumController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private CourseRepository courseRepository; // Injetar o novo repositório de cursos

    // Cria uma nova publicação no fórum
    @PostMapping("/posts")
    public ResponseEntity<?> createPost(@RequestBody CreatePostRequest createPostRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User currentUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));

        Course course = courseRepository.findById(createPostRequest.getCourseId())
                .orElseThrow(() -> new RuntimeException("Curso não encontrado com ID: " + createPostRequest.getCourseId()));

        Post post = new Post();
        post.setTitle(createPostRequest.getTitle());
        post.setContent(createPostRequest.getContent());
        post.setCreatedAt(LocalDateTime.now());
        post.setUser(currentUser);
        post.setCourse(course); // Vincular o curso ao post
        postRepository.save(post);
        return ResponseEntity.ok("Publicação criada com sucesso!");
    }

    @GetMapping("/courses")
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        List<CourseResponse> courseResponses = courses.stream()
                .map(course -> new CourseResponse(course.getId(), course.getName()))
                .toList();
        return ResponseEntity.ok(courseResponses);
    }
//    public ResponseEntity<List<CourseResponse>> getAllCourses() {
//        List<Course> courses = courseRepository.findAll();
//        List<CourseResponse> courseResponses = courses.stream().map(course -> {
//            CourseResponse cr = new CourseResponse(course.getId(), course.getName());
//            return cr;
//        }).collect(Collectors.toList());
//        System.out.println(courseResponses);
//
//        return ResponseEntity.ok(courseResponses);
//    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/courses")
    public ResponseEntity<?> createCourse(@RequestBody CreateCourseRequest createCourseRequest) {
        Course course = new Course();
        course.setName(createCourseRequest.name());
        courseRepository.save(course);
        return ResponseEntity.ok("Curso adicionado com sucesso!");
    }

//    @PostMapping ("/cursos")
//    public ResponseEntity<?> createCourse(@RequestBody CreateCourseRequest createCourseRequest) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userEmail = authentication.getName();
//        User currentUser = userRepository.findByEmail(userEmail)
//                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));
//
//        Course course = new Course();
//        course.setName(createCourseRequest.name());
//
//        // Verifica se o utilizador é o autor ou um administrador
//        if (!currentUser.getRole().equals("ADMIN")) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Não autorizado a incluir curso.");
//        }
//
//        courseRepository.save(course);
//        return ResponseEntity.ok("Curso adicionado com sucesso!");
//    }

    // Obtém todas as publicações do fórum
    @GetMapping("/posts")
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        List<PostResponse> postResponses = posts.stream().map(post -> {
            PostResponse pr = new PostResponse();
            pr.setId(post.getId());
            pr.setTitle(post.getTitle());
            pr.setContent(post.getContent());
            pr.setCreatedAt(post.getCreatedAt());
            pr.setUserEmail(post.getUser().getEmail());
            pr.setLikesCount(post.getLikes().size());
            pr.setCourseId(post.getCourse().getId()); // Adicionar ID do curso
            pr.setCourseName(post.getCourse().getName()); // Adicionar nome do curso
            pr.setComments(post.getComments().stream().map(comment -> {
                CommentResponse cr = new CommentResponse();
                cr.setId(comment.getId());
                cr.setContent(comment.getContent());
                cr.setCreatedAt(comment.getCreatedAt());
                cr.setUserEmail(comment.getUser().getEmail());
                return cr;
            }).collect(Collectors.toList()));
            return pr;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(postResponses);
    }

    // Obtém uma publicação específica por ID
    @GetMapping("/posts/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publicação não encontrada"));

        PostResponse pr = new PostResponse();
        pr.setId(post.getId());
        pr.setTitle(post.getTitle());
        pr.setContent(post.getContent());
        pr.setCreatedAt(post.getCreatedAt());
        pr.setUserEmail(post.getUser().getEmail());
        pr.setLikesCount(post.getLikes().size());
        pr.setCourseId(post.getCourse().getId()); // Adicionar ID do curso
        pr.setCourseName(post.getCourse().getName()); // Adicionar nome do curso
        pr.setComments(post.getComments().stream().map(comment -> {
            CommentResponse cr = new CommentResponse();
            cr.setId(comment.getId());
            cr.setContent(comment.getContent());
            cr.setCreatedAt(comment.getCreatedAt());
            cr.setUserEmail(comment.getUser().getEmail());
            return cr;
        }).collect(Collectors.toList()));
        return ResponseEntity.ok(pr);
    }

    // Adiciona um comentário a uma publicação
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<?> addCommentToPost(@PathVariable Long postId, @RequestBody CreateCommentRequest createCommentRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User currentUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Publicação não encontrada"));

        Comment comment = new Comment();
        comment.setContent(createCommentRequest.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUser(currentUser);
        comment.setPost(post);
        commentRepository.save(comment);
        return ResponseEntity.ok("Comentário adicionado com sucesso!");
    }

    // Adiciona uma curtida a uma publicação
    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<?> likePost(@PathVariable Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User currentUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Publicação não encontrada"));

        // Verifica se o utilizador já curtiu esta publicação
        Optional<Like> existingLike = likeRepository.findByUserAndPost(currentUser, post);
        if (existingLike.isPresent()) {
            return ResponseEntity.badRequest().body("Utilizador já curtiu esta publicação.");
        }

        Like like = new Like();
        like.setUser(currentUser);
        like.setPost(post);
        like.setCreatedAt(LocalDateTime.now());
        likeRepository.save(like);

        return ResponseEntity.ok("Publicação curtida com sucesso!");
    }

    // Remove uma curtida de uma publicação
    @DeleteMapping("/posts/{postId}/like")
    public ResponseEntity<?> unlikePost(@PathVariable Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User currentUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Publicação não encontrada"));

        // Encontra a curtida existente
        Optional<Like> existingLike = likeRepository.findByUserAndPost(currentUser, post);
        if (existingLike.isEmpty()) {
            return ResponseEntity.badRequest().body("Utilizador não curtiu esta publicação.");
        }

        likeRepository.delete(existingLike.get());
        return ResponseEntity.ok("Curtida removida com sucesso!");
    }

    // Atualiza uma publicação existente
    @PutMapping("/posts/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody CreatePostRequest updatePostRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User currentUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publicação não encontrada"));

        // Verifica se o utilizador é o autor ou um administrador
        if (!post.getUser().getEmail().equals(currentUser.getEmail()) && !currentUser.getRole().equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Não autorizado a alterar esta publicação.");
        }

        // Se um courseId for fornecido na requisição de atualização, tente buscar e vincular o novo curso
        if (updatePostRequest.getCourseId() != null) {
            Course newCourse = courseRepository.findById(updatePostRequest.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Curso não encontrado com ID: " + updatePostRequest.getCourseId()));
            post.setCourse(newCourse);
        }

        post.setTitle(updatePostRequest.getTitle());
        post.setContent(updatePostRequest.getContent());
        postRepository.save(post);
        return ResponseEntity.ok("Publicação atualizada com sucesso!");
    }

    // Exclui uma publicação existente
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User currentUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publicação não encontrada"));

        // Verifica se o utilizador é o autor ou um administrador
        if (!post.getUser().getEmail().equals(currentUser.getEmail()) && !currentUser.getRole().equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Não autorizado a excluir esta publicação.");
        }

        postRepository.delete(post);
        return ResponseEntity.ok("Publicação excluída com sucesso!");
    }
}

//@RestController
//@RequestMapping("/forum")
//public class ForumController {
//
//    @Autowired
//    private PostRepository postRepository;
//
//    @Autowired
//    private CommentRepository commentRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private LikeRepository likeRepository; // Injetar o novo repositório de curtidas
//
//    // Cria uma nova publicação no fórum
//    @PostMapping("/posts")
//    public ResponseEntity<?> createPost(@RequestBody CreatePostRequest createPostRequest) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userEmail = authentication.getName(); // O email agora é o nome de utilizador (sub do JWT)
//        User currentUser = userRepository.findByEmail(userEmail)
//                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));
//
//        Post post = new Post();
//        post.setTitle(createPostRequest.getTitle());
//        post.setContent(createPostRequest.getContent());
//        post.setCreatedAt(LocalDateTime.now());
//        post.setUser(currentUser);
//        postRepository.save(post);
//        return ResponseEntity.ok("Publicação criada com sucesso!");
//    }
//
//    // Obtém todas as publicações do fórum
//    @GetMapping("/posts")
//    public ResponseEntity<List<PostResponse>> getAllPosts() {
//        List<Post> posts = postRepository.findAll();
//        List<PostResponse> postResponses = posts.stream().map(post -> {
//            PostResponse pr = new PostResponse();
//            pr.setId(post.getId());
//            pr.setTitle(post.getTitle());
//            pr.setContent(post.getContent());
//            pr.setCreatedAt(post.getCreatedAt());
//            pr.setUserEmail(post.getUser().getEmail()); // Usar email
//            pr.setLikesCount(post.getLikes().size()); // Adicionar contagem de curtidas
//            pr.setComments(post.getComments().stream().map(comment -> {
//                CommentResponse cr = new CommentResponse();
//                cr.setId(comment.getId());
//                cr.setContent(comment.getContent());
//                cr.setCreatedAt(comment.getCreatedAt());
//                cr.setUserEmail(comment.getUser().getEmail()); // Usar email
//                return cr;
//            }).collect(Collectors.toList()));
//            return pr;
//        }).collect(Collectors.toList());
//        return ResponseEntity.ok(postResponses);
//    }
//
//    // Obtém uma publicação específica por ID
//    @GetMapping("/posts/{id}")
//    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) {
//        Post post = postRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Publicação não encontrada"));
//
//        PostResponse pr = new PostResponse();
//        pr.setId(post.getId());
//        pr.setTitle(post.getTitle());
//        pr.setContent(post.getContent());
//        pr.setCreatedAt(post.getCreatedAt());
//        pr.setUserEmail(post.getUser().getEmail()); // Usar email
//        pr.setLikesCount(post.getLikes().size()); // Adicionar contagem de curtidas
//        pr.setComments(post.getComments().stream().map(comment -> {
//            CommentResponse cr = new CommentResponse();
//            cr.setId(comment.getId());
//            cr.setContent(comment.getContent());
//            cr.setCreatedAt(comment.getCreatedAt());
//            cr.setUserEmail(comment.getUser().getEmail()); // Usar email
//            return cr;
//        }).collect(Collectors.toList()));
//        return ResponseEntity.ok(pr);
//    }
//
//    // Adiciona um comentário a uma publicação
//    @PostMapping("/posts/{postId}/comments")
//    public ResponseEntity<?> addCommentToPost(@PathVariable Long postId, @RequestBody CreateCommentRequest createCommentRequest) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userEmail = authentication.getName(); // O email agora é o nome de utilizador (sub do JWT)
//        User currentUser = userRepository.findByEmail(userEmail)
//                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));
//
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new RuntimeException("Publicação não encontrada"));
//
//        Comment comment = new Comment();
//        comment.setContent(createCommentRequest.getContent());
//        comment.setCreatedAt(LocalDateTime.now());
//        comment.setUser(currentUser);
//        comment.setPost(post);
//        commentRepository.save(comment);
//        return ResponseEntity.ok("Comentário adicionado com sucesso!");
//    }
//
//    // Adiciona uma curtida a uma publicação
//    @PostMapping("/posts/{postId}/like")
//    public ResponseEntity<?> likePost(@PathVariable Long postId) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userEmail = authentication.getName();
//        User currentUser = userRepository.findByEmail(userEmail)
//                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));
//
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new RuntimeException("Publicação não encontrada"));
//
//        // Verifica se o utilizador já curtiu esta publicação
//        Optional<Like> existingLike = likeRepository.findByUserAndPost(currentUser, post);
//        if (existingLike.isPresent()) {
//            return ResponseEntity.badRequest().body("Utilizador já curtiu esta publicação.");
//        }
//
//        Like like = new Like();
//        like.setUser(currentUser);
//        like.setPost(post);
//        like.setCreatedAt(LocalDateTime.now());
//        likeRepository.save(like);
//
//        return ResponseEntity.ok("Publicação curtida com sucesso!");
//    }
//
//    // Remove uma curtida de uma publicação
//    @DeleteMapping("/posts/{postId}/like")
//    public ResponseEntity<?> unlikePost(@PathVariable Long postId) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userEmail = authentication.getName();
//        User currentUser = userRepository.findByEmail(userEmail)
//                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));
//
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new RuntimeException("Publicação não encontrada"));
//
//        // Encontra a curtida existente
//        Optional<Like> existingLike = likeRepository.findByUserAndPost(currentUser, post);
//        if (existingLike.isEmpty()) {
//            return ResponseEntity.badRequest().body("Utilizador não curtiu esta publicação.");
//        }
//
//        likeRepository.delete(existingLike.get());
//        return ResponseEntity.ok("Curtida removida com sucesso!");
//    }
//
//    // Remove uma curtida de uma publicação
//    @DeleteMapping("/posts/{postId}/like")
//    public ResponseEntity<?> unlikePost(@PathVariable Long postId) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userEmail = authentication.getName();
//        User currentUser = userRepository.findByEmail(userEmail)
//                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));
//
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new RuntimeException("Publicação não encontrada"));
//
//        // Encontra a curtida existente
//        Optional<Like> existingLike = likeRepository.findByUserAndPost(currentUser, post);
//        if (existingLike.isEmpty()) {
//            return ResponseEntity.badRequest().body("Utilizador não curtiu esta publicação.");
//        }
//
//        likeRepository.delete(existingLike.get());
//        return ResponseEntity.ok("Curtida removida com sucesso!");
//    }
//
//    // Atualiza uma publicação existente
//    @PutMapping("/posts/{id}")
//    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody CreatePostRequest updatePostRequest) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userEmail = authentication.getName();
//        User currentUser = userRepository.findByEmail(userEmail)
//                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));
//
//        Post post = postRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Publicação não encontrada"));
//
//        // Verifica se o utilizador é o autor ou um administrador
//        if (!post.getUser().getEmail().equals(currentUser.getEmail()) && !currentUser.getRole().equals("ADMIN")) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Não autorizado a alterar esta publicação.");
//        }
//
//        post.setTitle(updatePostRequest.getTitle());
//        post.setContent(updatePostRequest.getContent());
//        postRepository.save(post);
//        return ResponseEntity.ok("Publicação atualizada com sucesso!");
//    }
//
//    // Exclui uma publicação existente
//    @DeleteMapping("/posts/{id}")
//    public ResponseEntity<?> deletePost(@PathVariable Long id) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userEmail = authentication.getName();
//        User currentUser = userRepository.findByEmail(userEmail)
//                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));
//
//        Post post = postRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Publicação não encontrada"));
//
//        // Verifica se o utilizador é o autor ou um administrador
//        if (!post.getUser().getEmail().equals(currentUser.getEmail()) && !currentUser.getRole().equals("ADMIN")) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Não autorizado a excluir esta publicação.");
//        }
//
//        postRepository.delete(post);
//        return ResponseEntity.ok("Publicação excluída com sucesso!");
//    }
//}
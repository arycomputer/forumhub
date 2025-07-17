package br.com.desafioalura.forumhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication //(exclude = {UserDetailsServiceAutoConfiguration.class })
public class ForumhubApplication {
	public static void main(String[] args) {
		SpringApplication.run(ForumhubApplication.class, args);
	}
}

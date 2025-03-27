package study.issue_mate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class IssueMateApplication {

	public static void main(String[] args) {
		SpringApplication.run(IssueMateApplication.class, args);
	}

}

package progres.tp4.api.dominospizzaapi.internals;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@EntityScan(basePackages = "progres.tp4.api.dominospizzaapi.bo")
@SpringBootApplication(scanBasePackages = {
	"progres.tp4.api.dominospizzaapi.dao",
	"progres.tp4.api.dominospizzaapi.rest"
})
public class DominosPizzaApiApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(DominosPizzaApiApplication.class, args);
	}
}

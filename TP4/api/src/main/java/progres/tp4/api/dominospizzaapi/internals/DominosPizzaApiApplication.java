package progres.tp4.api.dominospizzaapi.internals;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@EntityScan(basePackages = "progres.tp4.api.dominospizzaapi.bo")
@SpringBootApplication(scanBasePackages = {
	"progres.tp4.api.dominospizzaapi.bo",
	"progres.tp4.api.dominospizzaapi.dao",
	"progres.tp4.api.dominospizzaapi.rest",
}, scanBasePackageClasses = org.glassfish.jersey.jackson.JacksonFeature.class)
@ComponentScan(basePackages = {
	"progres.tp4.api.dominospizzaapi.dao",
	"progres.tp4.api.dominospizzaapi.rest",
})
@PropertySource("classpath:application.properties")
public class DominosPizzaApiApplication extends SpringBootServletInitializer {
	
	public static void main(String[] args) {
		new DominosPizzaApiApplication().configure(new SpringApplicationBuilder(DominosPizzaApiApplication.class))
		                                .run(args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(DominosPizzaApiApplication.class);
	}
}

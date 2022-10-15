package micro.table;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SecurityScheme(
		type = SecuritySchemeType.OAUTH2,
		name = "oauth2",
		description = "KeyCloak Oenik",
		flows = @OAuthFlows(
				implicit = @OAuthFlow(authorizationUrl = "http://localhost:6080/auth/realms/oenik/protocol/openid-connect/auth"
						+ "?client_id=account"
						+ "&redirect_uri=http://localhost:8080/swagger-ui/oauth2-redirect.html"
						+ "&response_type=code"
						+ "&scope=openid")
		)
)

@SecurityScheme(
		type = SecuritySchemeType.APIKEY,
		name = "apikey",
		paramName = "Authorization",
		description = "KeyCloak Oenik",
		in = SecuritySchemeIn.HEADER)


@SecurityScheme(
		type = SecuritySchemeType.OPENIDCONNECT,
		name = "openid",
		description = "KeyCloak Oenik",
		openIdConnectUrl = "http://localhost:6080/auth/realms/oenik/.well-known/openid-configuration"
)

@OpenAPIDefinition(
		servers = @Server(url = "http://localhost:8080", description = "local dev"),
		info = @Info(
				title = "Oenik Table API",
				version = "v1",
				description = "Oenik Table API for Graphical User Interface .")
)

@Configuration
@EnableWebMvc
@EnableAutoConfiguration
@SpringBootApplication
public class TableApplication {

	public static void main(String[] args) {
		SpringApplication.run(TableApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**");
			}
		};
	}

}

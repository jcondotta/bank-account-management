package com.jcondotta;

import com.jcondotta.configuration.AccountHolderCreatedSNSTopicConfig;
import com.jcondotta.configuration.BankAccountURIConfiguration;
import com.jcondotta.configuration.BankingEntitiesDynamoDBTableConfig;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@OpenAPIDefinition(
		info = @Info(
				title = "${api.title}",
				version = "${api.version}",
				description = "${api.description}",
				contact = @Contact(
						name = "${api.contact.name}",
						email = "${api.contact.email}",
						url = "${api.contact.url}"
				)
		),
		servers = @Server(url = "http://localhost:8090")
)
@SpringBootApplication
@EnableConfigurationProperties({
		BankingEntitiesDynamoDBTableConfig.class,
		BankAccountURIConfiguration.class,
		AccountHolderCreatedSNSTopicConfig.class
})
public class BankAccountManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankAccountManagementApplication.class, args);
	}
}
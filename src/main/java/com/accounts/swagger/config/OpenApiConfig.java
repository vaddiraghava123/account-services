package com.accounts.swagger.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@Configuration
@OpenAPIDefinition(
		 info = @Info(
	                title = "Your API Title",
	                version = "1.0",
	                description = "Your API Description",
	                contact = @Contact(
	                        name = "Your Name",
	                        email = "your-email@example.com",
	                        url = "https://your-url.com"
	                ),
	                license = @License(
	                        name = "Your License",
	                        url = "https://license-url.com"
	                )
	        ))
public class OpenApiConfig {

}

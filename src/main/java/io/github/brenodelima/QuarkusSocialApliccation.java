package io.github.brenodelima;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;

import javax.ws.rs.core.Application;

@OpenAPIDefinition(
        info = @Info(
                title = "API Quarkus Rede Social",
                version = "1.0",
                contact = @Contact(
                        name = "Breno de Lima Evangelista",
                        url = "https://github.com/brenodelima",
                        email = "liimab@hotmail.com")))
public class QuarkusSocialApliccation extends Application{
}

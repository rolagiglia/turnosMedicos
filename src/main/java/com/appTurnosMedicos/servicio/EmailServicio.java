package com.appTurnosMedicos.servicio;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


    
public class EmailServicio {
    private static final Logger logger = LoggerFactory.getLogger(EmailServicio.class);
    private final String apiKey;

    public EmailServicio(String apiKey) {
        this.apiKey = apiKey;
    }
    
    public void enviarCorreo(String toEmail, String nombeDestinatario, String asunto, String contenido) throws IOException, InterruptedException {
        String jsonBody = String.format(
            "{"
                + "    \"sender\":{"
                + "        \"name\":\"Web De Turnos - Recupero Pass\","
                + "        \"email\":\"rodrig009747@gmail.com\""
                + "    },"
                + "    \"to\":["
                + "        {"
                + "            \"email\":\"%s\","
                + "            \"name\":\"%s\""
                + "        }"
                + "    ],"
                + "    \"subject\":\"%s\","
                + "    \"htmlContent\":\"%s\""
                + "}", toEmail, nombeDestinatario, asunto, contenido
        );

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.brevo.com/v3/smtp/email"))
            .header("accept", "application/json")
            .header("api-key", apiKey)
            .header("content-type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
            .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if(response.statusCode() != 201){
            logger.error("Error al enviar el correo: {}", response.body());
            throw new IOException("Error al enviar el correo: " + response.body());
        }
        logger.info("Correo enviado exitosamente a: {}", toEmail);
    }
}
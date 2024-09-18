package ru.skillbox.security.jwt;
import com.nimbusds.jwt.SignedJWT;
import io.jsonwebtoken.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;

@Service
public class JwtUtils {
    @Value("${app.jwt.uriValidate}")
    private String uriValidate;
    public String getUsername(String jwtToken) {
        try {
            String sub = SignedJWT.parse(jwtToken).getPayload().toJSONObject().get("sub").toString();
            System.out.println(sub);
            return sub;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
//    public static String getSubjectFromJwt(String jwtToken) {
//        try {
//            // Разделяем токен на три части (header, payload, signature)
//            String[] tokenParts = jwtToken.split("\\.");
//
//            // Вторая часть - это payload (полезная нагрузка), декодируем её
//            String payload = new String(Base64.getUrlDecoder().decode(tokenParts[1]), StandardCharsets.UTF_8);
//
//            // Преобразуем строку в JSON и извлекаем поле "sub"
//            JSONObject jsonObject = new JSONObject(payload);
//            return jsonObject.getString("sub");
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to extract subject from JWT", e);
//        }
//    }
    public Boolean validateToken(String jwt) throws IOException, InterruptedException, java.io.IOException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uriValidate))
                .header("Authorization", "Bearer " + jwt)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body().trim().equals("true");
        } else {
            throw new RuntimeException("Failed to validate token: " + response.statusCode());
        }
    }
}

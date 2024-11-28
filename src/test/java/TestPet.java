// 1 - Imports
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

// 2 - Classe
public class TestPet {

    // 2.1 - Atributos
    static String ct = "application/json"; // Content type
    static String uriPet = "https://petstore.swagger.io/v2/pet";
    int petId = 748230601;

    // 2.2 - Funções e Métodos
    // 2.2.1 - Funções e Métodos comuns / úteis

    // Função de leitura de json
    public static String lerArquivoJson(String arquivoJson) throws IOException {
        return new String(Files.readAllBytes(Paths.get(arquivoJson)));
    }

    // 2.2.2 Métodos de teste
    @Test
    public void testPostPet() throws IOException {
        // Carregar os dados do arquivo json pet
        String jsonBody = lerArquivoJson("src/test/resources/json/pet1.json");

        // Começa o test via REST-assured
        given() // Dado que
            .contentType(ct) // o tipo do conteúdo é ct
            .log().all() // mostre tudo na ida
            .body(jsonBody) // envie o corpo da requisição
        .when() // Quando
            .post(uriPet) // Chamamos o endpoint fazendo post
        .then() // Então
            .log().all() // mostre tudo na volta
            .statusCode(200) // verifica se o status code é 200                
            .body("name", is("Snoopy")) // verifica se o nome é Snoopy
            .body("id", is(petId))
            .body("category.name", is("cachorro"))                
            .body("tags[0].name", is("vacinado"));
    }

    @Test
    public void testGetPet() throws IOException {
        // Configura
        String petName = "Smoopy";
        String categoryName = "cachorro";
        String tagName = "vacinado";

        given()
            .contentType(ct)
            .log().all()

         //Executa   
        .when()
            .get(uriPet + "/" + petId)  //Monta o endpoint
        
        //Valida
        .then()
            .log().all()
            .statusCode(200)
            .body("name", is("Snoopy")) // verifica se o nome é Snoopy
            .body("id", is(petId))
            .body("category.name", is("cachorro"))                
            .body("tags[0].name", is("vacinado"))

        ; //Fim do given
    }
}
// 1 - Imports
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

// 2 - Classe
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) //Ativa ordenação

public class TestPet {

    // 2.1 - Atributos
    static String ct = "application/json"; // Content type
    static String uriPet = "https://petstore.swagger.io/v2/pet";
    int petId = 748230601;
    String petName = "Snoopy";
    String categoryName = "cachorro";
    String tagName = "vacinado";
    String[] status = {"available", "sold"};

    // 2.2 - Funções e Métodos
    // 2.2.1 - Funções e Métodos comuns / úteis

    // Função de leitura de json
    public static String lerArquivoJson(String arquivoJson) throws IOException {
        return new String(Files.readAllBytes(Paths.get(arquivoJson)));
    }

    // 2.2.2 Métodos de teste
    @Test @Order(1)
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
            .body("name", is(petName)) // verifica se o nome é Snoopy
            .body("id", is(petId))
            .body("category.name", is(categoryName))                
            .body("tags[0].name", is(tagName));
    }

    @Test @Order(2)
    public void testGetPet() throws IOException {

        // Configura
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

    @Test @Order(3)
    public void testPutPet() throws IOException {

        String jsonBody = lerArquivoJson("src/test/resources/json/pet2.json");

        given() 
            .contentType(ct) 
            .log().all() 
            .body(jsonBody)
        .when()
            .post(uriPet)
        .then() 
            .log().all() 
            .statusCode(200)             
            .body("name", is("Snoopy")) 
            .body("id", is(petId))
            .body("category.name", is("cachorro"))                
            .body("tags[0].name", is("vacinado"))
            .body("status", is (status[1]));
    }

    @Test @Order(4)
    public void tesDeletePet() throws IOException {

        given() 
            .contentType(ct) 
            .log().all() 
        .when()
            .delete(uriPet + "/" + petId)
        .then()
            .statusCode(200)
            .body("code", is(200))
            .body("type", is("unknown"))
            .body("message", is(String.valueOf(petId))); //Transforma o id em string

    }
}
package br.ce.orlando.task.apitest;

import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class APITest {
	
	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "http://localhost:8001/tasks-backend";
	}
	
	@Test
	public void deveRetornarTarefas() {
		RestAssured.given()
			.log().all()
		.when()
			.get("/todo")
		.then()
			.statusCode(200)
			.log().all()
		;
	}
	
	@Test
	public void deveAdicionarTarefasComSucesso() {
		RestAssured.given()
			.body("{ \"task\": \"Teste via API\",\"dueDate\": \"2023-12-30\" }")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			.statusCode(201)
			.log().all();
	}
	
	@Test
	public void naoDeveAdicionarTarefaInvalida() {
		RestAssured.given()
			.body("{ \"task\": \"Teste via API\",\"dueDate\": \"2012-12-30\" }")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			.statusCode(400)
			.body("message", CoreMatchers.is("Due date must not be in past"))
			.log().all()
		;
	}
	
	@Test
	public void deveRemoverTarefasComSucesso() {
		
		Integer id =  RestAssured.given()
			.body("{ \"task\": \"Teste via API - Tarefa para remover\",\"dueDate\": \"2077-12-30\" }")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			.statusCode(201)
			.log().all()
		.extract().path("id");
		
		
		RestAssured.given()
		.when()
			.delete("/todo/" + id)
		.then()
			.statusCode(204);
	}
}





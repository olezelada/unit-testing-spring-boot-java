package com.fobbing.ole.test.unit.controllers;

import com.fobbing.ole.test.unit.models.Empleado;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

//inicialmente para usar web test necesitamos incluir en el pon la de dependencia de webflux.
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EmpleadoControllerWebTestClientTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @Order(1)
    public void testGuardarEmpleado() {
        //given
        Empleado empleado = Empleado.builder()
                .id(3L)
                .nombre("Diego")
                .apellido("Santos")
                .email("diego.santos@gmail.com")
                .build();

        //when
        webTestClient.post().uri("http://localhost:8080/api/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(empleado)
                .exchange() // envia el request

                //then
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo(empleado.getId())
                .jsonPath("$.nombre").isEqualTo(empleado.getNombre())
                .jsonPath("$.apellido").isEqualTo(empleado.getApellido())
                .jsonPath("$.email").isEqualTo(empleado.getEmail());
    }

    @Test
    @Order(2)
    public void testObtenerEmpleadoById() {
        webTestClient.get().uri("http://localhost:8080/api/empleados/3").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo(3)
                .jsonPath("$.nombre").isEqualTo("Diego")
                .jsonPath("$.apellido").isEqualTo("Santos")
                .jsonPath("$.email").isEqualTo("diego.santos@gmail.com");
    }

    @Test
    @Order(3)
    public void testListarEmpleados() {
        webTestClient.get().uri("http://localhost:8080/api/empleados").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[1].nombre").isEqualTo("Diego")
                .jsonPath("$[1].apellido").isEqualTo("Santos")
                .jsonPath("$[1].email").isEqualTo("diego.santos@gmail.com")
                .jsonPath("$").isArray()
                .jsonPath("$").value(hasSize(2));


    }

    @Test
    @Order(4)
    public void testObtenerListadoEmpleados() {
        webTestClient.get().uri("http://localhost:8080/api/empleados").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Empleado.class)
                .consumeWith(response -> {
                    List<Empleado> empleados = response.getResponseBody();
                    Assertions.assertEquals(2, empleados.size());
                    Assertions.assertNotNull(empleados);
                });
    }

    @Test
    @Order(5)
    public void testActualizarEmpleado() {
        Empleado empleadoActualizado = Empleado.builder()
                .nombre("Pepe")
                .apellido("Torrez")
                .email("pepeto@gmail.com")
                .build();

        webTestClient.put().uri("http://localhost:8080/api/empleados/2")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(empleadoActualizado)
                .exchange()//envio del request.
                // then
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @Order(6)
    public void testEliminarEmpleado() {
        webTestClient.get().uri("http://localhost:8080/api/empleados").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Empleado.class)
                .hasSize(2);

        webTestClient.delete().uri("http://localhost:8080/api/empleados/2").exchange()
                .expectStatus().isOk();

        webTestClient.get().uri("http://localhost:8080/api/empleados").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Empleado.class)
                .hasSize(1);

        webTestClient.get().uri("http://localhost:8080/api/empleados/2").exchange()
                .expectStatus().is4xxClientError();
    }
}

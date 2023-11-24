package com.fobbing.ole.test.unit.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import com.fobbing.ole.test.unit.models.Empleado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class EmpleadoRepositoryTest {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    private Empleado empleado;

    @BeforeEach
    void setup(){
        empleado = Empleado.builder()
                .nombre("Christian")
                .apellido("Perez")
                .email("cp1@gmail.com")
                .build();
    }

    @DisplayName("Test para guardar el empleado")
    @Test
    void testGuardarEmpleado(){
        //1. given - dato o condicion previa o configuracion
        //2. when - accion o el comportamiento que vamos a probar
        //3. then - verificar la salida

        //1 given - dato o condicion previa o configuracion
        Empleado empleadoFirst = Empleado.builder()
                .nombre("Pepe")
                .apellido("Lopez")
                .email("p12@gmail.com")
                .build();

        //2 when - accion o el comportamiento que vamos a probar
        Empleado empleadoGuardado = empleadoRepository.save(empleadoFirst);

        //3 then - verificar la salida
        assertThat(empleadoGuardado).isNotNull();
        assertThat(empleadoGuardado.getId()).isGreaterThan(0);
    }

    @DisplayName("Test para listar a los empleados")
    @Test
    void testListarEmpleados(){
        // given
        Empleado empleadoFirst = Empleado.builder()
                .nombre("Julen")
                .apellido("Olivia")
                .email("j2@gmail.com")
                .build();

        empleadoRepository.save(empleadoFirst);
        empleadoRepository.save(empleado);

        // when
        List<Empleado> listaEmpleados = empleadoRepository.findAll();

        // then
        assertThat(listaEmpleados).isNotNull();
        assertThat(listaEmpleados.size()).isEqualTo(2 );
    }

    @DisplayName("Test para Obtener un empleado por ID")
    @Test
    public void testObtenerEmpleadoById(){
        // given
        empleadoRepository.save(empleado);

        // when
        Empleado empleadoDb = empleadoRepository.findById(empleado.getId()).get();

        // then
        assertThat(empleadoDb).isNotNull();
    }

    @DisplayName("Test para actualizar un empleado")
    @Test
    public void actualizarEmpleado(){
        //given
        empleadoRepository.save(empleado);

        //when
        Empleado empleadoGuardado = empleadoRepository.findById(empleado.getId()).get();
        empleadoGuardado.setEmail("cris.pe@gmail.com");
        empleadoGuardado.setNombre("Cristian Raul");
        empleadoGuardado.setApellido("Ramirez Cucci");

        Empleado empleadoActualizado = empleadoRepository.save(empleadoGuardado);

        assertThat(empleadoActualizado.getEmail()).isEqualTo("cris.pe@gmail.com");
        assertThat(empleadoActualizado.getNombre()).isEqualTo("Cristian Raul");
    }

    @DisplayName("Test para eliminar un empleado")
    @Test
    public void eliminarEmpleado(){
        //given
        empleadoRepository.save(empleado);

        //when
        empleadoRepository.deleteById(empleado.getId());
        Optional<Empleado> empleadoOptional = empleadoRepository.findById(empleado.getId());

        //then
        assertThat(empleadoOptional).isEmpty();
    }
}

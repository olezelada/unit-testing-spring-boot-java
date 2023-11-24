package com.fobbing.ole.test.unit.services;

import com.fobbing.ole.test.unit.exceptions.ResourceNotFoundException;
import com.fobbing.ole.test.unit.models.Empleado;
import com.fobbing.ole.test.unit.repositories.EmpleadoRepository;
import com.fobbing.ole.test.unit.services.implementations.EmpleadoServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmpleadoServiceTest {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @InjectMocks
    private EmpleadoServiceImp empleadoService;

    private Empleado empleado;

    @BeforeEach
    public void setup() {
        empleado = Empleado.builder()
                .id(100L)
                .nombre("Christian Diego")
                .apellido("Perez Ramirez")
                .email("cris.perez180085@gmail.com")
                .build();
    }

    @DisplayName("Test para guardar empleado")
    @Test
    public void testGuardaEmpleado() {
        //given
        given(empleadoRepository.findByEmail(empleado.getEmail()))
                .willReturn(Optional.empty());

        given(empleadoRepository.save(empleado)).willReturn(empleado);
        //when
        Empleado empleadoGuardado = empleadoService.saveEmpleado(empleado);

        //then
        assertThat(empleadoGuardado).isNotNull();
    }

   /* @DisplayName("Test para guardar empleado Con Throw Exception")
    @Test
    public void testGuardaEmpleadoConThrowException() {
        //given
        given(empleadoRepository.findByEmail(empleado.getEmail()))
                .willReturn(Optional.of(empleado));

        //when
        assertThrows(ResourceNotFoundException.class, () -> {
            empleadoService.saveEmpleado(empleado);
        });

        //then
        verify(empleadoRepository, never()).save(any(Empleado.class));
    }*/

    @DisplayName("Test para listar Empleados")
    @Test
    public void testListarEmpleados() {
        //given
        Empleado firstEmpleado = Empleado.builder()
                .id(200L)
                .nombre("goku")
                .apellido("kakaroto")
                .email("goku@gmail.com")
                .build();

        given(empleadoRepository.findAll()).willReturn(List.of(empleado, firstEmpleado));

        //when
        List<Empleado> empleados = empleadoService.getAllEmpleados();

        //then
        assertThat(empleados).isNotNull();
        assertThat(empleados.size()).isEqualTo(2);
    }

    @DisplayName("Test para colleccion vacia de empleados")
    @Test
    public void testListarColeccionEmpleadosVacia(){
        //given
        Empleado firstEmpleado = Empleado.builder()
                .id(300L)
                .nombre("vegeta")
                .apellido("insecto")
                .email("vegeta@gmail.com")
                .build();
        given(empleadoRepository.findAll()).willReturn(Collections.emptyList());

        //when
        List<Empleado> listaEmpleados = empleadoService.getAllEmpleados();

        //then
        assertThat(listaEmpleados).isEmpty();
        assertThat(listaEmpleados.size()).isEqualTo(0);
    }

    @DisplayName("Test para obtener un empleado por id")
    @Test
    public void testObtenerEmpleadoById(){
        //given
        given(empleadoRepository.findById(100L)).willReturn(Optional.of(empleado));

        //when
        Empleado empleadoGuardado = empleadoService.getEmpleadoById(empleado.getId()).get();

        //then
        assertThat(empleadoGuardado).isNotNull();

    }

    @DisplayName("TEst Para actualizar un empleado")
    @Test
    public void testActualizarEmpleado(){
        //given
        given(empleadoRepository.save(empleado)).willReturn(empleado);
        empleado.setEmail("kiko@gmail.com");
        empleado.setNombre("Carlos");

        //when
        Empleado empleadoActualizado = empleadoService.updateEmpleado(empleado);

        //then
        assertThat(empleadoActualizado.getEmail()).isEqualTo("kiko@gmail.com");
        assertThat(empleadoActualizado.getNombre()).isEqualTo("Carlos");
    }

    @DisplayName("Test para eliminar un empleado")
    @Test
    public void testEliminarEmpleado(){
        //given
        long empleadoId = 100L;
        willDoNothing().given(empleadoRepository).deleteById(empleadoId);

        //when
        empleadoService.deleteEmpleado(empleadoId);

        //then
        verify(empleadoRepository, times(1)).deleteById(empleadoId);
    }
}

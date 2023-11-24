package com.fobbing.ole.test.unit.services;

import com.fobbing.ole.test.unit.models.Empleado;

import java.util.List;
import java.util.Optional;

public interface EmpleadoService {

    Empleado saveEmpleado(Empleado empleado);

    List<Empleado> getAllEmpleados();

    Optional<Empleado> getEmpleadoById(long id);

    Empleado updateEmpleado(Empleado empleadoToUpdate);

    void deleteEmpleado(long id);
}

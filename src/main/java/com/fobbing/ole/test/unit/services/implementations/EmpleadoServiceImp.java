package com.fobbing.ole.test.unit.services.implementations;

import com.fobbing.ole.test.unit.models.Empleado;
import com.fobbing.ole.test.unit.repositories.EmpleadoRepository;
import com.fobbing.ole.test.unit.services.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoServiceImp implements EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Override
    public Empleado saveEmpleado(Empleado empleado) {
        Optional<Empleado> empleadoGuardado = empleadoRepository.findByEmail(empleado.getEmail());
        if (empleadoGuardado.isPresent()) {
            throw new ResolutionException("El empleado con ese email ya existe : ".concat(empleado.getEmail()));
        }

        return empleadoRepository.save(empleado);

    }

    @Override
    public List<Empleado> getAllEmpleados() {
        return empleadoRepository.findAll();
    }

    @Override
    public Optional<Empleado> getEmpleadoById(long id) {
        return empleadoRepository.findById(id);
    }

    @Override
    public Empleado updateEmpleado(Empleado empleadoToUpdate) {
        return empleadoRepository.save(empleadoToUpdate);
    }

    @Override
    public void deleteEmpleado(long id) {
        empleadoRepository.deleteById(id);
    }
}

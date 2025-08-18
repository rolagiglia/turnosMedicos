package com.appTurnosMedicos.servicio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.appTurnosMedicos.modelo.Especialidad;
import com.appTurnosMedicos.modelo.Profesional;
import com.appTurnosMedicos.persistencia.EspecialidadDAO;
import com.appTurnosMedicos.persistencia.ProfesionalDAO;

public class CatalogoDeProfesionales {
    Map<Especialidad, List<Profesional>> profesionalesPorEspecialidad;
    private List<Profesional> profesionales;
    private List<Especialidad> especialidades;

    public CatalogoDeProfesionales() {
        this.profesionales = new ArrayList<>();
        this.especialidades = new ArrayList<>();
        profesionalesPorEspecialidad = new HashMap<>();

    }


    public Map<Especialidad, List<Profesional>> obtenerProfesionales() throws Exception {
        
        if(profesionalesPorEspecialidad.isEmpty()) {
            try{
                ProfesionalDAO profesionalDAO = new ProfesionalDAO();
                EspecialidadDAO especialidadDAO = new EspecialidadDAO();
                especialidadDAO.obtenerEspecialidades(especialidades);
                profesionalDAO.obtenerProfesionales(profesionales);
                for (Especialidad especialidad : especialidades) {
                    profesionalesPorEspecialidad.put(especialidad, new ArrayList<>());
                }

                for (Profesional profesional : profesionales) {
                    Especialidad especialidad = especialidades.stream()
                            .filter(e -> e.getId_especialidad() == profesional.getId_especialidad())
                            .findFirst()
                            .orElse(null);
                    if (especialidad != null) {
                        profesionalesPorEspecialidad.get(especialidad).add(profesional);
                    }
                }
                System.out.println("Profesionales cargados: " + profesionales.size());

            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("Error al cargar los profesionales: " + e.getMessage(), e);
            }
        }
        return profesionalesPorEspecialidad;
    }

   
    
}

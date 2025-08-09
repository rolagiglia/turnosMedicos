package com.appTurnosMedicos.servicio;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.appTurnosMedicos.modelo.PacienteConPass;
import com.appTurnosMedicos.modelo.Usuario;
import com.appTurnosMedicos.persistencia.PacienteDAO;
import com.appTurnosMedicos.persistencia.UsuarioDAO;

@ExtendWith(MockitoExtension.class)
public class RegistroPacienteTest {

    @Mock
    PacienteDAO pacienteDAO;

    @Mock
    UsuarioDAO usuarioDAO;

    @InjectMocks
    RegistroPaciente registroPaciente;

    @Test
    public void testRegistroPacienteExitoso() throws Exception {
        PacienteConPass paciente = new PacienteConPass();
        paciente.setDni(12345678);
        paciente.setNombre_paciente("Juan");
        paciente.setApellido_paciente("Pérez");
         // Fecha como Date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date fecha = sdf.parse("07/12/1987");
        
        paciente.setFecha_nacimiento(fecha);
        paciente.setMail("juan@mail.com");
        paciente.setNacionalidad("Argentina");
        paciente.setCel("123456789");
        paciente.setPassword("password123");

        // Simular comportamiento
        when(pacienteDAO.existePaciente(paciente.getDni())).thenReturn(false);
        doNothing().when(pacienteDAO).crearPaciente(anyInt(), anyString(), anyString(), any(), anyString(), anyString(), anyString());
        doNothing().when(usuarioDAO).crearUsuario(anyString(), anyString());
        when(usuarioDAO.obtenerUsuario(anyString())).thenReturn(new Usuario(1, String.valueOf(paciente.getDni()), "passwordhash", null, 1));
        doNothing().when(pacienteDAO).actualizarUsuarioIdDePaciente(anyInt(), anyInt());

        // Ejecutar
        assertDoesNotThrow(() -> registroPaciente.registraPaciente(paciente));

        // Verificar que se hayan llamado los métodos esperados
        verify(pacienteDAO).crearPaciente(anyInt(), anyString(), anyString(), any(), anyString(), anyString(), anyString());
        verify(usuarioDAO).crearUsuario(anyString(), anyString());
    }
}

package co.edu.uniquindio.proyecto;

import co.edu.uniquindio.proyecto.application.usecase.*;
import co.edu.uniquindio.proyecto.domain.repository.UsuarioRepository;
import co.edu.uniquindio.proyecto.infrastructure.rest.mapper.SolicitudMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class ProyectoApplicationTests {

	@MockitoBean CrearSolicitudUseCase crearSolicitudUseCase;
	@MockitoBean ClasificarSolicitudUseCase clasificarSolicitudUseCase;
	@MockitoBean AsignarResponsableUseCase asignarResponsableUseCase;
	@MockitoBean CerrarSolicitudUseCase cerrarSolicitudUseCase;
	@MockitoBean CambiarEstadoUseCase cambiarEstadoUseCase;
	@MockitoBean ObtenerSolicitudUseCase obtenerSolicitudUseCase;
	@MockitoBean ListarSolicitudesUseCase listarSolicitudesUseCase;
	@MockitoBean SolicitudMapper solicitudMapper;
	@MockitoBean UsuarioRepository usuarioRepository;

	@Test
	void contextLoads() {
	}

}

package com.rodasoft.recursos.humanos.service;

import com.rodasoft.recursos.humanos.dto.RegistroDTO;
import com.rodasoft.recursos.humanos.dto.RolDTO;
import com.rodasoft.recursos.humanos.dto.UsuarioDTO;
import com.rodasoft.recursos.humanos.exception.AuthException;
import com.rodasoft.recursos.humanos.exception.ValidacionException;
import com.rodasoft.recursos.humanos.model.Rol;
import com.rodasoft.recursos.humanos.model.Usuario;
import com.rodasoft.recursos.humanos.repository.RolRepository;
import com.rodasoft.recursos.humanos.repository.UsuarioRepository;
import com.rodasoft.recursos.humanos.segurity.BCrypt;
import java.util.List;

public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    public AuthService(UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    public List<RolDTO> listarRoles() {
        return rolRepository.listar().stream()
                .map(r -> new RolDTO(r.getIdRol(), r.getNombreRol()))
                .toList();
    }

    public void registrar(RegistroDTO dto) {
        if (estaVacio(dto.nombreUsuario()) || estaVacio(dto.contrasena()) || estaVacio(dto.confirmacion())) {
            throw new ValidacionException("Complete todos los campos.");
        }
        if (dto.rol() == null) {
            throw new ValidacionException("Seleccione un rol.");
        }
        if (dto.contrasena().length() < 6) {
            throw new ValidacionException("La contraseña debe tener al menos 6 caracteres.");
        }
        if (!dto.contrasena().equals(dto.confirmacion())) {
            throw new ValidacionException("Las contraseñas no coinciden.");
        }
        String nombre = dto.nombreUsuario().trim();
        if (usuarioRepository.buscarPorNombre(nombre).isPresent()) {
            throw new ValidacionException("Ese nombre de usuario ya está registrado.");
        }
        String hash = BCrypt.hashpw(dto.contrasena(), BCrypt.gensalt());
        usuarioRepository.insertar(new Usuario(0, nombre, hash, dto.rol().id()));
    }

    public UsuarioDTO iniciarSesion(String nombreUsuario, String contrasena) {
        if (estaVacio(nombreUsuario) || estaVacio(contrasena)) {
            throw new ValidacionException("Ingrese usuario y contraseña.");
        }
        Usuario usuario = usuarioRepository.buscarPorNombre(nombreUsuario.trim())
                .orElseThrow(() -> new AuthException("Usuario o contraseña incorrectos."));
        
        if (!BCrypt.checkpw(contrasena, usuario.getContrasenaHash())) {
            throw new AuthException("Usuario o contraseña incorrectos.");
        }

        String nombreRol = rolRepository.listar().stream()
                .filter(r -> r.getIdRol() == usuario.getIdRol())
                .map(Rol::getNombreRol)
                .findFirst()
                .orElse("DESCONOCIDO");

        return new UsuarioDTO(usuario.getNombreUsuario(), nombreRol);
    }

    private boolean estaVacio(String texto) {
        return texto == null || texto.isBlank();
    }
}

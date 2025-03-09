package com.espe.edu.User.service;

import com.espe.edu.User.dto.UsuarioResponse;
import com.espe.edu.User.dto.LoginRequest;
import com.espe.edu.User.entity.Usuario;
import com.espe.edu.User.entity.Rol;
import com.espe.edu.User.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, RestTemplate restTemplate) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.restTemplate = restTemplate;
    }

    /**
     * Registra un nuevo usuario en la base de datos.
     */
    public String registrarUsuario(Usuario usuario) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());
        if (usuarioExistente.isPresent()) {
            throw new RuntimeException("El correo ya está registrado");
        }

        // Si no se proporciona un rol, asignamos "CLIENTE" por defecto
        if (usuario.getRol() == null) {
            usuario.setRol(Rol.CLIENTE);
        }

        // Encriptar la contraseña antes de guardarla
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioRepository.save(usuario);

        // 🔥 Llamar al Servidor de Autenticación para enviar el correo de verificación
        enviarCorreoConfirmacion(usuario);

        return "Usuario registrado con éxito. Revisa tu correo para verificar la cuenta.";
    }

    /**
     * Enviar solicitud HTTP al Servidor de Autenticación para que envíe el correo de confirmación.
     */
    /**
     * Llamar al Servidor de Autenticación para enviar el correo de verificación.
     */
    private void enviarCorreoConfirmacion(Usuario usuario) {
        String authServerUrl = "http://localhost:8080/auth/enviarCorreo"; // 🔥 Asegurar que este URL es correcto

        try {
            System.out.println("Llamando a " + authServerUrl + " para enviar correo de " + usuario.getEmail()); // 🔥 LOG IMPORTANTE
            restTemplate.postForObject(authServerUrl, usuario, String.class);
        } catch (Exception e) {
            System.err.println("Error al enviar el correo: " + e.getMessage()); // 🔥 LOG DE ERRORES
        }
    }


    /**
     * Realiza el login enviando las credenciales al Servidor de Autenticación.
     */
    public String loginUsuario(String email, String password) {
        String authUrl = "http://localhost:8080/auth/login";

        // Crear objeto de solicitud para enviar al Servidor de Autenticación
        LoginRequest loginRequest = new LoginRequest(email, password);

        try {
            // Enviar credenciales al servidor de autenticación y recibir el token
            return restTemplate.postForObject(authUrl, loginRequest, String.class);
        } catch (HttpClientErrorException.Unauthorized e) {
            throw new RuntimeException("Credenciales incorrectas.");
        } catch (Exception e) {
            throw new RuntimeException("Error al conectarse con el Servidor de Autenticación.");
        }
    }

    /**
     * Obtiene la información de un usuario por su ID.
     */
    public UsuarioResponse obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 🔥 Asegurarse de incluir el rol en el constructor
        return new UsuarioResponse(usuario.getId(), usuario.getEmail(), usuario.getNombre(), usuario.isVerified(), usuario.getRol());
    }

}

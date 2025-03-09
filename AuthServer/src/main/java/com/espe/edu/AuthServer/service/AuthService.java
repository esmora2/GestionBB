package com.espe.edu.AuthServer.service;

import com.espe.edu.AuthServer.dto.LoginRequest;
import com.espe.edu.AuthServer.dto.RegistroRequest;
import com.espe.edu.AuthServer.entity.Rol;
import com.espe.edu.AuthServer.entity.Usuario;
import com.espe.edu.AuthServer.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil, EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

    public String registrarUsuario(RegistroRequest request) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(request.getEmail());
        if (usuarioExistente.isPresent()) {
            throw new RuntimeException("El correo ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRol(Rol.valueOf(request.getRol().toUpperCase())); // Convertir a enum
        usuario.setDireccion(request.getDireccion()); // ✅ Agregar la dirección
        usuario.setFechaRegistro(LocalDateTime.now()); // ✅ Asignar fecha automática

        usuarioRepository.save(usuario);

        // Enviar correo de confirmación
        emailService.enviarCorreoConfirmacion(usuario);

        return "Usuario registrado con éxito. Verifica tu correo.";
    }

    public String login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciales incorrectas"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        return jwtUtil.generarToken(usuario.getEmail(), usuario.getRol().name(), usuario.getNombre());
    }

    public String validarToken(String token) {
        return jwtUtil.validarToken(token).getSubject();
    }

    public String verificarUsuario(String email) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }

        Usuario usuario = usuarioOpt.get();
        if (usuario.isVerified()) {
            return "El usuario ya está verificado.";
        }

        usuario.setVerified(true);
        usuarioRepository.save(usuario);
        return "Correo verificado con éxito.";
    }
}

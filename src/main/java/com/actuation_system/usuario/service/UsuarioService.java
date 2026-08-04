package com.actuation_system.usuario.service;

import com.actuation_system.exceptions.BadRequestException;
import com.actuation_system.exceptions.ConflictRequestException;
import com.actuation_system.exceptions.NotFoundException;
import com.actuation_system.usuario.PerfilUsuario;
import com.actuation_system.usuario.entity.Usuario;
import com.actuation_system.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;

    @PreAuthorize("hasRole('DONO')")
    public Page<Usuario> getAllUsers (Pageable pageable){
        return repository.findAll(pageable);
    }

//    @PreAuthorize("hasRole('DONO') or #id == authentication.principal.id") à ser usado no futuro
    @PreAuthorize("hasRole('DONO')")
    public Usuario findById (Long id){
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found."));
    }

    @PreAuthorize("hasRole('DONO')")
    public Usuario createUser (Usuario dadosUsuario){


        if (repository.existsByEmail(dadosUsuario.getEmail())){
            throw new ConflictRequestException("E-mail already cadaster.");
        }

        Usuario usuario = new Usuario();
        usuario.setPerfil(PerfilUsuario.GARCOM);

        usuario.setNome(dadosUsuario.getNome());
        usuario.setEmail(dadosUsuario.getEmail());
        usuario.setAtivo(true);
        usuario.setSenha(passwordEncoder.encode(dadosUsuario.getSenha()));

        try {
            return repository.save(usuario);
        }
        catch (DataIntegrityViolationException e){
            throw new ConflictRequestException("E-mail already cadaster.");
        }
    }

    //    @PreAuthorize("hasRole('DONO') or #id == authentication.principal.id") à ser usado no futuro
    @PreAuthorize("hasRole('DONO')")
    public Usuario updateUser (Long id, Usuario user){

        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (repository.existsByEmailAndIdNot(user.getEmail(), id)){
            throw new ConflictRequestException("E-mail already cadaster.");
        }

        usuario.setNome(user.getNome());
        usuario.setEmail(user.getEmail());

        if (user.getSenha() != null && !user.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(user.getSenha()));
        }

        try {
            return repository.save(usuario);
        }
        catch (DataIntegrityViolationException e){
            throw new ConflictRequestException("Conflicting data ", e);
        }
    }

    @PreAuthorize("hasRole('DONO')")
    public void deleteUser (Long id){
        Usuario user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        user.setAtivo(false);
        repository.save(user);

    }


}

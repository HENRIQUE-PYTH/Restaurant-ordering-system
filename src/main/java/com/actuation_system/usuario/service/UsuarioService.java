package com.actuation_system.usuario.service;

import com.actuation_system.exceptions.BadRequestException;
import com.actuation_system.exceptions.NotFoundException;
import com.actuation_system.usuario.PerfilUsuario;
import com.actuation_system.usuario.entity.Usuario;
import com.actuation_system.usuario.repository.UsuarioRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    @PreAuthorize("hasRole('DONO')")
    public List<Usuario> getAllUsers (){
        return repository.findAll();
    }

//    @PreAuthorize("hasRole('DONO') or #id == authentication.principal.id") à ser usado no futuro
    @PreAuthorize("hasRole('DONO')")
    public Usuario findByUsers (Long id){
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario não encontrado"));
    }

    @PreAuthorize("hasRole('DONO')")
    public Usuario createUser (Usuario user){

        if (repository.existsByEmail(user.getEmail())){
            throw new BadRequestException("E-mail já cadastrado");
        }

        user.setPerfil(PerfilUsuario.GARCOM);

        return repository.save(user);
    }

    //    @PreAuthorize("hasRole('DONO') or #id == authentication.principal.id") à ser usado no futuro
    @PreAuthorize("hasRole('DONO')")
    public Usuario updateUser (Long id, Usuario user){
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        usuario.setNome(user.getNome());
        usuario.setEmail(user.getEmail());
        return repository.save(usuario);
    }

    @PreAuthorize("hasRole('DONO')")
    public void deleteUser (Long id){
        Usuario user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        repository.delete(user);
    }


}

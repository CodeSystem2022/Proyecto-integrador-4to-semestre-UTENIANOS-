package com.utn.ecommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utn.ecommerce.model.Usuario;
import com.utn.ecommerce.repository.IUsuarioRepository;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

	@Autowired
	private IUsuarioRepository usuarioRepository;

	@Override
	public Optional<Usuario> finById(Integer id) {

		return usuarioRepository.findById(id);
	}

	@Override
	public Usuario save(Usuario usuario) {

		return usuarioRepository.save(usuario);
	}

	@Override
	public Optional<Usuario> findByMail(String mail) {

		return usuarioRepository.findByMail(mail);
	}

	@Override
	public List<Usuario> finAll() {

		return usuarioRepository.findAll();
	}

}

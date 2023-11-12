package com.utn.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.utn.ecommerce.model.Usuario;

public interface IUsuarioService {

	List<Usuario> finAll();

	Optional<Usuario> finById(Integer id);

	Usuario save(Usuario usuario);

	Optional<Usuario> findByMail(String mail);
}

package com.utn.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.utn.ecommerce.model.Orden;
import com.utn.ecommerce.model.Usuario;

public interface IOrdenService {

	List<Orden> finAll();

	Orden save(Orden orden);

	String generarNumeroOrden();

	List<Orden> findByUsuario(Usuario usuario);

	Optional<Orden> findById(Integer id);

}

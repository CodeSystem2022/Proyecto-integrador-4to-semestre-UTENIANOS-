package com.utn.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.utn.ecommerce.model.Orden;
import com.utn.ecommerce.model.Usuario;

@Repository
public interface IOrdenRepository extends JpaRepository<Orden, Integer> {

	List<Orden> findByUsuario(Usuario usuario);

}

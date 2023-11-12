package com.utn.ecommerce.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utn.ecommerce.model.Orden;
import com.utn.ecommerce.model.Usuario;
import com.utn.ecommerce.repository.IOrdenRepository;

@Service
public class OrdenServiceImpl implements IOrdenService {

	@Autowired
	private IOrdenRepository ordenRepository;

	@Override
	public Orden save(Orden orden) {

		return ordenRepository.save(orden);
	}

	@Override
	public List<Orden> finAll() {

		return ordenRepository.findAll();
	}

	@Override
	public String generarNumeroOrden() {
		int numero = 0;

		String numeroConcatenado = "";

		List<Orden> ordenes = finAll();

		List<Integer> numeros = new ArrayList<>();
		ordenes.stream().forEach(o -> numeros.add(Integer.parseInt(o.getNumero()))); // para incrementar

		if (ordenes.isEmpty()) {
			numero = 1;
		} else {
			numero = numeros.stream().max(Integer::compare).get();
			numero++;
		}

		if (numero < 10) {
			numeroConcatenado = "000000000" + String.valueOf(numero);
		} else if (numero < 100) {
			numeroConcatenado = "00000000" + String.valueOf(numero);
		} else if (numero < 1000) {
			numeroConcatenado = "0000000" + String.valueOf(numero);
		} else if (numero < 10000) {
			numeroConcatenado = "000000" + String.valueOf(numero);
		}
		return numeroConcatenado;
	}

	@Override
	public List<Orden> findByUsuario(Usuario usuario) {

		return ordenRepository.findByUsuario(usuario);
	}

	@Override
	public Optional<Orden> findById(Integer id) {

		return ordenRepository.findById(id);
	}

}

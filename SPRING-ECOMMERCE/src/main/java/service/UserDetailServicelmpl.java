package com.utn.ecommerce.service;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.utn.ecommerce.model.Usuario;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private BCryptPasswordEncoder bCrypt;

	private Logger log = LoggerFactory.getLogger(UserDetailServiceImpl.class);// reemplaza el system
	@Autowired
	HttpSession session;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("Esto es el username");
		Optional<Usuario> optionalUser = usuarioService.findByMail(username);
		if (optionalUser.isPresent()) {
			log.info("Esto es el id del usuario: {}", optionalUser.get().getId());
			session.setAttribute("idusuario", optionalUser.get().getId());
			Usuario usuario = optionalUser.get();
			return User.builder().username(usuario.getNombre()).password(usuario.getPassword())
					.roles(usuario.getTipo()).build();
		} else {
			throw new UsernameNotFoundException("Usuario no encontrado");
		}

	}

}

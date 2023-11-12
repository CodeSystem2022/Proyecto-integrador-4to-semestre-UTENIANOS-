package com.utn.ecommerce.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.utn.ecommerce.model.DetalleOrden;
import com.utn.ecommerce.model.Orden;
import com.utn.ecommerce.model.Producto;
import com.utn.ecommerce.model.Usuario;
import com.utn.ecommerce.service.IDetalleOrdenService;
import com.utn.ecommerce.service.IOrdenService;
import com.utn.ecommerce.service.IUsuarioService;
import com.utn.ecommerce.service.ProductoService;

@Controller
@RequestMapping("/") // raiz del proyecto
public class HomeController {

	private final Logger log = LoggerFactory.getLogger(HomeController.class);
	@Autowired
	private ProductoService productoService;
	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private IOrdenService ordenService;

	@Autowired
	private IDetalleOrdenService detalleOrdenService;

	List<DetalleOrden> detalles = new ArrayList<>(); // para almacenar los detalles de la orden

	// datos de la orden
	Orden orden = new Orden();

	@GetMapping("")
	public String home(Model model, HttpSession session) {

		log.info("Sesión del usuario: {}", session.getAttribute("idusuario"));
		model.addAttribute("productos", productoService.findAll());
		// session
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		return "usuario/home";
	}

	@GetMapping("productohome/{id}")
	public String productohome(@PathVariable Integer id, Model model) {

		Producto producto = new Producto();
		Optional<Producto> productoOptional = productoService.get(id);
		producto = productoOptional.get();
		model.addAttribute("producto", producto);
		return "usuario/productohome";
	}

	@PostMapping("/cart")
	public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model) {
		DetalleOrden detalleOrden = new DetalleOrden();
		Producto producto = new Producto();
		double sumaTotal = 0;

		Optional<Producto> optionalProducto = productoService.get(id);
		producto = optionalProducto.get();

		detalleOrden.setCantidad(cantidad);
		detalleOrden.setPrecio(producto.getPrecio());
		detalleOrden.setNombre(producto.getNombre());
		detalleOrden.setTotal(producto.getPrecio() * cantidad);
		detalleOrden.setProducto(producto);

		// validacion para que el mismo producto no se añada dos veces al carrito de
		// compra
		Integer idProducto = producto.getId();
		boolean ingresado = detalles.stream().anyMatch(p -> p.getProducto().getId() == idProducto);
		if (!ingresado) {
			detalles.add(detalleOrden);
		}

		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();
		orden.setTotal(sumaTotal);

		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		return "usuario/carrito";
	}

	// quitar un producto del carrito

	@GetMapping("/delete/cart/{id}")
	public String deleteProductoCart(@PathVariable Integer id, Model model) {
		// lista de productos nueva
		List<DetalleOrden> ordenesNuevas = new ArrayList<>();
		for (DetalleOrden detalleOrden : detalles) {
			if (detalleOrden.getProducto().getId() != id) {
				ordenesNuevas.add(detalleOrden);
			}
		}
		// poner en la nueva lista los productos restantes
		detalles = ordenesNuevas;
		double sumaTotal = 0;
		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();
		orden.setTotal(sumaTotal);

		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		return "usuario/carrito";
	}

	@GetMapping("/getCart")
	public String getCard(Model model, HttpSession session) {
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		return "usuario/carrito";
	}

	@GetMapping("/order")
	public String order(Model model, HttpSession session) {

		Usuario usuario = usuarioService.finById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		model.addAttribute("usuario", usuario);
		return "usuario/resumenorden";
	}

	@GetMapping("/saveOrder")
	public String saveOrder(HttpSession session) {
		Date fechaCreacion = new Date();
		orden.setFechaCreacion(fechaCreacion);
		orden.setNumero(ordenService.generarNumeroOrden());
		// usuario
		Usuario usuario = usuarioService.finById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		// guardar orden
		orden.setUsuario(usuario);
		ordenService.save(orden);
		// guardar detalle orden
		for (DetalleOrden dt : detalles) {
			dt.setOrden(orden);
			detalleOrdenService.save(dt);
		}

		// limpiar lista y Orden
		orden = new Orden();
		detalles.clear();

		return "redirect:/";
	}

	@PostMapping("/search")
	public String searchProduct(@RequestParam String nombre, Model model) {
		List<Producto> productos = productoService.findAll().stream().filter(p -> p.getNombre().contains(nombre))
				.collect(Collectors.toList());
		model.addAttribute("productos", productos);
		return "usuario/home";

	}

}

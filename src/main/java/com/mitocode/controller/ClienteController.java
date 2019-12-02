package com.mitocode.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mitocode.model.Cliente;
import com.mitocode.model.Usuario;
import com.mitocode.service.IClienteService;
import com.mitocode.service.IUsuarioService;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

	@Autowired
	private IClienteService service;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	/*@PostMapping
	public Cliente registrar_1(@RequestPart("cliente") Cliente cliente, @RequestPart("file") MultipartFile file)
			throws IOException {
		Cliente c = cliente;		
		c.setFoto(file.getBytes());		
		return service.registrar(c);
	}*/
	
	
	@PostMapping
	public Cliente registrar(@RequestPart("cliente") Cliente cliente, @RequestPart("usuario") Usuario usuario, @RequestPart("file") MultipartFile file)
			throws IOException {
		Cliente c = cliente;
		c.setFoto(file.getBytes());
		usuario.setCliente(cliente);
		usuario.setClave(bcrypt.encode(usuario.getClave()));
		Cliente nuevoCliente = service.registrar(cliente);
		usuarioService.registrarTransaccional(usuario);
		return nuevoCliente;
	}
	
	
	/*@PutMapping
	public Cliente modificar_1(@RequestPart("cliente") Cliente cliente, @RequestPart("file") MultipartFile file)
			throws IOException {
		Cliente c = cliente;		
		c.setFoto(file.getBytes());
		return service.modificar(c);
	}*/

	@PutMapping
	public Cliente modificar(@RequestPart("cliente") Cliente cliente, @RequestPart("file") MultipartFile file)
			throws IOException {
		Cliente c = cliente;
		c.setFoto(file.getBytes());
		Cliente modiCliente = service.modificar(cliente);
		return modiCliente;
	}
	
	/*@GetMapping("/{id}")
	public Cliente listarPorId(@PathVariable("id") Integer id) {
		return service.listarPorId(id);
	}*/
	
	@GetMapping(value = "{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> listarPorId(@PathVariable("id") Integer id) {
		Cliente c = service.listarPorId(id);
		byte[] data = c.getFoto();
		return new ResponseEntity<byte[]>(data, HttpStatus.OK);
	}
	
	@GetMapping(value = "/foto/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> fotoPorId(@PathVariable("id") Integer id) {
		Cliente c = service.listarPorId(id);
		byte[] data = c.getFoto();
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@GetMapping
	public List<Cliente> listar() {
		return service.listar();
	}

	@DeleteMapping("/{id}")
	public void eliminar(@PathVariable("id") Integer id) {
		service.eliminar(id);
	}
}

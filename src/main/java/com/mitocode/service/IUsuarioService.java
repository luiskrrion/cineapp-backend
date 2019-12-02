package com.mitocode.service;

import com.mitocode.model.Usuario;

public interface IUsuarioService{

	Usuario registrarTransaccional(Usuario us);
	Usuario listarPorNombre(String userName); 
	Usuario listarPorId(Integer id);
}

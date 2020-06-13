package com.condominio.cobranca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.condominio.cobranca.model.Titulo;

public interface Titulos extends JpaRepository<Titulo, Long> {

	public Titulo findByCodigo(Long codigo);
	
	public List<Titulo> findByDescricaoContaining(String descricao);

}

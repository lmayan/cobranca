package com.condominio.cobranca.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.condominio.cobranca.model.StatusTitulo;
import com.condominio.cobranca.model.Titulo;
import com.condominio.cobranca.repository.Titulos;
import com.condominio.cobranca.repository.filter.TituloFilter;

@Service
public class CadastroTituloService {

	@Autowired
	private Titulos titulos;

	// SALVAR
	public void salvar(Titulo titulo) {
		try {
			titulos.save(titulo); // Salvando objeto
		} catch (DataIntegrityViolationException e) {
			throw new IllegalArgumentException("Formato de data inválido.");
		}
	}

	// DELETAR
	public void deletar(Long codigo) {
		titulos.deleteById(codigo);
	}
	
	// EDITAR
	public Optional<Titulo> editar(Long codigo) {
		return titulos.findById(codigo);
	}

	// PESQUISAR
	public List<Titulo> pesquisar(TituloFilter filtro) {
		// Cria condicao se descricao for nula, preencha com nada para buscar TODOS os titulos
		// Se nao a condicao ternaria ira preencher com a descricao do filtro
		String descricao = filtro.getDescricao() == null ? "" : filtro.getDescricao();
		List<Titulo> lst = titulos.findByDescricaoContaining(descricao);
		return lst;
	}

	public String receber(Long codigo) {
		// Encontrar o titulo 
		Titulo titulo = titulos.findByCodigo(codigo);
		
		// Alterar seu Status via ENUM
		titulo.setStatus(StatusTitulo.RECEBIDO);
		
		// Salvar a alteracao
		titulos.save(titulo);
	
		// Retorna String que vai ser usada via AJAX via o arquivo 'cobranca.js'
		return titulo.getStatus().getDescricao();
	}
	
	

}

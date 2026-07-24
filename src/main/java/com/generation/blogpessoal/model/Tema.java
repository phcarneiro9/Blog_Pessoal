package com.generation.blogpessoal.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity // vai gerar uma model
@Table(name = "tb_temas") // é Equivalente no SQL a CREATE TABLE tb_temas();

public class Tema {

	@Id // Equivalente a PRIMARY KEY
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Equivalente a AUTO_INCREMENT
	private Long id;

	@NotBlank(message = "O atributo descrição é obrigatório!")
	@Size(min = 5, max = 255, message = "O atributo descrição deve conter no mínimo 5 e no máximo 255 caracteres!")
	@Column(length = 255) // Configurações de coluna dentro do banco de dados
	private String descricao;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tema", cascade = CascadeType.REMOVE) // Remove a mais utilizada
	@JsonIgnoreProperties(value = "tema", allowSetters = true)
	private List<Postagem> postagem;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<Postagem> getPostagem() {
		return postagem;
	}

	public void setPostagem(List<Postagem> postagem) {
		this.postagem = postagem;
	}

}

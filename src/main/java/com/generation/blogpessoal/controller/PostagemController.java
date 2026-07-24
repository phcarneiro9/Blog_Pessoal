package com.generation.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;
import com.generation.blogpessoal.repository.TemaRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/postagens")
@CrossOrigin(origins = "*", allowedHeaders = "*")
// Libera requisições de qualquer origem.
// Isso ajuda no funcionamento da API quando o front estiver em outro domínio ou na nuvem.
public class PostagemController {

	// Injeta o repositório responsável pelas operações da entidade Postagem
	@Autowired
	private PostagemRepository postagemRepository;

	// Injeta o repositório de Tema para validar se o tema informado existe
	@Autowired
	private TemaRepository temaRepository;

	@GetMapping
	public ResponseEntity<List<Postagem>> getAll() {

		// Retorna todas as postagens cadastradas no banco
		// Equivalente SQL: SELECT * FROM tb_postagens;
		return ResponseEntity.ok(postagemRepository.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Postagem> getById(@PathVariable Long id) {

		// Busca uma postagem pelo id informado na URL
		// @PathVariable indica que o valor vem do caminho da requisição
		return postagemRepository.findById(id)

				// Se encontrar a postagem, retorna 200 OK com o objeto no corpo
				.map(resposta -> ResponseEntity.ok(resposta))

				// Se não encontrar, retorna 404 Not Found
				.orElse(ResponseEntity.notFound().build());

		// Equivalente SQL: SELECT * FROM tb_postagens WHERE id = ?;
	}

	@GetMapping("/titulo/{titulo}")
	public ResponseEntity<List<Postagem>> getAllByTitulo(@PathVariable String titulo) {

		// Busca todas as postagens cujo título contenha o texto informado,
		// sem diferenciar letras maiúsculas e minúsculas
		return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));

		// Equivalente SQL: SELECT * FROM tb_postagens WHERE titulo LIKE "%?%";
	}

	@PostMapping
	public ResponseEntity<Postagem> post(@Valid @RequestBody Postagem postagem) {

		// @RequestBody indica que os dados da postagem virão no corpo da requisição em
		// JSON
		// @Valid ativa as validações definidas na entidade antes de salvar

		// Verifica se o tema associado à postagem existe no banco
		if (temaRepository.existsById(postagem.getTema().getId())) {

			// Garante que a operação será tratada como cadastro de uma nova postagem
			// O id será gerado automaticamente pelo banco
			postagem.setId(null);

			// Salva a nova postagem e retorna 201 CREATED
			return ResponseEntity.status(HttpStatus.CREATED).body(postagemRepository.save(postagem));

			// Equivalente SQL:
			// INSERT INTO tb_postagens(titulo, texto, tema_id) VALUES (?, ?, ?);
		}

		// Se o tema informado não existir, retorna erro 400 Bad Request
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O Tema não existe!", null);
	}

	@PutMapping
	public ResponseEntity<Postagem> put(@Valid @RequestBody Postagem postagem) {

		// Atualiza uma postagem já existente
		// Primeiro verifica se a postagem existe
		if (postagemRepository.existsById(postagem.getId())) {

			// Depois verifica se o tema informado também existe
			if (temaRepository.existsById(postagem.getTema().getId())) {

				// Se ambos existirem, salva a atualização e retorna 200 OK
				return ResponseEntity.ok(postagemRepository.save(postagem));

				// Equivalente SQL:
				// UPDATE tb_postagens SET titulo = ?, texto = ?, tema_id = ? WHERE id = ?;
			}

			// Se o tema não existir, retorna erro 400 Bad Request
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O Tema não existe!", null);
		}

		// Se a postagem não existir, retorna 404 Not Found
		return ResponseEntity.notFound().build();
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {

		// Busca a postagem pelo id antes de excluir
		Optional<Postagem> postagem = postagemRepository.findById(id);

		// Se não encontrar, retorna 404 Not Found
		if (postagem.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		// Se encontrar, exclui a postagem do banco
		postagemRepository.deleteById(id);

		// Equivalente SQL: DELETE FROM tb_postagens WHERE id = ?;
	}
}
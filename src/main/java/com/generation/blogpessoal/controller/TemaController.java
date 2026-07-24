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

import com.generation.blogpessoal.model.Tema;
import com.generation.blogpessoal.repository.TemaRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/temas")
@CrossOrigin(origins = "*", allowedHeaders = "*") 
// Libera requisições de qualquer origem.
// Isso é útil quando o front-end estiver rodando em outro domínio ou na nuvem.
public class TemaController {

    // Injeta o repositório responsável pelas operações da entidade Tema
    @Autowired
    private TemaRepository temaRepository;

    @GetMapping
    public ResponseEntity<List<Tema>> getAll() {

        // Retorna a lista com todos os temas cadastrados
        // Equivalente SQL: SELECT * FROM tb_temas;
        return ResponseEntity.ok(temaRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tema> getById(@PathVariable Long id) {

        // Busca um tema pelo id informado na URL
        // @PathVariable indica que o valor vem do caminho da requisição
        return temaRepository.findById(id)

                // Se encontrar o tema, retorna 200 OK com o objeto no corpo
                .map(resposta -> ResponseEntity.ok(resposta))

                // Se não encontrar, retorna 404 Not Found
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/descricao/{descricao}")
    public ResponseEntity<List<Tema>> getAllByDescricao(@PathVariable String descricao) {

        // Busca todos os temas cuja descrição contenha o texto informado
        // Ignora diferença entre letras maiúsculas e minúsculas
        return ResponseEntity.ok(temaRepository.findAllByDescricaoContainingIgnoreCase(descricao));
    }

    @PostMapping
    public ResponseEntity<Tema> post(@Valid @RequestBody Tema tema){

        // @RequestBody indica que os dados virão no corpo da requisição em JSON
        // @Valid ativa as validações definidas na entidade Tema

        // Garante que o id será gerado automaticamente pelo banco
        // Isso evita que o cadastro tente reutilizar um id já existente
        tema.setId(null);

        // Salva o novo tema e retorna 201 CREATED
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(temaRepository.save(tema));
    }

    @PutMapping
    public ResponseEntity<Tema> put(@Valid @RequestBody Tema tema){

        // Busca o tema pelo id antes de atualizar
        // Se o id existir, salva a atualização
        return temaRepository.findById(tema.getId())

            // Se encontrar, salva o tema atualizado e retorna a resposta
            .map(resposta -> ResponseEntity.status(HttpStatus.CREATED)
            .body(temaRepository.save(tema)))

            // Se não encontrar, retorna 404 Not Found
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {

        // Busca o tema pelo id antes de excluir
        Optional<Tema> tema = temaRepository.findById(id);

        // Se não encontrar, lança exceção com status 404 Not Found
        if (tema.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        // Se encontrar, exclui o tema do banco
        temaRepository.deleteById(id);
    }
}
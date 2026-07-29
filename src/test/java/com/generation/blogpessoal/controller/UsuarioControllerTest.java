package com.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.model.UsuarioLogin;
import com.generation.blogpessoal.repository.UsuarioRepository;
import com.generation.blogpessoal.service.UsuarioService;
import com.generation.blogpessoal.util.JwtHelper;
import com.generation.blogpessoal.util.TestBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class UsuarioControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private UsuarioRepository usuarioRepository;

	private static final String BASE_URL = "/usuarios";
	private static final String USUARIO = "root@root.com";
	private static final String SENHA = "rootroot";

	@BeforeAll
	void inicio() {
		usuarioRepository.deleteAll();
		usuarioService.cadastrarUsuario(TestBuilder.criarUsuario(null, "Root", USUARIO, SENHA));
	}

	@Test
	@DisplayName("01 - Deve cadastrar um novo usuário com sucesso")
	void deveCadastrarUsuario() {
		// Given
		Usuario usuario = TestBuilder.criarUsuario(null, "Gabrielle Guimarães", "gabrielle@email.com.br", "gabi1234");

		// When

		// Corpo da Requisição
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(usuario);

		// Enviar a Requisição
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange(BASE_URL + "/cadastrar", HttpMethod.POST,
				corpoRequisicao, Usuario.class);

		// Then

		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
		assertNotNull(resposta.getBody());

	}

	@Test
	@DisplayName("02 - Não Deve cadastrar usuário duplicado")
	void naoDeveCadastrarUsuarioDuplicado() {
		// Given
		Usuario usuario = TestBuilder.criarUsuario(null, "Luiza Guimarães", "luiza@email.com.br", "luiza1234");
		usuarioService.cadastrarUsuario(usuario);

		// When

		// Corpo da Requisição
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(usuario);

		// Enviar a Requisição
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange(BASE_URL + "/cadastrar", HttpMethod.POST,
				corpoRequisicao, Usuario.class);

		// Then

		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
		assertNull(resposta.getBody());

	}

	@Test
	@DisplayName("03 - Deve Listar todos os usuários")
	void deveListarTodosUsuarios() {
		// Given
		usuarioService.cadastrarUsuario(TestBuilder.criarUsuario(null, "Kaue Dota", "kaue@email.com.br", "kaue1234"));
		usuarioService.cadastrarUsuario(
				TestBuilder.criarUsuario(null, "Edson Nascimento", "edson@email.com.br", "edson1234"));

		// When

		// Obter o Token
		String token = JwtHelper.obterToken(testRestTemplate, USUARIO, SENHA);

		// Cabeçalho da Requisição
		HttpEntity<Void> cabecalhoRequisicao = JwtHelper.criarRequisicaoComToken(token);

		// Enviar a Requisição
		ResponseEntity<Usuario[]> resposta = testRestTemplate.exchange(BASE_URL + "/all", HttpMethod.GET,
				cabecalhoRequisicao, Usuario[].class);

		// Then

		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertNotNull(resposta.getBody());

	}

	@Test
	@DisplayName("04 - Deve Atualizar os dados do usuário com sucesso")
	void deveAtualizarUsuario() {
		// Given

		// Objeto para fazer o cadastro
		Usuario usuario = TestBuilder.criarUsuario(null, "Daniel", "daniel@email.com.br", "daniel1234");

		// Fiz o cadastro e guardei os dados objeto
		Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(usuario);

		// Preparar o objeto com a atualização
		Usuario usuarioUpdate = TestBuilder.criarUsuario(usuarioCadastrado.get().getId(), "Daniel Araujo",
				"daniel_araujo@email.com.br", "abcd1234");

		// When

		// Obter o Token
		String token = JwtHelper.obterToken(testRestTemplate, USUARIO, SENHA);

		// Cabeçalho da Requisição
		HttpEntity<Usuario> cabecalhoRequisicao = JwtHelper.criarRequisicaoComToken(usuarioUpdate, token);
		
		// Enviar a Requisição
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange(BASE_URL + "/atualizar", HttpMethod.PUT,
				cabecalhoRequisicao, Usuario.class);

		// Then

		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertNotNull(resposta.getBody());

	}
	
	@Test
	@DisplayName("05 - Deve listar um usuário específico pelo id")
	void deveListarUsuarioPorId() {
		// Given
		Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(
				TestBuilder.criarUsuario(null, "Joao Pedro", "joao@email.com.br", "joao1234"));

		// When

		// Obter o Token
		String token = JwtHelper.obterToken(testRestTemplate, USUARIO, SENHA);
		
		// Obter o Id
		Long id = usuarioCadastrado.get().getId();

		// Cabeçalho da Requisição
		HttpEntity<Void> cabecalhoRequisicao = JwtHelper.criarRequisicaoComToken(token);

		// Enviar a Requisição
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange(BASE_URL + "/{id}", HttpMethod.GET,
				cabecalhoRequisicao, Usuario.class, id);

		// Then

		assertNotNull(resposta.getBody());
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		
		// Faz uma verificação se o id condiz com os dados do usuário
		Usuario usuarioResposta = resposta.getBody();
		assertEquals(id, usuarioResposta.getId());
		assertEquals("Joao Pedro", usuarioResposta.getNome());
		assertEquals("joao@email.com.br", usuarioResposta.getUsuario());
		
	}
	
	@Test
	@DisplayName("06 - Deve autenticar um usuário com sucesso")
	void deveAutenticarUsuario() {
		// Given
		UsuarioLogin usuarioLogin = new UsuarioLogin();
		
		usuarioLogin.setUsuario(USUARIO);
		usuarioLogin.setSenha(SENHA);

		// When

		// Corpo da Requisição
		HttpEntity<UsuarioLogin> corpoRequisicao = new HttpEntity<UsuarioLogin>(usuarioLogin);

		// Enviar a Requisição
		ResponseEntity<UsuarioLogin> resposta = testRestTemplate.exchange(BASE_URL + "/logar", HttpMethod.POST,
				corpoRequisicao, UsuarioLogin.class);

		// Then

		assertNotNull(resposta.getBody());
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		
		// Faz a verificação se o token está vazio e depois verifica se o corpo da requisição está vazio
		assertNotNull(resposta.getBody().getToken());
		assertFalse(resposta.getBody().getToken().isBlank());
		
	}
}

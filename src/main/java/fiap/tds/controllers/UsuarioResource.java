package fiap.tds.controllers;

import fiap.tds.dtos.UsuarioLoginDTO;    // DTO para login
import fiap.tds.dtos.UsuarioRegistroDTO; // DTO para registro
import fiap.tds.entities.Usuario;
import fiap.tds.exceptions.BadRequestException;
import fiap.tds.exceptions.NotFoundException;
import fiap.tds.services.UsuarioService;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.util.List;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    private static final Logger logger = Logger.getLogger(UsuarioResource.class);

    @Inject
    UsuarioService usuarioService;

    @POST
    @Path("/registrar")
    public Response registrar(UsuarioRegistroDTO registroDTO) {
        logger.info("Requisição para registrar novo usuário: " + (registroDTO != null ? registroDTO.getEmail() : "DTO nulo"));
        try {
            Usuario usuarioRegistrado = usuarioService.registrar(registroDTO); // O serviço agora recebe o DTO
            logger.info("Usuário registrado com sucesso. ID: " + usuarioRegistrado.getUserId());
            return Response.status(Response.Status.CREATED).entity(usuarioRegistrado).build();
        } catch (BadRequestException e) {
            logger.warn("Dados inválidos para registro: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.error("Erro ao registrar usuário: " + e.getMessage(), e);
            return Response.serverError().entity("Erro ao tentar registrar usuário.").build();
        }
    }

    @POST
    @Path("/login")
    public Response login(UsuarioLoginDTO loginDTO) {
        logger.info("Tentativa de login para: " + (loginDTO != null ? loginDTO.getEmail() : "DTO nulo"));
        try {
            Usuario usuario = usuarioService.login(loginDTO); // Serviço agora recebe o DTO
            logger.info("Login bem-sucedido para: " + usuario.getEmail());
            return Response.ok(usuario).build();
        } catch (BadRequestException | NotFoundException e) { // NotFoundException também pode ser lançada pelo serviço de login
            logger.warn("Falha no login para " + (loginDTO != null ? loginDTO.getEmail() : "DTO nulo") + ": " + e.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED).entity("Email ou senha inválidos.").build();
        } catch (Exception e) {
            logger.error("Erro no processo de login: " + e.getMessage(), e);
            return Response.serverError().entity("Erro no processo de login.").build();
        }
    }

    @GET
    public Response listar() {
        logger.info("Listando todos os usuários...");
        try {
            List<Usuario> lista = usuarioService.listarTodos();
            return Response.ok(lista).build();
        } catch (Exception e) {
            logger.error("Erro ao listar usuários: " + e.getMessage(), e);
            return Response.serverError().entity("Erro ao buscar usuários.").build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") int id) { // ID como int
        logger.info("Buscando usuário com ID " + id);
        try {
            Usuario usuario = usuarioService.buscarPorId(id);
            return Response.ok(usuario).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (BadRequestException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        catch (Exception e) {
            logger.error("Erro ao buscar usuário por ID: " + e.getMessage(), e);
            return Response.serverError().entity("Erro ao buscar usuário.").build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") int id, Usuario usuario) { // ID como int
        logger.info("Atualizando usuário com ID " + id);
        try {
            Usuario usuarioAtualizado = usuarioService.atualizar(id, usuario);
            return Response.ok(usuarioAtualizado).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (BadRequestException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.error("Erro ao atualizar usuário: " + e.getMessage(), e);
            return Response.serverError().entity("Erro ao atualizar usuário.").build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response remover(@PathParam("id") int id) { // ID como int
        logger.info("Removendo usuário com ID " + id);
        try {
            usuarioService.deletar(id);
            return Response.ok("Usuário removido com sucesso.").build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (BadRequestException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        catch (Exception e) {
            logger.error("Erro ao remover usuário: " + e.getMessage(), e);
            return Response.serverError().entity("Erro ao remover usuário.").build();
        }
    }
}
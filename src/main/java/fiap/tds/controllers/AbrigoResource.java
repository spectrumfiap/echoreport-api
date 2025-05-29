package fiap.tds.controllers;

import fiap.tds.entities.Abrigo;
import fiap.tds.exceptions.BadRequestException;
import fiap.tds.exceptions.NotFoundException;
import fiap.tds.services.AbrigoService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import jakarta.annotation.security.PermitAll;

import java.util.List;

@Path("/abrigos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AbrigoResource {

    private static final Logger logger = Logger.getLogger(AbrigoResource.class);
    private final AbrigoService abrigoService = new AbrigoService();

    @GET
    @PermitAll
    public Response listar() {
        logger.info("Requisição para listar todos os abrigos...");
        try {
            List<Abrigo> listaDeAbrigos = abrigoService.listarTodos();
            logger.info("Total de abrigos encontrados: " + listaDeAbrigos.size());
            return Response.ok(listaDeAbrigos).build();
        } catch (Exception e) {
            logger.error("Erro ao listar abrigos: " + e.getMessage(), e);
            return Response.serverError().entity("Ocorreu um erro ao buscar a lista de abrigos.").build();
        }
    }

    @POST
    public Response adicionar(Abrigo abrigo) {
        logger.info("Requisição para adicionar novo abrigo: " + (abrigo != null ? abrigo.getName() : "null"));
        try {
            abrigoService.registrar(abrigo);
            assert abrigo != null;
            logger.info("Abrigo adicionado com sucesso. ID: " + abrigo.getId());
            return Response.status(Response.Status.CREATED).entity(abrigo).build();
        } catch (BadRequestException e) {
            logger.warn("Dados inválidos para adicionar abrigo: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.error("Erro ao adicionar abrigo: " + e.getMessage(), e);
            return Response.serverError().entity("Ocorreu um erro ao tentar adicionar o abrigo.").build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") int id) {
        logger.info("Requisição para buscar abrigo com ID: " + id);
        try {
            Abrigo abrigo = abrigoService.buscarPorId(id);
            logger.info("Abrigo encontrado: " + abrigo.getName());
            return Response.ok(abrigo).build();
        } catch (NotFoundException e) {
            logger.warn("Abrigo com ID " + id + " não encontrado: " + e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (BadRequestException e) {
            logger.warn("ID inválido para busca: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        catch (Exception e) {
            logger.error("Erro ao buscar abrigo por ID " + id + ": " + e.getMessage(), e);
            return Response.serverError().entity("Ocorreu um erro ao buscar o abrigo.").build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") int id, Abrigo abrigo) {
        logger.info("Requisição para atualizar abrigo com ID: " + id);
        if (abrigo == null) {
            logger.warn("Tentativa de atualização com dados nulos para o abrigo ID: " + id);
            return Response.status(Response.Status.BAD_REQUEST).entity("Dados do abrigo não podem ser nulos.").build();
        }
        try {
            abrigoService.atualizar(id, abrigo);
            logger.info("Abrigo com ID " + id + " atualizado com sucesso.");
            return Response.ok(abrigo).build();
        } catch (NotFoundException e) {
            logger.warn("Abrigo com ID " + id + " não encontrado para atualização: " + e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (BadRequestException e) {
            logger.warn("Dados inválidos para atualização do abrigo ID " + id + ": " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.error("Erro ao atualizar abrigo ID " + id + ": " + e.getMessage(), e);
            return Response.serverError().entity("Ocorreu um erro ao tentar atualizar o abrigo.").build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response remover(@PathParam("id") int id) {
        logger.info("Requisição para remover abrigo com ID: " + id);
        try {
            abrigoService.deletar(id);
            logger.info("Abrigo com ID " + id + " removido com sucesso.");
            return Response.ok().entity("Abrigo com ID " + id + " removido com sucesso.").build();
        } catch (NotFoundException e) {
            logger.warn("Abrigo com ID " + id + " não encontrado para remoção: " + e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (BadRequestException e) {
            logger.warn("ID inválido para remoção: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        catch (Exception e) {
            logger.error("Erro ao remover abrigo ID " + id + ": " + e.getMessage(), e);
            return Response.serverError().entity("Ocorreu um erro ao tentar remover o abrigo.").build();
        }
    }
}
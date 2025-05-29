package fiap.tds.controllers;

import fiap.tds.entities.Alerta;
import fiap.tds.exceptions.BadRequestException;
import fiap.tds.exceptions.NotFoundException;
import fiap.tds.services.AlertaService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.util.List;

@Path("/alertas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AlertaResource {

    private static final Logger logger = Logger.getLogger(AlertaResource.class);
    private final AlertaService alertaService = new AlertaService();

    @GET
    public Response listar() {
        logger.info("Requisição para listar todos os alertas...");
        try {
            List<Alerta> listaDeAlertas = alertaService.listarTodos();
            logger.info("Total de alertas encontrados: " + listaDeAlertas.size());
            return Response.ok(listaDeAlertas).build();
        } catch (Exception e) {
            logger.error("Erro ao listar alertas: " + e.getMessage(), e);
            return Response.serverError().entity("Ocorreu um erro ao buscar a lista de alertas.").build();
        }
    }

    @POST
    public Response adicionar(Alerta alerta) {
        logger.info("Requisição para adicionar novo alerta: " + (alerta != null ? alerta.getTitle() : "null"));
        try {
            alertaService.registrar(alerta); // Chama alertaService.registrar()
            logger.info("Alerta adicionado com sucesso. ID: " + alerta.getId());
            return Response.status(Response.Status.CREATED).entity(alerta).build();
        } catch (BadRequestException e) {
            logger.warn("Dados inválidos para adicionar alerta: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.error("Erro ao adicionar alerta: " + e.getMessage(), e);
            return Response.serverError().entity("Ocorreu um erro ao tentar adicionar o alerta.").build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") int id) {
        logger.info("Requisição para buscar alerta com ID: " + id);
        try {
            Alerta alerta = alertaService.buscarPorId(id);
            logger.info("Alerta encontrado: " + alerta.getTitle());
            return Response.ok(alerta).build();
        } catch (NotFoundException e) {
            logger.warn("Alerta com ID " + id + " não encontrado: " + e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (BadRequestException e) {
            logger.warn("ID inválido para busca de alerta: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        catch (Exception e) {
            logger.error("Erro ao buscar alerta por ID " + id + ": " + e.getMessage(), e);
            return Response.serverError().entity("Ocorreu um erro ao buscar o alerta.").build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") int id, Alerta alerta) {
        logger.info("Requisição para atualizar alerta com ID: " + id);
        if (alerta == null) {
            logger.warn("Tentativa de atualização com dados nulos para o alerta ID: " + id);
            return Response.status(Response.Status.BAD_REQUEST).entity("Dados do alerta não podem ser nulos.").build();
        }
        try {
            alertaService.atualizar(id, alerta); // Chama alertaService.atualizar()
            logger.info("Alerta com ID " + id + " atualizado com sucesso.");
            return Response.ok(alerta).build(); // Retornando o objeto atualizado
        } catch (NotFoundException e) {
            logger.warn("Alerta com ID " + id + " não encontrado para atualização: " + e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (BadRequestException e) {
            logger.warn("Dados inválidos para atualização do alerta ID " + id + ": " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.error("Erro ao atualizar alerta ID " + id + ": " + e.getMessage(), e);
            return Response.serverError().entity("Ocorreu um erro ao tentar atualizar o alerta.").build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response remover(@PathParam("id") int id) {
        logger.info("Requisição para remover alerta com ID: " + id);
        try {
            alertaService.deletar(id); // Chama alertaService.deletar()
            logger.info("Alerta com ID " + id + " removido com sucesso.");
            return Response.ok().entity("Alerta com ID " + id + " removido com sucesso.").build();
        } catch (NotFoundException e) {
            logger.warn("Alerta com ID " + id + " não encontrado para remoção: " + e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (BadRequestException e) {
            logger.warn("ID inválido para remoção de alerta: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        catch (Exception e) {
            logger.error("Erro ao remover alerta ID " + id + ": " + e.getMessage(), e);
            return Response.serverError().entity("Ocorreu um erro ao tentar remover o alerta.").build();
        }
    }
}
package fiap.tds.controllers;

import fiap.tds.entities.Mapa;
import fiap.tds.exceptions.BadRequestException;
import fiap.tds.exceptions.NotFoundException;
import fiap.tds.services.MapaService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.util.List;

@Path("/mapas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MapaResource {

    private static final Logger logger = Logger.getLogger(MapaResource.class);
    private final MapaService mapaService = new MapaService();

    @GET
    public Response listar() { // Lista todas as áreas de risco
        logger.info("Requisição para listar todas as áreas de risco (mapas)...");
        try {
            List<Mapa> listaDeAreasDeRisco = mapaService.listarTodos();
            logger.info("Total de áreas de risco encontradas: " + listaDeAreasDeRisco.size());
            return Response.ok(listaDeAreasDeRisco).build();
        } catch (Exception e) {
            logger.error("Erro ao listar áreas de risco: " + e.getMessage(), e);
            return Response.serverError().entity("Ocorreu um erro ao buscar a lista de áreas de risco.").build();
        }
    }

    @POST
    public Response adicionar(Mapa areaDeRisco) { // Adiciona uma nova área de risco
        logger.info("Requisição para adicionar nova área de risco: " + (areaDeRisco != null ? areaDeRisco.getTitle() : "null"));
        try {
            mapaService.registrar(areaDeRisco);
            logger.info("Área de risco adicionada com sucesso. ID: " + areaDeRisco.getId());
            return Response.status(Response.Status.CREATED).entity(areaDeRisco).build();
        } catch (BadRequestException e) {
            logger.warn("Dados inválidos para adicionar área de risco: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.error("Erro ao adicionar área de risco: " + e.getMessage(), e);
            return Response.serverError().entity("Ocorreu um erro ao tentar adicionar a área de risco.").build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") int id) { // Busca uma área de risco específica pelo ID
        logger.info("Requisição para buscar área de risco com ID: " + id);
        try {
            Mapa areaDeRisco = mapaService.buscarPorId(id);
            logger.info("Área de risco encontrada: " + areaDeRisco.getTitle());
            return Response.ok(areaDeRisco).build();
        } catch (NotFoundException e) {
            logger.warn("Área de risco com ID " + id + " não encontrada: " + e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (BadRequestException e) {
            logger.warn("ID inválido para busca de área de risco: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        catch (Exception e) {
            logger.error("Erro ao buscar área de risco por ID " + id + ": " + e.getMessage(), e);
            return Response.serverError().entity("Ocorreu um erro ao buscar a área de risco.").build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") int id, Mapa areaDeRisco) { // Atualiza uma área de risco
        logger.info("Requisição para atualizar área de risco com ID: " + id);
        if (areaDeRisco == null) {
            logger.warn("Tentativa de atualização com dados nulos para a área de risco ID: " + id);
            return Response.status(Response.Status.BAD_REQUEST).entity("Dados da área de risco não podem ser nulos.").build();
        }
        try {
            mapaService.atualizar(id, areaDeRisco);
            logger.info("Área de risco com ID " + id + " atualizada com sucesso.");
            return Response.ok(areaDeRisco).build();
        } catch (NotFoundException e) {
            logger.warn("Área de risco com ID " + id + " não encontrada para atualização: " + e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (BadRequestException e) {
            logger.warn("Dados inválidos para atualização da área de risco ID " + id + ": " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.error("Erro ao atualizar área de risco ID " + id + ": " + e.getMessage(), e);
            return Response.serverError().entity("Ocorreu um erro ao tentar atualizar a área de risco.").build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response remover(@PathParam("id") int id) { // Remove uma área de risco
        logger.info("Requisição para remover área de risco com ID: " + id);
        try {
            mapaService.deletar(id);
            logger.info("Área de risco com ID " + id + " removida com sucesso.");
            return Response.ok().entity("Área de risco com ID " + id + " removida com sucesso.").build();
        } catch (NotFoundException e) {
            logger.warn("Área de risco com ID " + id + " não encontrada para remoção: " + e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (BadRequestException e) {
            logger.warn("ID inválido para remoção de área de risco: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        catch (Exception e) {
            logger.error("Erro ao remover área de risco ID " + id + ": " + e.getMessage(), e);
            return Response.serverError().entity("Ocorreu um erro ao tentar remover a área de risco.").build();
        }
    }
}
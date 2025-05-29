// src/app/reportes/ReporteResource.java
package fiap.tds.controllers;

import fiap.tds.dtos.ReporteComImagemDTO;
import fiap.tds.entities.Reporte;
import fiap.tds.exceptions.BadRequestException;
import fiap.tds.exceptions.NotFoundException;
import fiap.tds.services.ReporteService;

import jakarta.inject.Inject; // Para Injeção de Dependência do Serviço.
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.util.List;

@Path("/reportes") // Define o caminho base para os endpoints de reporte.
@Produces(MediaType.APPLICATION_JSON)
public class ReporteResource {

    private static final Logger logger = Logger.getLogger(ReporteResource.class);

    @Inject // Injeta a instância do ReporteService gerenciada pelo CDI.
    ReporteService reporteService;

    @POST // Endpoint para criar novos reportes.
    @Consumes(MediaType.MULTIPART_FORM_DATA) // Este método consome dados de formulário com upload de arquivo.
    public Response adicionar(@BeanParam ReporteComImagemDTO reporteDTO) { // @BeanParam para mapear dados do formulário para o DTO.
        logger.info("Requisição para adicionar novo reporte via DTO...");
        try {
            if (reporteDTO == null) {
                throw new BadRequestException("Dados do formulário (DTO) não podem ser nulos.");
            }

            // Chama o serviço para registrar o reporte, que lida com a lógica de negócio e persistência.
            Reporte reportePersistido = reporteService.registrar(reporteDTO);

            logger.info("Reporte adicionado com sucesso. ID: " + reportePersistido.getId());
            return Response.status(Response.Status.CREATED).entity(reportePersistido).build();

        } catch (BadRequestException e) {
            logger.warn("Dados inválidos para adicionar reporte: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.error("Erro crítico ao adicionar reporte: " + e.getMessage(), e);
            return Response.serverError().entity("Ocorreu um erro inesperado ao tentar adicionar o reporte.").build();
        }
    }

    @GET // Endpoint para listar todos os reportes.
    public Response listar() {
        logger.info("Requisição para listar todos os reportes...");
        try {
            List<Reporte> listaDeReportes = reporteService.listarTodos();
            return Response.ok(listaDeReportes).build();
        } catch (Exception e) {
            logger.error("Erro ao listar reportes: " + e.getMessage(), e);
            return Response.serverError().entity("Erro ao listar reportes.").build();
        }
    }

    @GET // Endpoint para buscar um reporte específico pelo ID.
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") int id) {
        logger.info("Requisição para buscar reporte com ID: " + id);
        try {
            Reporte reporte = reporteService.buscarPorId(id);
            return Response.ok(reporte).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (BadRequestException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.error("Erro ao buscar reporte por ID " + id + ": " + e.getMessage(), e);
            return Response.serverError().entity("Erro ao buscar reporte.").build();
        }
    }

    @PUT // Endpoint para atualizar um reporte existente.
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response atualizar(@PathParam("id") int id, Reporte reporteParaAtualizar) {
        logger.info("Requisição para atualizar reporte com ID: " + id);
        try {
            Reporte reporteAtualizado = reporteService.atualizar(id, reporteParaAtualizar);
            return Response.ok(reporteAtualizado).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (BadRequestException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.error("Erro ao atualizar reporte ID " + id + ": " + e.getMessage(), e);
            return Response.serverError().entity("Erro ao atualizar reporte.").build();
        }
    }

    @DELETE // Endpoint para remover um reporte.
    @Path("/{id}")
    public Response remover(@PathParam("id") int id) {
        logger.info("Requisição para remover reporte com ID: " + id);
        try {
            reporteService.deletar(id);
            return Response.ok().entity("Reporte com ID " + id + " removido com sucesso.").build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (BadRequestException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.error("Erro ao remover reporte ID " + id + ": " + e.getMessage(), e);
            return Response.serverError().entity("Erro ao remover reporte.").build();
        }
    }
}
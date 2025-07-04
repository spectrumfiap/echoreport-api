package fiap.tds.controllers;

import fiap.tds.dtos.ReporteComImagemDTO;
import fiap.tds.dtos.StatusUpdateRequestDTO;
import fiap.tds.entities.Reporte;
import fiap.tds.exceptions.BadRequestException;
import fiap.tds.exceptions.NotFoundException;
import fiap.tds.services.ReporteService;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Map;

@Path("/reportes")
@Produces(MediaType.APPLICATION_JSON)
public class ReporteResource {

    private static final Logger logger = Logger.getLogger(ReporteResource.class);

    @Inject
    ReporteService reporteService;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response adicionar(@BeanParam ReporteComImagemDTO reporteDTO) {
        logger.info("Requisição para adicionar novo reporte via DTO...");
        try {
            if (reporteDTO == null) {
                throw new BadRequestException("Dados do formulário (DTO) não podem ser nulos.");
            }
            Reporte reportePersistido = reporteService.registrar(reporteDTO);
            logger.info("Reporte adicionado com sucesso. ID: " + reportePersistido.getId());
            return Response.status(Response.Status.CREATED).entity(reportePersistido).build();
        } catch (BadRequestException e) {
            logger.warn("Dados inválidos para adicionar reporte: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", e.getMessage())).build();
        } catch (Exception e) {
            logger.error("Erro crítico ao adicionar reporte: " + e.getMessage(), e);
            return Response.serverError().entity(Map.of("error", "Ocorreu um erro inesperado ao tentar adicionar o reporte.")).build();
        }
    }

    @GET
    public Response listar() {
        logger.info("Requisição para listar todos os reportes...");
        try {
            List<Reporte> listaDeReportes = reporteService.listarTodos();
            return Response.ok(listaDeReportes).build();
        } catch (Exception e) {
            logger.error("Erro ao listar reportes: " + e.getMessage(), e);
            return Response.serverError().entity(Map.of("error", "Erro ao listar reportes.")).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") int id) {
        logger.info("Requisição para buscar reporte com ID: " + id);
        try {
            Reporte reporte = reporteService.buscarPorId(id);
            return Response.ok(reporte).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", e.getMessage())).build();
        } catch (BadRequestException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", e.getMessage())).build();
        } catch (Exception e) {
            logger.error("Erro ao buscar reporte por ID " + id + ": " + e.getMessage(), e);
            return Response.serverError().entity(Map.of("error", "Erro ao buscar reporte.")).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response atualizar(@PathParam("id") int id, Reporte reporteParaAtualizar) {
        logger.info("Requisição para atualizar reporte com ID: " + id);
        try {
            Reporte reporteAtualizado = reporteService.atualizar(id, reporteParaAtualizar);
            return Response.ok(reporteAtualizado).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", e.getMessage())).build();
        } catch (BadRequestException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", e.getMessage())).build();
        } catch (Exception e) {
            logger.error("Erro ao atualizar reporte ID " + id + ": " + e.getMessage(), e);
            return Response.serverError().entity(Map.of("error", "Erro ao atualizar reporte.")).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response remover(@PathParam("id") int id) {
        logger.info("Requisição para remover reporte com ID: " + id);
        try {
            reporteService.deletar(id);
            return Response.ok(Map.of("message", "Reporte com ID " + id + " removido com sucesso.")).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", e.getMessage())).build();
        } catch (BadRequestException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", e.getMessage())).build();
        } catch (Exception e) {
            logger.error("Erro ao remover reporte ID " + id + ": " + e.getMessage(), e);
            return Response.serverError().entity(Map.of("error", "Erro ao remover reporte.")).build();
        }
    }

    @PATCH
    @Path("/{id}/status")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response atualizarStatus(
            @PathParam("id") int id,
            StatusUpdateRequestDTO statusUpdateRequest) {

        logger.info("Requisição para atualizar status do reporte ID: " + id + " para: " + statusUpdateRequest.getStatus());
        try {
            if (statusUpdateRequest == null || statusUpdateRequest.getStatus() == null || statusUpdateRequest.getStatus().trim().isEmpty()) {
                throw new BadRequestException("O novo status não pode ser nulo ou vazio.");
            }
            Reporte reporteAtualizado = reporteService.atualizarStatusDoReporte(id, statusUpdateRequest.getStatus());
            logger.info("Status do reporte ID " + id + " atualizado com sucesso para " + reporteAtualizado.getStatus());
            return Response.ok(reporteAtualizado).build();
        } catch (NotFoundException e) {
            logger.warn("Reporte ID " + id + " não encontrado para atualização de status: " + e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", e.getMessage())).build();
        } catch (BadRequestException e) {
            logger.warn("Dados inválidos para atualizar status do reporte ID " + id + ": " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", e.getMessage())).build();
        } catch (Exception e) {
            logger.error("Erro crítico ao atualizar status do reporte ID " + id + ": " + e.getMessage(), e);
            return Response.serverError().entity(Map.of("error", "Ocorreu um erro inesperado ao tentar atualizar o status do reporte.")).build();
        }
    }
}
package fiap.tds.services;

import fiap.tds.dtos.ReporteComImagemDTO;
import fiap.tds.entities.Reporte;
import fiap.tds.repositories.ReporteRepository;
import fiap.tds.exceptions.BadRequestException;
import fiap.tds.exceptions.NotFoundException;
import fiap.tds.utils.FileUploadUtil;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class ReporteService {

    @Inject
    FileUploadUtil fileUploadUtil;

    private final ReporteRepository repository = new ReporteRepository();

    public Reporte registrar(ReporteComImagemDTO reporteDTO) {
        if (reporteDTO == null) {
            throw new BadRequestException("Dados do formulário de reporte não podem ser nulos.");
        }
        if (reporteDTO.getEventType() == null || reporteDTO.getEventType().trim().isEmpty()) {
            throw new BadRequestException("Tipo do evento é obrigatório.");
        }
        if (reporteDTO.getDescription() == null || reporteDTO.getDescription().trim().isEmpty()) {
            throw new BadRequestException("Descrição do reporte é obrigatória.");
        }
        if (reporteDTO.getLocation() == null || reporteDTO.getLocation().trim().isEmpty()) {
            throw new BadRequestException("Localização do reporte é obrigatória.");
        }

        Reporte novoReporte = new Reporte();
        novoReporte.setReporterName(reporteDTO.getReporterName() != null && !reporteDTO.getReporterName().trim().isEmpty() ? reporteDTO.getReporterName() : "Anônimo");
        novoReporte.setEventType(reporteDTO.getEventType());
        novoReporte.setDescription(reporteDTO.getDescription());
        novoReporte.setLocation(reporteDTO.getLocation());
        novoReporte.setUserId(reporteDTO.getUserId());
        novoReporte.setCreatedAt(LocalDateTime.now());

        novoReporte.setStatus("novo");
        novoReporte.setSeverity("nao_definida");
        novoReporte.setAdminNotes("");

        if (reporteDTO.getImageFile() != null && reporteDTO.getImageFile().size() > 0) {
            try {
                String imageUrl = fileUploadUtil.salvarImagem(reporteDTO.getImageFile());
                novoReporte.setImageUrl(imageUrl);
            } catch (IOException e) {
                System.err.println("Falha ao salvar imagem durante o registro do reporte: " + e.getMessage());
                throw new BadRequestException("Erro ao processar o upload da imagem: " + e.getMessage());
            }
        }

        repository.registrar(novoReporte);
        return novoReporte;
    }

    public List<Reporte> listarTodos() {
        return repository.buscarTodos();
    }

    public Reporte buscarPorId(int id) {
        if (id <= 0) {
            throw new BadRequestException("ID do reporte deve ser um número positivo.");
        }
        Reporte reporte = repository.buscarPorId(id);
        if (reporte == null) {
            throw new NotFoundException("Reporte com ID " + id + " não encontrado.");
        }
        return reporte;
    }

    public Reporte atualizar(int id, Reporte reporteComNovosDados) {
        if (id <= 0 || reporteComNovosDados == null) {
            throw new BadRequestException("ID e dados do reporte para atualização são obrigatórios.");
        }

        if (reporteComNovosDados.getEventType() == null || reporteComNovosDados.getEventType().trim().isEmpty() ||
                reporteComNovosDados.getDescription() == null || reporteComNovosDados.getDescription().trim().isEmpty() ||
                reporteComNovosDados.getLocation() == null || reporteComNovosDados.getLocation().trim().isEmpty()) {
            throw new BadRequestException("Tipo, descrição e localização são obrigatórios para atualização.");
        }
        if (reporteComNovosDados.getStatus() == null || reporteComNovosDados.getStatus().trim().isEmpty()){
            throw new BadRequestException("Status do reporte é obrigatório para atualização.");
        }
        if (reporteComNovosDados.getSeverity() == null || reporteComNovosDados.getSeverity().trim().isEmpty()){
            throw new BadRequestException("Severidade do reporte é obrigatória para atualização.");
        }


        Reporte existente = buscarPorId(id);

        existente.setEventType(reporteComNovosDados.getEventType());
        existente.setDescription(reporteComNovosDados.getDescription());
        existente.setLocation(reporteComNovosDados.getLocation());
        existente.setStatus(reporteComNovosDados.getStatus());
        existente.setSeverity(reporteComNovosDados.getSeverity());

        if (reporteComNovosDados.getAdminNotes() != null) {
            existente.setAdminNotes(reporteComNovosDados.getAdminNotes());
        } else {
            existente.setAdminNotes("");
        }

        if (reporteComNovosDados.getReporterName() != null) {
            existente.setReporterName(reporteComNovosDados.getReporterName());
        }

        if (reporteComNovosDados.getImageUrl() != null) {
            existente.setImageUrl(reporteComNovosDados.getImageUrl());
        }

        repository.atualizar(existente);
        return existente;
    }

    public void deletar(int id) {
        if (id <= 0) {
            throw new BadRequestException("ID do reporte para exclusão deve ser um número positivo.");
        }
        Reporte existente = buscarPorId(id);

        if (existente.getImageUrl() != null && !existente.getImageUrl().trim().isEmpty()) {
            try {
                fileUploadUtil.deletarImagem(existente.getImageUrl());
            } catch (Exception e) {
                System.err.println("Falha ao tentar deletar arquivo de imagem associado ao reporte " + id + ": " + e.getMessage());
            }
        }
        repository.deletar(id);
    }
}
package fiap.tds.services;

import fiap.tds.dtos.ReporteComImagemDTO;
import fiap.tds.entities.Reporte;
import fiap.tds.repositories.ReporteRepository;
import fiap.tds.exceptions.BadRequestException;
import fiap.tds.exceptions.NotFoundException;
import fiap.tds.utils.FileUploadUtil;

import jakarta.enterprise.context.ApplicationScoped; // Define o escopo do bean CDI.
import jakarta.inject.Inject; // Para injeção de dependência.

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped // Torna esta classe um serviço gerenciado pelo CDI.
public class ReporteService {

    @Inject // Injeta a instância do FileUploadUtil.
    FileUploadUtil fileUploadUtil;

    private final ReporteRepository repository = new ReporteRepository(); // Instância do repositório para acesso ao banco.

    /**
     * Valida dados do DTO, processa upload de imagem (se houver) e registra um novo reporte.
     */
    public Reporte registrar(ReporteComImagemDTO reporteDTO) {
        if (reporteDTO == null) {
            throw new BadRequestException("Dados do formulário de reporte não podem ser nulos.");
        }
        // Validações de campos obrigatórios do DTO.
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
        // Mapeia dados do DTO para a entidade Reporte.
        novoReporte.setReporterName(reporteDTO.getReporterName() != null && !reporteDTO.getReporterName().trim().isEmpty() ? reporteDTO.getReporterName() : "Anônimo");
        novoReporte.setEventType(reporteDTO.getEventType());
        novoReporte.setDescription(reporteDTO.getDescription());
        novoReporte.setLocation(reporteDTO.getLocation());
        novoReporte.setUserId(reporteDTO.getUserId());
        novoReporte.setCreatedAt(LocalDateTime.now()); // Define a data de criação.

        // Processa e salva a imagem, se uma foi fornecida no DTO.
        if (reporteDTO.getImageFile() != null && reporteDTO.getImageFile().size() > 0) {
            try {
                String imageUrl = fileUploadUtil.salvarImagem(reporteDTO.getImageFile());
                novoReporte.setImageUrl(imageUrl);
            } catch (IOException e) {
                System.err.println("Falha ao salvar imagem durante o registro do reporte: " + e.getMessage());
                throw new BadRequestException("Erro ao processar o upload da imagem: " + e.getMessage());
            }
        }

        repository.registrar(novoReporte); // Persiste a entidade; o ID é preenchido pelo repositório.
        return novoReporte; // Retorna a entidade Reporte com o ID.
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

    /**
     * Atualiza um reporte existente. A atualização da imagem é tratada separadamente ou
     * o DTO de atualização precisaria incluir o campo de arquivo.
     */
    public Reporte atualizar(int id, Reporte reporteComNovosDados) {
        if (id <= 0 || reporteComNovosDados == null) {
            throw new BadRequestException("ID e dados do reporte para atualização são obrigatórios.");
        }
        // Validações dos campos atualizáveis.
        if (reporteComNovosDados.getEventType() == null || reporteComNovosDados.getEventType().trim().isEmpty() ||
                reporteComNovosDados.getDescription() == null || reporteComNovosDados.getDescription().trim().isEmpty() ||
                reporteComNovosDados.getLocation() == null || reporteComNovosDados.getLocation().trim().isEmpty()) {
            throw new BadRequestException("Tipo, descrição e localização são obrigatórios para atualização.");
        }

        Reporte existente = buscarPorId(id); // Busca o reporte existente para garantir que ele existe.

        // Atualiza os campos permitidos da entidade 'existente'.
        existente.setEventType(reporteComNovosDados.getEventType());
        existente.setDescription(reporteComNovosDados.getDescription());
        existente.setLocation(reporteComNovosDados.getLocation());

        if (reporteComNovosDados.getImageUrl() != null) {
            existente.setImageUrl(reporteComNovosDados.getImageUrl());
        }
        // Considerar adicionar um campo 'updatedAt' e atualizá-lo aqui.

        repository.atualizar(existente);
        return existente;
    }

    public void deletar(int id) {
        if (id <= 0) {
            throw new BadRequestException("ID do reporte para exclusão deve ser um número positivo.");
        }
        Reporte existente = buscarPorId(id); // Verifica se o reporte existe antes de deletar.

        // Lógica para deletar o arquivo de imagem associado, se existir.
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
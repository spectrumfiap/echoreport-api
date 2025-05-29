package fiap.tds.services;

import fiap.tds.entities.Alerta;
import fiap.tds.repositories.AlertaRepository;
import fiap.tds.exceptions.BadRequestException;
import fiap.tds.exceptions.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public class AlertaService {

    private final AlertaRepository repository = new AlertaRepository();

    /**
     * Registra um novo alerta.
     * @param alerta O objeto Alerta a ser registrado. O ID deve ser 0 se for autoincrementado.
     * O ID será preenchido pelo método registrar do repositório.
     */
    public void registrar(Alerta alerta) {
        if (alerta == null) {
            throw new BadRequestException("Dados do alerta não podem ser nulos.");
        }
        if (alerta.getTitle() == null || alerta.getTitle().trim().isEmpty()) {
            throw new BadRequestException("Título do alerta é obrigatório.");
        }
        if (alerta.getDescription() == null || alerta.getDescription().trim().isEmpty()) {
            throw new BadRequestException("Descrição do alerta é obrigatória.");
        }
        if (alerta.getSeverity() == null || alerta.getSeverity().trim().isEmpty()) {
            throw new BadRequestException("Nível de severidade do alerta é obrigatório.");
        }
        // Validação para os valores permitidos de severidade (exemplo)
        String severity = alerta.getSeverity().toLowerCase();
        if (!severity.equals("alto") && !severity.equals("médio") && !severity.equals("baixo") && !severity.equals("informativo")) {
            throw new BadRequestException("Nível de severidade inválido. Use 'alto', 'médio', 'baixo' ou 'informativo'.");
        }
        if (alerta.getSource() == null || alerta.getSource().trim().isEmpty()) {
            throw new BadRequestException("Fonte do alerta é obrigatória.");
        }

        // Define a data de publicação para agora se não for fornecida
        if (alerta.getPublishedAt() == null) {
            alerta.setPublishedAt(LocalDateTime.now());
        }

        repository.registrar(alerta);
    }

    /**
     * Retorna todos os alertas cadastrados.
     * @return Lista de Alertas.
     */
    public List<Alerta> listarTodos() {
        return repository.buscarTodos();
    }

    /**
     * Busca um alerta pelo seu ID.
     * @param id O ID do alerta a ser buscado.
     * @return O objeto Alerta encontrado.
     * @throws NotFoundException se nenhum alerta com o ID fornecido for encontrado.
     */
    public Alerta buscarPorId(int id) {
        if (id <= 0) {
            throw new BadRequestException("ID do alerta deve ser um número positivo.");
        }
        Alerta alerta = repository.buscarPorId(id);
        if (alerta == null) {
            throw new NotFoundException("Alerta com ID " + id + " não encontrado.");
        }
        return alerta;
    }

    /**
     * Atualiza um alerta existente.
     * @param id O ID do alerta a ser atualizado.
     * @param alerta O objeto Alerta com os novos dados.
     * @throws NotFoundException se nenhum alerta com o ID fornecido for encontrado.
     * @throws BadRequestException se os dados fornecidos para atualização forem inválidos.
     */
    public void atualizar(int id, Alerta alerta) {
        if (id <= 0) {
            throw new BadRequestException("ID do alerta para atualização deve ser um número positivo.");
        }
        if (alerta == null) {
            throw new BadRequestException("Dados do alerta para atualização não podem ser nulos.");
        }
        // Validações dos campos obrigatórios para atualização
        if (alerta.getTitle() == null || alerta.getTitle().trim().isEmpty()) {
            throw new BadRequestException("Título do alerta é obrigatório para atualização.");
        }
        if (alerta.getDescription() == null || alerta.getDescription().trim().isEmpty()) {
            throw new BadRequestException("Descrição do alerta é obrigatória para atualização.");
        }
        if (alerta.getSeverity() == null || alerta.getSeverity().trim().isEmpty()) {
            throw new BadRequestException("Nível de severidade é obrigatório para atualização.");
        }
        String severity = alerta.getSeverity().toLowerCase();
        if (!severity.equals("alto") && !severity.equals("medio") && !severity.equals("baixo") && !severity.equals("informativo")) {
            throw new BadRequestException("Nível de severidade inválido para atualização. Use 'alto', 'medio', 'baixo' ou 'informativo'.");
        }
        if (alerta.getSource() == null || alerta.getSource().trim().isEmpty()) {
            throw new BadRequestException("Fonte do alerta é obrigatória para atualização.");
        }

        Alerta existente = repository.buscarPorId(id);
        if (existente == null) {
            throw new NotFoundException("Alerta com ID " + id + " não encontrado para atualização.");
        }

        alerta.setId(id); // Garante que o ID correto está sendo usado para nossa atualização.
        if (alerta.getPublishedAt() == null) {
            alerta.setPublishedAt(existente.getPublishedAt()); // Mantém o original se não fornecido
        }


        repository.atualizar(alerta);
    }

    /**
     * Deleta um alerta pelo seu ID.
     * @param id O ID do alerta a ser deletado.
     * @throws NotFoundException se nenhum alerta com o ID fornecido for encontrado.
     */
    public void deletar(int id) {
        if (id <= 0) {
            throw new BadRequestException("ID do alerta para exclusão deve ser um número positivo.");
        }
        Alerta existente = repository.buscarPorId(id); // Verifica se existe, já lança logo ali um NotFoundException se não.
        if (existente == null) {
            // Redundante se buscarPorId já lança, mas para clareza:
            throw new NotFoundException("Alerta com ID " + id + " não encontrado para exclusão.");
        }

        repository.deletar(id);
    }
}
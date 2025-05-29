package fiap.tds.services;

import fiap.tds.entities.Mapa;
import fiap.tds.repositories.MapaRepository;
import fiap.tds.exceptions.BadRequestException;
import fiap.tds.exceptions.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public class MapaService {

    private final MapaRepository repository = new MapaRepository(); // Instancia o repositório

    /**
     * Valida e registra uma nova área de risco (Mapa).
     */
    public void registrar(Mapa mapa) {
        if (mapa == null) {
            throw new BadRequestException("Dados da área de risco (mapa) não podem ser nulos.");
        }
        if (mapa.getTitle() == null || mapa.getTitle().trim().isEmpty()) {
            throw new BadRequestException("Título da área de risco é obrigatório.");
        }
        if (mapa.getDescription() == null || mapa.getDescription().trim().isEmpty()) {
            throw new BadRequestException("Descrição da área de risco é obrigatória.");
        }
        if (mapa.getLatitude() == 0 && mapa.getLongitude() == 0) { // Validação simples de coordenadas
            throw new BadRequestException("Coordenadas (latitude e longitude) são obrigatórias.");
        }
        if (mapa.getRadius() <= 0) {
            throw new BadRequestException("Raio da área de risco deve ser positivo.");
        }
        if (mapa.getRiskLevel() == null || mapa.getRiskLevel().trim().isEmpty()) {
            throw new BadRequestException("Nível de risco é obrigatório.");
        }
        // Define o timestamp de atualização/criação se não estiver definido
        if (mapa.getLastUpdatedTimestamp() == null) {
            mapa.setLastUpdatedTimestamp(LocalDateTime.now());
        }

        repository.registrar(mapa);
    }

    /**
     * Retorna todas as áreas de risco (Mapas) cadastradas.
     * @return Lista de Mapas.
     */
    public List<Mapa> listarTodos() {
        return repository.buscarTodos();
    }

    /**
     * Busca uma área de risco (Mapa) pelo seu ID.
     * @param id O ID da área de risco a ser buscada.
     * @return O objeto Mapa encontrado.
     * @throws NotFoundException se nenhuma área de risco com o ID fornecido for encontrada.
     */
    public Mapa buscarPorId(int id) {
        if (id <= 0) {
            throw new BadRequestException("ID da área de risco deve ser um número positivo.");
        }
        Mapa mapa = repository.buscarPorId(id);
        if (mapa == null) {
            throw new NotFoundException("Área de Risco (Mapa) com ID " + id + " não encontrada.");
        }
        return mapa;
    }

    /**
     * Atualiza uma área de risco (Mapa) existente.
     * @param id O ID da área de risco a ser atualizada.
     * @param mapa O objeto Mapa com os novos dados.
     * @throws NotFoundException se nenhuma área de risco com o ID fornecido for encontrada.
     * @throws BadRequestException se os dados fornecidos para atualização forem inválidos.
     */
    public void atualizar(int id, Mapa mapa) {
        if (id <= 0) {
            throw new BadRequestException("ID da área de risco para atualização deve ser um número positivo.");
        }
        if (mapa == null) {
            throw new BadRequestException("Dados da área de risco (mapa) para atualização não podem ser nulos.");
        }
        // Validações dos campos obrigatórios para atualização (podem ser as mesmas do registro)
        if (mapa.getTitle() == null || mapa.getTitle().trim().isEmpty()) {
            throw new BadRequestException("Título da área de risco é obrigatório para atualização.");
        }
        if (mapa.getDescription() == null || mapa.getDescription().trim().isEmpty()) {
            throw new BadRequestException("Descrição da área de risco é obrigatória para atualização.");
        }
        if (mapa.getLatitude() == 0 && mapa.getLongitude() == 0) {
            throw new BadRequestException("Coordenadas (latitude e longitude) são obrigatórias para atualização.");
        }
        if (mapa.getRadius() <= 0) {
            throw new BadRequestException("Raio da área de risco deve ser positivo para atualização.");
        }
        if (mapa.getRiskLevel() == null || mapa.getRiskLevel().trim().isEmpty()) {
            throw new BadRequestException("Nível de risco é obrigatório para atualização.");
        }

        // Verifica se a área de risco existe antes de tentar atualizar
        Mapa existente = repository.buscarPorId(id);
        if (existente == null) {
            throw new NotFoundException("Área de Risco (Mapa) com ID " + id + " não encontrada para atualização.");
        }

        // Define o ID no objeto 'mapa' para garantir que o ID correto seja usado na query UPDATE
        mapa.setId(id);
        // Atualiza o timestamp
        mapa.setLastUpdatedTimestamp(LocalDateTime.now());

        repository.atualizar(mapa); // O repositório usa o ID do objeto mapa para o WHERE
    }

    /**
     * Deleta uma área de risco (Mapa) pelo seu ID.
     * @param id O ID da área de risco a ser deletada.
     * @throws NotFoundException se nenhuma área de risco com o ID fornecido for encontrada.
     */
    public void deletar(int id) {
        if (id <= 0) {
            throw new BadRequestException("ID da área de risco para exclusão deve ser um número positivo.");
        }
        // Verifica se a área de risco existe antes de tentar deletar
        Mapa existente = repository.buscarPorId(id);
        if (existente == null) {
        }

        repository.deletar(id);
    }
}
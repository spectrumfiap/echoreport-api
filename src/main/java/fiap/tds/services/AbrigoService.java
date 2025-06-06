package fiap.tds.services;

import fiap.tds.entities.Abrigo;
import fiap.tds.repositories.AbrigoRepository;
import fiap.tds.exceptions.BadRequestException;
import fiap.tds.exceptions.NotFoundException;

import java.util.List;

public class AbrigoService {

    private final AbrigoRepository repository = new AbrigoRepository(); // Instancia o repositório

    /**
     * Registra um novo abrigo.
     * @param abrigo O objeto Abrigo a ser registrado.
     * O ID será preenchido pelo método registrar do repositório.
     */
    public void registrar(Abrigo abrigo) {
        // Validações básicas
        if (abrigo == null) {
            throw new BadRequestException("Dados do abrigo não podem ser nulos.");
        }
        if (abrigo.getName() == null || abrigo.getName().trim().isEmpty()) {
            throw new BadRequestException("Nome do abrigo é obrigatório.");
        }
        if (abrigo.getAddress() == null || abrigo.getAddress().trim().isEmpty()) {
            throw new BadRequestException("Endereço do abrigo é obrigatório.");
        }
        if (abrigo.getNeighborhood() == null || abrigo.getNeighborhood().trim().isEmpty()) {
            throw new BadRequestException("Bairro do abrigo é obrigatório.");
        }
        if (abrigo.getCityState() == null || abrigo.getCityState().trim().isEmpty()) {
            throw new BadRequestException("Cidade/Estado do abrigo é obrigatório.");
        }
        if (abrigo.getCapacityStatus() == null || abrigo.getCapacityStatus().trim().isEmpty()) {
            throw new BadRequestException("Status da capacidade do abrigo é obrigatório.");
        }
        if (abrigo.getServicesOffered() == null || abrigo.getServicesOffered().length == 0) {
            throw new BadRequestException("Ao menos um serviço oferecido deve ser informado.");
        }
        if (abrigo.getTargetAudience() == null || abrigo.getTargetAudience().trim().isEmpty()) {
            throw new BadRequestException("Público alvo do abrigo é obrigatório.");
        }
        if (abrigo.getOperatingHours() == null || abrigo.getOperatingHours().trim().isEmpty()) {
            throw new BadRequestException("Horário de funcionamento do abrigo é obrigatório.");
        }

        repository.registrar(abrigo);
    }

    /**
     * Retorna todos os abrigos cadastrados.
     * @return Lista de Abrigos.
     */
    public List<Abrigo> listarTodos() {
        return repository.buscarTodos();
    }

    /**
     * Busca um abrigo pelo seu ID.
     * @param id O ID do abrigo a ser buscado.
     * @return O objeto Abrigo encontrado.
     * @throws NotFoundException se nenhum abrigo com o ID fornecido for encontrado.
     */
    public Abrigo buscarPorId(int id) {
        if (id <= 0) {
            throw new BadRequestException("ID do abrigo deve ser um número positivo.");
        }
        Abrigo abrigo = repository.buscarPorId(id);
        if (abrigo == null) {
            throw new NotFoundException("Abrigo com ID " + id + " não encontrado.");
        }
        return abrigo;
    }

    /**
     * Atualiza um abrigo existente.
     * @param id O ID do abrigo a ser atualizado.
     * @param abrigo O objeto Abrigo com os novos dados.
     * @throws NotFoundException se nenhum abrigo com o ID fornecido for encontrada.
     * @throws BadRequestException se os dados fornecidos para atualização forem inválidos.
     */
    public void atualizar(int id, Abrigo abrigo) {
        if (id <= 0) {
            throw new BadRequestException("ID do abrigo para atualização deve ser um número positivo.");
        }
        if (abrigo == null) {
            throw new BadRequestException("Dados do abrigo para atualização não podem ser nulos.");
        }
        // Validações dos campos obrigatórios para atualização
        if (abrigo.getName() == null || abrigo.getName().trim().isEmpty()) {
            throw new BadRequestException("Nome do abrigo é obrigatório para atualização.");
        }
        if (abrigo.getAddress() == null || abrigo.getAddress().trim().isEmpty()) {
            throw new BadRequestException("Endereço do abrigo é obrigatório para atualização.");
        }

        Abrigo existente = repository.buscarPorId(id);
        if (existente == null) {
            throw new NotFoundException("Abrigo com ID " + id + " não encontrado para atualização.");
        }

        // Garante que o ID do objeto a ser atualizado é o mesmo do parâmetro
        abrigo.setId(id);
        // abrigo.setUpdatedAt(LocalDateTime.now());

        repository.atualizar(abrigo);
    }

    /**
     * Deleta um abrigo pelo seu ID.
     * @param id O ID do abrigo a ser deletado.
     * @throws NotFoundException se nenhum abrigo com o ID fornecido for encontrado.
     */
    public void deletar(int id) {
        if (id <= 0) {
            throw new BadRequestException("ID do abrigo para exclusão deve ser um número positivo.");
        }
        // Verifica se o abrigo existe antes de tentar deletar
        Abrigo existente = repository.buscarPorId(id); // buscarPorId já lança NotFoundException se não encontrar
        if (existente == null) {
            throw new NotFoundException("Abrigo com ID " + id + " não encontrado para exclusão.");
        }

        repository.deletar(id);
    }
}
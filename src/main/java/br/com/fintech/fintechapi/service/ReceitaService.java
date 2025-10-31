package br.com.fintech.fintechapi.service;

import br.com.fintech.fintechapi.exception.AcessoNegadoException;
import br.com.fintech.fintechapi.exception.RecursoNaoEncontradoException;
import br.com.fintech.fintechapi.model.Categoria;
import br.com.fintech.fintechapi.model.Receita;
import br.com.fintech.fintechapi.model.Subcategoria;
import br.com.fintech.fintechapi.model.Usuario;
import br.com.fintech.fintechapi.repository.CategoriaRepository;
import br.com.fintech.fintechapi.repository.ReceitaRepository;
import br.com.fintech.fintechapi.repository.SubcategoriaRepository;
import br.com.fintech.fintechapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service para lógica de negócio de Receitas
 */
@Service
public class ReceitaService {

    @Autowired
    private ReceitaRepository receitaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private SubcategoriaRepository subcategoriaRepository;

    /**
     * Cria uma nova receita
     * @param receita Receita a ser criada
     * @param idUsuario ID do usuário dono da receita
     * @return Receita criada
     */
    @Transactional
    public Receita criar(Receita receita, Long idUsuario) {
        // Validações
        if (receita.getValor() == null || receita.getValor().signum() <= 0) {
            throw new IllegalArgumentException("Valor da receita deve ser maior que zero");
        }

        if (receita.getDataEntrada() == null) {
            throw new IllegalArgumentException("Data de entrada é obrigatória");
        }

        // Buscar o usuário
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com ID: " + idUsuario));

        // Associar usuário à receita
        receita.setUsuario(usuario);
        receita.setCriadoEm(LocalDateTime.now());

        // Garantir valores padrão
        if (receita.getRecorrente() == null) {
            receita.setRecorrente(0);
        }
        if (receita.getPendente() == null) {
            receita.setPendente(0);
        }

        // Validar e associar categoria (opcional)
        if (receita.getCategoria() != null && receita.getCategoria().getIdCategoria() != null) {
            Categoria categoria = categoriaRepository.findById(receita.getCategoria().getIdCategoria())
                    .orElseThrow(() -> new RecursoNaoEncontradoException(
                            "Categoria não encontrada com ID: " + receita.getCategoria().getIdCategoria()));
            receita.setCategoria(categoria);
        }

        // Validar e associar subcategoria (opcional)
        if (receita.getSubcategoria() != null && receita.getSubcategoria().getIdSubcategoria() != null) {
            Subcategoria subcategoria = subcategoriaRepository.findById(receita.getSubcategoria().getIdSubcategoria())
                    .orElseThrow(() -> new RecursoNaoEncontradoException(
                            "Subcategoria não encontrada com ID: " + receita.getSubcategoria().getIdSubcategoria()));
            receita.setSubcategoria(subcategoria);
        }

        return receitaRepository.save(receita);
    }

    /**
     * Busca uma receita por ID
     * @param id ID da receita
     * @param idUsuario ID do usuário autenticado
     * @return Receita encontrada
     */
    public Receita buscarPorId(Long id, Long idUsuario) {
        Receita receita = receitaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Receita não encontrada com ID: " + id));

        // Validar que o usuário é o dono da receita
        if (!receita.getUsuario().getIdUsuario().equals(idUsuario)) {
            throw new AcessoNegadoException("Você não tem permissão para acessar esta receita");
        }

        return receita;
    }

    /**
     * Lista todas as receitas de um usuário
     * @param idUsuario ID do usuário
     * @return Lista de receitas
     */
    public List<Receita> listarPorUsuario(Long idUsuario) {
        return receitaRepository.findByUsuarioIdUsuario(idUsuario);
    }

    /**
     * Lista receitas de um usuário em um período
     * @param idUsuario ID do usuário
     * @param dataInicio Data inicial
     * @param dataFim Data final
     * @return Lista de receitas no período
     */
    public List<Receita> listarPorPeriodo(Long idUsuario, LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio == null || dataFim == null) {
            throw new IllegalArgumentException("Data início e data fim são obrigatórias");
        }

        if (dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("Data início não pode ser maior que data fim");
        }

        return receitaRepository.findByUsuarioIdUsuarioAndDataEntradaBetween(idUsuario, dataInicio, dataFim);
    }

    /**
     * Lista receitas pendentes de um usuário
     * @param idUsuario ID do usuário
     * @return Lista de receitas pendentes
     */
    public List<Receita> listarPendentes(Long idUsuario) {
        return receitaRepository.findByUsuarioIdUsuarioAndPendente(idUsuario, 1);
    }

    /**
     * Atualiza uma receita existente
     * @param receita Receita com dados atualizados
     * @param idUsuario ID do usuário autenticado
     * @return Receita atualizada
     */
    @Transactional
    public Receita atualizar(Receita receita, Long idUsuario) {
        if (receita.getIdReceita() == null) {
            throw new IllegalArgumentException("ID da receita é obrigatório para atualização");
        }

        // Buscar receita existente e validar permissão
        Receita receitaExistente = buscarPorId(receita.getIdReceita(), idUsuario);

        // Validações
        if (receita.getValor() != null && receita.getValor().signum() <= 0) {
            throw new IllegalArgumentException("Valor da receita deve ser maior que zero");
        }

        // Atualizar campos
        if (receita.getDescricao() != null) {
            receitaExistente.setDescricao(receita.getDescricao());
        }
        if (receita.getValor() != null) {
            receitaExistente.setValor(receita.getValor());
        }
        if (receita.getDataEntrada() != null) {
            receitaExistente.setDataEntrada(receita.getDataEntrada());
        }
        if (receita.getRecorrente() != null) {
            receitaExistente.setRecorrente(receita.getRecorrente());
        }
        if (receita.getPendente() != null) {
            receitaExistente.setPendente(receita.getPendente());
        }

        // Atualizar categoria (opcional)
        if (receita.getCategoria() != null) {
            if (receita.getCategoria().getIdCategoria() != null) {
                Categoria categoria = categoriaRepository.findById(receita.getCategoria().getIdCategoria())
                        .orElseThrow(() -> new RecursoNaoEncontradoException(
                                "Categoria não encontrada com ID: " + receita.getCategoria().getIdCategoria()));
                receitaExistente.setCategoria(categoria);
            } else {
                receitaExistente.setCategoria(null);
            }
        }

        // Atualizar subcategoria (opcional)
        if (receita.getSubcategoria() != null) {
            if (receita.getSubcategoria().getIdSubcategoria() != null) {
                Subcategoria subcategoria = subcategoriaRepository.findById(receita.getSubcategoria().getIdSubcategoria())
                        .orElseThrow(() -> new RecursoNaoEncontradoException(
                                "Subcategoria não encontrada com ID: " + receita.getSubcategoria().getIdSubcategoria()));
                receitaExistente.setSubcategoria(subcategoria);
            } else {
                receitaExistente.setSubcategoria(null);
            }
        }

        // Não atualizar criadoEm e usuario

        return receitaRepository.save(receitaExistente);
    }

    /**
     * Deleta uma receita
     * @param id ID da receita
     * @param idUsuario ID do usuário autenticado
     */
    @Transactional
    public void deletar(Long id, Long idUsuario) {
        // Buscar e validar permissão
        Receita receita = buscarPorId(id, idUsuario);
        receitaRepository.delete(receita);
    }
}


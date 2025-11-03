package br.com.fintech.fintechapi.service;

import br.com.fintech.fintechapi.exception.AcessoNegadoException;
import br.com.fintech.fintechapi.exception.RecursoNaoEncontradoException;
import br.com.fintech.fintechapi.model.Categoria;
import br.com.fintech.fintechapi.model.Despesa;
import br.com.fintech.fintechapi.model.Subcategoria;
import br.com.fintech.fintechapi.model.Usuario;
import br.com.fintech.fintechapi.repository.CategoriaRepository;
import br.com.fintech.fintechapi.repository.DespesaRepository;
import br.com.fintech.fintechapi.repository.SubcategoriaRepository;
import br.com.fintech.fintechapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DespesaService {

    @Autowired
    private DespesaRepository despesaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private SubcategoriaRepository subcategoriaRepository;

    @Transactional
    public Despesa criar(Despesa despesa, Long idUsuario) {
        if (despesa.getValor() == null || despesa.getValor().signum() <= 0) {
            throw new IllegalArgumentException("Valor da despesa deve ser maior que zero");
        }

        if (despesa.getDataVencimento() == null) {
            throw new IllegalArgumentException("Data de vencimento é obrigatória");
        }

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com ID: " + idUsuario));

        despesa.setUsuario(usuario);
        despesa.setCriadoEm(LocalDateTime.now());

        if (despesa.getRecorrente() == null) {
            despesa.setRecorrente(0);
        }
        if (despesa.getPendente() == null) {
            despesa.setPendente(0);
        }

        if (despesa.getCategoria() != null && despesa.getCategoria().getIdCategoria() != null) {
            Categoria categoria = categoriaRepository.findById(despesa.getCategoria().getIdCategoria())
                    .orElseThrow(() -> new RecursoNaoEncontradoException(
                            "Categoria não encontrada com ID: " + despesa.getCategoria().getIdCategoria()));
            despesa.setCategoria(categoria);
        }

        if (despesa.getSubcategoria() != null && despesa.getSubcategoria().getIdSubcategoria() != null) {
            Subcategoria subcategoria = subcategoriaRepository.findById(despesa.getSubcategoria().getIdSubcategoria())
                    .orElseThrow(() -> new RecursoNaoEncontradoException(
                            "Subcategoria não encontrada com ID: " + despesa.getSubcategoria().getIdSubcategoria()));
            despesa.setSubcategoria(subcategoria);
        }

        return despesaRepository.save(despesa);
    }

    public Despesa buscarPorId(Long id, Long idUsuario) {
        Despesa despesa = despesaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Despesa não encontrada com ID: " + id));

        if (!despesa.getUsuario().getIdUsuario().equals(idUsuario)) {
            throw new AcessoNegadoException("Você não tem permissão para acessar esta despesa");
        }

        return despesa;
    }

    public List<Despesa> listarPorUsuario(Long idUsuario) {
        return despesaRepository.findByUsuarioIdUsuario(idUsuario);
    }

    public List<Despesa> listarPorPeriodo(Long idUsuario, LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio == null || dataFim == null) {
            throw new IllegalArgumentException("Data início e data fim são obrigatórias");
        }

        if (dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("Data início não pode ser maior que data fim");
        }

        return despesaRepository.findByUsuarioIdUsuarioAndDataVencimentoBetween(idUsuario, dataInicio, dataFim);
    }

    public List<Despesa> listarPendentes(Long idUsuario) {
        return despesaRepository.findByUsuarioIdUsuarioAndPendente(idUsuario, 1);
    }

    /**
     * Lista despesas com filtros opcionais
     * @param idUsuario ID do usuário
     * @param dataInicio Data inicial (opcional)
     * @param dataFim Data final (opcional)
     * @param idCategoria ID da categoria (opcional)
     * @param pendente Status pendente (opcional)
     * @return Lista de despesas filtradas
     */
    public List<Despesa> listarComFiltros(
            Long idUsuario,
            LocalDate dataInicio,
            LocalDate dataFim,
            Long idCategoria,
            Integer pendente) {
        
        // Validação de período (se ambos forem fornecidos)
        if (dataInicio != null && dataFim != null && dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("Data início não pode ser maior que data fim");
        }

        return despesaRepository.findByFiltros(idUsuario, dataInicio, dataFim, idCategoria, pendente);
    }

    /**
     * Lista despesas com filtros opcionais e paginação
     * @param idUsuario ID do usuário
     * @param dataInicio Data inicial (opcional)
     * @param dataFim Data final (opcional)
     * @param idCategoria ID da categoria (opcional)
     * @param pendente Status pendente (opcional)
     * @param page Número da página (0-based)
     * @param size Tamanho da página
     * @return Página de despesas filtradas
     */
    public Page<Despesa> listarComFiltrosEPaginacao(
            Long idUsuario,
            LocalDate dataInicio,
            LocalDate dataFim,
            Long idCategoria,
            Integer pendente,
            int page,
            int size) {
        
        // Validação de período (se ambos forem fornecidos)
        if (dataInicio != null && dataFim != null && dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("Data início não pode ser maior que data fim");
        }

        // Validação de paginação
        if (page < 0) {
            throw new IllegalArgumentException("Número da página não pode ser negativo");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Tamanho da página deve ser maior que zero");
        }
        if (size > 100) {
            throw new IllegalArgumentException("Tamanho da página não pode ser maior que 100");
        }

        // Criar Pageable com ordenação por dataVencimento DESC
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dataVencimento"));

        return despesaRepository.findByFiltrosComPaginacao(
            idUsuario, dataInicio, dataFim, idCategoria, pendente, pageable
        );
    }

    @Transactional
    public Despesa atualizar(Despesa despesa, Long idUsuario) {
        if (despesa.getIdDespesa() == null) {
            throw new IllegalArgumentException("ID da despesa é obrigatório para atualização");
        }

        Despesa despesaExistente = buscarPorId(despesa.getIdDespesa(), idUsuario);

        if (despesa.getValor() != null && despesa.getValor().signum() <= 0) {
            throw new IllegalArgumentException("Valor da despesa deve ser maior que zero");
        }

        if (despesa.getDescricao() != null) {
            despesaExistente.setDescricao(despesa.getDescricao());
        }
        if (despesa.getValor() != null) {
            despesaExistente.setValor(despesa.getValor());
        }
        if (despesa.getDataVencimento() != null) {
            despesaExistente.setDataVencimento(despesa.getDataVencimento());
        }
        if (despesa.getRecorrente() != null) {
            despesaExistente.setRecorrente(despesa.getRecorrente());
        }
        if (despesa.getPendente() != null) {
            despesaExistente.setPendente(despesa.getPendente());
        }

        if (despesa.getCategoria() != null) {
            if (despesa.getCategoria().getIdCategoria() != null) {
                Categoria categoria = categoriaRepository.findById(despesa.getCategoria().getIdCategoria())
                        .orElseThrow(() -> new RecursoNaoEncontradoException(
                                "Categoria não encontrada com ID: " + despesa.getCategoria().getIdCategoria()));
                despesaExistente.setCategoria(categoria);
            } else {
                despesaExistente.setCategoria(null);
            }
        }

        if (despesa.getSubcategoria() != null) {
            if (despesa.getSubcategoria().getIdSubcategoria() != null) {
                Subcategoria subcategoria = subcategoriaRepository.findById(despesa.getSubcategoria().getIdSubcategoria())
                        .orElseThrow(() -> new RecursoNaoEncontradoException(
                                "Subcategoria não encontrada com ID: " + despesa.getSubcategoria().getIdSubcategoria()));
                despesaExistente.setSubcategoria(subcategoria);
            } else {
                despesaExistente.setSubcategoria(null);
            }
        }

        return despesaRepository.save(despesaExistente);
    }

    @Transactional
    public void deletar(Long id, Long idUsuario) {
        Despesa despesa = buscarPorId(id, idUsuario);
        despesaRepository.delete(despesa);
    }
}


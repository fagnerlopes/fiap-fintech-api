package br.com.fintech.fintechapi.service;

import br.com.fintech.fintechapi.exception.DadosDuplicadosException;
import br.com.fintech.fintechapi.exception.RecursoNaoEncontradoException;
import br.com.fintech.fintechapi.model.Categoria;
import br.com.fintech.fintechapi.model.Subcategoria;
import br.com.fintech.fintechapi.repository.SubcategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SubcategoriaService {

    @Autowired
    private SubcategoriaRepository subcategoriaRepository;

    @Autowired
    private CategoriaService categoriaService;

    @Transactional
    public Subcategoria criar(Subcategoria subcategoria, Long idCategoria) {
        if (subcategoria.getNomeSubcat() == null || subcategoria.getNomeSubcat().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da subcategoria é obrigatório");
        }

        Categoria categoria = categoriaService.buscarPorId(idCategoria);

        if (subcategoriaRepository.existsByCategoriaIdCategoriaAndNomeSubcat(
                idCategoria, subcategoria.getNomeSubcat())) {
            throw new DadosDuplicadosException("Já existe uma subcategoria com este nome nesta categoria");
        }

        subcategoria.setCategoria(categoria);
        return subcategoriaRepository.save(subcategoria);
    }

    public Subcategoria buscarPorId(Long id) {
        return subcategoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Subcategoria não encontrada com ID: " + id));
    }

    public List<Subcategoria> listarTodas() {
        return subcategoriaRepository.findAll();
    }

    public List<Subcategoria> listarPorCategoria(Long idCategoria) {
        return subcategoriaRepository.findByCategoriaIdCategoria(idCategoria);
    }

    @Transactional
    public Subcategoria atualizar(Subcategoria subcategoria) {
        if (subcategoria.getIdSubcategoria() == null) {
            throw new IllegalArgumentException("ID da subcategoria é obrigatório para atualização");
        }

        Subcategoria subcategoriaExistente = buscarPorId(subcategoria.getIdSubcategoria());

        if (subcategoria.getNomeSubcat() != null && !subcategoria.getNomeSubcat().trim().isEmpty()) {
            Long idCategoria = subcategoriaExistente.getCategoria().getIdCategoria();
            
            if (!subcategoriaExistente.getNomeSubcat().equals(subcategoria.getNomeSubcat())) {
                if (subcategoriaRepository.existsByCategoriaIdCategoriaAndNomeSubcat(
                        idCategoria, subcategoria.getNomeSubcat())) {
                    throw new DadosDuplicadosException("Já existe uma subcategoria com este nome nesta categoria");
                }
            }
            subcategoriaExistente.setNomeSubcat(subcategoria.getNomeSubcat());
        }

        if (subcategoria.getCategoria() != null && subcategoria.getCategoria().getIdCategoria() != null) {
            Categoria categoria = categoriaService.buscarPorId(subcategoria.getCategoria().getIdCategoria());
            subcategoriaExistente.setCategoria(categoria);
        }

        return subcategoriaRepository.save(subcategoriaExistente);
    }

    @Transactional
    public void deletar(Long id) {
        Subcategoria subcategoria = buscarPorId(id);
        subcategoriaRepository.delete(subcategoria);
    }
}


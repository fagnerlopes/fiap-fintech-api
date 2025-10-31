package br.com.fintech.fintechapi.service;

import br.com.fintech.fintechapi.exception.DadosDuplicadosException;
import br.com.fintech.fintechapi.exception.RecursoNaoEncontradoException;
import br.com.fintech.fintechapi.model.Categoria;
import br.com.fintech.fintechapi.model.TipoCategoria;
import br.com.fintech.fintechapi.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Transactional
    public Categoria criar(Categoria categoria) {
        if (categoria.getNomeCategoria() == null || categoria.getNomeCategoria().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da categoria é obrigatório");
        }

        if (categoria.getTipoCategoria() == null) {
            throw new IllegalArgumentException("Tipo da categoria é obrigatório");
        }

        if (categoriaRepository.existsByNomeCategoriaAndTipoCategoria(
                categoria.getNomeCategoria(), categoria.getTipoCategoria())) {
            throw new DadosDuplicadosException("Já existe uma categoria com este nome e tipo");
        }

        return categoriaRepository.save(categoria);
    }

    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada com ID: " + id));
    }

    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    public List<Categoria> listarPorTipo(TipoCategoria tipoCategoria) {
        return categoriaRepository.findByTipoCategoria(tipoCategoria);
    }

    @Transactional
    public Categoria atualizar(Categoria categoria) {
        if (categoria.getIdCategoria() == null) {
            throw new IllegalArgumentException("ID da categoria é obrigatório para atualização");
        }

        Categoria categoriaExistente = buscarPorId(categoria.getIdCategoria());

        if (categoria.getNomeCategoria() != null && !categoria.getNomeCategoria().trim().isEmpty()) {
            if (!categoriaExistente.getNomeCategoria().equals(categoria.getNomeCategoria()) ||
                !categoriaExistente.getTipoCategoria().equals(categoria.getTipoCategoria())) {
                
                if (categoriaRepository.existsByNomeCategoriaAndTipoCategoria(
                        categoria.getNomeCategoria(), categoria.getTipoCategoria())) {
                    throw new DadosDuplicadosException("Já existe uma categoria com este nome e tipo");
                }
            }
            categoriaExistente.setNomeCategoria(categoria.getNomeCategoria());
        }

        if (categoria.getTipoCategoria() != null) {
            categoriaExistente.setTipoCategoria(categoria.getTipoCategoria());
        }

        return categoriaRepository.save(categoriaExistente);
    }

    @Transactional
    public void deletar(Long id) {
        Categoria categoria = buscarPorId(id);
        categoriaRepository.delete(categoria);
    }
}


package br.com.fintech.fintechapi.controller;

import br.com.fintech.fintechapi.model.Categoria;
import br.com.fintech.fintechapi.model.Subcategoria;
import br.com.fintech.fintechapi.service.SubcategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/subcategorias")
public class SubcategoriaController {

    @Autowired
    private SubcategoriaService subcategoriaService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Subcategoria>> listarTodas() {
        List<Subcategoria> subcategorias = subcategoriaService.listarTodas();
        return ResponseEntity.ok(subcategorias);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Subcategoria> buscarPorId(@PathVariable Long id) {
        Subcategoria subcategoria = subcategoriaService.buscarPorId(id);
        return ResponseEntity.ok(subcategoria);
    }

    @GetMapping("/categoria/{idCategoria}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Subcategoria>> listarPorCategoria(@PathVariable Long idCategoria) {
        List<Subcategoria> subcategorias = subcategoriaService.listarPorCategoria(idCategoria);
        return ResponseEntity.ok(subcategorias);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Map<String, Object>> criar(@RequestBody Map<String, Object> requestBody) {
        Subcategoria subcategoria = new Subcategoria();
        subcategoria.setNomeSubcat((String) requestBody.get("nomeSubcat"));

        Long idCategoria = Long.valueOf(requestBody.get("idCategoria").toString());

        Subcategoria subcategoriaCriada = subcategoriaService.criar(subcategoria, idCategoria);

        Map<String, Object> response = new HashMap<>();
        response.put("idSubcategoria", subcategoriaCriada.getIdSubcategoria());
        response.put("nomeSubcat", subcategoriaCriada.getNomeSubcat());
        response.put("idCategoria", subcategoriaCriada.getCategoria().getIdCategoria());
        response.put("nomeCategoria", subcategoriaCriada.getCategoria().getNomeCategoria());
        response.put("message", "Subcategoria criada com sucesso");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<String, Object>> atualizar(
            @PathVariable Long id,
            @RequestBody Map<String, Object> requestBody) {

        Subcategoria subcategoria = new Subcategoria();
        subcategoria.setIdSubcategoria(id);

        if (requestBody.containsKey("nomeSubcat")) {
            subcategoria.setNomeSubcat((String) requestBody.get("nomeSubcat"));
        }

        if (requestBody.containsKey("idCategoria")) {
            Categoria categoria = new Categoria();
            categoria.setIdCategoria(Long.valueOf(requestBody.get("idCategoria").toString()));
            subcategoria.setCategoria(categoria);
        }

        Subcategoria subcategoriaAtualizada = subcategoriaService.atualizar(subcategoria);

        Map<String, Object> response = new HashMap<>();
        response.put("idSubcategoria", subcategoriaAtualizada.getIdSubcategoria());
        response.put("nomeSubcat", subcategoriaAtualizada.getNomeSubcat());
        response.put("idCategoria", subcategoriaAtualizada.getCategoria().getIdCategoria());
        response.put("nomeCategoria", subcategoriaAtualizada.getCategoria().getNomeCategoria());
        response.put("message", "Subcategoria atualizada com sucesso");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        subcategoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}


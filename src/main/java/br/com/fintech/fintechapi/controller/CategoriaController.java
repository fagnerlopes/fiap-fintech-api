package br.com.fintech.fintechapi.controller;

import br.com.fintech.fintechapi.model.Categoria;
import br.com.fintech.fintechapi.model.TipoCategoria;
import br.com.fintech.fintechapi.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Categoria>> listarTodas() {
        List<Categoria> categorias = categoriaService.listarTodas();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Categoria> buscarPorId(@PathVariable Long id) {
        Categoria categoria = categoriaService.buscarPorId(id);
        return ResponseEntity.ok(categoria);
    }

    @GetMapping("/tipo/{tipo}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Categoria>> listarPorTipo(@PathVariable String tipo) {
        TipoCategoria tipoCategoria = TipoCategoria.valueOf(tipo.toUpperCase());
        List<Categoria> categorias = categoriaService.listarPorTipo(tipoCategoria);
        return ResponseEntity.ok(categorias);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Map<String, Object>> criar(@RequestBody Map<String, Object> requestBody) {
        Categoria categoria = new Categoria();
        categoria.setNomeCategoria((String) requestBody.get("nomeCategoria"));
        
        String tipo = (String) requestBody.get("tipoCategoria");
        categoria.setTipoCategoria(TipoCategoria.valueOf(tipo.toUpperCase()));

        Categoria categoriaCriada = categoriaService.criar(categoria);

        Map<String, Object> response = new HashMap<>();
        response.put("idCategoria", categoriaCriada.getIdCategoria());
        response.put("nomeCategoria", categoriaCriada.getNomeCategoria());
        response.put("tipoCategoria", categoriaCriada.getTipoCategoria());
        response.put("message", "Categoria criada com sucesso");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<String, Object>> atualizar(
            @PathVariable Long id,
            @RequestBody Map<String, Object> requestBody) {

        Categoria categoria = new Categoria();
        categoria.setIdCategoria(id);

        if (requestBody.containsKey("nomeCategoria")) {
            categoria.setNomeCategoria((String) requestBody.get("nomeCategoria"));
        }

        if (requestBody.containsKey("tipoCategoria")) {
            String tipo = (String) requestBody.get("tipoCategoria");
            categoria.setTipoCategoria(TipoCategoria.valueOf(tipo.toUpperCase()));
        }

        Categoria categoriaAtualizada = categoriaService.atualizar(categoria);

        Map<String, Object> response = new HashMap<>();
        response.put("idCategoria", categoriaAtualizada.getIdCategoria());
        response.put("nomeCategoria", categoriaAtualizada.getNomeCategoria());
        response.put("tipoCategoria", categoriaAtualizada.getTipoCategoria());
        response.put("message", "Categoria atualizada com sucesso");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        categoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}


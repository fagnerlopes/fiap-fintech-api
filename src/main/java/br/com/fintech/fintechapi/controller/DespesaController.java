package br.com.fintech.fintechapi.controller;

import br.com.fintech.fintechapi.model.Categoria;
import br.com.fintech.fintechapi.model.Despesa;
import br.com.fintech.fintechapi.model.Subcategoria;
import br.com.fintech.fintechapi.model.Usuario;
import br.com.fintech.fintechapi.service.DespesaService;
import br.com.fintech.fintechapi.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/despesas")
public class DespesaController {

    @Autowired
    private DespesaService despesaService;

    @Autowired
    private UsuarioService usuarioService;

    private Long getUsuarioAutenticadoId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Usuario usuario = usuarioService.buscarPorEmail(email);
        return usuario.getIdUsuario();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Despesa>> listarTodas() {
        Long idUsuario = getUsuarioAutenticadoId();
        List<Despesa> despesas = despesaService.listarPorUsuario(idUsuario);
        return ResponseEntity.ok(despesas);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Despesa> buscarPorId(@PathVariable Long id) {
        Long idUsuario = getUsuarioAutenticadoId();
        Despesa despesa = despesaService.buscarPorId(id, idUsuario);
        return ResponseEntity.ok(despesa);
    }

    @GetMapping("/periodo")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Despesa>> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        
        Long idUsuario = getUsuarioAutenticadoId();
        List<Despesa> despesas = despesaService.listarPorPeriodo(idUsuario, dataInicio, dataFim);
        return ResponseEntity.ok(despesas);
    }

    @GetMapping("/pendentes")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Despesa>> listarPendentes() {
        Long idUsuario = getUsuarioAutenticadoId();
        List<Despesa> despesas = despesaService.listarPendentes(idUsuario);
        return ResponseEntity.ok(despesas);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Map<String, Object>> criar(@RequestBody Map<String, Object> requestBody) {
        Long idUsuario = getUsuarioAutenticadoId();

        Despesa despesa = new Despesa();
        despesa.setDescricao((String) requestBody.get("descricao"));
        despesa.setValor(new java.math.BigDecimal(requestBody.get("valor").toString()));
        despesa.setDataVencimento(LocalDate.parse((String) requestBody.get("dataVencimento")));
        
        if (requestBody.containsKey("recorrente")) {
            despesa.setRecorrente((Integer) requestBody.get("recorrente"));
        }
        if (requestBody.containsKey("pendente")) {
            despesa.setPendente((Integer) requestBody.get("pendente"));
        }
        
        if (requestBody.containsKey("idCategoria") && requestBody.get("idCategoria") != null) {
            Categoria categoria = new Categoria();
            categoria.setIdCategoria(Long.valueOf(requestBody.get("idCategoria").toString()));
            despesa.setCategoria(categoria);
        }
        
        if (requestBody.containsKey("idSubcategoria") && requestBody.get("idSubcategoria") != null) {
            Subcategoria subcategoria = new Subcategoria();
            subcategoria.setIdSubcategoria(Long.valueOf(requestBody.get("idSubcategoria").toString()));
            despesa.setSubcategoria(subcategoria);
        }

        Despesa despesaCriada = despesaService.criar(despesa, idUsuario);

        Map<String, Object> response = new HashMap<>();
        response.put("idDespesa", despesaCriada.getIdDespesa());
        response.put("descricao", despesaCriada.getDescricao());
        response.put("valor", despesaCriada.getValor());
        response.put("dataVencimento", despesaCriada.getDataVencimento());
        response.put("recorrente", despesaCriada.getRecorrente());
        response.put("pendente", despesaCriada.getPendente());
        
        if (despesaCriada.getCategoria() != null) {
            Map<String, Object> categoriaMap = new HashMap<>();
            categoriaMap.put("idCategoria", despesaCriada.getCategoria().getIdCategoria());
            categoriaMap.put("nomeCategoria", despesaCriada.getCategoria().getNomeCategoria());
            categoriaMap.put("tipoCategoria", despesaCriada.getCategoria().getTipoCategoria());
            response.put("categoria", categoriaMap);
        }
        
        if (despesaCriada.getSubcategoria() != null) {
            Map<String, Object> subcategoriaMap = new HashMap<>();
            subcategoriaMap.put("idSubcategoria", despesaCriada.getSubcategoria().getIdSubcategoria());
            subcategoriaMap.put("nomeSubcat", despesaCriada.getSubcategoria().getNomeSubcat());
            response.put("subcategoria", subcategoriaMap);
        }
        
        response.put("criadoEm", despesaCriada.getCriadoEm());
        response.put("message", "Despesa criada com sucesso");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<String, Object>> atualizar(
            @PathVariable Long id,
            @RequestBody Map<String, Object> requestBody) {
        
        Long idUsuario = getUsuarioAutenticadoId();

        Despesa despesa = new Despesa();
        despesa.setIdDespesa(id);
        
        if (requestBody.containsKey("descricao")) {
            despesa.setDescricao((String) requestBody.get("descricao"));
        }
        if (requestBody.containsKey("valor")) {
            despesa.setValor(new java.math.BigDecimal(requestBody.get("valor").toString()));
        }
        if (requestBody.containsKey("dataVencimento")) {
            despesa.setDataVencimento(LocalDate.parse((String) requestBody.get("dataVencimento")));
        }
        if (requestBody.containsKey("recorrente")) {
            despesa.setRecorrente((Integer) requestBody.get("recorrente"));
        }
        if (requestBody.containsKey("pendente")) {
            despesa.setPendente((Integer) requestBody.get("pendente"));
        }
        
        if (requestBody.containsKey("idCategoria")) {
            if (requestBody.get("idCategoria") != null) {
                Categoria categoria = new Categoria();
                categoria.setIdCategoria(Long.valueOf(requestBody.get("idCategoria").toString()));
                despesa.setCategoria(categoria);
            } else {
                despesa.setCategoria(new Categoria());
            }
        }
        
        if (requestBody.containsKey("idSubcategoria")) {
            if (requestBody.get("idSubcategoria") != null) {
                Subcategoria subcategoria = new Subcategoria();
                subcategoria.setIdSubcategoria(Long.valueOf(requestBody.get("idSubcategoria").toString()));
                despesa.setSubcategoria(subcategoria);
            } else {
                despesa.setSubcategoria(new Subcategoria());
            }
        }

        Despesa despesaAtualizada = despesaService.atualizar(despesa, idUsuario);

        Map<String, Object> response = new HashMap<>();
        response.put("idDespesa", despesaAtualizada.getIdDespesa());
        response.put("descricao", despesaAtualizada.getDescricao());
        response.put("valor", despesaAtualizada.getValor());
        response.put("dataVencimento", despesaAtualizada.getDataVencimento());
        response.put("recorrente", despesaAtualizada.getRecorrente());
        response.put("pendente", despesaAtualizada.getPendente());
        
        if (despesaAtualizada.getCategoria() != null) {
            Map<String, Object> categoriaMap = new HashMap<>();
            categoriaMap.put("idCategoria", despesaAtualizada.getCategoria().getIdCategoria());
            categoriaMap.put("nomeCategoria", despesaAtualizada.getCategoria().getNomeCategoria());
            categoriaMap.put("tipoCategoria", despesaAtualizada.getCategoria().getTipoCategoria());
            response.put("categoria", categoriaMap);
        }
        
        if (despesaAtualizada.getSubcategoria() != null) {
            Map<String, Object> subcategoriaMap = new HashMap<>();
            subcategoriaMap.put("idSubcategoria", despesaAtualizada.getSubcategoria().getIdSubcategoria());
            subcategoriaMap.put("nomeSubcat", despesaAtualizada.getSubcategoria().getNomeSubcat());
            response.put("subcategoria", subcategoriaMap);
        }
        
        response.put("criadoEm", despesaAtualizada.getCriadoEm());
        response.put("message", "Despesa atualizada com sucesso");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        Long idUsuario = getUsuarioAutenticadoId();
        despesaService.deletar(id, idUsuario);
        return ResponseEntity.noContent().build();
    }
}


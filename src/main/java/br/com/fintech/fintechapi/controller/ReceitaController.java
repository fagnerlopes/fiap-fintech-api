package br.com.fintech.fintechapi.controller;

import br.com.fintech.fintechapi.model.Categoria;
import br.com.fintech.fintechapi.model.Receita;
import br.com.fintech.fintechapi.model.Subcategoria;
import br.com.fintech.fintechapi.model.Usuario;
import br.com.fintech.fintechapi.service.ReceitaService;
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

/**
 * Controller responsável pelos endpoints de Receitas
 */
@RestController
@RequestMapping("/api/receitas")
public class ReceitaController {

    @Autowired
    private ReceitaService receitaService;

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Método auxiliar para obter o ID do usuário autenticado
     */
    private Long getUsuarioAutenticadoId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Usuario usuario = usuarioService.buscarPorEmail(email);
        return usuario.getIdUsuario();
    }

    /**
     * Lista todas as receitas do usuário autenticado com filtros opcionais
     * GET /api/receitas
     * GET /api/receitas?dataInicio=2025-01-01&dataFim=2025-12-31
     * GET /api/receitas?idCategoria=1&pendente=1
     * 
     * @param dataInicio Data inicial (opcional)
     * @param dataFim Data final (opcional)
     * @param idCategoria ID da categoria (opcional)
     * @param pendente Status pendente 0=não, 1=sim (opcional)
     * @return Lista de receitas filtradas
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Receita>> listarTodas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(required = false) Long idCategoria,
            @RequestParam(required = false) Integer pendente) {
        
        Long idUsuario = getUsuarioAutenticadoId();
        
        // Se algum filtro foi fornecido, usa o método com filtros
        if (dataInicio != null || dataFim != null || idCategoria != null || pendente != null) {
            List<Receita> receitas = receitaService.listarComFiltros(
                idUsuario, dataInicio, dataFim, idCategoria, pendente
            );
            return ResponseEntity.ok(receitas);
        }
        
        // Caso contrário, lista todas
        List<Receita> receitas = receitaService.listarPorUsuario(idUsuario);
        return ResponseEntity.ok(receitas);
    }

    /**
     * Busca uma receita por ID
     * GET /api/receitas/{id}
     * 
     * @param id ID da receita
     * @return Receita encontrada
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Receita> buscarPorId(@PathVariable Long id) {
        Long idUsuario = getUsuarioAutenticadoId();
        Receita receita = receitaService.buscarPorId(id, idUsuario);
        return ResponseEntity.ok(receita);
    }

    /**
     * Lista receitas por período
     * GET /api/receitas/periodo?dataInicio=2025-01-01&dataFim=2025-12-31
     * 
     * @param dataInicio Data inicial
     * @param dataFim Data final
     * @return Lista de receitas no período
     */
    @GetMapping("/periodo")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Receita>> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        
        Long idUsuario = getUsuarioAutenticadoId();
        List<Receita> receitas = receitaService.listarPorPeriodo(idUsuario, dataInicio, dataFim);
        return ResponseEntity.ok(receitas);
    }

    /**
     * Lista receitas pendentes
     * GET /api/receitas/pendentes
     * 
     * @return Lista de receitas pendentes
     */
    @GetMapping("/pendentes")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Receita>> listarPendentes() {
        Long idUsuario = getUsuarioAutenticadoId();
        List<Receita> receitas = receitaService.listarPendentes(idUsuario);
        return ResponseEntity.ok(receitas);
    }

    /**
     * Cria uma nova receita
     * POST /api/receitas
     * 
     * @param requestBody Map contendo dados da receita
     * @return Receita criada
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Map<String, Object>> criar(@RequestBody Map<String, Object> requestBody) {
        Long idUsuario = getUsuarioAutenticadoId();

        // Montar objeto Receita
        Receita receita = new Receita();
        receita.setDescricao((String) requestBody.get("descricao"));
        receita.setValor(new java.math.BigDecimal(requestBody.get("valor").toString()));
        receita.setDataEntrada(LocalDate.parse((String) requestBody.get("dataEntrada")));
        
        // Campos opcionais
        if (requestBody.containsKey("recorrente")) {
            receita.setRecorrente((Integer) requestBody.get("recorrente"));
        }
        if (requestBody.containsKey("pendente")) {
            receita.setPendente((Integer) requestBody.get("pendente"));
        }
        
        // Categoria (opcional)
        if (requestBody.containsKey("idCategoria") && requestBody.get("idCategoria") != null) {
            Categoria categoria = new Categoria();
            categoria.setIdCategoria(Long.valueOf(requestBody.get("idCategoria").toString()));
            receita.setCategoria(categoria);
        }
        
        // Subcategoria (opcional)
        if (requestBody.containsKey("idSubcategoria") && requestBody.get("idSubcategoria") != null) {
            Subcategoria subcategoria = new Subcategoria();
            subcategoria.setIdSubcategoria(Long.valueOf(requestBody.get("idSubcategoria").toString()));
            receita.setSubcategoria(subcategoria);
        }

        Receita receitaCriada = receitaService.criar(receita, idUsuario);

        // Montar resposta
        Map<String, Object> response = new HashMap<>();
        response.put("idReceita", receitaCriada.getIdReceita());
        response.put("descricao", receitaCriada.getDescricao());
        response.put("valor", receitaCriada.getValor());
        response.put("dataEntrada", receitaCriada.getDataEntrada());
        response.put("recorrente", receitaCriada.getRecorrente());
        response.put("pendente", receitaCriada.getPendente());
        
        if (receitaCriada.getCategoria() != null) {
            Map<String, Object> categoriaMap = new HashMap<>();
            categoriaMap.put("idCategoria", receitaCriada.getCategoria().getIdCategoria());
            categoriaMap.put("nomeCategoria", receitaCriada.getCategoria().getNomeCategoria());
            categoriaMap.put("tipoCategoria", receitaCriada.getCategoria().getTipoCategoria());
            response.put("categoria", categoriaMap);
        }
        
        if (receitaCriada.getSubcategoria() != null) {
            Map<String, Object> subcategoriaMap = new HashMap<>();
            subcategoriaMap.put("idSubcategoria", receitaCriada.getSubcategoria().getIdSubcategoria());
            subcategoriaMap.put("nomeSubcat", receitaCriada.getSubcategoria().getNomeSubcat());
            response.put("subcategoria", subcategoriaMap);
        }
        
        response.put("criadoEm", receitaCriada.getCriadoEm());
        response.put("message", "Receita criada com sucesso");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Atualiza uma receita existente
     * PUT /api/receitas/{id}
     * 
     * @param id ID da receita
     * @param requestBody Map contendo dados atualizados
     * @return Receita atualizada
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<String, Object>> atualizar(
            @PathVariable Long id,
            @RequestBody Map<String, Object> requestBody) {
        
        Long idUsuario = getUsuarioAutenticadoId();

        // Montar objeto Receita
        Receita receita = new Receita();
        receita.setIdReceita(id);
        
        if (requestBody.containsKey("descricao")) {
            receita.setDescricao((String) requestBody.get("descricao"));
        }
        if (requestBody.containsKey("valor")) {
            receita.setValor(new java.math.BigDecimal(requestBody.get("valor").toString()));
        }
        if (requestBody.containsKey("dataEntrada")) {
            receita.setDataEntrada(LocalDate.parse((String) requestBody.get("dataEntrada")));
        }
        if (requestBody.containsKey("recorrente")) {
            receita.setRecorrente((Integer) requestBody.get("recorrente"));
        }
        if (requestBody.containsKey("pendente")) {
            receita.setPendente((Integer) requestBody.get("pendente"));
        }
        
        // Categoria (opcional)
        if (requestBody.containsKey("idCategoria")) {
            if (requestBody.get("idCategoria") != null) {
                Categoria categoria = new Categoria();
                categoria.setIdCategoria(Long.valueOf(requestBody.get("idCategoria").toString()));
                receita.setCategoria(categoria);
            } else {
                receita.setCategoria(new Categoria()); // Para remover a categoria
            }
        }
        
        // Subcategoria (opcional)
        if (requestBody.containsKey("idSubcategoria")) {
            if (requestBody.get("idSubcategoria") != null) {
                Subcategoria subcategoria = new Subcategoria();
                subcategoria.setIdSubcategoria(Long.valueOf(requestBody.get("idSubcategoria").toString()));
                receita.setSubcategoria(subcategoria);
            } else {
                receita.setSubcategoria(new Subcategoria()); // Para remover a subcategoria
            }
        }

        Receita receitaAtualizada = receitaService.atualizar(receita, idUsuario);

        // Montar resposta
        Map<String, Object> response = new HashMap<>();
        response.put("idReceita", receitaAtualizada.getIdReceita());
        response.put("descricao", receitaAtualizada.getDescricao());
        response.put("valor", receitaAtualizada.getValor());
        response.put("dataEntrada", receitaAtualizada.getDataEntrada());
        response.put("recorrente", receitaAtualizada.getRecorrente());
        response.put("pendente", receitaAtualizada.getPendente());
        
        if (receitaAtualizada.getCategoria() != null) {
            Map<String, Object> categoriaMap = new HashMap<>();
            categoriaMap.put("idCategoria", receitaAtualizada.getCategoria().getIdCategoria());
            categoriaMap.put("nomeCategoria", receitaAtualizada.getCategoria().getNomeCategoria());
            categoriaMap.put("tipoCategoria", receitaAtualizada.getCategoria().getTipoCategoria());
            response.put("categoria", categoriaMap);
        }
        
        if (receitaAtualizada.getSubcategoria() != null) {
            Map<String, Object> subcategoriaMap = new HashMap<>();
            subcategoriaMap.put("idSubcategoria", receitaAtualizada.getSubcategoria().getIdSubcategoria());
            subcategoriaMap.put("nomeSubcat", receitaAtualizada.getSubcategoria().getNomeSubcat());
            response.put("subcategoria", subcategoriaMap);
        }
        
        response.put("criadoEm", receitaAtualizada.getCriadoEm());
        response.put("message", "Receita atualizada com sucesso");

        return ResponseEntity.ok(response);
    }

    /**
     * Deleta uma receita
     * DELETE /api/receitas/{id}
     * 
     * @param id ID da receita
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        Long idUsuario = getUsuarioAutenticadoId();
        receitaService.deletar(id, idUsuario);
        return ResponseEntity.noContent().build();
    }
}


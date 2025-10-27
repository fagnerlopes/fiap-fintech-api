package br.com.fintech.fintechapi.service;

import br.com.fintech.fintechapi.exception.DadosDuplicadosException;
import br.com.fintech.fintechapi.exception.RecursoNaoEncontradoException;
import br.com.fintech.fintechapi.model.PessoaFisica;
import br.com.fintech.fintechapi.model.PessoaJuridica;
import br.com.fintech.fintechapi.model.TipoUsuario;
import br.com.fintech.fintechapi.model.Usuario;
import br.com.fintech.fintechapi.repository.PessoaFisicaRepository;
import br.com.fintech.fintechapi.repository.PessoaJuridicaRepository;
import br.com.fintech.fintechapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service responsável pela lógica de negócio relacionada a usuários
 */
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PessoaFisicaRepository pessoaFisicaRepository;

    @Autowired
    private PessoaJuridicaRepository pessoaJuridicaRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Registra um novo usuário no sistema
     * @param usuario Usuário a ser registrado
     * @return Usuario registrado
     */
    @Transactional
    public Usuario registrar(Usuario usuario) {
        // Validar se o email já existe
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new DadosDuplicadosException("Email já cadastrado: " + usuario.getEmail());
        }

        // Validar tipo de usuário
        if (usuario.getTipoUsuario() == null) {
            throw new IllegalArgumentException("Tipo de usuário é obrigatório");
        }

        // Hash da senha
        if (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty()) {
            throw new IllegalArgumentException("Senha é obrigatória");
        }
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        // Definir data de criação
        if (usuario.getCriadoEm() == null) {
            usuario.setCriadoEm(LocalDateTime.now());
        }

        // Validar dados específicos do tipo de usuário
        if (usuario.getTipoUsuario() == TipoUsuario.PF) {
            validarPessoaFisica(usuario.getPessoaFisica());
        } else if (usuario.getTipoUsuario() == TipoUsuario.PJ) {
            validarPessoaJuridica(usuario.getPessoaJuridica());
        }

        // Salvar usuário
        return usuarioRepository.save(usuario);
    }

    /**
     * Valida os dados de uma Pessoa Física
     */
    private void validarPessoaFisica(PessoaFisica pf) {
        if (pf == null) {
            throw new IllegalArgumentException("Dados de Pessoa Física são obrigatórios");
        }
        if (pf.getCpf() == null || pf.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("CPF é obrigatório");
        }
        if (pf.getNome() == null || pf.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        if (pessoaFisicaRepository.existsByCpf(pf.getCpf())) {
            throw new DadosDuplicadosException("CPF já cadastrado: " + pf.getCpf());
        }
    }

    /**
     * Valida os dados de uma Pessoa Jurídica
     */
    private void validarPessoaJuridica(PessoaJuridica pj) {
        if (pj == null) {
            throw new IllegalArgumentException("Dados de Pessoa Jurídica são obrigatórios");
        }
        if (pj.getCnpj() == null || pj.getCnpj().trim().isEmpty()) {
            throw new IllegalArgumentException("CNPJ é obrigatório");
        }
        if (pj.getRazaoSocial() == null || pj.getRazaoSocial().trim().isEmpty()) {
            throw new IllegalArgumentException("Razão Social é obrigatória");
        }
        if (pessoaJuridicaRepository.existsByCnpj(pj.getCnpj())) {
            throw new DadosDuplicadosException("CNPJ já cadastrado: " + pj.getCnpj());
        }
    }

    /**
     * Busca um usuário por email
     * @param email Email do usuário
     * @return Usuario encontrado
     */
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com email: " + email));
    }

    /**
     * Busca um usuário por ID
     * @param id ID do usuário
     * @return Usuario encontrado
     */
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com ID: " + id));
    }

    /**
     * Lista todos os usuários
     * @return Lista de usuários
     */
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    /**
     * Atualiza um usuário existente (incluindo dados de PF/PJ se fornecidos)
     * @param usuario Usuario com dados atualizados
     * @param pessoaFisicaData Dados de Pessoa Física (opcional)
     * @param pessoaJuridicaData Dados de Pessoa Jurídica (opcional)
     * @return Usuario atualizado
     */
    @Transactional
    public Usuario atualizar(Usuario usuario, PessoaFisica pessoaFisicaData, PessoaJuridica pessoaJuridicaData) {
        if (usuario.getIdUsuario() == null) {
            throw new IllegalArgumentException("ID do usuário é obrigatório para atualização");
        }

        // Verificar se o usuário existe
        Usuario usuarioExistente = buscarPorId(usuario.getIdUsuario());

        // Se o email foi alterado, verificar se o novo email já está em uso
        if (!usuarioExistente.getEmail().equals(usuario.getEmail())) {
            if (usuarioRepository.existsByEmail(usuario.getEmail())) {
                throw new DadosDuplicadosException("Email já cadastrado: " + usuario.getEmail());
            }
        }

        // Atualizar campos do Usuario
        usuarioExistente.setEmail(usuario.getEmail());
        usuarioExistente.setTipoUsuario(usuario.getTipoUsuario());

        // Se uma nova senha foi fornecida, fazer o hash
        if (usuario.getSenha() != null && !usuario.getSenha().trim().isEmpty()) {
            usuarioExistente.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        // Atualizar dados de Pessoa Física (se fornecidos)
        if (pessoaFisicaData != null && usuario.getTipoUsuario() == TipoUsuario.PF) {
            try {
                PessoaFisica pfExistente = buscarPessoaFisicaPorIdUsuario(usuario.getIdUsuario());
                
                // Atualizar campos
                pfExistente.setNome(pessoaFisicaData.getNome());
                pfExistente.setDataNasc(pessoaFisicaData.getDataNasc());
                
                // CPF só pode ser atualizado se não houver duplicata
                if (!pfExistente.getCpf().equals(pessoaFisicaData.getCpf())) {
                    if (pessoaFisicaRepository.existsByCpf(pessoaFisicaData.getCpf())) {
                        throw new DadosDuplicadosException("CPF já cadastrado: " + pessoaFisicaData.getCpf());
                    }
                    pfExistente.setCpf(pessoaFisicaData.getCpf());
                }
                
                pessoaFisicaRepository.save(pfExistente);
            } catch (RecursoNaoEncontradoException e) {
                // Se não existe, pode criar uma nova
                pessoaFisicaData.setUsuario(usuarioExistente);
                pessoaFisicaRepository.save(pessoaFisicaData);
            }
        }

        // Atualizar dados de Pessoa Jurídica (se fornecidos)
        if (pessoaJuridicaData != null && usuario.getTipoUsuario() == TipoUsuario.PJ) {
            try {
                PessoaJuridica pjExistente = buscarPessoaJuridicaPorIdUsuario(usuario.getIdUsuario());
                
                // Atualizar campos
                pjExistente.setRazaoSocial(pessoaJuridicaData.getRazaoSocial());
                
                // CNPJ só pode ser atualizado se não houver duplicata
                if (!pjExistente.getCnpj().equals(pessoaJuridicaData.getCnpj())) {
                    if (pessoaJuridicaRepository.existsByCnpj(pessoaJuridicaData.getCnpj())) {
                        throw new DadosDuplicadosException("CNPJ já cadastrado: " + pessoaJuridicaData.getCnpj());
                    }
                    pjExistente.setCnpj(pessoaJuridicaData.getCnpj());
                }
                
                pessoaJuridicaRepository.save(pjExistente);
            } catch (RecursoNaoEncontradoException e) {
                // Se não existe, pode criar uma nova
                pessoaJuridicaData.setUsuario(usuarioExistente);
                pessoaJuridicaRepository.save(pessoaJuridicaData);
            }
        }

        return usuarioRepository.save(usuarioExistente);
    }

    /**
     * Deleta um usuário
     * @param id ID do usuário a ser deletado
     */
    @Transactional
    public void deletar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Usuário não encontrado com ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    /**
     * Busca dados de Pessoa Física pelo ID do usuário
     * @param idUsuario ID do usuário
     * @return PessoaFisica encontrada
     */
    public PessoaFisica buscarPessoaFisicaPorIdUsuario(Long idUsuario) {
        return pessoaFisicaRepository.findByUsuarioIdUsuario(idUsuario)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pessoa Física não encontrada para o usuário ID: " + idUsuario));
    }

    /**
     * Busca dados de Pessoa Jurídica pelo ID do usuário
     * @param idUsuario ID do usuário
     * @return PessoaJuridica encontrada
     */
    public PessoaJuridica buscarPessoaJuridicaPorIdUsuario(Long idUsuario) {
        return pessoaJuridicaRepository.findByUsuarioIdUsuario(idUsuario)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pessoa Jurídica não encontrada para o usuário ID: " + idUsuario));
    }
}


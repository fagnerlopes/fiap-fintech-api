package br.com.fintech.fintechapi.exception;

/**
 * Exceção lançada quando um usuário tenta acessar um recurso sem permissão
 * Retorna HTTP 403 Forbidden
 */
public class AcessoNegadoException extends RuntimeException {
    
    public AcessoNegadoException(String mensagem) {
        super(mensagem);
    }
    
    public AcessoNegadoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}


package br.com.fintech.fintechapi.exception;

/**
 * Exceção lançada quando um recurso não é encontrado no banco de dados
 * Retorna HTTP 404 Not Found
 */
public class RecursoNaoEncontradoException extends RuntimeException {
    
    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
    
    public RecursoNaoEncontradoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}


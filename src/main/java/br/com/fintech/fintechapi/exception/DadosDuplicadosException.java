package br.com.fintech.fintechapi.exception;

/**
 * Exceção lançada quando há tentativa de criar um recurso com dados únicos já existentes
 * Retorna HTTP 409 Conflict
 */
public class DadosDuplicadosException extends RuntimeException {
    
    public DadosDuplicadosException(String mensagem) {
        super(mensagem);
    }
    
    public DadosDuplicadosException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}


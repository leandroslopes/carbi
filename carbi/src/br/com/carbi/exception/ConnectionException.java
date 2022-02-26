
package br.com.carbi.exception;

public class ConnectionException extends Exception {

    private static final long serialVersionUID = 1L;

    public ConnectionException(String descricao) {
        super(descricao);
    }
}

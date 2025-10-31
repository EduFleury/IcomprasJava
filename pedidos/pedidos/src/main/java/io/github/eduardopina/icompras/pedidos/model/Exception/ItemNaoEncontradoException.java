package io.github.eduardopina.icompras.pedidos.model.Exception;

public class ItemNaoEncontradoException extends RuntimeException{

    public ItemNaoEncontradoException(String message) {
        super(message);
    }
}

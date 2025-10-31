package io.github.eduardopina.icompras.pedidos.validator;

import feign.FeignException;
import io.github.eduardopina.icompras.pedidos.client.ClientsClient;
import io.github.eduardopina.icompras.pedidos.client.ProdutosClient;
import io.github.eduardopina.icompras.pedidos.client.representation.ClientRepresentation;
import io.github.eduardopina.icompras.pedidos.client.representation.ProdutoRepresentation;
import io.github.eduardopina.icompras.pedidos.model.Exception.ValidationException;
import io.github.eduardopina.icompras.pedidos.model.ItemPedido;
import io.github.eduardopina.icompras.pedidos.model.Pedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PedidoValidator {

    private final ProdutosClient produtosClient;
    private final ClientsClient clientsClient;

    public void validar(Pedido pedido){
        Long codigoCliente = pedido.getCodigoCliente();
        validarCliente(codigoCliente);
        pedido.getItens().forEach(this::validarItem);
    }

    private void validarCliente(Long codigoCliente){
        try{
            var response = clientsClient.obterDados(codigoCliente);
            ClientRepresentation client = response.getBody();
            log.info("Cliente de código {} encontrado: {}", client.codigo(), client.nome());
        }catch (FeignException.NotFound e){
            var message = String.format("Cliente de código %d não encontrado.", codigoCliente);
            throw new ValidationException("codigoCliente", message);
        }
    }

    private void validarItem(ItemPedido item){
        try{
            var response = produtosClient.obterProduto(item.getCodigoProduto());
            ProdutoRepresentation produto = response.getBody();
            log.info("Produto de código {} encontrado: {}", produto.codigo(), produto.nome());
        } catch (FeignException.NotFound e) {
            var message = String.format("Produto de código %d não encontrado.", item.getCodigoProduto());
            throw new ValidationException("codigoProduto", message);
        }
    }
}

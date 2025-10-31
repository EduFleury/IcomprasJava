package io.github.eduardopina.icompras.pedidos.service;

import io.github.eduardopina.icompras.pedidos.client.ClientsClient;
import io.github.eduardopina.icompras.pedidos.client.ProdutosClient;
import io.github.eduardopina.icompras.pedidos.client.ServicoBancarioClient;
import io.github.eduardopina.icompras.pedidos.client.representation.ClientRepresentation;
import io.github.eduardopina.icompras.pedidos.client.representation.ProdutoRepresentation;
import io.github.eduardopina.icompras.pedidos.model.DadosPagamento;
import io.github.eduardopina.icompras.pedidos.model.ErroResponse;
import io.github.eduardopina.icompras.pedidos.model.Exception.ItemNaoEncontradoException;
import io.github.eduardopina.icompras.pedidos.model.Exception.ValidationException;
import io.github.eduardopina.icompras.pedidos.model.ItemPedido;
import io.github.eduardopina.icompras.pedidos.model.Pedido;
import io.github.eduardopina.icompras.pedidos.model.enums.StatusPedido;
import io.github.eduardopina.icompras.pedidos.model.enums.TipoPagamento;
import io.github.eduardopina.icompras.pedidos.publisher.PagamentoPublisher;
import io.github.eduardopina.icompras.pedidos.repository.ItemPedidoRepository;
import io.github.eduardopina.icompras.pedidos.repository.PedidoRepository;
import io.github.eduardopina.icompras.pedidos.validator.PedidoValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PedidoService {

    private final PedidoRepository repository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final PedidoValidator validator;
    private final ServicoBancarioClient client;
    private final ClientsClient apiClients;
    private final ProdutosClient apiProdutos;
    private final PagamentoPublisher pagamentoPublisher;

    @Transactional
    public Pedido criarPedido(Pedido pedido){
        validator.validar(pedido);
        realizarPersistencia(pedido);
        enviarSolicitacaoPagamento(pedido);
        return pedido;
    }

    private void enviarSolicitacaoPagamento(Pedido pedido) {
        var chavePagamento = client.solicitarPagamento(pedido);
        pedido.setChavePagamento(chavePagamento);
    }

    private void realizarPersistencia(Pedido pedido) {
        repository.save(pedido);
        itemPedidoRepository.saveAll(pedido.getItens());
    }

    public void atualizarStatusPagamento(Long codigoPedido,
                                         String chavePagamento,
                                         boolean sucesso,
                                         String observacoes) {

        var pedidoEncontrado = repository.findByCodigoAndChavePagamento(codigoPedido,
                chavePagamento);

        if(pedidoEncontrado.isEmpty()){
            var msg = String.format("Pedido não encontrado para o código %d e chave pagamento %s",
                    codigoPedido, chavePagamento);

            log.error(msg);
            return;
        }

        Pedido pedido = pedidoEncontrado.get();
        if(sucesso){
            prepararEPublicarPedidoPago(pedido);
        }else{
            pedido.setStatus(StatusPedido.ERRO_PAGAMENTO);
            pedido.setObservacoes(observacoes);
        }

        repository.save(pedido);
    }

    private void prepararEPublicarPedidoPago(Pedido pedido) {
        pedido.setStatus(StatusPedido.PAGO);
        carregarDadosCliente(pedido);
        carregarItensPedido(pedido);
        pagamentoPublisher.publicar(pedido);
    }

    @Transactional
    public void adicionarNovoPagamento(Long codigoPedido, String dadosCartao, TipoPagamento tipo){
        var pedidoEncontrado = repository.findById(codigoPedido);

        if(pedidoEncontrado.isEmpty()){
            throw new ItemNaoEncontradoException("Pedido não encontrado para código informado.");
        }

        var pedido = pedidoEncontrado.get();

        DadosPagamento dadosPagamento = new DadosPagamento();
        dadosPagamento.setDados(dadosCartao);
        dadosPagamento.setTipoPagamento(tipo);
        pedido.setDadosPagamento(dadosPagamento);
        pedido.setStatus(StatusPedido.REALIZADO);
        pedido.setObservacoes("Novo pagamento realizado, aguardando novo processamento.");

        String novaChavePagamento = client.solicitarPagamento(pedido);
        pedido.setChavePagamento(novaChavePagamento);

        repository.save(pedido);
    }

    public Optional<Pedido> carregarDadosCompletosPedido(Long codigo){
        Optional<Pedido> pedido = repository.findById(codigo);
        pedido.ifPresent(this::carregarDadosCliente);
        pedido.ifPresent(this::carregarItensPedido);
        return pedido;
    }

    private void carregarDadosCliente(Pedido pedido){
        Long codigoCliente = pedido.getCodigoCliente();
        var response = apiClients.obterDados(codigoCliente);
        pedido.setDadosCliente(response.getBody());
    }

    private void carregarItensPedido(Pedido pedido){
        List<ItemPedido> itens = itemPedidoRepository.findByPedido(pedido);
        pedido.setItens(itens);

        pedido.getItens().forEach(this::carregarDadosProduto);
    }

    private void carregarDadosProduto(ItemPedido item){
        var response = apiProdutos.obterProduto(item.getCodigoProduto());
        item.setNome(response.getBody().nome());
    }
}

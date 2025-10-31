package io.github.eduardopina.icompras.pedidos.controller.dto;

import java.util.List;

public record NovoPedidoDTO(Long codigoCliente, List<ItemPedidoDTO> itens,
                            DadosPagamentoDTO dadosPagamento) {
}

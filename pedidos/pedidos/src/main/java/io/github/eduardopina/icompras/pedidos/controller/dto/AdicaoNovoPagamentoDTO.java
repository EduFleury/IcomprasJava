package io.github.eduardopina.icompras.pedidos.controller.dto;

import io.github.eduardopina.icompras.pedidos.model.enums.TipoPagamento;

public record AdicaoNovoPagamentoDTO(Long codigoPedido, String dados, TipoPagamento tipoPagamento) {
}

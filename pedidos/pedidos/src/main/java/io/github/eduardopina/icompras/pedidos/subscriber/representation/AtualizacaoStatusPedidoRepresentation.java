package io.github.eduardopina.icompras.pedidos.subscriber.representation;

import io.github.eduardopina.icompras.pedidos.model.enums.StatusPedido;

public record AtualizacaoStatusPedidoRepresentation(Long codigo,
                                                    StatusPedido status,
                                                    String urlNotaFiscal,
                                                    String codigoRastreio){
}

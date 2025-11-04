package io.github.eduardopina.icompras.logistica.subscriber.representation;

import io.github.eduardopina.icompras.logistica.model.enums.StatusPedido;

public record AtualizacaoFaturamentoRepresentation(Long codigo, StatusPedido status, String urlNotaFiscal) {
}

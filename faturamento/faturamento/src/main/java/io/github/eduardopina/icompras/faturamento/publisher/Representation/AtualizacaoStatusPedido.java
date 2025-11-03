package io.github.eduardopina.icompras.faturamento.publisher.Representation;

public record AtualizacaoStatusPedido(Long codigo, StatusPedido status, String urlNotaFiscal) {
}

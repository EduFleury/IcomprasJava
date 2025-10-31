package io.github.eduardopina.icompras.pedidos.controller.dto;

public record RecebendoCallBackPagamentoDTO(Long codigo, String chavePagamento,
                                            boolean status, String observacoes) {
}

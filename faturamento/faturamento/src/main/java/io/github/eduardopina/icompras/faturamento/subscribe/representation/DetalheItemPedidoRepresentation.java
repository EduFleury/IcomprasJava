package io.github.eduardopina.icompras.faturamento.subscribe.representation;

import java.math.BigDecimal;

public record DetalheItemPedidoRepresentation(
        Long codigoProduto,
        String nome,
        Integer quantidade,
        BigDecimal valorUnitario,

        BigDecimal total
) { }

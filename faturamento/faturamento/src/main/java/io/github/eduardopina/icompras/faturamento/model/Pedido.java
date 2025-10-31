package io.github.eduardopina.icompras.faturamento.model;

import org.apache.kafka.common.protocol.types.Field;

import java.math.BigDecimal;
import java.util.List;

public record Pedido(Long codigo, Cliente cliente, String data, BigDecimal total, List<ItemPedido> itens) {
}

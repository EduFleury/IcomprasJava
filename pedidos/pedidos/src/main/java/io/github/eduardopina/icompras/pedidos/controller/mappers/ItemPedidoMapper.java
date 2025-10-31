package io.github.eduardopina.icompras.pedidos.controller.mappers;

import io.github.eduardopina.icompras.pedidos.controller.dto.ItemPedidoDTO;
import io.github.eduardopina.icompras.pedidos.model.ItemPedido;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemPedidoMapper {
    ItemPedido map(ItemPedidoDTO dto);
}

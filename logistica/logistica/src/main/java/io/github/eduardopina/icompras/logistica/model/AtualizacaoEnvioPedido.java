package io.github.eduardopina.icompras.logistica.model;

import io.github.eduardopina.icompras.logistica.model.enums.StatusPedido;

public record AtualizacaoEnvioPedido(Long codigo, StatusPedido status, String codigoRastreio){

}

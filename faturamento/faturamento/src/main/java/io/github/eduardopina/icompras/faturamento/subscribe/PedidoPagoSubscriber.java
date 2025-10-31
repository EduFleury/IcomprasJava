package io.github.eduardopina.icompras.faturamento.subscribe;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.eduardopina.icompras.faturamento.GeradorNotaFiscalService;
import io.github.eduardopina.icompras.faturamento.mapper.PedidoMapper;
import io.github.eduardopina.icompras.faturamento.model.Pedido;
import io.github.eduardopina.icompras.faturamento.subscribe.representation.DetalhePedidoRepresentation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PedidoPagoSubscriber {

    private final ObjectMapper mapper;
    private final GeradorNotaFiscalService service;
    private final PedidoMapper pedidoMapper;

    @KafkaListener(groupId = "icompras-faturamento", topics = "${icompras.config.kafka.topics.pedidos-pagos}")
    public void listen(String json){
        System.out.println("MENSAGEM RECEBIDA: " + json);
        log.info("Recebendo pedido para faturamento: {}", json);
        try {
            var representation = mapper.readValue(json, DetalhePedidoRepresentation.class);
            Pedido pedido = pedidoMapper.map(representation);
            service.gerar(pedido);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

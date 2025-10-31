package io.github.eduardopina.icompras.pedidos.controller;

import io.github.eduardopina.icompras.pedidos.controller.dto.AdicaoNovoPagamentoDTO;
import io.github.eduardopina.icompras.pedidos.controller.dto.NovoPedidoDTO;
import io.github.eduardopina.icompras.pedidos.controller.mappers.PedidoMapper;
import io.github.eduardopina.icompras.pedidos.model.ErroResponse;
import io.github.eduardopina.icompras.pedidos.model.Exception.ItemNaoEncontradoException;
import io.github.eduardopina.icompras.pedidos.model.Exception.ValidationException;
import io.github.eduardopina.icompras.pedidos.publisher.DetalhePedidoMapper;
import io.github.eduardopina.icompras.pedidos.publisher.representation.DetalhePedidoRepresentation;
import io.github.eduardopina.icompras.pedidos.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService service;
    private final PedidoMapper mapper;
    private final DetalhePedidoMapper detalhePedidoMapper;

    @PostMapping
    public ResponseEntity<Object> criar(@RequestBody NovoPedidoDTO dto){
        try{
            var pedido = mapper.map(dto);
            var novoPedido = service.criarPedido(pedido);
            return ResponseEntity.ok(novoPedido.getCodigo());
        }catch (ValidationException e){
            var erro = new ErroResponse("Erro validação", e.getField(), e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }

    @PostMapping("pagamentos")
    public ResponseEntity<Object> adicionarNovoPagamento(@RequestBody AdicaoNovoPagamentoDTO dto){
        try {
            service.adicionarNovoPagamento(dto.codigoPedido(), dto.dados(), dto.tipoPagamento());
            return ResponseEntity.noContent().build();
        }catch (ItemNaoEncontradoException e){
            var erro = new ErroResponse("Item não Encontrado", "codigoPedido", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }

    @GetMapping("{codigo}")
    public ResponseEntity<DetalhePedidoRepresentation> obterDadosPedido(@PathVariable Long codigo){
        return service.carregarDadosCompletosPedido(codigo)
                .map(detalhePedidoMapper::map)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

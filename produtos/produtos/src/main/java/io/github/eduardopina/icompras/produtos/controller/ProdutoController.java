package io.github.eduardopina.icompras.produtos.controller;

import io.github.eduardopina.icompras.produtos.model.Produto;
import io.github.eduardopina.icompras.produtos.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService service;

    @PostMapping
    public ResponseEntity<Produto> salvar(@RequestBody Produto produto){
        service.salvar(produto);
        return ResponseEntity.ok(produto);
    }

    @GetMapping("{codigo}")
    public ResponseEntity<Produto> obterProduto(@PathVariable("codigo") Long codigo){
        return service.obterPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{codigo}")
    public ResponseEntity<Void> deletar(@PathVariable("codigo") Long codigo){

        Produto produto = service.obterPorCodigo(codigo).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Produto Inexistente"
        ));

        service.deletar(produto);
        return ResponseEntity.noContent().build();
    }
}

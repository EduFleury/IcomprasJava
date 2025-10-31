package io.github.eduardopina.icompras.pedidos.client;

import io.github.eduardopina.icompras.pedidos.client.representation.ClientRepresentation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "clientes", url = "${icompras.pedidos.clients.clientes.url}")
public interface ClientsClient {

    @GetMapping("{codigo}")
    ResponseEntity<ClientRepresentation> obterDados(@PathVariable Long codigo);

}

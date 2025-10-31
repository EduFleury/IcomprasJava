package io.github.eduardopina.icompras.clientes.service;

import io.github.eduardopina.icompras.clientes.model.Cliente;
import io.github.eduardopina.icompras.clientes.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;

    public Cliente salvar(Cliente produto){
        return repository.save(produto);
    }

    public Optional<Cliente> obterPorCodigo(Long codigo){
        return repository.findById(codigo);
    }
}

package io.github.eduardopina.icompras.clientes.repository;

import io.github.eduardopina.icompras.clientes.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}

package io.github.eduardopina.icompras.produtos.repository;

import io.github.eduardopina.icompras.produtos.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}

package com.sistema.estoque.repository;

import com.sistema.estoque.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional; 

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    
    Optional<Produto> findByCodigoBarras(String codigoBarras);
}
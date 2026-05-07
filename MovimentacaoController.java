package com.sistema.estoque.controller;

import com.sistema.estoque.model.MovimentacaoEstoque;
import com.sistema.estoque.repository.MovimentacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimentacoes")
public class MovimentacaoController {

    @Autowired
    private MovimentacaoRepository repository;

    @PostMapping
    public MovimentacaoEstoque registrar(@RequestBody MovimentacaoEstoque movimentacao) {
        // Obs: Em um cenário real, aqui entraria um Service para validar se há estoque
        // suficiente antes de registrar uma "SAIDA".
        return repository.save(movimentacao);
    }

    @GetMapping("/produto/{produtoId}")
    public List<MovimentacaoEstoque> listarPorProduto(@PathVariable Long produtoId) {
        return repository.findByProdutoId(produtoId);
    }
}

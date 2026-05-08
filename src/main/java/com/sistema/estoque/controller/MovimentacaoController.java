package com.sistema.estoque.controller;

import com.sistema.estoque.model.MovimentacaoEstoque;
import com.sistema.estoque.service.MovimentacaoService;
import com.sistema.estoque.repository.MovimentacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimentacoes")
public class MovimentacaoController {

    @Autowired
    private MovimentacaoService service; // Agora usa o Service

    @Autowired
    private MovimentacaoRepository repository;

    @PostMapping
    public MovimentacaoEstoque registrar(@RequestBody MovimentacaoEstoque movimentacao) {
        // Delega para o serviço fazer a validação de saldo
        return service.registrarMovimentacao(movimentacao);
    }

    @GetMapping("/produto/{produtoId}")
    public List<MovimentacaoEstoque> listarPorProduto(@PathVariable Long produtoId) {
        return repository.findByProdutoId(produtoId);
    }
}

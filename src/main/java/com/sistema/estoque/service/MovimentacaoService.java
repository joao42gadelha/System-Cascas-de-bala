package com.sistema.estoque.service;

import com.sistema.estoque.model.MovimentacaoEstoque;
import com.sistema.estoque.model.Produto;
import com.sistema.estoque.repository.MovimentacaoRepository;
import com.sistema.estoque.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovimentacaoService {

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Transactional
    public MovimentacaoEstoque registrarMovimentacao(MovimentacaoEstoque movimentacao) {
        // Busca o produto no banco de dados
        Produto produto = produtoRepository.findById(movimentacao.getProduto().getId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado."));

        // Aplica a regra de negócio
        if ("SAIDA".equalsIgnoreCase(movimentacao.getTipoMovimentacao())) {
            if (produto.getQuantidadeEstoque() < movimentacao.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente para esta saída.");
            }
            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - movimentacao.getQuantidade());
        } else if ("ENTRADA".equalsIgnoreCase(movimentacao.getTipoMovimentacao())) {
            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + movimentacao.getQuantidade());
        } else {
            throw new RuntimeException("Tipo de movimentação inválido. Use ENTRADA ou SAIDA.");
        }

        // Salva o novo saldo do produto e registra o histórico da movimentação
        produtoRepository.save(produto);
        return movimentacaoRepository.save(movimentacao);
    }
}

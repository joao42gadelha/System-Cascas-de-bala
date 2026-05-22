package com.sistema.estoque.view;

import com.sistema.estoque.model.Produto;
import com.sistema.estoque.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

@Component
public class TelaListaProdutos extends JFrame {

    private final ProdutoRepository produtoRepository;
    private JTable tabelaProdutos;
    private DefaultTableModel modeloTabela;

    @Autowired
    public TelaListaProdutos(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;

        setTitle("Produtos Cadastrados - Cascas de bala e CIA");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Tabela
        String[] colunas = {"Código de Barras", "Descrição", "Valor (R$)", "Estoque", "Tipo"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaProdutos = new JTable(modeloTabela);
        tabelaProdutos.setRowHeight(25);
        add(new JScrollPane(tabelaProdutos), BorderLayout.CENTER);

        // Painel Inferior
        JPanel painelBaixo = new JPanel();
        JButton btnAtualizar = new JButton("Atualizar Dados");
        JButton btnFechar = new JButton("Fechar");
        
        painelBaixo.add(btnAtualizar);
        painelBaixo.add(btnFechar);
        add(painelBaixo, BorderLayout.SOUTH);

        // Eventos
        btnAtualizar.addActionListener(e -> atualizarTabela());
        btnFechar.addActionListener(e -> setVisible(false));
    }

    // Método que busca no banco e preenche a tabela
    public void atualizarTabela() {
        modeloTabela.setRowCount(0); // Limpa os dados antigos da tela
        List<Produto> produtos = produtoRepository.findAll();
        
        for (Produto p : produtos) {
            modeloTabela.addRow(new Object[]{
                    p.getCodigoBarras(),
                    p.getDescricao(),
                    String.format("%.2f", p.getValor()),
                    p.getQuantidadeEstoque(),
                    p.getTipoProduto()
            });
        }
    }
}
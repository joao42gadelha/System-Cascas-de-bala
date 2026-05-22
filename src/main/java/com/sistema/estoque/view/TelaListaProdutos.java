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
        setSize(900, 500);
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
        tabelaProdutos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
        add(new JScrollPane(tabelaProdutos), BorderLayout.CENTER);

        // Painel Inferior
        JPanel painelBaixo = new JPanel();
        JButton btnRecarregar = new JButton("Recarregar Lista");
        JButton btnAtualizarEstoque = new JButton("Atualizar Estoque");
        JButton btnEditarProduto = new JButton("Editar Dados (Nome/Preço)");
        JButton btnFechar = new JButton("Fechar");
        
        painelBaixo.add(btnRecarregar);
        painelBaixo.add(btnAtualizarEstoque);
        painelBaixo.add(btnEditarProduto);
        painelBaixo.add(btnFechar);
        add(painelBaixo, BorderLayout.SOUTH);

        // Eventos
        btnRecarregar.addActionListener(e -> atualizarTabela());
        btnAtualizarEstoque.addActionListener(e -> alterarEstoqueProduto());
        btnEditarProduto.addActionListener(e -> editarDadosProduto());
        btnFechar.addActionListener(e -> setVisible(false));
    }

    public void atualizarTabela() {
        modeloTabela.setRowCount(0); 
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

    private void alterarEstoqueProduto() {
        int linhaSelecionada = tabelaProdutos.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String codigoBarras = (String) modeloTabela.getValueAt(linhaSelecionada, 0);
        
        try {
            Produto produto = produtoRepository.findByCodigoBarras(codigoBarras)
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado."));

            String input = JOptionPane.showInputDialog(this, 
                    "Produto: " + produto.getDescricao() + "\nEstoque atual: " + produto.getQuantidadeEstoque() + 
                    "\n\nDigite a quantidade a ADICIONAR (use '-' para remover):", 
                    "Atualizar Estoque", JOptionPane.QUESTION_MESSAGE);

            if (input != null && !input.trim().isEmpty()) {
                int quantidade = Integer.parseInt(input.trim());
                int novoEstoque = produto.getQuantidadeEstoque() + quantidade;
                
                if (novoEstoque < 0) {
                    JOptionPane.showMessageDialog(this, "Erro! O estoque não pode ficar negativo.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                produto.setQuantidadeEstoque(novoEstoque);
                produtoRepository.save(produto);
                
                JOptionPane.showMessageDialog(this, "Estoque atualizado com sucesso!");
                atualizarTabela(); 
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Digite apenas números inteiros.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarDadosProduto() {
        int linhaSelecionada = tabelaProdutos.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String codigoBarras = (String) modeloTabela.getValueAt(linhaSelecionada, 0);
        
        try {
            Produto produto = produtoRepository.findByCodigoBarras(codigoBarras)
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado."));

            // Atualizar Nome
            String novoNome = (String) JOptionPane.showInputDialog(this, 
                    "Alterar Nome do Produto:", 
                    "Editar Nome", JOptionPane.QUESTION_MESSAGE, null, null, produto.getDescricao());

            if (novoNome != null && !novoNome.trim().isEmpty()) {
                produto.setDescricao(novoNome.trim());
            }

            // Atualizar Preço
            String novoPrecoStr = (String) JOptionPane.showInputDialog(this, 
                    "Alterar Valor do Produto (Use ponto em vez de vírgula):", 
                    "Editar Preço", JOptionPane.QUESTION_MESSAGE, null, null, String.valueOf(produto.getValor()));

            if (novoPrecoStr != null && !novoPrecoStr.trim().isEmpty()) {
                double novoPreco = Double.parseDouble(novoPrecoStr.trim());
                produto.setValor(novoPreco);
            }

            // Atualizar Tipo
            String novoTipo = (String) JOptionPane.showInputDialog(this, 
                    "Alterar Tipo do Produto:", 
                    "Editar Tipo", JOptionPane.QUESTION_MESSAGE, null, null, produto.getTipoProduto());

            if (novoTipo != null && !novoTipo.trim().isEmpty()) {
                produto.setTipoProduto(novoTipo.trim());
            }

            // Salva as alterações
            produtoRepository.save(produto);
            JOptionPane.showMessageDialog(this, "Dados do produto atualizados com sucesso!");
            atualizarTabela();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor inválido. Certifique-se de usar ponto (ex: 10.50).", "Erro de Digitação", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao editar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
package com.sistema.estoque.view;

import com.sistema.estoque.model.Produto;
import com.sistema.estoque.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class TelaCadastro extends JFrame {

    private final ProdutoRepository produtoRepository;

    private JTextField txtCodigo, txtDescricao, txtValor, txtQuantidade, txtTipo;

    @Autowired
    public TelaCadastro(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;

        setTitle("Registo de Produtos - Cascas de bala e CIA");
        setSize(400, 350);
        setLayout(new GridLayout(6, 2, 10, 10));
        setLocationRelativeTo(null);

        // Campos do Formulário
        add(new JLabel("  Código de Barras:"));
        txtCodigo = new JTextField();
        add(txtCodigo);

        add(new JLabel("  Descrição:"));
        txtDescricao = new JTextField();
        add(txtDescricao);

        add(new JLabel("  Preço Unitário:"));
        txtValor = new JTextField();
        add(txtValor);

        add(new JLabel("  Stock Inicial:"));
        txtQuantidade = new JTextField();
        add(txtQuantidade);

        add(new JLabel("  Categoria/Tipo:"));
        txtTipo = new JTextField();
        add(txtTipo);

        JButton btnSalvar = new JButton("Guardar Produto");
        JButton btnVoltar = new JButton("Cancelar");

        add(btnSalvar);
        add(btnVoltar);

        // Ações
        btnSalvar.addActionListener(e -> guardarProduto());
        btnVoltar.addActionListener(e -> setVisible(false));
    }

    private void guardarProduto() {
        try {
            Produto p = new Produto();
            p.setCodigoBarras(txtCodigo.getText());
            p.setDescricao(txtDescricao.getText());
            p.setValor(Double.parseDouble(txtValor.getText()));
            p.setQuantidadeEstoque(Integer.parseInt(txtQuantidade.getText()));
            p.setTipoProduto(txtTipo.getText());

            produtoRepository.save(p);
            
            JOptionPane.showMessageDialog(this, "Produto registado com sucesso!");
            limparCampos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro nos dados: " + ex.getMessage());
        }
    }

    private void limparCampos() {
        txtCodigo.setText("");
        txtDescricao.setText("");
        txtValor.setText("");
        txtQuantidade.setText("");
        txtTipo.setText("");
    }
}
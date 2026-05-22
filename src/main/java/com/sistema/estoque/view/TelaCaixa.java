package com.sistema.estoque.view;

import com.sistema.estoque.model.MovimentacaoEstoque;
import com.sistema.estoque.model.Produto;
import com.sistema.estoque.repository.ProdutoRepository;
import com.sistema.estoque.service.ImpressoraCupom;
import com.sistema.estoque.service.MovimentacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class TelaCaixa extends JFrame {

    private final ProdutoRepository produtoRepository;
    private final MovimentacaoService movimentacaoService;
    private final TelaCadastro telaCadastro;
    private final TelaConfiguracaoLeitor telaConfiguracaoLeitor;
    private final ImpressoraCupom impressoraCupom;
    private final TelaListaProdutos telaListaProdutos;

    private JTextField campoCodigoBarras;
    private JTable tabelaProdutos;
    private DefaultTableModel modeloTabela;
    private JLabel labelTotal;
    private JButton btnPagamento;
    private JButton btnAbrirCadastro;
    private JButton btnConfigurarLeitor;
    private JButton btnVerProdutos;
    private JCheckBox chkImprimirCupom; 

    private List<Produto> carrinho = new ArrayList<>();
    private double valorTotal = 0.0;

    @Autowired
    public TelaCaixa(ProdutoRepository produtoRepository, 
                     MovimentacaoService movimentacaoService, 
                     TelaCadastro telaCadastro, 
                     TelaConfiguracaoLeitor telaConfiguracaoLeitor,
                     ImpressoraCupom impressoraCupom,
                     TelaListaProdutos telaListaProdutos) { 
        
        this.produtoRepository = produtoRepository;
        this.movimentacaoService = movimentacaoService;
        this.telaCadastro = telaCadastro;
        this.telaConfiguracaoLeitor = telaConfiguracaoLeitor;
        this.impressoraCupom = impressoraCupom; 
        this.telaListaProdutos = telaListaProdutos;

        // 1. Configurações da Janela
        setTitle("PDV - Cascas de bala e CIA");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 2. Painel Superior
        JPanel painelTopo = new JPanel(new BorderLayout(10, 10));
        painelTopo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel tituloDaLoja = new JLabel("SUPERMERCADO CASCAS DE BALA E CIA", SwingConstants.CENTER);
        tituloDaLoja.setFont(new Font("Arial", Font.BOLD, 28));
        tituloDaLoja.setForeground(new Color(0, 102, 204));
        painelTopo.add(tituloDaLoja, BorderLayout.NORTH);

        JPanel painelAcoes = new JPanel(new BorderLayout());
        JPanel painelLeitor = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelLeitor.add(new JLabel("Bipar Código: "));
        campoCodigoBarras = new JTextField(20);
        campoCodigoBarras.setFont(new Font("Arial", Font.PLAIN, 18));
        painelLeitor.add(campoCodigoBarras);

        JPanel painelBotoesConfig = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnVerProdutos = new JButton("Ver Produtos");
        btnAbrirCadastro = new JButton("Cadastrar");
        btnConfigurarLeitor = new JButton("Testar Leitor");
        
        painelBotoesConfig.add(btnVerProdutos);
        painelBotoesConfig.add(btnAbrirCadastro);
        painelBotoesConfig.add(btnConfigurarLeitor);

        painelAcoes.add(painelLeitor, BorderLayout.WEST);
        painelAcoes.add(painelBotoesConfig, BorderLayout.EAST);
        painelTopo.add(painelAcoes, BorderLayout.SOUTH);

        add(painelTopo, BorderLayout.NORTH);

        // 3. Painel Central (Tabela)
        String[] colunas = {"Item", "Descrição", "Qtd", "Preço (R$)", "Subtotal (R$)"};
        modeloTabela = new DefaultTableModel(colunas, 0);
        tabelaProdutos = new JTable(modeloTabela);
        tabelaProdutos.setRowHeight(25);
        add(new JScrollPane(tabelaProdutos), BorderLayout.CENTER);

        // 4. Painel Inferior 
        JPanel painelBaixo = new JPanel(new BorderLayout(10, 10));
        painelBaixo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        painelBaixo.setBackground(new Color(45, 45, 45));

        labelTotal = new JLabel("Total: R$ 0.00");
        labelTotal.setFont(new Font("Arial", Font.BOLD, 36));
        labelTotal.setForeground(Color.GREEN);
        painelBaixo.add(labelTotal, BorderLayout.WEST);

        JPanel painelAcoesPagamento = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelAcoesPagamento.setBackground(new Color(45, 45, 45));

        chkImprimirCupom = new JCheckBox("Imprimir Cupom", true);
        chkImprimirCupom.setFont(new Font("Arial", Font.PLAIN, 16));
        chkImprimirCupom.setForeground(Color.WHITE);
        chkImprimirCupom.setBackground(new Color(45, 45, 45));

        btnPagamento = new JButton("FINALIZAR COMPRA");
        btnPagamento.setFont(new Font("Arial", Font.BOLD, 20));
        btnPagamento.setBackground(new Color(0, 153, 51));
        btnPagamento.setForeground(Color.WHITE);

        painelAcoesPagamento.add(chkImprimirCupom);
        painelAcoesPagamento.add(btnPagamento);

        painelBaixo.add(painelAcoesPagamento, BorderLayout.EAST);

        add(painelBaixo, BorderLayout.SOUTH);

        // 5. Eventos
        campoCodigoBarras.addActionListener(e -> adicionarAoCarrinho());
        btnPagamento.addActionListener(e -> finalizarVenda());
        btnAbrirCadastro.addActionListener(e -> telaCadastro.setVisible(true));
        btnConfigurarLeitor.addActionListener(e -> telaConfiguracaoLeitor.setVisible(true));
        
        // Ação do novo botão de ver produtos
        btnVerProdutos.addActionListener(e -> {
            telaListaProdutos.atualizarTabela(); // Carrega os dados atualizados do banco
            telaListaProdutos.setVisible(true);  // Abre a janela
        });
    }

    private void adicionarAoCarrinho() {
        String codigo = campoCodigoBarras.getText().trim();
        produtoRepository.findByCodigoBarras(codigo).ifPresentOrElse(p -> {
            carrinho.add(p);
            double valor = p.getValor();
            modeloTabela.addRow(new Object[]{carrinho.size(), p.getDescricao(), 1, String.format("%.2f", valor), String.format("%.2f", valor)});
            valorTotal += valor;
            labelTotal.setText(String.format("Total: R$ %.2f", valorTotal));
        }, () -> JOptionPane.showMessageDialog(this, "Produto não encontrado!"));
        
        campoCodigoBarras.setText("");
        campoCodigoBarras.requestFocus();
    }

    private void finalizarVenda() {
        if (carrinho.isEmpty()) return;
        
        String[] formas = {"Dinheiro", "Cartão", "PIX"};
        String f = (String) JOptionPane.showInputDialog(this, "Total: R$ " + String.format("%.2f", valorTotal), "Pagamento", JOptionPane.PLAIN_MESSAGE, null, formas, formas[0]);

        if (f != null) {
            carrinho.forEach(p -> {
                MovimentacaoEstoque m = new MovimentacaoEstoque();
                m.setProduto(p);
                m.setQuantidade(1);
                m.setTipoMovimentacao("SAIDA");
                movimentacaoService.registrarMovimentacao(m);
            });
            
            String mensagemFinal = "Venda finalizada!";

            if (chkImprimirCupom.isSelected()) {
                impressoraCupom.imprimirCupom(carrinho, valorTotal, f);
                mensagemFinal += " Cupom gerado no console.";
            }

            JOptionPane.showMessageDialog(this, mensagemFinal);
            
            carrinho.clear();
            modeloTabela.setRowCount(0);
            valorTotal = 0.0;
            labelTotal.setText("Total: R$ 0.00");
        }
    }
}
package com.sistema.estoque.view;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class TelaConfiguracaoLeitor extends JFrame {

    private JTextField campoTeste;
    private JTextArea areaLog;

    public TelaConfiguracaoLeitor() {
        setTitle("Configuração e Teste do Leitor - Cascas de bala e CIA");
        setSize(500, 400);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);

        // Painel de Instruções
        JPanel painelTopo = new JPanel(new GridLayout(2, 1));
        painelTopo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel lblInstrucao = new JLabel("Bipe qualquer código de barras no campo abaixo para testar a comunicação:", SwingConstants.CENTER);
        lblInstrucao.setFont(new Font("Arial", Font.BOLD, 12));
        painelTopo.add(lblInstrucao);

        campoTeste = new JTextField();
        campoTeste.setFont(new Font("Arial", Font.PLAIN, 18));
        painelTopo.add(campoTeste);

        add(painelTopo, BorderLayout.NORTH);

        // Painel de Log (Mostra o que o sistema "enxergou")
        areaLog = new JTextArea();
        areaLog.setEditable(false);
        areaLog.setFont(new Font("Monospaced", Font.PLAIN, 14));
        areaLog.setBackground(Color.BLACK);
        areaLog.setForeground(Color.GREEN);
        
        JScrollPane scrollLog = new JScrollPane(areaLog);
        scrollLog.setBorder(BorderFactory.createTitledBorder("Log de Leitura (Eventos do Teclado)"));
        add(scrollLog, BorderLayout.CENTER);

        // Painel Inferior
        JPanel painelBaixo = new JPanel();
        JButton btnLimpar = new JButton("Limpar Log");
        JButton btnFechar = new JButton("Fechar");

        painelBaixo.add(btnLimpar);
        painelBaixo.add(btnFechar);
        add(painelBaixo, BorderLayout.SOUTH);

        // Ações
        campoTeste.addActionListener(e -> registrarBipe());
        
        btnLimpar.addActionListener(e -> {
            areaLog.setText("");
            campoTeste.requestFocus();
        });
        
        btnFechar.addActionListener(e -> setVisible(false));
    }

    private void registrarBipe() {
        String leitura = campoTeste.getText().trim();
        if (!leitura.isEmpty()) {
            areaLog.append(">> SUCESSO! Código capturado: " + leitura + "\n");
            areaLog.append(">> Sinal de 'ENTER' recebido corretamente.\n");
            areaLog.append("--------------------------------------------------\n");
            campoTeste.setText("");
            campoTeste.requestFocus();
        }
    }
}
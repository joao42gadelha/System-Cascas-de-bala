package com.sistema.estoque;

import com.sistema.estoque.view.TelaCaixa;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class EstoqueApplication {

    public static void main(String[] args) {
        // Configura o Spring para permitir janelas nativas (Swing)
        ConfigurableApplicationContext context = new SpringApplicationBuilder(EstoqueApplication.class)
                .headless(false)
                .run(args);

        // Inicia a interface gráfica passando o controle para o Java Swing
        java.awt.EventQueue.invokeLater(() -> {
            TelaCaixa tela = context.getBean(TelaCaixa.class);
            tela.setVisible(true);
        });
    }
}
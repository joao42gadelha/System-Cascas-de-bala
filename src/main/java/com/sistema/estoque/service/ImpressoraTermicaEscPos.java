package com.sistema.estoque.service;

import com.sistema.estoque.model.Produto;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ImpressoraTermicaEscPos implements ImpressoraCupom {

    @Override
    public void imprimirCupom(List<Produto> itens, double total, String formaPagamento) {
        StringBuilder cupom = new StringBuilder();
        
        cupom.append("--------------------------------\n");
        cupom.append("    CASCAS DE BALA E CIA        \n");
        cupom.append("--------------------------------\n");
        cupom.append("        CUPOM NAO FISCAL        \n");
        cupom.append("--------------------------------\n");
        cupom.append("ITEM | DESC | QTD | VALOR\n");
        
        for (int i = 0; i < itens.size(); i++) {
            Produto p = itens.get(i);
            cupom.append(String.format("%02d %-15s %02d R$%.2f\n", 
                    (i+1), p.getDescricao(), 1, p.getValor()));
        }
        
        cupom.append("--------------------------------\n");
        cupom.append(String.format("TOTAL A PAGAR:       R$ %.2f\n", total));
        cupom.append(String.format("FORMA DE PAGAMENTO:  %s\n", formaPagamento));
        cupom.append("--------------------------------\n");
        cupom.append("     Obrigado pela compra!      \n");
        cupom.append("\n\n\n"); 

        System.out.println(cupom.toString());
    }
}
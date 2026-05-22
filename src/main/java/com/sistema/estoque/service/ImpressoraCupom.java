package com.sistema.estoque.service;

import com.sistema.estoque.model.Produto;
import java.util.List;

public interface ImpressoraCupom {
    void imprimirCupom(List<Produto> itens, double total, String formaPagamento);
}
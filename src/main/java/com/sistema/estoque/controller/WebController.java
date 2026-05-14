package com.sistema.estoque.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String paginaInicial(Model model) {
        return "index";
    }

    @GetMapping("/produtos/novo")
    public String paginaCadastroProduto() {
        return "cadastro-produto";
    }

    @GetMapping("/movimentacoes/nova")
    public String paginaMovimentacao() {
        return "movimentacao";
    }
}
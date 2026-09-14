package com.senai.SAEP.controller;

import com.senai.SAEP.entity.ProdutoEntity;
import com.senai.SAEP.service.ProdutoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping("/cadastro-produto")
    public String cadastroProduto(@RequestParam(value = "busca", required = false) String busca, HttpSession session, Model model) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        model.addAttribute("produtos", produtoService.buscarPorNome(busca));
        model.addAttribute("produto", new ProdutoEntity());
        model.addAttribute("busca", busca);
        return "cadastro-produto";
    }

    @PostMapping("/produtos/salvar")
    public String salvarProduto(@ModelAttribute ProdutoEntity produto, HttpSession session, Model model) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        if (produto.getNome() == null || produto.getNome().trim().isEmpty() ||
                produto.getPreco() == null || produto.getPreco() < 0 ||
                produto.getQuantidade() == null || produto.getQuantidade() < 0) {

            model.addAttribute("erro", "Preencha todos os campos com valores válidos.");
            model.addAttribute("produtos", produtoService.listarTodos());
            model.addAttribute("produto", produto);
            return "cadastro-produto";
        }

        produtoService.salvar(produto);
        return "redirect:/cadastro-produto";
    }

    @GetMapping("/produtos/editar/{id}")
    public String editarProduto(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        produtoService.buscarPorId(id).ifPresent(p -> model.addAttribute("produto", p));
        model.addAttribute("produtos", produtoService.listarTodos());
        return "cadastro-produto";
    }

    @GetMapping("/produtos/deletar/{id}")
    public String deletarProduto(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        produtoService.deletar(id);
        return "redirect:/cadastro-produto";
    }
}
package com.senai.SAEP.service;

import com.senai.SAEP.entity.ProdutoEntity;
import com.senai.SAEP.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<ProdutoEntity> listarTodos() {
        return produtoRepository.findAll();
    }

    public List<ProdutoEntity> buscarPorNome(String nome) {
        if (nome == null || nome.isBlank()) {
            return produtoRepository.findAll();
        }
        return produtoRepository.findByNomeContainingIgnoreCase(nome);
    }

    public void salvar(ProdutoEntity produto) {
        produtoRepository.save(produto);
    }

    public Optional<ProdutoEntity> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }

    public void deletar(Long id) {
        produtoRepository.deleteById(id);
    }
}

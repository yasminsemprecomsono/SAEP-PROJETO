package com.senai.SAEP.service;

import com.senai.SAEP.entity.MovimentacaoEntity;
import com.senai.SAEP.entity.ProdutoEntity;
import com.senai.SAEP.entity.UsuarioEntity;
import com.senai.SAEP.repository.MovimentacaoRepository;
import com.senai.SAEP.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovimentacaoService {

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;


    // Lista todas as movimentações
    public List<MovimentacaoEntity> listarTodas() {
        return movimentacaoRepository.findAllByOrderByDataDesc();
    }


    // Registra uma entrada ou saída de estoque
    public void registrarMovimentacao(
            Long produtoId,
            String tipo,
            Integer quantidade,
            UsuarioEntity usuario) {

        // REGRA 1:
        // A quantidade deve ser maior que zero

        if (quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException(
                    "A quantidade deve ser maior que zero."
            );
        }


        // REGRA 2:
        // O produto precisa existir

        ProdutoEntity produto = produtoRepository
                .findById(produtoId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Produto não encontrado."
                        )
                );


        // REGRA 3:
        // O tipo deve ser ENTRADA ou SAIDA

        if (!tipo.equals("ENTRADA") && !tipo.equals("SAIDA")) {

            throw new IllegalArgumentException(
                    "Tipo de movimentação inválido."
            );
        }


        // REGRA 4:
        // ENTRADA aumenta a quantidade

        if (tipo.equals("ENTRADA")) {

            produto.setQuantidade(
                    produto.getQuantidade() + quantidade
            );
        }


        // REGRA 5:
        // SAIDA diminui a quantidade

        if (tipo.equals("SAIDA")) {

            // Não permite estoque negativo

            if (quantidade > produto.getQuantidade()) {

                throw new IllegalArgumentException(
                        "Não é possível realizar a saída. " +
                                "A quantidade solicitada é maior que o estoque disponível."
                );
            }

            produto.setQuantidade(
                    produto.getQuantidade() - quantidade
            );
        }


        // Atualiza o produto no banco

        produtoRepository.save(produto);


        // Cria o registro da movimentação

        MovimentacaoEntity movimentacao =
                new MovimentacaoEntity();

        movimentacao.setProduto(produto);

        movimentacao.setTipo(tipo);

        movimentacao.setQuantidade(quantidade);

        movimentacao.setUsuario(usuario);

        // Data gerada automaticamente

        movimentacao.setData(LocalDateTime.now());


        // Salva a movimentação no banco

        movimentacaoRepository.save(movimentacao);
    }
}
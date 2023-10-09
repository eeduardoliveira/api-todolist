package com.example.demo.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("estudo")
public class ToDoController {
    //Primeira camada de acesso da requisicao do usuario

    /**
     * Metodo de Acesso HTTP
     * Get - Buscar
     * Post -Adicionar
     * Put - Alterar
     * Delete - Remover
     * Patch - Alterar somente uma parte da informacao
     */


    @GetMapping(path = {"primeira-mensagem"})
    public String primeiraMensagem () {
        return "Funcionou";
    }
}

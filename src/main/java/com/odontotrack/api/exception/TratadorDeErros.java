package com.odontotrack.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice // Avisa o Spring: "Sou o megafone oficial de erros"
public class TratadorDeErros {

    // TRATANDO ERROS DE VALIDAÇÃO (Ex: E-mail em branco, CPF vazio)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarErro400(MethodArgumentNotValidException ex) {
        // Pega todos os campos que deram erro e transforma num DTO limpo
        var erros = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(erros.stream().map(DadosErroValidacao::new).toList());
    }

    // TRATANDO ERRO 403 (Sem permissão / Token inválido)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity tratarErroAcessoNegado() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado. Verifique se você está logado e se o seu Token é válido.");
    }

    // TRATANDO ERROS DE REGRA DE NEGÓCIO (Aqueles "throw new RuntimeException" que são feitos no Service)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity tratarErroRegraDeNegocio(RuntimeException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    // DTO INTERNO SÓ PARA FORMATAR ERROS DE DIGITAÇÃO
    private record DadosErroValidacao(String campo, String mensagem) {
        public DadosErroValidacao(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }
}
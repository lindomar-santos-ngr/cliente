package br.com.builders.controller.interceptor;

import br.com.builders.controller.error.RespostaErro;
import br.com.builders.exception.RecursoNaoEncontradoException;
import br.com.builders.exception.SemConteundoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ManipuladorDeExececao extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ManipuladorDeExececao.class);

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<RespostaErro> handleAllExceptions(Exception ex, WebRequest request) {
        return getRespostaErroResponseEntity(ex, HttpStatus.INTERNAL_SERVER_ERROR, "ERROR");
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public final ResponseEntity<RespostaErro> handleAllExceptions(RecursoNaoEncontradoException ex, WebRequest request) {
        return getRespostaErroResponseEntity(ex, HttpStatus.NOT_FOUND, "ERROR");
    }

    @ExceptionHandler(SemConteundoException.class)
    public final ResponseEntity<RespostaErro> handleAllExceptions(SemConteundoException ex, WebRequest request) {
        return getRespostaErroResponseEntity(ex, HttpStatus.BAD_REQUEST, "ERROR");
    }

    private ResponseEntity<RespostaErro> getRespostaErroResponseEntity(Exception ex, HttpStatus internalServerError, String error) {
        RespostaErro exceptionResponse = new RespostaErro();
        exceptionResponse.setCodigo(internalServerError.value());
        exceptionResponse.setMensagem(ex.getMessage());
        exceptionResponse.setStatus(error);

        return new ResponseEntity<>(exceptionResponse, internalServerError);
    }
}

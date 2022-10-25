package one.digitalinnovation.parking.handler;

import lombok.extern.slf4j.Slf4j;
import one.digitalinnovation.parking.exception.ParkingNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalHandlerException extends ResponseEntityExceptionHandler {

    private static final String PEDIDO_NAO_ENCONTRADO = "Parking not found";

    @ExceptionHandler(value = ParkingNotFoundException.class)
    protected ResponseEntity<MensagemErro> handlerPartidoNaoEncontrado(ParkingNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MensagemErro(PEDIDO_NAO_ENCONTRADO));
    }

    protected ResponseEntity<Object> handleMethodArgumentNotValid
            (MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> validationList = ex.getBindingResult().getFieldErrors().stream().map(fieldError ->
                        "Campo: " + fieldError.getField() +
                                " || " + fieldError.getDefaultMessage())
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MensagemErro(validationList));
    }

}

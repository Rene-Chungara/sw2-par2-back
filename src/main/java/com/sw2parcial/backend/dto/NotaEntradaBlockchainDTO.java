package com.sw2parcial.backend.dto;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class NotaEntradaBlockchainDTO {
    public Integer id;
    public String lote;
    public String fecha;
    public String proveedor;
}

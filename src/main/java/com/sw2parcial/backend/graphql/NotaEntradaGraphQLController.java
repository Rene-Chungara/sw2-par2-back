package com.sw2parcial.backend.graphql;

import com.sw2parcial.backend.dto.NotaEntradaBlockchainDTO;
import com.sw2parcial.backend.model.NotaEntrada;
import com.sw2parcial.backend.model.Proveedor;
import com.sw2parcial.backend.repository.NotaEntradaRepository;
import com.sw2parcial.backend.repository.ProveedorRepository;

import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import reactor.core.publisher.Mono;
import java.net.http.*;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@Controller
public class NotaEntradaGraphQLController {

    @Autowired
    private NotaEntradaRepository notaEntradaRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @QueryMapping
    public List<NotaEntrada> listarNotasEntrada() {
        return notaEntradaRepository.findAll();
    }

    @QueryMapping
    public NotaEntrada obtenerNotaEntrada(@Argument Integer id) {
        return notaEntradaRepository.findById(id).orElse(null);
    }

    @QueryMapping
    public List<NotaEntrada> buscarNotaEntradaPorProveedor(@Argument String nombre) {
        return notaEntradaRepository.findByProveedorNombreContainingIgnoreCase(nombre);
    }

    @MutationMapping
    public NotaEntrada crearNotaEntrada(@Argument String fecha, @Argument String lote, @Argument Float costoTotal,
            @Argument Integer proveedorId) {
        Proveedor proveedor = proveedorRepository.findById(proveedorId).orElseThrow();
        NotaEntrada nota = new NotaEntrada();
        nota.setFecha(LocalDate.parse(fecha));
        nota.setLote(lote);
        nota.setCostoTotal(costoTotal);
        nota.setProveedor(proveedor);

        NotaEntrada saved = notaEntradaRepository.save(nota);

        // 🔗 Enviar datos al microservicio blockchain
        try {
            HttpClient client = HttpClient.newHttpClient();
            String jsonBody = String.format("""
                    {
                      "lote": "%s",
                      "id_proveedor": %d,
                      "fecha": "%s"
                    }
                    """,
                    saved.getLote(),
                    proveedorId,
                    saved.getFecha().toString());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://blockchain-service:4000/notas/registrar"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        System.out.println("✅ Blockchain response: " + response.body());
                    })
                    .exceptionally(e -> {
                        System.err.println("❌ Error registrando en blockchain: " + e.getMessage());
                        return null;
                    });
        } catch (Exception e) {
            System.err.println("❌ Excepción al conectar con blockchain: " + e.getMessage());
        }
        return saved;
    }

    @MutationMapping
    public NotaEntrada actualizarNotaEntrada(@Argument Integer id, @Argument String fecha, @Argument String lote,
            @Argument Float costoTotal, @Argument Integer proveedorId) {
        NotaEntrada nota = notaEntradaRepository.findById(id).orElse(null);
        if (nota == null)
            return null;
        Proveedor proveedor = proveedorRepository.findById(proveedorId).orElseThrow();
        nota.setFecha(LocalDate.parse(fecha));
        nota.setLote(lote);
        nota.setCostoTotal(costoTotal);
        nota.setProveedor(proveedor);
        return notaEntradaRepository.save(nota);
    }

    @MutationMapping
    public Boolean eliminarNotaEntrada(@Argument Integer id) {
        if (!notaEntradaRepository.existsById(id))
            return false;
        notaEntradaRepository.deleteById(id);
        return true;
    }

    private final WebClient webClient = WebClient.create("http://localhost:3000"); 

    private void registrarEnBlockchain(NotaEntrada nota) {
        NotaEntradaBlockchainDTO dto = new NotaEntradaBlockchainDTO();
        dto.id = nota.getId();
        dto.lote = nota.getLote();
        dto.fecha = nota.getFecha().toString();
        dto.proveedor = nota.getProveedor().getNombre();

        webClient.post()
                .uri("/api/blockchain/agregar")
                .body(Mono.just(dto), NotaEntradaBlockchainDTO.class)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(); // también puedes manejar la respuesta si quieres guardar el QR
    }
}



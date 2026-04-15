package com.pcabrera.hotel_search.infrastructure.adapter.in;

import com.pcabrera.hotel_search.application.port.in.CountSearchPortIn;
import com.pcabrera.hotel_search.application.port.in.CreateSearchPortIn;
import com.pcabrera.hotel_search.infrastructure.adapter.in.domain.CountResponseDomain;
import com.pcabrera.hotel_search.infrastructure.adapter.in.domain.SearchRequestDomain;
import com.pcabrera.hotel_search.infrastructure.adapter.in.domain.SearchResponseDomain;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping
@Tag(name = "Search", description = "API para búsquedas de disponibilidad de hoteles")
public class SearchController {

    private final CreateSearchPortIn createSearchPortIn;
    private final CountSearchPortIn countSearchPortIn;

    public SearchController(
            CreateSearchPortIn createSearchPortIn,
            CountSearchPortIn countSearchPortIn
    ) {
        this.createSearchPortIn = createSearchPortIn;
        this.countSearchPortIn = countSearchPortIn;
    }

    @PostMapping("/search")
    @Operation(summary = "Crear una búsqueda de disponibilidad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Request inválido")
    })
    public ResponseEntity<SearchResponseDomain> search(@Valid @RequestBody SearchRequestDomain request) {
        log.info("POST /search - request recibido: hotelId={}, checkIn={}, checkOut={}, ages={}",
                request.hotelId(),
                request.checkIn(),
                request.checkOut(),
                request.ages());

        SearchResponseDomain response = createSearchPortIn.execute(request);

        log.info("POST /search - búsqueda creada con searchId={}", response.searchId());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/count")
    @Operation(summary = "Obtener cantidad de búsquedas iguales")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conteo obtenido correctamente"),
            @ApiResponse(responseCode = "404", description = "searchId no encontrado")
    })
    public ResponseEntity<CountResponseDomain> count(@RequestParam String searchId) {
        log.info("GET /count - searchId={}", searchId);

        CountResponseDomain response = countSearchPortIn.execute(searchId);

        log.info("GET /count - resultado para searchId={} count={}", searchId, response.count());

        return ResponseEntity.ok(response);
    }
}
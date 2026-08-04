package com.actuation_system.pedido.controller;

import com.actuation_system.comanda.entity.Comanda;
import com.actuation_system.exceptions.ErrorResponse;
import com.actuation_system.pedido.dto.AlterarQuantidadeRequestDTO;
import com.actuation_system.pedido.dto.ItemPedidoRequestDTO;
import com.actuation_system.pedido.dto.PedidoRequestDTO;
import com.actuation_system.pedido.dto.PedidoResponseDTO;
import com.actuation_system.pedido.entity.ItemPedido;
import com.actuation_system.pedido.entity.Pedido;
import com.actuation_system.pedido.mapper.ItemPedidoMapper;
import com.actuation_system.pedido.mapper.PedidoMapper;
import com.actuation_system.pedido.service.PedidoService;
import com.actuation_system.produto.entity.Produto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final ItemPedidoMapper itemPedidoMapper;
    private final PedidoMapper pedidoMapper;


    @GetMapping
    @Operation(
            summary = "Returns a paginated list of orders",
            description = "Retrieves all orders using pagination and sorting."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Orders retrieved successfully.",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = PedidoResponseDTO.class))
            )
    )
    public ResponseEntity<Page<PedidoResponseDTO>> getAll (
            @PageableDefault (size = 20, sort = "horario") Pageable pageable){
        Page<Pedido> pedidos = pedidoService.getAll(pageable);
        Page<PedidoResponseDTO> responses = pedidos.map(pedidoMapper::toResponse);
        return ResponseEntity.ok(responses);

    }

    @GetMapping("/{pedidoId}/")
    @Operation(
            summary = "Find an order by ID",
            description = "Retrieves an order using its unique identifier."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Order found successfully.",
            content = @Content(schema = @Schema(implementation = PedidoResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Order not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<PedidoResponseDTO> findById(@PathVariable Long pedidoId){
        Pedido pedido = pedidoService.findById(pedidoId);
        return ResponseEntity.ok(pedidoMapper.toResponse(pedido));
    }

    @PostMapping
    @Operation(
            summary = "Create a new order",
            description = "Creates a new order associated with an existing tab."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Order created successfully.",
            content = @Content(schema = @Schema(implementation = PedidoResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid request data.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<PedidoResponseDTO> create(@Valid @RequestBody PedidoRequestDTO dto) {
        Pedido pedido = pedidoMapper.toEntity(dto);

        Comanda comandaRef = new Comanda();
        comandaRef.setId(dto.comandaId());
        pedido.setComanda(comandaRef);

        Pedido salvo = pedidoService.create(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoMapper.toResponse(salvo));
    }

    @PatchMapping("/pedido/{pedidoId}/itens/{itemId}")
    @Operation(
            summary = "Update item quantity",
            description = "Updates the quantity of a specific item in an order."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Item quantity updated successfully.",
            content = @Content(schema = @Schema(implementation = PedidoResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Order or item not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<PedidoResponseDTO> changeQuantity(
            @PathVariable Long pedidoId,
            @PathVariable Long itemId,
            @RequestBody @Valid AlterarQuantidadeRequestDTO dto){

        Pedido pedido = pedidoService.updateItemQuantity(
                pedidoId,
                itemId,
                dto.quantidade()
        );
        return ResponseEntity.ok(pedidoMapper.toResponse(pedido));
    }

    @PatchMapping("/pedido/{pedidoId}/iniciar-preparo")
    @Operation(
            summary = "Start order preparation",
            description = "Changes the order status from CREATED to IN_PREPARATION."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Order sent to the kitchen successfully.",
            content = @Content(schema = @Schema(implementation = PedidoResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid order status or the order has no items.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<PedidoResponseDTO> startPreparation (@PathVariable Long pedidoId){
        Pedido preparo = pedidoService.startPreparation(pedidoId);
        return ResponseEntity.ok(pedidoMapper.toResponse(preparo));
    }

    @PatchMapping("/pedido/{pedidoId}/marcar-pronto")
    @Operation(
            summary = "Mark order as ready",
            description = "Changes the order status from IN_PREPARATION to READY."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Order marked as ready successfully.",
            content = @Content(schema = @Schema(implementation = PedidoResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid order status.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<PedidoResponseDTO> markAsDone (@PathVariable Long pedidoId){
        Pedido marcar = pedidoService.markAsDone(pedidoId);
        return ResponseEntity.ok(pedidoMapper.toResponse(marcar));
    }

    @PatchMapping("/pedido/{pedidoId}/marcar-entregue")
    @Operation(
            summary = "Mark order as delivered",
            description = "Changes the order status from READY to DELIVERED."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Order marked as delivered successfully.",
            content = @Content(schema = @Schema(implementation = PedidoResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid order status.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<PedidoResponseDTO> markAsDelivered (@PathVariable Long pedidoId){
        Pedido entregue = pedidoService.markAsDelivered(pedidoId);
        return ResponseEntity.ok(pedidoMapper.toResponse(entregue));
    }

    @PatchMapping("/pedido/{pedidoId}/finalizar")
    @Operation(
            summary = "Finalize an order",
            description = "Changes the order status from DELIVERED to FINISHED."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Order finalized successfully.",
            content = @Content(schema = @Schema(implementation = PedidoResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid order status.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<PedidoResponseDTO> finish (@PathVariable Long pedidoId){
        Pedido finalizar = pedidoService.finish(pedidoId);
        return ResponseEntity.ok(pedidoMapper.toResponse(finalizar));
    }

    @PatchMapping("/pedido/{pedidoId}/cancelar")
    @Operation(
            summary = "Cancel an order",
            description = "Cancels an order that is still eligible for cancellation."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Order canceled successfully.",
            content = @Content(schema = @Schema(implementation = PedidoResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "The order cannot be canceled.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<PedidoResponseDTO> cancel (@PathVariable Long pedidoId){
        Pedido cancelar = pedidoService.cancel(pedidoId);
        return ResponseEntity.ok(pedidoMapper.toResponse(cancelar));
    }

    @PostMapping("/{pedidoId}/itens")
    @Operation(
            summary = "Add an item to an order",
            description = "Adds a new item to an existing order."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Item added successfully.",
            content = @Content(schema = @Schema(implementation = PedidoResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid request data.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Order or product not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<PedidoResponseDTO> addItem(
            @PathVariable Long pedidoId,
            @Valid @RequestBody ItemPedidoRequestDTO dto) {

        ItemPedido itemPedido = itemPedidoMapper.toEntity(dto);

        Produto produtoRef = new Produto();
        produtoRef.setId(dto.produtoId());
        itemPedido.setProduto(produtoRef);

        Pedido pedido = pedidoService.addItem(pedidoId, itemPedido);
        return ResponseEntity.ok(pedidoMapper.toResponse(pedido));
    }
}

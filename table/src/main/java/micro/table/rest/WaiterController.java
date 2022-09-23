package micro.table.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import micro.table.datamodel.OrderItem;
import micro.table.store.repository.OrderItemRepository;
import micro.table.store.service.OrderItemsState;
import micro.table.store.service.OrderStateEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/waiter")
public class WaiterController {

    @Autowired
    private final OrderItemRepository orderItemRepository;

    public WaiterController(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sikeres Fizetés"),
            @ApiResponse(responseCode = "500", description = "Sikertelen Fizetés")
    })
    @Operation(summary = "Asztal fizetése egyben")
    @PutMapping("/payment/{tableId}")
    public void payment(@PathVariable String tableId) {
        List<OrderItemsState> orders = orderItemRepository.getAll(tableId);
        List<String> orderIds = new ArrayList<>();
        orders.forEach(orderItemsState -> {
            orderIds.addAll(orderItemsState.getOrders().stream()
                    .map(OrderItem::getOrderId)
                    .collect(Collectors.toList()));
        });
        orderItemRepository.modifyState(orderIds, OrderStateEnum.paid);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sikeres Rendelés"),
            @ApiResponse(responseCode = "500", description = "Sikertelen Rendelés")
    })
    @Operation(summary = "Rendelések leadása egy adott asztalhoz")
    @PostMapping("/order/{tableId}")
    public void order(@PathVariable String tableId,
                      @RequestBody List<OrderItem> orders) {
        List<OrderItemsState> list = new ArrayList<>();
        list.add(OrderItemsState.builder()
                        .orders(orders)
                        .state(OrderStateEnum.ordered)
                        .build());
        orderItemRepository.add(tableId, list);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sikeres Kivitel"),
            @ApiResponse(responseCode = "500", description = "Sikertelen Kivitel")
    })
    @Operation(summary = "Rendelések kivitele")
    @PutMapping("/delivery")
    public void deliver(@RequestBody List<String> orderIds) {
        orderItemRepository.modifyState(orderIds, OrderStateEnum.delivered);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sikeres lekérdezés",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = OrderItemsState.class))) })
    })
    @Operation(summary = "Egy asztalhoz tartozó rendelések státuszainak lekérdezése")
    @GetMapping("/statuses/{tableId}")
    List<OrderItemsState> getOrdersStatuses(@PathVariable String tableId) {
        return orderItemRepository.getAll(tableId);
    }

}

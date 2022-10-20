package micro.table.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import micro.table.datamodel.OrderItem;
import micro.table.store.service.OrderItemService;
import micro.table.store.service.OrderItemsState;
import micro.table.store.service.OrderStateEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@Validated
@RequestMapping("/waiter")
public class WaiterController {

    @Autowired
    private final OrderItemService orderItemService;

    public WaiterController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sikeres Fizetés"),
            @ApiResponse(responseCode = "500", description = "Sikertelen Fizetés"),
            @ApiResponse(responseCode = "302", description = "Nincs bejelentkezve, átirányítás a login oldalra"),
            @ApiResponse(responseCode = "403", description = "Nincs megfelelő jogosultságod")
    })
    @Operation(
            summary = "Asztal fizetése egyben",
            security = {
                    @SecurityRequirement(name = "apikey", scopes = {"table"}),
                    @SecurityRequirement(name = "openid", scopes = {"table"}),
                    @SecurityRequirement(name = "oauth2", scopes = {"table"}),
            }
    )
    @PutMapping("/payment/{tableId}")
    public void payment(@PathVariable @Pattern(regexp = "^\\d{3}$", message = "error.tableId.regex") String tableId) {
        List<OrderItemsState> orders = orderItemService.getAll(tableId);
        List<String> orderIds = new ArrayList<>();
        orders.forEach(orderItemsState -> {
            orderIds.addAll(orderItemsState.getOrders().stream()
                    .map(OrderItem::getId)
                    .collect(Collectors.toList()));
        });
        orderItemService.modifyState(orderIds, OrderStateEnum.paid);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sikeres Rendelés"),
            @ApiResponse(responseCode = "500", description = "Sikertelen Rendelés"),
            @ApiResponse(responseCode = "302", description = "Nincs bejelentkezve, átirányítás a login oldalra"),
            @ApiResponse(responseCode = "403", description = "Nincs megfelelő jogosultságod")
    })
    @Operation(
            summary = "Rendelések leadása egy adott asztalhoz",
            security = {
                    @SecurityRequirement(name = "apikey", scopes = {"table"}),
                    @SecurityRequirement(name = "openid", scopes = {"table"}),
                    @SecurityRequirement(name = "oauth2", scopes = {"table"}),
            }
    )
    @PostMapping("/order/{tableId}")
    public void order(@PathVariable @Pattern(regexp = "^\\d{3}$", message = "error.tableId.regex") String tableId,
                      @RequestBody List<@Valid OrderItem> orders) {
        orders.forEach(orderItem -> {
            orderItem.setTableId(tableId);
            orderItem.setState(OrderStateEnum.ordered);
        });
        orderItemService.add(tableId, orders);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sikeres Kivitel"),
            @ApiResponse(responseCode = "500", description = "Sikertelen Kivitel"),
            @ApiResponse(responseCode = "302", description = "Nincs bejelentkezve, átirányítás a login oldalra"),
            @ApiResponse(responseCode = "403", description = "Nincs megfelelő jogosultságod")
    })
    @Operation(
            summary = "Rendelések kivitele",
            security = {
                    @SecurityRequirement(name = "apikey", scopes = {"table"}),
                    @SecurityRequirement(name = "openid", scopes = {"table"}),
                    @SecurityRequirement(name = "oauth2", scopes = {"table"}),
            }
    )
    @PutMapping("/delivery")
    public void deliver(@RequestBody List<String> orderIds) {
        orderItemService.modifyState(orderIds, OrderStateEnum.delivered);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sikeres lekérdezés",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = OrderItemsState.class))) })
    })
    @Operation(summary = "Egy asztalhoz tartozó rendelések státuszainak lekérdezése")
    @GetMapping("/statuses/{tableId}")
    List<OrderItemsState> getOrdersStatuses(@PathVariable @Pattern(regexp = "^\\d{3}$", message = "error.tableId.regex") String tableId) {
        return orderItemService.getAll(tableId);
    }

}

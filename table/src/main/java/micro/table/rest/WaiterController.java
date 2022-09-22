package micro.table.rest;

import micro.table.datamodel.OrderItem;
import micro.table.store.repository.OrderItemRepository;
import micro.table.store.service.OrderItemsState;
import micro.table.store.service.OrderStateEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/waiter")
public class WaiterController {

    @Autowired
    private final OrderItemRepository orderItemRepository;

    public WaiterController(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @PutMapping("/payment/{tableId}")
    public void payment(@PathVariable String tableId) {
        // TODO ...
    }

    @PostMapping("/order/{tableId}") // TODO: tableId needed?
    public void order(@PathVariable String tableId,
                      @RequestBody List<OrderItem> orders) {

        List<OrderItemsState> list = new ArrayList<>();
        list.add(OrderItemsState.builder()
                        .orders(orders)
                        .state(OrderStateEnum.ordered)
                        .build());
        orderItemRepository.add(tableId, list);

    }

    @PutMapping("/delivery")
    public void deliver(List<String> orderIds) {
        // TODO ...
    }

    @GetMapping("/statuses/{tableId}")
    List<OrderItemsState> getOrdersStatuses(@PathVariable String tableId) {
        return orderItemRepository.getAll(tableId);
    }

}

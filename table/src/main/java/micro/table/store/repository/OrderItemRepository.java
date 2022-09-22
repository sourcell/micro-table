package micro.table.store.repository;

import micro.table.store.service.OrderItemsState;
import micro.table.store.service.OrderStateEnum;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderItemRepository {

    // TODO: is the hash key orderId or tableId?
    //       currently: tableId
    Map<String, List<OrderItemsState>> orders = new HashMap<>();

    void add(String tableId, List<OrderItemsState> orders) {
        List<OrderItemsState> ordersByTableId = this.orders.get(tableId);
        if (ordersByTableId == null) {
            this.orders.put(tableId, new ArrayList<>(orders));
        } else {
            ordersByTableId.addAll(orders);
        }
    }

    // TODO: wtf?
    void modifyState(List<String> orderId, OrderStateEnum state) {

    }

    void delete(List<String> orderId) {
        List<OrderItemsState> allOrders = new ArrayList<>();
        orders.forEach((key, value) -> {
            allOrders.addAll(value);
        });

        allOrders.forEach(orderItemsState -> {
            orderItemsState.getOrders().removeIf(orderItem -> orderId.contains(orderItem.getOrderId()));
        });
    }

    void clear(String tableId) {
        orders.put(tableId, new ArrayList<>());
    }

    List<OrderItemsState> getAll(String tableId) {
        return orders.get(tableId);
    }

}

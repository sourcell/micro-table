package micro.table.store.repository;

import micro.table.datamodel.OrderItem;
import micro.table.store.service.OrderItemsState;
import micro.table.store.service.OrderStateEnum;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderItemRepository {

    Map<String, List<OrderItemsState>> orders = new HashMap<>();

    public void add(String tableId, List<OrderItemsState> orders) {
        if (this.orders.get(tableId) == null) {
            clear(tableId);
        }
        orders.forEach(orderItemsState -> {
            this.orders.get(tableId).stream()
                    .filter(item -> item.getState() == orderItemsState.getState())
                    .findFirst().orElseThrow()
                    .getOrders()
                    .addAll(orderItemsState.getOrders());
        });
    }

    public void modifyState(List<String> orderId, OrderStateEnum state) {
        for (String id : orderId) {
            OrderItemsState source = null;
            OrderItemsState target = null;
            OrderItem item = null;
            for (List<OrderItemsState> table : orders.values()) {
                for (OrderItemsState orderItemsState : table) {
                    if (orderItemsState.getState() == state) {
                        target = orderItemsState;
                    }
                    for (OrderItem orderItem : orderItemsState.getOrders()) {
                        if (orderItem.getOrderId().equals(id)) {
                            source = orderItemsState;
                            item = orderItem;
                            break;
                        }
                    }
                }
            }
            if (source != null && target != null) {
                source.getOrders().remove(item);
                target.getOrders().add(item);
            } else {
                throw new RuntimeException();
            }
        }
    }

    public void delete(List<String> orderId) {
        List<OrderItemsState> allOrders = new ArrayList<>();
        orders.forEach((key, value) -> {
            allOrders.addAll(value);
        });

        allOrders.forEach(orderItemsState -> {
            orderItemsState.getOrders().removeIf(orderItem -> orderId.contains(orderItem.getOrderId()));
        });
    }

    public void clear(String tableId) {
        orders.put(tableId, new ArrayList<>());
        List<OrderItemsState> orderItemsStates = orders.get(tableId);
        orderItemsStates.add(OrderItemsState.builder().orders(new ArrayList<>()).state(OrderStateEnum.ordered).build());
        orderItemsStates.add(OrderItemsState.builder().orders(new ArrayList<>()).state(OrderStateEnum.completed).build());
        orderItemsStates.add(OrderItemsState.builder().orders(new ArrayList<>()).state(OrderStateEnum.delivered).build());
        orderItemsStates.add(OrderItemsState.builder().orders(new ArrayList<>()).state(OrderStateEnum.paid).build());
    }

    public List<OrderItemsState> getAll(String tableId) {
        return orders.get(tableId);
    }

}

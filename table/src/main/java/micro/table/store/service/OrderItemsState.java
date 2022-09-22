package micro.table.store.service;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import micro.table.datamodel.OrderItem;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderItemsState { // TODO: is this class really needed?

    private List<OrderItem> orders = new ArrayList<>();
    private OrderStateEnum state;

    @Builder
    public OrderItemsState(List<OrderItem> orders, OrderStateEnum state) {
        this.orders = orders;
        this.state = state;
    }

}

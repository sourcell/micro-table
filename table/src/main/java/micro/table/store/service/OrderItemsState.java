package micro.table.store.service;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import micro.table.datamodel.OrderItem;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderItemsState { // TODO: useless class

    //private String tableId;
    private List<OrderItem> orders = new ArrayList<>();
    private OrderStateEnum state;

    @Builder
    public OrderItemsState(/*String tableId,*/ List<OrderItem> orders, OrderStateEnum state) {
        //this.tableId = tableId;
        this.orders = orders;
        this.state = state;
    }

}

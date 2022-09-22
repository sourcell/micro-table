package micro.table.store.service;

import lombok.Data;
import micro.table.datamodel.OrderItem;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderItemsState {

    private List<OrderItem> orders = new ArrayList<>();
    private OrderStateEnum state;

}

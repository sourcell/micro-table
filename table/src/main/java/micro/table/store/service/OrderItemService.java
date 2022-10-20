package micro.table.store.service;

import micro.table.datamodel.OrderItem;
import micro.table.store.repository.FakeOrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderItemService {

    //Map<String, List<OrderItemsState>> orders = new HashMap<>();
    @Autowired
    private final FakeOrderItemRepository orderItemRepository;

    public OrderItemService(FakeOrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public void add(String tableId, List<OrderItem> orders) {
        orders.forEach(orderItem -> orderItem.setTableId(tableId));
        orderItemRepository.saveAll(orders);
    }

    public void modifyState(List<String> orderIds, OrderStateEnum state) {
        List<OrderItem> orders = new ArrayList<>();
        orderIds.forEach(id -> {
            orders.add(orderItemRepository.findById(id));
        });

        orders.forEach(orderItem -> orderItem.setState(state));
        orderItemRepository.saveAll(orders);
    }

    public void delete(List<String> orderIds) {
        orderIds.forEach(orderItemRepository::deleteById);
    }

    public void clear(String tableId) {
        orderItemRepository.removeByTableId(tableId);
    }

    public List<OrderItemsState> getAll(String tableId) {
        List<OrderItem> allOrders = orderItemRepository.findByTableId(tableId);
        List<OrderItem> ordered = allOrders.stream().filter(orderItem -> orderItem.getState() == OrderStateEnum.ordered).collect(Collectors.toList());
        List<OrderItem> completed = allOrders.stream().filter(orderItem -> orderItem.getState() == OrderStateEnum.completed).collect(Collectors.toList());
        List<OrderItem> delivered = allOrders.stream().filter(orderItem -> orderItem.getState() == OrderStateEnum.delivered).collect(Collectors.toList());
        List<OrderItem> paid = allOrders.stream().filter(orderItem -> orderItem.getState() == OrderStateEnum.paid).collect(Collectors.toList());

        List<OrderItemsState> orderItemsStates = new ArrayList<>();
        orderItemsStates.add(OrderItemsState.builder()/*.tableId(tableId)*/.orders(ordered).state(OrderStateEnum.ordered).build());
        orderItemsStates.add(OrderItemsState.builder()/*.tableId(tableId)*/.orders(completed).state(OrderStateEnum.completed).build());
        orderItemsStates.add(OrderItemsState.builder()/*.tableId(tableId)*/.orders(delivered).state(OrderStateEnum.delivered).build());
        orderItemsStates.add(OrderItemsState.builder()/*.tableId(tableId)*/.orders(paid).state(OrderStateEnum.paid).build());
        return orderItemsStates;
    }

}

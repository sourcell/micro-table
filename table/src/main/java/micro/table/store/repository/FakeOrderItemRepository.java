package micro.table.store.repository;

import micro.table.datamodel.OrderItem;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class FakeOrderItemRepository {

    private List<OrderItem> database = new ArrayList<>();

    public void saveAll(List<OrderItem> orders) {
        for (OrderItem orderItem : orders) {
            Optional<OrderItem> old = database.stream().filter(oldOrder -> oldOrder.getId().equals(orderItem.getId())).findFirst();
            old.ifPresent(item -> database.remove(item));
        }
        database.addAll(orders);
    }

    public OrderItem findById(String id) {
        Optional<OrderItem> optional = database.stream().filter(orderItem -> orderItem.getId().equals(id)).findFirst();
        return optional.get();
    }

    public void deleteById(String id) {
        Optional<OrderItem> optional = database.stream().filter(orderItem -> orderItem.getId().equals(id)).findFirst();
        optional.ifPresent(orderItem -> database.remove(orderItem));
    }

    public void removeByTableId(String tableId) {
        List<OrderItem> orders = database.stream().filter(orderItem -> orderItem.getTableId().equals(tableId)).collect(Collectors.toList());
        database.removeAll(orders);
    }

    public List<OrderItem> findByTableId(String tableId) {
        return database.stream().filter(orderItem -> orderItem.getTableId().equals(tableId)).collect(Collectors.toList());
    }

}

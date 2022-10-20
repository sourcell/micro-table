package micro.table.store.repository;

import micro.table.datamodel.OrderItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderItemRepository extends CrudRepository<OrderItem, String> {

    void removeByTableId(String tableId);
    List<OrderItem> findByTableId(String tableId);

}

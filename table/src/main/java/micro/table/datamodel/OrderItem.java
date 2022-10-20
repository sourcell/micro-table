package micro.table.datamodel;

import org.hibernate.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import micro.table.store.service.OrderStateEnum;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode()
@NoArgsConstructor
@Entity
@Table(name = "order_item")
@Schema(description = "Rendelés elem")
public class OrderItem {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = {
                    @Parameter(
                            name = "uuid_gen_strategy_class",
                            value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
                    )
            }
    )
    @Column(name = "id", updatable = false, nullable = false)
    @Schema(description = "Rendelés azonosító")
    private String id;

    @Schema(description = "Asztal azonosító")
    @Column(name = "`table_id`")
    private String tableId;

    @Schema(description = "Étel azonosító")
    @NotBlank(message = "error.orderItem.foodId.notBlank")
    @NotNull(message = "error.orderItem.foodId.notNull")
    @Column(name = "`food_id`")
    private String foodId;

    @Schema(description = "Mennyiség")
    @Min(value = 1, message = "error.orderItem.amount.min")
    @Column(name = "`quantity`")
    private int quantity;

    @Schema(description = "Megjegyzés")
    @Column(name = "`note`")
    private String note;

    @Schema(description = "Csoportos megjegyzés")
    @Column(name = "`note4all`")
    private boolean note4all;

    @Schema(description = "Csoportnév")
    @Column(name = "`group_name`")
    private String groupName;

    @Schema(description = "Státusz")
    @Column(name = "`state`")
    private OrderStateEnum state;

    @Builder
    public OrderItem(String tableId, String foodId, int quantity, String note, boolean note4all, String groupName, OrderStateEnum state) {
        this.tableId = tableId;
        this.foodId = foodId;
        this.quantity = quantity;
        this.note = note;
        this.note4all = note4all;
        this.groupName = groupName;
        this.state = state;
    }

}

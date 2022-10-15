package micro.table.datamodel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode()
@NoArgsConstructor
@Schema(description = "Rendelés elem")
public class OrderItem {

    @Schema(description = "Rendelés azonosító")
    private String orderId;

    @Schema(description = "Étel azonosító")
    @NotBlank(message = "error.orderItem.foodId.notBlank")
    @NotNull(message = "error.orderItem.foodId.notNull")
    private String foodId;

    @Schema(description = "Mennyiség")
    @Min(value = 1, message = "error.orderItem.amount.min")
    private int quantity;

    @Schema(description = "Megjegyzés")
    private String note;

    @Schema(description = "Csoportos megjegyzés")
    private boolean note4all;

    @Schema(description = "Csoportnév")
    private String groupName;

    @Builder
    public OrderItem(String foodId, int quantity, String note, boolean note4all) {
        this.foodId = foodId;
        this.quantity = quantity;
        this.note = note;
        this.note4all = note4all;
    }

}

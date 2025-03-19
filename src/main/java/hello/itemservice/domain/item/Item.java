package hello.itemservice.domain.item;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
//@Data -> 모두 만들기때문에 지양 주의하여 사용

public class Item {

    private Long id;
    private  String itemName;
    private  Integer price;
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }

}

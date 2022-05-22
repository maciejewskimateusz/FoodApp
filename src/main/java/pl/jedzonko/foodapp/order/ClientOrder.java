package pl.jedzonko.foodapp.order;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import pl.jedzonko.foodapp.item.Item;

@Component
@SessionScope
public class ClientOrder {

    private Order order;

    public ClientOrder(){
        clear();
    }

    void clear(){
        order = new Order();
        order.setStatus(OrderStatus.NEW);
    }

    public Order getOrder() {
        return order;
    }

    void add(Item item) {
        order.getItems().add(item);
    }

}

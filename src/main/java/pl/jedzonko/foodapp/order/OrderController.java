package pl.jedzonko.foodapp.order;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.jedzonko.foodapp.item.Item;
import pl.jedzonko.foodapp.item.ItemRepository;
import pl.jedzonko.foodapp.other.Message;

import java.util.Optional;

@Controller
public class OrderController {

    private ClientOrder clientOrder;
    private ItemRepository itemRepository;
    private OrderRepository orderRepository;

    public OrderController(final ClientOrder clientOrder, final ItemRepository itemRepository, final OrderRepository orderRepository) {
        this.clientOrder = clientOrder;
        this.itemRepository = itemRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/zamowienie/dodaj")
    public String addItemToOrder(@RequestParam Long itemId, Model model) {
        Optional<Item> item = itemRepository.findById(itemId);
        item.ifPresent(clientOrder::add);
        if (item.isPresent()) {
            model.addAttribute("message", new Message("Dodano", "Do zamówienia dodano: " + item.get().getName()));
        } else {
            model.addAttribute("message", new Message("Nie dodano", "Porano błędne id z menu: " + itemId));
        }
        return "message";
    }

    @GetMapping("/zamowienie")
    public String currentOrder(Model model) {
        model.addAttribute("order", clientOrder.getOrder());
        model.addAttribute("sum", clientOrder.getOrder()
                .getItems()
                .stream()
                .mapToDouble(Item::getPrice)
                .sum());
        return "order";
    }

    @PostMapping("/zamowienie/realizuj")
    public String proceedOrder(@RequestParam String address, @RequestParam String telephone, Model model) {
        Order order = clientOrder.getOrder();
        order.setAddress(address);
        order.setTelephone(telephone);
        orderRepository.save(order);
        clientOrder.clear();
        model.addAttribute("message", new Message("Dziękujemy", "Zamówienie przekazane do realizacji"));
        return "message";
    }
}

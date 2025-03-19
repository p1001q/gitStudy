package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }


    //@PostMapping("/add")
    public String addItemV1(@RequestParam("itemName") String itemName,
                       @RequestParam("price") Integer price,
                       @RequestParam("quantity") Integer quantity,
                       Model model) {
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item", item);

        return "basic/item";
    }

    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item, Model model) {

        itemRepository.save(item);
        //model.addAttribute("item", item); <<@ModelAttribute("item") 이름:item 모델에 넣어주는 이 역할도 스프링이 해줌

        return "basic/item";
    }

    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, Model model) {

        //클래스명으로 Item -> item 이렇게 바꿔서 모델에 넣어줌
        //Item item > Hello item 로 바꾼다면 Hello item > hello 로 저장
        itemRepository.save(item);
        //model.addAttribute("item", item); <<@ModelAttribute("item") 이름:item 모델에 넣어주는 이 역할도 스프링이 해줌

        return "basic/item";
    }

    //새로고침하면 아이템이 계속 등록되는 문제점이 있다
    //@PostMapping("/add")
    public String addItemV4(Item item) {
        //Item item > item
        itemRepository.save(item);
        return "basic/item"; //상세화면을 매핑해준 것
    }

    //PRG 문제해결 방식 = Post/Redirect/Get
    //@PostMapping("/add")
    public String addItemV5(Item item) {
        itemRepository.save(item);
        return "redirect:/basic/items/" + item.getId(); //리다이렉트 해줌
    }
 
    //url만 주는 게 아니라 쿼리 파라미터도 넘겨주는 것 > 활용해서 저장된 거 사용자에게 메세지로 보여줄 수 있게 됨
    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable("itemId") Long itemId,@ModelAttribute Item item) {
        itemRepository.update(itemId,item);
       return "redirect:/basic/items/{itemId}";
    }

}

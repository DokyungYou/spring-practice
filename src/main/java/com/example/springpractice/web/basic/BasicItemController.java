package com.example.springpractice.web.basic;

import com.example.springpractice.domain.item.Item;
import com.example.springpractice.domain.item.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor // final 이 붙은 멤버변수만 사용해서 생성자를 자동으로 만들어줌
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model){
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model){

        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }


    /**
     * 등록 form
     */
    @GetMapping("/add")
    public String addForm(){
        return "basic/addForm";
    }

//    @PostMapping("/add")
//    public String addItemV1(@RequestParam String itemName, // input태그의 name으로 넘어옴
//                       @RequestParam Integer price,
//                       @RequestParam Integer quantity,
//                       Model model){
//
//        Item item = new Item(itemName, price, quantity);
//        Item savedItem = itemRepository.save(item);
//        model.addAttribute("item", savedItem);
//
//        return "basic/item";
//    }


    /**
     * @ModelAttribute 를 사용하려면, 객체의 필드이름과 input태그의 name과 일치해야함( 불일치시 null값으로 바인딩)
     *
     * 요청 파라미터처리, Model 추가 두가지 기능을 함
     */
//    @PostMapping("/add")
//    public String addItemV2(@ModelAttribute("item") Item item//
//                          //Model model //
//    ){
//
//        log.debug("item.name = {}", item.getName());
//
//        Item savedItem = itemRepository.save(item);
//        //model.addAttribute("item", savedItem);
//
//        return "basic/item";
//    }


    /**
     *  @ModelAttribute의 이름을 설정하지 않으면, 자동으로 클래스명의 첫글자를 소문자로 바꿔서
     *  모델 attribute 에 넣어준다.
     */
//        @PostMapping("/add")
//        public String addItemV3(@ModelAttribute Item item){
//
//            log.debug("item.name = {}", item.getName());
//
//            Item savedItem = itemRepository.save(item);
//            //model.addAttribute("item", savedItem);
//
//            return "basic/item";
//    }

    /**
     * 임의객체를 파라미터로 받을 시 자동으로 @ModelAttribute 적용
     */
    @PostMapping("/add")
    public String addItemV4(Item item){

        log.debug("item.name = {}", item.getName());

        Item savedItem = itemRepository.save(item);
        //model.addAttribute("item", savedItem);

        return "basic/item";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable long itemId, Model model){
        model.addAttribute("item", itemRepository.findById(itemId));
        return "basic/editForm";
    }


    /**
     * HTML Form 전송은 PUT, PATCH를 지원하지 않는다. (Get, Post만 가능)
     */
    @PostMapping("/{itemId}/edit")
    public String editItem(@PathVariable long itemId, @ModelAttribute Item item){
        itemRepository.updateItem(itemId, item);

        return "redirect:/basic/items/{itemId}";
    }


    /**
     * 테스트용 데이터
     */
    @PostConstruct
    public void init(){
        itemRepository.save(new Item("상품1",10_000,2));
        itemRepository.save(new Item("상품2",100_000,1));
    }
}

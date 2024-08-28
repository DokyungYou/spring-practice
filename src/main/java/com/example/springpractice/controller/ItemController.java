package com.example.springpractice.controller;

import com.example.springpractice.domain.item.Book;
import com.example.springpractice.domain.item.Item;
import com.example.springpractice.repository.ItemRepository;
import com.example.springpractice.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/items")
@Controller
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/new")
    public String createForm(Model model){
        model.addAttribute("form", new BookForm());
        return "/items/createItemForm";
    }

    @PostMapping("/new")
    public String create(@Valid BookForm form){
        
        // setter 방식은 지양
        Book book = new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setAuthor(form.getAuthor());
        book.setIsbn(book.getIsbn());
        book.setStockQuantity(form.getStockQuantity());

        itemService.saveItem(book);
        return "redirect:/items";
    }

    @GetMapping
    public String list(Model model){
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "/items/itemList";
    }

    @GetMapping("/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId")Long itemId, Model model){
        Book item = (Book) itemService.findOne(itemId);

        // setter 방식은 지양
        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());
        form.setStockQuantity(item.getStockQuantity());

        model.addAttribute("form", form);

        return "/items/updateItemForm";
    }

//    @PostMapping("/{itemId}/edit")
//    public String updateItem(@ModelAttribute("form") BookForm form){
//
//        // setter 방식은 지양
//        // 준영속엔티티, merge 방식 (이렇게 하면 안됨)
//        Book book = new Book();
//        book.setId(form.getId());
//        book.setName(form.getName());
//        book.setPrice(form.getPrice());
//        book.setStockQuantity(form.getStockQuantity());
//        book.setAuthor(form.getAuthor());
//
//        book.setIsbn(form.getIsbn());
//        itemService.saveItem(book);
//
//        return "redirect:/items";
//    }

    /**
     * 엔티티를 변경할 때는 항상 변경 감지를 사용해야함
     * 트랜잭션이 있는 서비스 계층에서 영속 상태의 엔티티를 조회 후, 엔티티의 데이터를 직접 변경해야함
     * 트랜잭션 커밋 시점에 변경 감지가 실행됨
     */
    @PostMapping("/{itemId}/edit")
    public String updateItem(@ModelAttribute("form") BookForm form){

        itemService.updateItem(form.getId(), form);
        return "redirect:/items";
    }
}

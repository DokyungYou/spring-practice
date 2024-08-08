package com.example.springpractice.web.validation;

import com.example.springpractice.domain.item.Item;
import com.example.springpractice.domain.item.ItemRepository;
import com.example.springpractice.domain.item.SaveCheck;
import com.example.springpractice.domain.item.UpdateCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/validation/v4/items")
@RequiredArgsConstructor
public class ValidationItemControllerV4 {

    private final ItemRepository itemRepository;


    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v4/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v4/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v4/addForm";
    }

    //@PostMapping("/add")
    public String addItem(@Validated @ModelAttribute Item item, // @Validated : 자동으로 검증기를 실행하는 어노테이션
                            BindingResult bindingResult, // 위의 객체에 바인딩 된 결과를 담는용 (바인딩이 되는 파마리터 바로 다음의 위치에서 사용해야한다.)
                            RedirectAttributes redirectAttributes,
                            Model model) {

        // 검증 실패 시 다시 입력 form 으로 이동
        if(bindingResult.hasErrors()){
            // BindingResult 는 자동으로 view에 넘어가기때문에 model에 따로 직접 넣어줄 필요가 없다.

            // @ModelAttribute 로 값을 받아왔기때문에, 해당 form 으로 돌아갈 때 다시 가지고 감
            // (addForm 에선 빈 객체를 받아서 랜더링 하게끔 해놔서 가능, @GetMapping("/add") 참고)
            return "validation/v4/addForm";
        }

        // 특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v4/items/{itemId}";
    }

    @PostMapping("/add")
    public String addItem2(@Validated(SaveCheck.class) @ModelAttribute Item item, // @Validated : 자동으로 검증기를 실행하는 어노테이션
                           BindingResult bindingResult, // 위의 객체에 바인딩 된 결과를 담는용 (바인딩이 되는 파마리터 바로 다음의 위치에서 사용해야한다.)
                           RedirectAttributes redirectAttributes,
                           Model model) {

        // 검증 실패 시 다시 입력 form 으로 이동
        if(bindingResult.hasErrors()){
            // BindingResult 는 자동으로 view에 넘어가기때문에 model에 따로 직접 넣어줄 필요가 없다.

            // @ModelAttribute 로 값을 받아왔기때문에, 해당 form 으로 돌아갈 때 다시 가지고 감
            // (addForm 에선 빈 객체를 받아서 랜더링 하게끔 해놔서 가능, @GetMapping("/add") 참고)
            return "validation/v4/addForm";
        }

        // 특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v4/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v4/editForm";
    }

    //@PostMapping("/{itemId}/edit")t
    public String edit(@PathVariable Long itemId,
                       @Validated @ModelAttribute Item item,
                       BindingResult bindingResult) {

        // 특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }
        if(bindingResult.hasErrors()){
            log.info("errors ={}", bindingResult);
            return "/validation/v4/editForm";
        }

        itemRepository.update(itemId, item);
        return "redirect:/validation/v4/items/{itemId}";
    }

    @PostMapping("/{itemId}/edit")
    public String edit2(@PathVariable Long itemId,
                       @Validated(UpdateCheck.class) @ModelAttribute Item item,
                       BindingResult bindingResult) {

        // 특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }
        if(bindingResult.hasErrors()){
            log.info("errors ={}", bindingResult);
            return "/validation/v4/editForm";
        }

        itemRepository.update(itemId, item);
        return "redirect:/validation/v4/items/{itemId}";
    }

}


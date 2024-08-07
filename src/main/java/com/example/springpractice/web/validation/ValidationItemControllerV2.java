package com.example.springpractice.web.validation;

import com.example.springpractice.domain.item.Item;
import com.example.springpractice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }

    @PostMapping("/add")
    public String addItemV1(@ModelAttribute Item item,
                          BindingResult bindingResult, // 위의 객체에 바인딩 된 결과를 담는용 (바인딩이 되는 파마리터 바로 다음의 위치에서 사용해야한다.)
                          RedirectAttributes redirectAttributes,
                          Model model) {

        // 검증 로직
        if(!StringUtils.hasText(item.getItemName())){
            bindingResult.addError(new FieldError("item","itemName", "상품 이름은 필수입니다."));
        }
        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1_000_000){
            bindingResult.addError(new FieldError("item","price",  " 가격은 1,000 ~ 1,000,000원까지 허용합니다."));
        }
        if(item.getQuantity() == null || item.getQuantity() > 9999 || item.getQuantity() < 1){
            bindingResult.addError(new FieldError("item","quantity",  "수량은 최대 1 ~ 9,999개 까지 허용합니다."));
        }
        
        // 특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                // 특정 필드가 아니어서 임의로 글로벌 오류로 지칭
                bindingResult.addError(new ObjectError("item",
                                "가격 * 수량의 합은 10,000원 이상이어야합니다. 현재 값 :"  + resultPrice + "원"));
            }
        }

        // 검증 실패 시 다시 입력 form 으로 이동
        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            // BindingResult 는 자동으로 view에 넘어가기때문에 model에 따로 직접 넣어줄 필요가 없다.

            // @ModelAttribute 로 값을 받아왔기때문에, 해당 form 으로 돌아갈 때 다시 가지고 감
            // (addForm 에선 빈 객체를 받아서 랜더링 하게끔 해놔서 가능, @GetMapping("/add") 참고)
            return "validation/v2/addForm";
        }

        
        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }

}


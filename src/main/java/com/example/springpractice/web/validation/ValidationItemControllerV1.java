package com.example.springpractice.web.validation;

import com.example.springpractice.domain.item.Item;
import  com.example.springpractice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/validation/v1/items")
@RequiredArgsConstructor
public class ValidationItemControllerV1 {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v1/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v1/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v1/addForm";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes, Model model) {

        // 검증 오류 결과를 보관
        Map<String, String> errors = new HashMap<>();

        // 검증 로직
        if(!StringUtils.hasText(item.getItemName())){
            errors.put("itemName", "상품 이름은 필수입니다.");
        }
        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1_000_000){
            errors.put("price", " 가격은 1,000 ~ 1,000,000원까지 허용합니다.");
        }
        if(item.getQuantity() == null || item.getQuantity() > 9999){
            errors.put("quantity","수량은 최대 9,999개 까지 허용합니다.");
        }
        
        // 특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                // 특정 필드가 아니어서 임의로 글로벌 오류로 지칭
                errors.put("globalError","가격 * 수량의 합은 10,000원 이상이어야합니다. 현재 값 : " + resultPrice + "원");
            }
        }

        // 검증 실패 시 다시 입력 form 으로 이동
        if(!errors.isEmpty()){ // 실무에서 부정의 부정식으로 쓰면 직관적이지 못하기 때문에 웬만하면, hasError() 이런식으로 하는걸 추천 (혹은 제공해줌)
            log.info("errors = {}", errors);
            model.addAttribute("errors", errors);

            // @ModelAttribute 로 값을 받아왔기때문에, 해당 form 으로 돌아갈 때 다시 가지고 감
            // (addForm 에선 빈 객체를 받아서 랜더링 하게끔 해놔서 가능, @GetMapping("/add") 참고)
            return "validation/v1/addForm";
        }

        
        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v1/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v1/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v1/items/{itemId}";
    }

}


package com.example.springpractice.web.form;

import com.example.springpractice.domain.item.Item;
import com.example.springpractice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/form/items")
@RequiredArgsConstructor
public class FormItemController {

    private final ItemRepository itemRepository;

    /** regions 는 addForm, detail, editForm 에서 모두 반복적으로 보여줘야하는 상황
     * 해당 컨트롤러를 요청할 때  @ModelAttribute 가 붙은 메서드에서 반환한 값이 자동으로 모델( model )에 담기게 된다.
     *
     * 동적으로 변하지 않는다면 미리 static으로 만들어 놓고 가져와서 쓰는 방식을 선택하는게 성능상 효율적일 수 있다. (@ModelAttribute 은 매번 호출되니까)
     */
    @ModelAttribute("regions")
    public Map<String, String> regions(){
        Map<String, String> regions  = new LinkedHashMap<>(); // 순서보장을 위해 LinkedHashMap 사용
        regions.put("SEOUL", "서울");
        regions.put("BUSAN", "부산");
        regions.put("JEJU", "제주");

        return regions;
    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "form/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "form/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());

//        Map<String, String> regions  = new LinkedHashMap<>(); // 순서보장을 위해 LinkedHashMap 사용
//        regions.put("SEOUL", "서울");
//        regions.put("BUSAN", "부산");
//        regions.put("JEJU", "제주");
//        model.addAttribute("regions", regions);

        return "form/addForm";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {

        log.info("item.open={}", item.getOpen());
        log.info("item.regions={}", item.getRegions());

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/form/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "form/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/form/items/{itemId}";
    }

}


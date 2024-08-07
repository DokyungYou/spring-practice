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
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/validation/v3/items")
@RequiredArgsConstructor
public class ValidationItemControllerV3 {

    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;

    /**
     * 해당 컨트롤러가 호출될 때마다 항상 호출됨
     */
    @InitBinder
    public void init(WebDataBinder dataBinder){ // 요청이 올때마다 dataBinder 가 내부적으로 만들어져서 온다.
        dataBinder.addValidators(itemValidator); // WebDataBinder에 검증기 추가
    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v3/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v3/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v3/addForm";
    }

   // @PostMapping("/add")
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
            return "/validation/v3/addForm";
        }

        
        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
    }

    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item,
                            BindingResult bindingResult, // 위의 객체에 바인딩 된 결과를 담는용 (바인딩이 되는 파마리터 바로 다음의 위치에서 사용해야한다.)
                            RedirectAttributes redirectAttributes,
                            Model model) {
        // 타입 오류로 바인딩에 실패하면 스프링은 FieldError 를 생성하면서 사용자가 입력한 값을 넣어둠
        // 해당 오류를 BindingResult 에 담아서 컨트롤러를 호출
        // 따라서 타입 오류 같은 바인딩 실패시에도 사용자의 오류 메시지를 정상 출력할 수 있음

        // 검증 로직
        // 해당 검증로직에 실패한 값들을 다시 담아서 보내게끔 수정 (바인딩 자체가 실패한 경우 제외)
        if(!StringUtils.hasText(item.getItemName())){
            bindingResult.addError(new FieldError("item","itemName",
                    item.getItemName(),false,null,null,"상품 이름은 필수입니다."));
        }
        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1_000_000){
            bindingResult.addError(new FieldError("item","price",
                    item.getPrice(),false,null,null," 가격은 1,000 ~ 1,000,000원까지 허용합니다."));
        }
        if(item.getQuantity() == null || item.getQuantity() > 9999 || item.getQuantity() < 1){
            bindingResult.addError(new FieldError("item","quantity",
                    item.getQuantity(),false,null,null,"수량은 최대 1 ~ 9,999개 까지 허용합니다."));
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
            return "validation/v3/addForm";
        }


        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
    }


    /**
     * 오류코드와 메시지 처리1
     */
    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item,
                            BindingResult bindingResult, // 위의 객체에 바인딩 된 결과를 담는용 (바인딩이 되는 파마리터 바로 다음의 위치에서 사용해야한다.)
                            RedirectAttributes redirectAttributes,
                            Model model) {

        // 검증 로직
        // 해당 검증로직에 실패한 값들을 다시 담아서 보내게끔 수정 (바인딩 자체가 실패한 경우 제외)
        if(!StringUtils.hasText(item.getItemName())){
            bindingResult.addError(new FieldError("item","itemName",
                    // String[] codes 에 값을 여러개를 넣으면 차례대로 메세지소스를 조회하게되고, 처음 매칭되는 메세지가 사용됨
                    item.getItemName(),false, new String[]{"required.item.itemName"},null,null));
        }
        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1_000_000){
            bindingResult.addError(new FieldError("item","price",
                    // range.item.price=가격은 {0} ~ {1} 까지 허용합니다.  <- new Object[]{1000, 100000}
                    item.getPrice(),false, new String[]{"range.item.price"}, new Object[]{1000, 1000000},null));
        }
        if(item.getQuantity() == null || item.getQuantity() > 9999 || item.getQuantity() < 1){
            bindingResult.addError(new FieldError("item","quantity",
                    item.getQuantity(),false, new String[]{"range.item.quantity"},new Object[]{1, 9999},null));
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
            return "validation/v3/addForm";
        }


        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
    }


    /**
     * rejectValue() , reject()
     */
    //@PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item,
                            BindingResult bindingResult, // 위의 객체에 바인딩 된 결과를 담는용 (바인딩이 되는 파마리터 바로 다음의 위치에서 사용해야한다.)
                            RedirectAttributes redirectAttributes,
                            Model model) {

        // BindingResult 는 이미 타겟과 이름을 알고있다.
        log.info("objectName = {}", bindingResult.getObjectName());
        log.info("target ={}", bindingResult.getTarget());

        // 검증 로직
        
        // 아래 주석처리한 코드와 같은역할을 해줌
        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName", "required");
//        if(!StringUtils.hasText(item.getItemName())){
//            bindingResult.rejectValue("itemName", "required");
//        }
        
        
        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1_000_000){
            bindingResult.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
        }
        if(item.getQuantity() == null || item.getQuantity() > 9999 || item.getQuantity() < 1){
            bindingResult.rejectValue("quantity","range", new Object[]{1, 9999}, null);
        }

        // 특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        // 검증 실패 시 다시 입력 form 으로 이동
        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            // BindingResult 는 자동으로 view에 넘어가기때문에 model에 따로 직접 넣어줄 필요가 없다.

            // @ModelAttribute 로 값을 받아왔기때문에, 해당 form 으로 돌아갈 때 다시 가지고 감
            // (addForm 에선 빈 객체를 받아서 랜더링 하게끔 해놔서 가능, @GetMapping("/add") 참고)
            return "validation/v3/addForm";
        }


        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
    }

    //@PostMapping("/add")
    public String addItemV5(@ModelAttribute Item item,
                            BindingResult bindingResult, // 위의 객체에 바인딩 된 결과를 담는용 (바인딩이 되는 파마리터 바로 다음의 위치에서 사용해야한다.)
                            RedirectAttributes redirectAttributes,
                            Model model) {


        if(itemValidator.supports(item.getClass())){
            itemValidator.validate(item, bindingResult);
        }

        // 검증 실패 시 다시 입력 form 으로 이동
        if(bindingResult.hasErrors()){
            // BindingResult 는 자동으로 view에 넘어가기때문에 model에 따로 직접 넣어줄 필요가 없다.

            // @ModelAttribute 로 값을 받아왔기때문에, 해당 form 으로 돌아갈 때 다시 가지고 감
            // (addForm 에선 빈 객체를 받아서 랜더링 하게끔 해놔서 가능, @GetMapping("/add") 참고)
            return "validation/v3/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
    }

    @PostMapping("/add")
    public String addItemV6(@Validated @ModelAttribute Item item, // @Validated : 자동으로 검증기를 실행하는 어노테이션
                            BindingResult bindingResult, // 위의 객체에 바인딩 된 결과를 담는용 (바인딩이 되는 파마리터 바로 다음의 위치에서 사용해야한다.)
                            RedirectAttributes redirectAttributes,
                            Model model) {

        // 검증 실패 시 다시 입력 form 으로 이동
        if(bindingResult.hasErrors()){
            // BindingResult 는 자동으로 view에 넘어가기때문에 model에 따로 직접 넣어줄 필요가 없다.

            // @ModelAttribute 로 값을 받아왔기때문에, 해당 form 으로 돌아갈 때 다시 가지고 감
            // (addForm 에선 빈 객체를 받아서 랜더링 하게끔 해놔서 가능, @GetMapping("/add") 참고)
            return "validation/v3/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v3/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v3/items/{itemId}";
    }

}


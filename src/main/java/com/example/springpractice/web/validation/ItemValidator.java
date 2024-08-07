package com.example.springpractice.web.validation;

import com.example.springpractice.domain.item.Item;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ItemValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Item.class.isAssignableFrom(clazz); // == 보다는 isAssignableFrom 을 권장 (자식클래스까지 커버해줌)
        // item == clazz
        // item == item의 자식
    }

    @Override
    public void validate(Object target, Errors errors) { // Errors <- BindingResult
        Item item = (Item) target;


        // 검증 로직
        // 아래 주석처리한 코드와 같은역할을 해줌
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "itemName", "required");


        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1_000_000){
            errors.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
        }
        if(item.getQuantity() == null || item.getQuantity() > 9999 || item.getQuantity() < 1){
            errors.rejectValue("quantity","range", new Object[]{1, 9999}, null);
        }

        // 특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                errors.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }



    }

    @Override
    public Errors validateObject(Object target) {
        return Validator.super.validateObject(target);
    }
}

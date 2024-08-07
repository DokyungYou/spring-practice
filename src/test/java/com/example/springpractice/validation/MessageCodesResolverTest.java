package com.example.springpractice.validation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class MessageCodesResolverTest {
    MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();

    @Test
    void messageResolverObject(){

        String[] messageCodes = codesResolver.resolveMessageCodes("required","item");
        log.info("messageCodes ={}", (Object) messageCodes); // messageCodes =[required.item, required] 상세한 것이 우선으로 나옴

        // 아래와같이 코드를 순서대로 넣어주면 순서대로 찾는데, 이 부분을  rejectValue(), reject() 가 해주는 것임
        //new ObjectError("item", new String[]{"required.item", "required"},null ,null);

        assertThat(messageCodes).containsExactly("required.item", "required");
    }

    @Test
    void messageResolverFiled(){
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item", "itemName", String.class);
        log.info("messageCodes ={}", (Object) messageCodes); //  messageCodes =[required.item.itemName, required.itemName, required.java.lang.String, required]

        // 	public FieldError(String objectName, String field, @Nullable Object rejectedValue, boolean bindingFailure,
        //			@Nullable String[] codes, @Nullable Object[] arguments, @Nullable String defaultMessage)

        assertThat(messageCodes).containsExactly("required.item.itemName",
                                                "required.itemName",
                                                "required.java.lang.String",
                                                "required"
        );
    }
}

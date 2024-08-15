package com.example.springpractice.converter;

import com.example.springpractice.type.IpPort;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ConverterTest {

  @Test
  void stringToInteger(){
      StringToIntegerConverter converter = new StringToIntegerConverter();
      Integer result = converter.convert("10");

      assertThat(result).isEqualTo(10);
  }

    @Test
    void integerToString(){
        IntegerToStringConverter converter = new IntegerToStringConverter();
        String result = converter.convert(10);

        assertThat(result).isEqualTo("10");
    }

    @Test
    void ipPortToString(){
        IpPortToStringConverter converter = new IpPortToStringConverter();
        String result = converter.convert(new IpPort("127.0.0.1", 8080));

        assertThat(result).isEqualTo("127.0.0.1:8080");
    }

    @Test
    void stringToIpPort(){
        StringToIpPortConverter converter = new StringToIpPortConverter();
        IpPort result = converter.convert("127.0.0.1:8080");

        assertThat(result).isEqualTo(new IpPort("127.0.0.1", 8080)); // @EqualsAndHashCode 등록해줘서 true
    }



}

package com.example.excel.service.validator;

import com.example.excel.service.validator.dto.MessageValidatorParam;
import com.example.excel.service.validator.dto.RcsCarouselValidatorParam;
import com.example.excel.service.validator.dto.ValidatorBindingReuslt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RcsCarouselValidator implements MessageValidator {

    // db 값을 불러와 검증을 하기 위해 Component로 선언
    // brand_info(회신번호 목록) 또는 productCode를 불러오기 위하여 사용함

    @Override
    public boolean supports(MessageType messageType) {
        return MessageType.RCS_CAROUSEL.equals(messageType);
    }

    @Override
    public List<ValidatorBindingReuslt> validate(MessageValidatorParam param) {
        if (!(param instanceof RcsCarouselValidatorParam rcsCarouselValidatorParam)) {
            throw new IllegalArgumentException("param is not instance of RcsCarouselValidatorParam");
        }

        return new ArrayList<>();
    }
}

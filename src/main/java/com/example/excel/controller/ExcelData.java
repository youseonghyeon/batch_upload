package com.example.excel.controller;

import com.example.excel.validator.MessageType;
import com.example.excel.validator.dto.MmsValidatorParam;
import com.example.excel.validator.dto.RcsCarouselValidatorParam;
import com.example.excel.validator.dto.RcsMmsValidatorParam;
import com.example.excel.validator.dto.SmsValidatorParam;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@ToString
public class ExcelData {

    /**
     * 절대 필드의 순서를 바꾸지 마시오
     */
    private String crmStatus; // CRM여부
    private String messageName; // 메시지명
    private String messageTypeValue; // 메시지구분값
    private String messageAttributeValue; // 메시지특성값
    private String messageTitle; // 메시지제목
    private String fourReviewPoints; // 4대검토사항
    private String messageContent; // 메시지내용
    private String reservationDate; // 예약일시
    private String reservationHour; // 예약시간
    private String reservationMinute; // 예약분
    private String replyNumberType; // 회신번호유형
    private String bannerUsage; // 배너사용여부
    private String imagePosition; // 이미지위치
    private String messageContentFileName; // 메시지내용파일명
    private String receiverMemo; // 착신대상메모
    private String messageValidityDateTime; // 메시지유효일시
    private String closingRemarkFlag; // 맺음말여부
    private String senderNumber1; // 발송번호1
    private String senderNumber2; // 발송번호2
    private String senderNumber3; // 발송번호3
    private String senderNumber4; // 발송번호4
    private String senderNumber5; // 발송번호5
    private String variableText1; // variableText1
    private String variableText2; // variableText2
    private String variableText3; // variableText3
    private String variableText4; // variableText4
    private String variableText5; // variableText5
    private String variableText6; // variableText6
    private String variableText7; // variableText7
    private String variableText8; // variableText8
    private String variableText9; // variableText9
    private String variableText10; // variableText10
    private String carouselFlag; // 캐러셀여부
    private String adFlag; // 광고여부
    private String rcsReplyNumber; // RCS회신번호
    private String rcsSlide1Image; // RCS슬라이드1이미지
    private String slide1Content; // 슬라이드1내용
    private String slide1Button1; // 슬라이드1버튼1
    private String slide1Button1Url; // 슬라이드1버튼1URL
    private String slide1Button2; // 슬라이드1버튼2
    private String slide1Button2Url; // 슬라이드1버튼2URL
    private String rcsSlide2Image; // RCS슬라이드2이미지
    private String slide2Content; // 슬라이드2내용
    private String slide2Button1; // 슬라이드2버튼1
    private String slide2Button1Url; // 슬라이드2버튼1URL
    private String slide2Button2; // 슬라이드2버튼2
    private String slide2Button2Url; // 슬라이드2버튼2URL
    private String rcsSlide3Image; // RCS슬라이드3이미지
    private String slide3Content; // 슬라이드3내용
    private String slide3Button1; // 슬라이드3버튼1
    private String slide3Button1Url; // 슬라이드3버튼1URL
    private String slide3Button2; // 슬라이드3버튼2
    private String slide3Button2Url; // 슬라이드3버튼2URL
    private String optOutCallbackNumber; // 수신거부콜백번호

    public MessageType getMessageType() {
        return MessageType.valueOf(messageTypeValue);
    }

    public SmsValidatorParam toSmsValidatorParam() {
        return new SmsValidatorParam(messageName, messageContent);
    }

    public MmsValidatorParam toMmsValidatorParam() {
        return new MmsValidatorParam(messageName, messageContent);
    }

    public RcsMmsValidatorParam toRcsMmsValidatorParam() {
        return new RcsMmsValidatorParam(messageName, messageContent);
    }

    public RcsCarouselValidatorParam toRcsCarouselValidatorParam() {
        return new RcsCarouselValidatorParam(messageName, messageContent);
    }
}

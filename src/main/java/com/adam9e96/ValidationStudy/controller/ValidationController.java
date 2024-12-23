package com.adam9e96.ValidationStudy.controller;


import com.adam9e96.ValidationStudy.form.CalcForm;
import com.adam9e96.ValidationStudy.validator.CalcValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class ValidationController {
    /**
     * 주입(인젝션)
     */
    @Autowired
    CalcValidator calcValidator;

    /**
     * 커스텀 유효성 검사기 등록
     */
    @InitBinder("calcForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(calcValidator);
    }

    @Autowired
    private MessageSource messageSource;

    public ValidationController(@Qualifier("messageSource") MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * form-backing bean 초기화
     */
    @ModelAttribute
    public CalcForm setUpForm() {
//        log.info("setUpForm");
        return new CalcForm();
    }

    /**
     * 입력 화면 표시
     */
    @GetMapping("show")
    public String showView() {
        log.info("showView method called");

        // 메시지 가져오기
        String message = messageSource.getMessage("welcome.message", null, LocaleContextHolder.getLocale());
        String message2 = messageSource.getMessage("calcForm.leftNum", null, LocaleContextHolder.getLocale());

        log.info(message);
        log.info(message2);
        // 반환값으로 뷰 이름을 돌려줌
        return "entry";
    }


    /**
     * 확인 화면을 표시 : Form 클래스 이용
     */
    @PostMapping("calc")
    public String confirmView(@Validated CalcForm ff,
                              BindingResult bindingResult, Model model) {
        log.info("confirmView method called");
        // 입력 체크에서 에러가 발생한 경우
        if (bindingResult.hasErrors()) {
            log.info("confirmView method has Errors");
            // 입력 화면으로
            return "entry";
        }
        // 값 더하기
        Integer result = ff.getLeftNum() + ff.getRightNum();
        log.info("confirmView method has Result: {}", result);

        // Model 에 저장
        model.addAttribute("result", result);

        // 확인 화면으로
        return "confirm";
    }
}

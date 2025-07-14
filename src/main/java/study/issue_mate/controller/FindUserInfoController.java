package study.issue_mate.controller;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.issue_mate.common.ApiResponse;
import study.issue_mate.common.SuccessType;
import study.issue_mate.dto.RequestSendCodeDto;
import study.issue_mate.dto.RequestVerifyCodeDto;
import study.issue_mate.service.FindUserInfoService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/findUser")
public class FindUserInfoController {

    private final FindUserInfoService findUserInfoService;

    @PostMapping("/code")
    public ResponseEntity<?> sendCodeForFindingId(@RequestBody @Valid RequestSendCodeDto dto){
        findUserInfoService.sendCode(dto.getName(), dto.getPhoneNumber());
        return ResponseEntity.ok(ApiResponse.success(SuccessType.INQUERY_SUCCESS));

    }
    @PostMapping("/id")
    public ResponseEntity<ApiResponse<String>> findId(@RequestBody RequestVerifyCodeDto dto){
        String userEmail = findUserInfoService.verifyCodeAndSendEmail(
            dto.getName(),
            dto.getPhoneNumber(),
            dto.getCode());
        return ResponseEntity.ok(ApiResponse.success(SuccessType.INQUERY_SUCCESS,userEmail));
    }
}

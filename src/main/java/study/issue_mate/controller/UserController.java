package study.issue_mate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import study.issue_mate.common.ApiResponse;
import study.issue_mate.common.SuccessType;
import study.issue_mate.dto.RequestUpdateUserDto;
import study.issue_mate.dto.SignUpRequestDto;
import study.issue_mate.jwt.CustomUserDetails;
import study.issue_mate.service.UserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequestDto request) {
        userService.signUp(request);
        return ResponseEntity.ok(ApiResponse.success(SuccessType.INQUERY_SUCCESS));
    }

    @GetMapping("/checkEmail")
    public ResponseEntity<ApiResponse<Boolean>> checkEmail(@RequestParam String userEmail){
        boolean b = userService.checkEmail(userEmail);
        return ResponseEntity.ok(ApiResponse.success(SuccessType.INQUERY_SUCCESS,b));
    }

    @PutMapping("/")
    public ResponseEntity<?> user(@AuthenticationPrincipal CustomUserDetails userDetails,
                                  @RequestParam RequestUpdateUserDto dto){
        String userEmail = userDetails.getUser().getUserEmail();
        userService.updateUser(userEmail,dto);
        return ResponseEntity.ok(ApiResponse.success(SuccessType.UPDATE_SUCCESS));

    }

    @DeleteMapping("/")
    public ResponseEntity<?> user(@AuthenticationPrincipal CustomUserDetails userDetails){
        String userEmail = userDetails.getUser().getUserEmail();
        userService.deleteByEmail(userEmail);
        return ResponseEntity.ok(ApiResponse.success(SuccessType.DELETE_SUCCESS));
    }
}

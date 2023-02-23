package gdsc.skhu.liferary.controller;

import gdsc.skhu.liferary.domain.DTO.MemberDTO;
import gdsc.skhu.liferary.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;

@Tag(name = "Member", description = "API for authentication and authorization")
@RequiredArgsConstructor
@RequestMapping("/api/firebase")
@RestController
public class FirebaseController {
    private final MemberService memberService;

    // Create
    @Operation(summary = "firebase login", description = "Login with Firebase")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PostMapping("/login")
    public MemberDTO.Response login(ServletRequest request) {
        return memberService.login(request);
    }

    // Read
    @Operation(summary = "firebase user info", description = "Read user info")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping("/info")
    public MemberDTO.Response getInfo(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        return memberService.findByEmail(currentUser.getUsername());
    }
}

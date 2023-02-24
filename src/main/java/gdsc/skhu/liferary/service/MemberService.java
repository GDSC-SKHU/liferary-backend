package gdsc.skhu.liferary.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import gdsc.skhu.liferary.domain.DTO.MemberDTO;
import gdsc.skhu.liferary.domain.DTO.TokenDTO;
import gdsc.skhu.liferary.domain.Member;
import gdsc.skhu.liferary.token.TokenProvider;
import gdsc.skhu.liferary.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final FirebaseAuth firebaseAuth;

    @Transactional
    public MemberDTO.Response signup(MemberDTO.@Valid SignUp signUpRequestDTO) {
        if (memberRepository.findByEmail(signUpRequestDTO.getEmail()).isPresent()) {
            throw new IllegalStateException("Duplicated email");
        }
        if (!signUpRequestDTO.getPassword().equals(signUpRequestDTO.getCheckedPassword())) {
            throw new IllegalStateException("Password mismatch");
        }
        List<String> roles = new ArrayList<>();
        roles.add("USER");

        Member member = memberRepository.saveAndFlush(Member.builder()
                .email(signUpRequestDTO.getEmail())
                .nickname(signUpRequestDTO.getNickname())
                .password(passwordEncoder.encode(signUpRequestDTO.getPassword()))
                .roles(roles).build());

        return new MemberDTO.Response(memberRepository.findById(member.getId())
                .orElseThrow(() -> new NoSuchElementException("Member not found")));
    }


    /* 회원가입 시, 유효성 체크 */
    @Transactional(readOnly = true)
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        /* 유효성 검사에 실패한 필드 목록을 받음 */
        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }

    //email 중복 확인
    @Transactional(readOnly = true)
    public void checkEmailDuplication(MemberDTO.@Valid SignUp dto) {
        boolean emailDuplicate = memberRepository.existsByEmail(dto.toEntity().getEmail());
        if (emailDuplicate) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }
    }

    @Transactional
    public TokenDTO login(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return tokenProvider.createToken(authentication);
    }

    @Transactional
    public MemberDTO.Response login(ServletRequest request) {
        String token;
        FirebaseToken firebaseToken;
        try {
            token = ((HttpServletRequest) request).getHeader("Authorization");
            firebaseToken = firebaseAuth.verifyIdToken(token);
        } catch (IllegalArgumentException | FirebaseAuthException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        String password = passwordEncoder.encode(UUID.randomUUID().toString());
        return signup(MemberDTO.SignUp.builder()
                .email(firebaseToken.getUid())
                .nickname(firebaseToken.getName())
                .password(password)
                .checkedPassword(password)
                .build());
    }
    @Transactional
    public void withdraw(Long id) {
        memberRepository.deleteById(id);
    }


    //회원 정보 조회
    @Transactional(readOnly = true)
    public MemberDTO.Login findById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(()-> new NoSuchElementException("Member not found"));
        return MemberDTO.Login.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .build();
    }

    public MemberDTO.Response findByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new NoSuchElementException("Member not found"));
        return new MemberDTO.Response(member);
    }

//    @Transactional
//    public void withdraw(String checkPassword) throws Exception {
//        Member member = memberRepository.findById(SecurityUtil.getLoginUsername()).orElseThrow(() -> new Exception("회원이 존재하지 않습니다"));
//
//        if(!member.matchPassword(passwordEncoder, checkPassword) ) {
//            throw new Exception("비밀번호가 일치하지 않습니다.");
//        }
//
//        memberRepository.delete(member);
//    }


//    //회원 정보 삭제
//    @Transactional
//    public void removeMember(Long id) {
//        memberRepository.deleteById(id);
//    }
}

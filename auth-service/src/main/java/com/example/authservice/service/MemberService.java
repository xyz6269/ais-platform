package com.example.authservice.service;


import com.example.authservice.DTO.LoginDTO;
import com.example.authservice.DTO.LoginResponse;
import com.example.authservice.DTO.MemberDTO;
import com.example.authservice.entity.Member;
import com.example.authservice.enums.Gender;
import com.example.authservice.enums.Major;
import com.example.authservice.repository.MemberRepository;
import com.example.authservice.security.CustomUserDetails;
import com.example.authservice.security.jwt.JwtUtil;
import com.example.authservice.util.PhoneNumberValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse authenticateUser(LoginDTO dto) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(token);
        } catch (BadCredentialsException e) {
            throw new RuntimeException("credentials are incorrect");
        }

        if (authentication.getPrincipal() instanceof CustomUserDetails customUser) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return new LoginResponse(jwtUtil.generateToken(customUser));
        } else {
            throw new RuntimeException("credentials are incorrect");
        }
    }

    public String registerUser(MemberDTO dto) {
        Member newMember = new Member();
        newMember.setEmail(dto.email());
        newMember.setFirstName(dto.firstName());
        newMember.setLastName(dto.lastName());
        newMember.setPhoneNumber(validatePhoneNumber(dto.phoneNumber()));
        newMember.setGender(Gender.valueOf(dto.gender()));
        newMember.setMajor(Major.valueOf(dto.major()));
        newMember.setPassword(passwordEncoder.encode(dto.password()));
        memberRepository.save(newMember);

        return newMember.getEmail();
    }


    public Member getUserById(Long id) {
        return memberRepository.findUserById((id))
                .orElseThrow(() -> new RuntimeException("user doesn't exit"));
    }

    public Member getUserByEmail(String email) {
        return memberRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("user doesn't exit"));
    }

    public String getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private String validatePhoneNumber(String phoneNumber) {

        if (!PhoneNumberValidator.isValid(phoneNumber, "MA")) {
           throw new RuntimeException("invalid phone number");
        }

        return PhoneNumberValidator.formatToE164(phoneNumber, "MA");
    }
}

package com.example.authservice.service;


import com.example.authservice.DTO.LoginDTO;
import com.example.authservice.DTO.LoginResponse;
import com.example.authservice.DTO.SignUpDTO;
import com.example.authservice.entity.Member;
import com.example.authservice.enums.*;
import com.example.authservice.exceptions.InvalidPhoneNumber;
import com.example.authservice.exceptions.UserNotFoundException;
import com.example.authservice.mail.EmailTemplateService;
import com.example.authservice.repository.MemberRepository;
import com.example.authservice.security.CustomUserDetails;
import com.example.authservice.security.jwt.JwtUtil;
import com.example.authservice.util.PhoneNumberValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final EmailTemplateService emailTemplateService;


    public LoginResponse authenticateUser(LoginDTO dto) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(token);
        } catch (BadCredentialsException e) {
            log.warn("user : {}, entered bad credentials : {}",dto.email() ,e.getMessage());
            throw e;
        }

        if (authentication.getPrincipal() instanceof CustomUserDetails customUser) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("user : {}, logged In", dto.email());
            return new LoginResponse(jwtUtil.generateToken(customUser));
        } else {
            log.warn("user : {}, entered bad credentials",dto.email());
            throw new BadCredentialsException("credentials are incorrect");
        }
    }

    public String registerUser(SignUpDTO dto) {
        Member newMember = Member.builder()
                .email(dto.email())
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .phoneNumber(validatePhoneNumber(dto.phoneNumber()))
                .gender(Gender.valueOf(dto.gender()))
                .major(Major.valueOf(dto.major()))
                .academicYear(AcademicYear.valueOf(dto.academicYear()))
                .interests(dto.interests())
                .status(AccountStatus.DISABLED)
                .roles(Set.of(Role.USER))
                .password(passwordEncoder.encode(dto.password()))
                .build();

        memberRepository.save(newMember);
        log.info("user : {} has been registered", dto.email());
        sendWelcomeEmail(dto.email(), dto.lastName(), dto.firstName());
        return newMember.getEmail();
    }

    public String activateUserAccount(Long id) {
        Member member = getUserById(id);
        log.debug("activating the account of : {}", member.getEmail());
        member.setStatus(AccountStatus.ACTIVATED);
        memberRepository.save(member);
        log.debug("user : {} account has been activated", member.getEmail());

        sendActivationEmail(member.getEmail(), member.getLastName(), member.getFirstName());
        return member.getEmail();
    }

    public Member getUserById(Long id) {
        return memberRepository.findMemberById((id))
                .orElseThrow(() -> new UserNotFoundException("user doesn't exist"));
    }

    public Member getUserByEmail(String email) {
        return memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("user doesn't exist"));
    }

    public String getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private String validatePhoneNumber(String phoneNumber) {

        if (!PhoneNumberValidator.isValid(phoneNumber, "MA")) {
           throw new InvalidPhoneNumber("invalid phone number");
        }

        return PhoneNumberValidator.formatToE164(phoneNumber, "MA");
    }

    @Async
    public void sendActivationEmail(String email, String lastName, String firstName) {
        try {
            emailTemplateService.sendAccountActivationEmailNotification(email, lastName, firstName);
        } catch (Exception e) {
            log.error("failed to send email to user : {}, due to : {}",email ,e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Async
    public void sendWelcomeEmail(String email, String lastName, String firstName) {
        try {
            emailTemplateService.sendWelcomeEmail(email, lastName, firstName);
        } catch (Exception e) {
            log.error("failed to send email to user : {}, due to : {}",email ,e.getMessage());
            throw new RuntimeException(e);
        }
    }




//    public void makeAdmin() {
//        Member admin = Member.builder()
//                .email("admin@email.com")
//                .firstName("admin")
//                .lastName("admin")
//                .phoneNumber("123456789")
//                .gender(Gender.MALE)
//                .major(Major.SDIA)
//                .academicYear(AcademicYear.FIFTH_YEAR)
//                .interests("modir l9orob")
//                .status(AccountStatus.ACTIVATED)
//                .roles(Set.of(Role.ADMIN))
//                .password(passwordEncoder.encode("admin"))
//                .build();
//
//        memberRepository.save(admin);
//    }

}

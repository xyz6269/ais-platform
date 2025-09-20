package com.example.chatservice.service;

import com.example.chatservice.DTO.ParticipantDTO;
import com.example.chatservice.entity.Participant;
import com.example.chatservice.exceptions.ParticipantNotFoundException;
import com.example.chatservice.repository.ParticipantRepository;
import com.example.chatservice.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParticipantService {

    private final ParticipantRepository participantRepository;

    @Transactional
    public void createParticipant(ParticipantDTO dto) {
        Participant participant = new Participant(dto.id(), dto.email());
        log.debug("creating new participant : {}", dto.email());
        participantRepository.save(participant);
    }

    @Transactional
    public void updateParticipant(Participant participant) {
        log.debug("updating participant : {}", participant.getEmail());
        participantRepository.save(participant);
    }

    @Transactional(readOnly = true)
    public Participant getParticipantByEmail(String email) {
        return participantRepository.
                findParticipantByEmail(email)
                .orElseThrow(() -> new ParticipantNotFoundException("No participant with the given email exists"));
    }

    @Transactional(readOnly = true)
    public List<Participant> getParticipantsByEmails(List<String> emails) {
        return participantRepository.findParticipantsByEmails(emails);
    }

    public static String getCurrentUser() {
        return JwtUtil.getCurrentUserEmail();
    }

}

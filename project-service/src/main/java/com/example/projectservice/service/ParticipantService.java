package com.example.projectservice.service;


import com.example.projectservice.DTO.ParticipantDTO;
import com.example.projectservice.entity.Participant;
import com.example.projectservice.exceptions.ParticipantNotFoundException;
import com.example.projectservice.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;

    @Transactional
    public void createParticipant(ParticipantDTO dto) {
        Participant participant = Participant.builder()
                .id(dto.id())
                .email(dto.email())
                .build();
        participantRepository.save(participant);
    }

    @Transactional(readOnly = true)
    public Participant getParticipantByEmail(String email) {
        return participantRepository.findByEmail(email)
                .orElseThrow(() -> new ParticipantNotFoundException("this participant with this email : "+ email + " doesn't exist"));
    }

    @Transactional(readOnly = true)
    public Participant getParticipantById(Long id) {
        return participantRepository.findById(id)
                .orElseThrow(() -> new ParticipantNotFoundException("this participant with this id : "+ id + " doesn't exist"));
    }

    @Transactional
    public void makeParticipantAdmin(Long id) {
        log.info("updating participant of id : {}", id);
        Participant participant = getParticipantById(id);
        participant.setAdmin(true);
    }

    @Transactional
    public void deleteParticipant(Long id) {
        log.info("removing participant of id : {}", id);
        participantRepository.deleteById(id);
    }



}

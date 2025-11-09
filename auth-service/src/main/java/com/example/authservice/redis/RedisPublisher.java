package com.example.authservice.redis;

import com.example.authservice.DTO.ParticipantDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic channelTopicParticipantMessage = new ChannelTopic("channel-participant");
    private final ChannelTopic channelTopicPromoteToAdminMessage = new ChannelTopic("channel-participant-admin");

    @Autowired
    public RedisPublisher(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void publishParticipant(Object message) {
        redisTemplate.convertAndSend(channelTopicParticipantMessage.getTopic(), message);
    }

    public void publishAdminMessage(Object message) {
        redisTemplate.convertAndSend(channelTopicPromoteToAdminMessage.getTopic(), message);
    }
}
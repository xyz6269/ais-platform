package com.example.chatservice.redis;

import com.example.chatservice.DTO.ChatMessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic channelTopicDirectMessage = new ChannelTopic("channel-direct_chat_message");
    private final ChannelTopic channelTopicGroupMessage = new ChannelTopic("channel-group_chat_message");

    @Autowired
    public RedisPublisher(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void publishDirectMessage(ChatMessageDTO message) {
        redisTemplate.convertAndSend(channelTopicDirectMessage.getTopic(), message);
    }

    public void publishGroupMessage(ChatMessageDTO message) {
        redisTemplate.convertAndSend(channelTopicGroupMessage.getTopic(), message);
    }


}
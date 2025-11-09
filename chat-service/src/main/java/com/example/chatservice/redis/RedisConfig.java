package com.example.chatservice.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {

    @Bean
    public MessageListenerAdapter participantListener(RedisSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "handleParticipantMessage");
    }

    @Bean
    public MessageListenerAdapter adminParticipantListener(RedisSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "handleAdminMessage");
    }

    @Bean
    public MessageListenerAdapter directChatMessageListener(RedisSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "handleDirectChatMessage");
    }

    @Bean
    public MessageListenerAdapter groupChatMessageListener(RedisSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "handleGroupChatMessage");
    }

    @Bean
    public RedisMessageListenerContainer container(
            RedisConnectionFactory factory,
            MessageListenerAdapter participantListener,
            MessageListenerAdapter directChatMessageListener,
            MessageListenerAdapter groupChatMessageListener,
            MessageListenerAdapter adminParticipantListener) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);

        container.addMessageListener(participantListener, new PatternTopic("channel-participant"));
        container.addMessageListener(directChatMessageListener, new PatternTopic("channel-chat_message"));
        container.addMessageListener(groupChatMessageListener, new PatternTopic("channel-group_message"));
        container.addMessageListener(adminParticipantListener, new PatternTopic("channel-participant-admin"));

        return container;
    }
}
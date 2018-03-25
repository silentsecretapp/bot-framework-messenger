package com.botscrew.messengercdk.service.impl;

import com.botscrew.messengercdk.config.property.MessengerProperties;
import com.botscrew.messengercdk.exception.SendAPIException;
import com.botscrew.messengercdk.model.MessengerBot;
import com.botscrew.messengercdk.model.MessengerUser;
import com.botscrew.messengercdk.model.incomming.UserInfo;
import com.botscrew.messengercdk.model.outgoing.attachment.TemplateAttachment;
import com.botscrew.messengercdk.model.outgoing.element.TemplateElement;
import com.botscrew.messengercdk.model.outgoing.element.quickreply.QuickReply;
import com.botscrew.messengercdk.model.outgoing.message.GenericTemplateMessage;
import com.botscrew.messengercdk.model.outgoing.message.Message;
import com.botscrew.messengercdk.model.outgoing.message.QuickReplyMessage;
import com.botscrew.messengercdk.model.outgoing.payload.GenericTemplatePayload;
import com.botscrew.messengercdk.model.outgoing.request.Request;
import com.botscrew.messengercdk.service.TokenizedSender;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@RequiredArgsConstructor
public class TokenizedSenderImpl implements TokenizedSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultReportHandler.class);

    private final RestTemplate restTemplate;
    private final MessengerProperties properties;
    private final ThreadPoolTaskScheduler scheduler;

    @Override
    public void send(String token, Request request) {
        post(token, request);
    }

    @Override
    public void send(String token, MessengerUser recipient, String text) {
        Request message = Request.builder()
                .message(new Message(text))
                .recipient(new UserInfo(recipient.getChatId()))
                .build();

        post(token, message);
    }

    @Override
    public ScheduledFuture send(String token, MessengerUser recipient, String text, Integer delayMillis) {
        return scheduler.schedule(() -> send(token, recipient, text), currentDatePlusMillis(delayMillis));
    }

    @Override
    public void send(String token, MessengerUser recipient, String text, List<QuickReply> quickReplies) {
        Request message = Request.builder()
                .message(new QuickReplyMessage(text, quickReplies))
                .recipient(new UserInfo(recipient.getChatId()))
                .build();

        post(token, message);
    }

    @Override
    public ScheduledFuture send(String token, MessengerUser recipient, String text, List<QuickReply> quickReplies, Integer delayMillis) {
        return scheduler.schedule(() -> send(token, recipient, text, quickReplies), currentDatePlusMillis(delayMillis));
    }

    @Override
    public void send(String token, MessengerUser recipient, List<TemplateElement> elements) {
        TemplateAttachment templateAttachment = new TemplateAttachment(new GenericTemplatePayload(elements));
        Request message = Request.builder()
                .message(new GenericTemplateMessage(templateAttachment))
                .recipient(new UserInfo(recipient.getChatId()))
                .build();

        post(token, message);
    }

    @Override
    public ScheduledFuture send(String token, MessengerUser recipient, List<TemplateElement> elements, Integer delayMillis) {
        return scheduler.schedule(() -> send(token, recipient, elements), currentDatePlusMillis(delayMillis));
    }

    @Override
    public void send(String token, MessengerUser recipient, List<TemplateElement> elements, List<QuickReply> quickReplies) {
        TemplateAttachment templateAttachment = new TemplateAttachment(new GenericTemplatePayload(elements));
        Request message = Request.builder()
                .message(new GenericTemplateMessage(quickReplies, templateAttachment))
                .recipient(new UserInfo(recipient.getChatId()))
                .build();

        post(token, message);
    }

    @Override
    public ScheduledFuture send(String token, MessengerUser recipient, List<TemplateElement> elements, List<QuickReply> quickReplies, Integer delayMillis) {
        return scheduler.schedule(() -> send(token, recipient, elements, quickReplies), currentDatePlusMillis(delayMillis));
    }

    @Override
    public ScheduledFuture send(String token, Request request, Integer delayMillis) {
        return scheduler.schedule(() -> send(token, request), currentDatePlusMillis(delayMillis));
    }

    private void post(String token, Request message) {
        LOGGER.debug("Posting message: \n{}", message);
        try {
            restTemplate.postForObject(properties.getMessagingUrl(token), message, String.class);
        }catch (HttpClientErrorException|HttpServerErrorException e) {
            throw new SendAPIException(e.getResponseBodyAsString());
        }
    }

    private Date currentDatePlusMillis(Integer millis) {
        return addToDate(new Date(), Calendar.MILLISECOND, millis);
    }

    private Date addToDate(Date date, int calendarField, int amount) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }

    @Override
    public void send(MessengerBot bot, Request request) {
        post(bot.getAccessToken(), request);
    }
}

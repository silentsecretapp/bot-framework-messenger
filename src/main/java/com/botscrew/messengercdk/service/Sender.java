/*
 * Copyright 2018 BotsCrew
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.botscrew.messengercdk.service;

import com.botscrew.messengercdk.model.MessengerUser;
import com.botscrew.messengercdk.model.outgoing.element.TemplateElement;
import com.botscrew.messengercdk.model.outgoing.element.quickreply.QuickReply;
import com.botscrew.messengercdk.model.outgoing.request.Request;

import java.util.List;
import java.util.concurrent.ScheduledFuture;

/**
 * Sender for Facebook Messenger abstraction(supposed that we have one Send API credentials available in the system)
 */
public interface Sender {
    void send(MessengerUser recipient, String text);

    ScheduledFuture send(MessengerUser recipient, String text, Integer delayMillis);

    void send(MessengerUser recipient, String text, List<QuickReply> quickReplies);

    ScheduledFuture send(MessengerUser recipient, String text, List<QuickReply> quickReplies, Integer delayMillis);

    void send(MessengerUser recipient, List<TemplateElement> elements);

    ScheduledFuture send(MessengerUser recipient, List<TemplateElement> elements, Integer delayMillis);

    void send(MessengerUser recipient, List<TemplateElement> elements, List<QuickReply> quickReplies);

    ScheduledFuture send(MessengerUser recipient, List<TemplateElement> elements, List<QuickReply> quickReplies, Integer delayMillis);

    void send(Request request);

    ScheduledFuture send(Request request, Integer delayMillis);
}

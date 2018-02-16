package com.botscrew.messenger.cdk.model.outgoing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuickRepliesMessage extends Message {

    @JsonProperty("quick_replies")
    private List<QuickReply> quickReplies;

    public QuickRepliesMessage(String text, List<QuickReply> quickReplies) {
        super(text);
        this.quickReplies = quickReplies;
    }
}

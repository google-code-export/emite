package com.calclab.examplechat.client.chatuiplugin.params;

public class ChatInputMessageParam {

    final private String chatId;
    final private String fromUser;
    final private String message;

    /**
     * @param chatId
     *                we are user the room name in Group chats and the other
     *                user jid for pairchat
     * @param fromUser
     *                Currently we use jid in pair chats and alias in group chat
     *                :-/
     */
    public ChatInputMessageParam(final String chatId, final String fromUser, final String message) {
        this.chatId = chatId;
        this.fromUser = fromUser;
        this.message = message;
    }

    public String getChatId() {
        return chatId;
    }

    public String getFromUser() {
        return fromUser;
    }

    public String getMessage() {
        return message;
    }

}

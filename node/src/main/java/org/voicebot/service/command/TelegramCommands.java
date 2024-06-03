package org.voicebot.service.command;

public enum TelegramCommands {

    HELP("/help"),
    REGISTRATION("/registration"),
    CANCEL("/cancel"),
    START("/start"),
    CLEAR("/clear");
    private final String cmd;


    TelegramCommands(String cmd) {
        this.cmd = cmd;
    }
    @Override
    public String toString(){
        return cmd;
    }

    public String getCommandValue() {
        return cmd;
    }

}

package org.voicebot.service.enums;

public enum ServiceCommands {
    HELP("/help"),
    REGISTRATION("/registration"),
    CANCEL("/cancel"),
    START("/start"),
    CLEAR("/clear");
    private final String cmd;


    ServiceCommands(String cmd) {
        this.cmd = cmd;
    }
@Override
    public String toString(){
        return cmd;
    }

    public boolean isEqual(String cmd){
       return this.toString().equals(cmd);
    }
}

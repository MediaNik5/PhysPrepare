package org.pooh.cmds;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface CMD{


    /**
     * @return the name of cmd
     */
    public String getName();

    /**Handles the command
     * @param e Update sent by telegram
     * @param chatid chat id to send message to
     */
    public void handle(Update e, long chatid);

    /**
     * @param user user id
     * @param chatid chat id
     * @return true if user has rights to use this command here
     */
    public boolean hasRights(int user, long chatid);
}

package org.pooh.cmds;

import org.pooh.Phys;
import org.pooh.tools.AAUser;
import org.telegram.telegrambots.meta.api.objects.Update;

public class start implements CMD{


    /**
     * @return the name of cmd
     */
    @Override
    public String getName(){
        return "/start";
    }

    /**Handles the command
     * @param e Update sent by telegram
     * @param chatid chat id to send message to
     */
    @Override
    public void handle(Update e, long chatid){

        AAUser user = new AAUser(e.getMessage().getFrom());

        Phys.ph.sendMessage(chatid, user.getString("message", "Выберите секцию"), user.getKeyboard());
    }

    /**
     * @param user user id
     * @param chatid chat id
     * @return true if user has rights to use this command here
     */
    @Override
    public boolean hasRights(int user, long chatid){
        return user == chatid;
    }
}

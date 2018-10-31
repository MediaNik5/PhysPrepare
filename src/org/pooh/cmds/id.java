package org.pooh.cmds;

import org.pooh.Phys;
import org.telegram.telegrambots.meta.api.objects.Update;

public class id implements CMD{
    @Override
    public String getName(){
        return "/id";
    }

    @Override
    public void handle(Update e, long chatid){
        try{
            Phys.ph.sendMessage(chatid, e.getMessage().getPhoto().get(0).getFileId());
        }catch(Throwable ex){
            try{
                Phys.ph.sendMessage(chatid, e.getMessage().getReplyToMessage().getPhoto().get(0).getFileId());
            }catch(Throwable ex1){
                Phys.ph.sendMessage(chatid, chatid+"");
            }
        }
    }

    @Override
    public boolean hasRights(int user, long chatid){
        return user == chatid;
    }
}

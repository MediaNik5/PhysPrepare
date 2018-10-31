package org.pooh.cmds;

import org.pooh.Phys;
import org.pooh.tools.AAContainer;
import org.pooh.tools.AAUser;
import org.pooh.tools.Config;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;

public class Handler{

    /**
     * The HahsMap of cmds
     */
    public static final HashMap<String, CMD> cmds = new HashMap<>();

    /**
     * Handle method
     * @param e Update sent by telegram
     */
    public static void handle(Update e){

        if(e.hasCallbackQuery()){//if this is button click/tap

            CallbackQuery q = e.getCallbackQuery();//back data which was set in Phys#getKeyboard
            String[] s = q.getData().split("\\.");//the indexes
            AAUser u = new AAUser(q.getFrom());//user for this session
            AAContainer c = u.containers.get(s[0]);//first container

            String quest = null;//quest - the question or  theory
            String[] photos = null;//photos - the list of photos

            for(int i = 1; i < s.length; i++){
                if(c.containers != null && c.containers.containsKey(s[i]))//checking if the containers list is null and if it contains key s[i]
                    c = c.containers.get(s[i]);//replaces the current container with questioned one
                else if(c.getType() == AAContainer.ContentType.Answer && c.strings != null && c.strings.containsKey(s[i])){//if C doesnt have containers or there is no key s[i]
                    //checks if this is Answer, strings is not null and if strings contains key s[i]
                    quest = c.strings.get(s[i]);
                    photos = AAUser.getContainer(u, String.join(".", s).replaceAll("."+s[i-1]+"."+s[i], "")).getPhotos("Ответ");
                    String res = String.join(".", s);
                    res = res.substring(0, res.length()-s[i].length()-s[i-1].length()-2);
                    AAContainer res1 = AAUser.getContainer(u,  res);
                    if(res1.getType() == AAContainer.ContentType.Quest && res1.strings.get("Ответ").equalsIgnoreCase(s[i]))
                        res1.setDone();//checks the answer and if right, sets the plus after name of it
                    break;
                }
                if(c.getType() == AAContainer.ContentType.Theory && c.strings != null && c.strings.containsKey(s[i])){
                    quest = c.strings.get(s[i]);//if this is theory, then gives it
                    photos = c.getPhotos(s[i]);
                }
                if(c.getType() == AAContainer.ContentType.Quest){
                    quest = c.strings.get("Вопрос");//if this is quest then gives the question
                    photos = c.getPhotos("Вопрос");
                }
            }

            if(quest == null)
                Phys.ph.editMessage(q.getMessage().getMessageId(),
                        q.getInlineMessageId(),
                        q.getFrom().getId(),
                        u.getString("message", "Выберите секцию из " + c.getName()),
                        photos,
                        c.getKeyboard(q.getData() + "."));//if it is not theory or quest, sends the contained sequences and questions
            else
                Phys.ph.editMessage(q.getMessage().getMessageId(),
                        q.getInlineMessageId(), q.getFrom().getId(),
                        quest, photos,
                        c.getKeyboard(q.getData()+"."));//if it is theory or quest, sends the text of it, keyboard and photos if exist
            Config.save(System.getProperty("user.dir") + "/" + u.getId()+".json", u);//saves the user
        }

        if(!e.hasMessage() || !e.getMessage().hasText())//if there is no message stops the handler
            return;
        long chatid = e.getMessage().getChatId();

        for(CMD c : cmds.values())
            if(exact(c.getName().toLowerCase(), e.getMessage().getText().split(" ", 2)[0].toLowerCase()))
                if(c.hasRights(e.getMessage().getFrom().getId(), chatid))
                    c.handle(e, chatid);//handling cmd

    }

    /**
     * Checks if command equal
     * @param name the command name
     * @param s the got command
     * @return true if name equals s. eg s = "/start@MyBot" and name = "/start", it returns true
     * OR s = "/startie@Mysecondbot" and name = "/start", it returns false
     */
    private static boolean exact(String name, String s){
        return name.equals(s) || name.equals(s.split("@", 2)[0]);
    }
}

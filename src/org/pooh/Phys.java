package org.pooh;

import org.pooh.cmds.Handler;
import org.pooh.cmds.count;
import org.pooh.cmds.id;
import org.pooh.cmds.start;
import org.pooh.tools.Config;
import org.pooh.tools.dflt;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

/**
 * The bot for getting ready for exams(for now just for physics)
 * by @MediaNik
 */
public class Phys extends TelegramLongPollingBot{

    /**
     * initializer of telegram
     */
    static{
        ApiContextInitializer.init();
    }
    /**
     * instance of bot to use
     */
    public static final Phys ph = new Phys();
    /**
     * Config to write some cache in it
     */
//    Config cfg = new Config(System.getProperty("user.dir") + "/" + dflt.cfgFileName);

    /**
     * initializer of bot
     * @param s useless args
     */
    public static void main(String[] s){
        Handler.cmds.put("/start", new start());
        Handler.cmds.put("/count", new count());
        Handler.cmds.put("/id", new id());

        TelegramBotsApi botapi = new TelegramBotsApi();
        try{
            botapi.registerBot(new Phys());
        }catch(TelegramApiException e){
            e.printStackTrace();
        }
    }

    /**
     * creates keyboard of buttons for telegram message
     * @param start start index of the list of entries for *THIS* keyboard
     * @param end end index of the list of entries for *THIS* keyboard
     * @param entry list of entries
     * @param callback list of back values(for each index of that one equal from ENTRY),
     *                 e.g entry = {"Senior", "Senorita"} callback = {"Mr", "Ms"}, then if user chooses "Senior",
     *                 we get back the "Mr"
     * @return the completed keyboard of values
     */
    public static InlineKeyboardMarkup getKeyboard(int start, int end, List<String> entry, List<String> callback){

        InlineKeyboardMarkup kb = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for(int i = start; i < end && i < entry.size() && i < callback.size(); i += 2){
            List<InlineKeyboardButton> buttons1 = new ArrayList<>();
            for(int a = 0; a < 2 && i + a < entry.size() && i + a < callback.size(); a++)
                buttons1.add(new InlineKeyboardButton(entry.get(i + a)).setCallbackData(callback.get(i + a)));
            buttons.add(buttons1);
        }

        List<InlineKeyboardButton> arrows = new ArrayList<>();
        if(start > 0)//checking if left arrow is needed
            arrows.add(new InlineKeyboardButton("<").setCallbackData(((2 * start - end) < 0 ? 0 : 2 * start - end) + ""));
        if(end < entry.size())//checking if right arrow is needed
            arrows.add(new InlineKeyboardButton(">").setCallbackData(((2 * end - start) > entry.size() - 1 ? entry.size() : 2 * end - start) + ""));
        if(!arrows.isEmpty())
            buttons.add(arrows);
        kb.setKeyboard(buttons);
        return kb;
    }

    /**
     * getting telegram updates(i mean, updates of conversation with user)
     * @param e
     */
    @Override
    public void onUpdateReceived(Update e){
//        cfg = Config.load(System.getProperty("user.dir") + "/" + dflt.cfgFileName);

        Handler.handle(e);

//        cfg.save();
    }

    /**
     * @return standard telegram bot username
     */
    @Override
    public String getBotUsername(){
        return dflt.botName;
    }

    /**
     * @return standard telegram bot token(got by @BotFather)
     */
    @Override
    public String getBotToken(){
        return dflt.botTocken;
    }

    /**
     * sends message to user/group with chatid and with text
     * @param chatid chat id to send to
     * @param text the message
     * @return the returned message
     */
    public Message sendMessage(long chatid, String text){
        return sendMessage(chatid, text, null, null);
    }

    /**
     * sends message to user/group with chatid, with keyboard and with text
     * @param chatid chat id to send to
     * @param text the message
     * @param kb the Keyboard for inline mod
     * @return the returned message
     */
    public Message sendMessage(long chatid, String text, InlineKeyboardMarkup kb){
        return sendMessage(chatid, text, null, kb);
    }

    /**
     * sends message to user/group with chatid, with keyboard and with text
     * @param chatid chat id to send to
     * @param text the message
     * @param photos Ids of telegram files OR URLs of photos to download for telegram
     * @param kb the Keyboard for inline mod
     * @return the returned message
     */
    public Message sendMessage(long chatid, String text, String[] photos, InlineKeyboardMarkup kb){
        SendMessage m = new SendMessage()
                .setChatId(chatid)
                .setText(text)
                .setReplyMarkup(kb);

        Message got = null;
        try{
            got = this.execute(m);
        }catch(TelegramApiException e){
            e.printStackTrace();
        }

        return got;
    }

    /**
     * sends photos
     *
     * @param chatid chat id to send to
     * @param photos ids of telegram files OR URLs of photos to download for telegram
     * @return the List of sent messages with photos
     */
    public List<Message> sendPhotos(long chatid, String[] photos){

        SendPhoto ph;
        List<Message> msgs = new ArrayList<>();
        if(photos != null && photos.length > 0){
            for(String p : photos){
                ph = new SendPhoto().setPhoto(p).setChatId(chatid);
                try{
                    msgs.add(this.execute(ph));
                }catch(TelegramApiException e){
                    e.printStackTrace();
                }
            }
        }
        return msgs;
    }

    /**
     * edites message with id, with inlineId, with chat id, with message, with photos, withc keyboard
     * @param id id of the message
     * @param inlineId inline id to change th inline
     * @param chatid chat id where to edit to
     * @param message the edited text
     * @param photos the photos that being sent before editing the message
     * @param keyboard the keyboard for inline mod
     */
    public void editMessage(int id, String inlineId, long chatid, String message, String[] photos, InlineKeyboardMarkup keyboard){
        EditMessageText mark = new EditMessageText().setMessageId(id).setChatId(chatid).setInlineMessageId(inlineId).setReplyMarkup(keyboard).setText(message);

        sendPhotos(chatid, photos);
        try{
            this.execute(mark);
        }catch(TelegramApiException e){
            e.printStackTrace();
        }
    }

    /**
     * edites message with id, with inlineId, with chat id, with message, withc keyboard
     * no return
     * @param id id of the message
     * @param inlineId inline id to change th inline
     * @param chatid chat id where to edit to
     * @param message the edited text
     * @param keyboard the keyboard for inline mod
     */
    public void editMessage(int id, String inlineId, long chatid, String message, InlineKeyboardMarkup keyboard){
        editMessage(id, inlineId, chatid, message, new String[]{}, keyboard);
    }
}

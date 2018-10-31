package org.pooh.tools;

import org.pooh.Phys;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class AAContainer{


    public HashMap<String, AAContainer> containers;
    public HashMap<String, String> strings;
    final ContentType t;
    String name;
    boolean done = false;

    /**
     * @param name The name of container
     * @param type The type of container
     */
    public AAContainer(String name, ContentType type, AAContainer... containers){
        this.name = name;
        t = type;
        this.containers = new HashMap<>();
        strings = new HashMap<>();
        for(AAContainer c : containers)
            this.containers.put(c.name, c);
    }
    public void setDone(){
        if(done!=true && t == ContentType.Quest){
            done=true;
            name+=". Сделано";
        }
    }

    public static AAContainer getDefaultSequence(AAContainer.SequenceType type){
        //todo: loading from some type of files the Sequence of quests
        return null;
    }

    public String getString(String name, String defaulz){
        String s = strings.get(name);
        return s == null ? defaulz : s;
    }

    public String getString(String name){
        return getString(name, "");
    }

    public void addContainer(String name, AAContainer container){
        containers.put(name, container);
    }

    public InlineKeyboardMarkup getKeyboard(String sub){


        List<String> entry = new ArrayList<>();
        List<String> callback = new ArrayList<>();
        if(this.containers != null)
            for(AAContainer c : this.containers.values()){
                if(c.t == ContentType.Sequence || c.t == ContentType.Quest || c.t == ContentType.Theory){
                    entry.add(c.getName());
                    callback.add(sub + c.getName());
                }
            }
        if(this.t != ContentType.Quest && this.t != ContentType.Answer && this.getType() != ContentType.Theory){

            for(String s : getStrings()){
                entry.add(getString(s));
                callback.add(sub + s);
            }
        }else{
            if(this.containers != null && this.containers.containsKey("Ответ") && sub.endsWith(this.name+".")){
                for(String s : this.containers.get("Ответ").strings.keySet()){
                    entry.add(s);
                    callback.add(sub + "Ответ." + s);
                }
            }
            if(this.getType() == ContentType.Theory){
                for(String s : this.strings.keySet()){
                    if(sub.endsWith(s+".")){
                        entry.clear();
                        break;
                    }
                    entry.add(s);
                    callback.add(sub+s);
                }
            }
        }
        return Phys.getKeyboard(0, 18, entry, callback);
    }

    public InlineKeyboardMarkup getKeyboard(){
        return getKeyboard("");
    }

    public void addString(String name, String value){
        strings.put(name, value);
    }

    public String getName(){
        return name;
    }

    protected AAContainer setName(String newName){
        this.name = newName;
        return this;
    }

    public ContentType getType(){
        return t;
    }

    public Set<String> getStrings(){
        return strings.keySet();
    }

    public String[] getPhotos(String number){

        if(containers != null && containers.containsKey("photos")){
            AAContainer a = containers.get("photos");
            String s = a.strings.get(number);
            if(s == null)
                return new String[]{};
            return s.split(" ");

        }
        return new String[]{};
    }

    public enum ContentType{
        Quest,
        Answer,
        User,
        Sequence,
        Photo,
        Theory
    }

    public enum SequenceType{
        Light("Свет"),
        Electricity("Электр");
//        Mechanics("Механика"),
//        Thermodynamics("Термодинамика");

        String name;
        SequenceType(String name){
            this.name = name;
        }

        public String getName(){
            return name;
        }
    }
}

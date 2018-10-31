package org.pooh.cmds;

import org.pooh.Phys;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class count implements CMD{
    @Override
    public String getName(){
        return "/count";
    }

    @Override
    public void handle(Update e, long chatid){

        String[] t = e.getMessage().getText().split(" ", 2);
        if(t.length > 1){
            HashMap<Character, Integer> h = new HashMap<>();
            char[] a = t[1].toCharArray();
            for(char c : a)
                if(h.containsKey(c))
                    h.put(c, h.get(c)+1);
                else h.put(c, 1);
            List<Character> sortedKeys = new ArrayList(h.keySet());
            Collections.sort(sortedKeys);
            Phys.ph.sendMessage(chatid, getStringFromHashMap(h, sortedKeys));
        }
    }
    public String getStringFromHashMap(HashMap<Character, Integer> m, List<Character> l){
        StringBuilder b = new StringBuilder();
        for(Character c : l){
            b.append("'" + c + "' : ");
            b.append(m.get(c) + ";\n");
        }
        return b.toString();
    }

    @Override
    public boolean hasRights(int user, long chatid){
        return true;
    }
}

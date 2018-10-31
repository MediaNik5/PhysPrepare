package org.pooh.tools;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Config{

    /**
     * file for config
     */
    File fle;
    /**
     * strings of config
     */
    HashMap<String, String> strings = new HashMap<>();
    /**
     * lists of strings of config
     */
    HashMap<String, List<String>> lists = new HashMap<>();
    /**
     * name used for saving and loading config file
     */
    String name;


    /**
     * Creates new Config instance with name
     * @param name name used for saving and loading config file
     */
    public Config(String name){
        this.name = name;
        fle = new File(name);//name will be "BugBot.java" + dflt.cfg
        try{
            if(!fle.exists()) fle.createNewFile();
        }catch(IOException e){
            e.printStackTrace();
        }
        load(name);
    }

    /**
     * Creates new Config instance with name
     * @param name name used for saving and loading config file
     * @param empty used for nothing, just to get another constructor
     */
    private Config(String name, boolean empty){
        this.name = name;
        fle = new File(name);//name will be "BugBot.java" + dflt.cfg
        try{
            if(!fle.exists()) fle.createNewFile();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Saves the TARGET to file with name NAME + ".json"
     * @param name name used for saving file
     * @param target the instance of some class
     * @return  the json of TARGET
     */
    public static String save(String name, Object target){
        String json = new Gson().toJson(target);
        try{
            BufferedWriter r = new BufferedWriter(new FileWriter(new File(name)));
            r.write(json);
            r.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return json;
    }

    /**
     * Load json file with name NAME + ".json" and log the error if it gets thrown and if logged == true
     * @param name name used for loading file
     * @param logged the log state
     * @return the json of the file
     */
    public static String loadFile(String name, boolean logged){
        String json = null;
        try{
            json = new String(Files.readAllBytes(Paths.get(name)));
        }catch(IOException e){
            if(logged) e.printStackTrace();
            json = "";
        }
        return json;
    }

    /**
     * Load json file with name NAME + ".json" and log the error if it gets thrown
     * @param name name used for loading file
     * @return the json of the file
     */
    public static String loadFile(String name){
        return loadFile(name, true);
    }

    /**
     * @param name name used for loading file
     * @return the Config instance with name
     */
    public static Config load(String name){
        Config cfg = new Gson().fromJson(loadFile(name), Config.class);
        return cfg == null ? new Config(name, true) : cfg;
    }

    public String getString(String key, String default1){
        String s = getString(key);
        return s == null || s.equals("") ? default1 : s;
    }

    /**
     * @param key The key
     * @return the String for key may be null
     */
    public String getString(String key){
        return strings.get(key);
    }

    /**
     * @param key The key
     * @return the String List for key may be empty list
     */
    public List<String> getStringList(String key){
        List<String> l = lists.get(key);
        return l == null ? new ArrayList<>() : l;
    }

    //unused config methods
//    public List<String> getStringListAndAdd(String key, String... args){
//        List l = getStringList(key);
//        for(String a : args){
//            if(l.contains(a))
//                l.remove(a);
//            l.add(a);
//        }
//        return l;
//    }
//
//    public void save(){
//
//        String json = new Gson().toJson(this);
//        try{
//            BufferedWriter r = new BufferedWriter(new FileWriter(
//                    new File(name)));
//            r.write(json);
//            r.close();
//        }catch(IOException e){
//            e.printStackTrace();
//        }
//    }
//
//    public void setStringList(String key, List<String> l, String... args){
//        if(strings.keySet().contains(key))
//            strings.remove(key);
//        for(String s : args)
//            l.add(s);
//        lists.put(key, l);
//    }
//
//    public void setString(String key, String s){
//        if(lists.keySet().contains(key))
//            lists.remove(key);
//        strings.put(key, s);
//    }
//
//    public void removeString(String key){
//        if(strings.keySet().contains(key))
//            strings.remove(key);
//    }

}

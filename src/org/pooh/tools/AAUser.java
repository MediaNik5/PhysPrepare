package org.pooh.tools;

import com.google.gson.Gson;
import org.telegram.telegrambots.meta.api.objects.User;

public class AAUser extends AAContainer{


    final User user;

    /**
     * @param user The user, for whom this container is made
     */
    public AAUser(User user){
        super(user.getId() + "", ContentType.User);

        this.user = user;
        AAUser us = new Gson().fromJson(Config.loadFile(System.getProperty("user.dir") + "/" + user.getId()+".json", false), AAUser.class);

        for(SequenceType t : SequenceType.values())
            if(us != null && us.containers != null && us.containers.containsKey(t.getName()))
                this.containers.put(t.getName(), us.containers.get(t.getName()));
            else
                loadDefaults(t);
    }

    /**
     * Sets the sequence to default quests(resetting)
     * @param type the type of SequenceType for which to load the defaults
     */
    private void loadDefaults(SequenceType type){
        AAContainer one = new AAContainer(type.getName(), ContentType.Sequence);
        one.containers.put("Теория", new Gson().fromJson(Config.loadFile(System.getProperty("user.dir") + "/" + type.name().toLowerCase() + ".json"), AAContainer.class).setName("Теория"));
        loadQuests(type, one);
        this.containers.put(type.getName(), one);
    }

    /**
     *
     * @param user the AAUser instance
     * @param index the index of needed container, every section should be split via dot,
     *              e.g. for "Optics.Question 1.Standard Question"
     *              it will return the container with name "Standard Question"
     *              of the container with name "Question 1"
     *              of the container with name "Optics"
     *              of the container-user
     * @return the end container with index
     */
    public static AAContainer getContainer(AAUser user, String index){
        String[] s = index.split("\\.");
        AAContainer b = user.containers.get(s[0]);
        for(int i = 1; i < s.length; i++){
            if(b.containers != null && b.containers.containsKey(s[i]))
                b = b.containers.get(s[i]);
        }
        return b;
    }

    /**
     * load from file with name from 't.name() + " 1.json"' to 't.name() + " N.json"' where N - is the last existing file
     * @param type type of quest
     * @param seq sequences quests are being loading to
     */
    public void loadQuests(SequenceType type, AAContainer seq){
        for(int i = 1; ; i++){
            String json;
            if(!(json = Config.loadFile(System.getProperty("user.dir") + "/" + type.name().toLowerCase() + i + ".json", false)).equals(""))
                seq.containers.put(type.getName()+" " + i, new Gson().fromJson(json, AAContainer.class).setName(type.getName() + " " + i));
            else break;
        }
    }

    /**
     * @return the user id
     */
    public int getId(){
        return user.getId();
    }
}

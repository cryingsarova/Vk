import java.util.ArrayList;

public class OpenedFriend implements Friend{

    private ArrayList<Friend> friends;
    private int ID;
    private String name;

    public OpenedFriend(int ID, String name) {
        this.ID = ID;
        this.name = name;
        friends = new ArrayList<Friend>();
    }

    public String getName() {
        return name;
    }

    public void add(Friend friend){
        friends.add(friend);
    }

    public ArrayList<Friend> getFriends(){
        return friends;
    }

    public int getID(){
        return ID;
    }

    public String toString(){
        return "Friend[ID = " + ID + "]";
    }
    public void draw(int level) {
        String result = "";
        String delta = "";
        for(int i =0; i< (level*2);i++){
            delta+='\t';
        }
        result += ("\u001B[34m" +delta+ name + " : ");
        System.out.println(result);
        if(friends.size()>0) {
            for (Friend friend: friends){
                friend.draw(level + 1);
            }
            System.out.println('\n');
        }

    }
}

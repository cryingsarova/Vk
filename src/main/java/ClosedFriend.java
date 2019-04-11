public class ClosedFriend implements Friend {

    private int ID;
    private String name;

    public ClosedFriend(int ID,String name) {
        this.ID = ID;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getID(){
        return ID;
    }

    public String toString(){
        return "Friend[ID = " + ID + "]";
    }

    public void draw(int level) {
        String delta = "";
        for(int i =0; i< (level*2);i++){
            delta+='\t';
        }
        System.out.println("\u001B[31m"+delta+name);
    }
}

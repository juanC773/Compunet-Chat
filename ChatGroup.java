import java.util.ArrayList;


public class ChatGroup {



    private ArrayList<User> chatUsers;


    public ChatGroup() {
        chatUsers=new ArrayList<>();
    }


    public void addUser(User userToAdd){

        chatUsers.add(userToAdd);
    }

    public void deleteUser(User userToDelete){

        chatUsers.remove(userToDelete);

    }


    public ArrayList<User> getChatUsers() {
        return chatUsers;
    }

    public void setChatUsers(ArrayList<User> chatUsers) {
        this.chatUsers = chatUsers;
    }




}

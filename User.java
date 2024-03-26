
import java.net.Socket;

public class User {

   private String username;

   private Socket socketUser;

   public User(String username, Socket socketUser) {

      this.username = username;
      this.socketUser = socketUser;

   }


   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public Socket getSocketUser() {
      return socketUser;
   }

   public void setSocketUser(Socket socketUser) {
      this.socketUser = socketUser;
   }


}
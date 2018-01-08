import java.util.Scanner;

public class Main {
    public static void main(String args[]){
        try {
            System.out.println("Press CTRL+C to quit");

            int port = 6765;
            if (args.length != 0) {
                if (!(Integer.parseInt(args[0]) > 65535)) {
                    port = Integer.parseInt(args[0]);
                } else {
                    System.out.println("Port too high!");
                }
            }

            if(args.length == 0) {
                Scanner portInput = new Scanner(System.in);
                System.out.print("Server port: ");
                port = portInput.nextInt();
            }

            Scanner passIntput = new Scanner(System.in);
            System.out.print("Server password: ");
            String pass = passIntput.nextLine();

            Server server = new Server(port, pass);
            server.start();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}

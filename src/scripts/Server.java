package scripts;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;


public class Server {
    static String fileDirectory = System.getProperty("user.dir");

    public static void main(String args[]){
        fileDirectory += "/destination/";
        receive();

    }
    // public Client(){
    //     fileDirectory += "/destination";
    // }
    // public Client(String dir){
    //     fileDirectory = dir;
    // }

    public static void receive(){
        int byteRead = 0;
        InputStream inputStream;
        DataInputStream dataInputStream;
        OutputStream outputStream;

        Utils ulti = new Utils();
        ulti.checkDirectory(fileDirectory);

        try{
            ServerSocket serverSocket = new ServerSocket();
            SocketAddress socketAddress = new InetSocketAddress("localhost", 1037);
            serverSocket.bind(socketAddress);

            Socket socket = serverSocket.accept();
            while(!socket.isClosed()){
                byte[] byteArray = new byte[2048];
                inputStream = socket.getInputStream();
                dataInputStream = new DataInputStream(inputStream);
                String fileName = dataInputStream.readUTF();
                System.out.println(fileDirectory + "/" + fileName);
                outputStream = new FileOutputStream(fileDirectory + fileName);

                long fileSize = dataInputStream.readLong();
                while(fileSize > 0 && (byteRead = dataInputStream.read(byteArray, 0, byteArray.length < fileSize ? byteArray.length : (int)fileSize)) != -1){
                    outputStream.write(byteArray, 0, byteRead);
                    fileSize -= byteRead;
                }
                
                outputStream.flush();
                
            }

        }catch(IOException e){
            e.printStackTrace();
        }

    }
}
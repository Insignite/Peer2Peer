package scripts;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Queue;
import java.util.LinkedList;

public class Client{
    static String fileDirectory = System.getProperty("user.dir");

    public static void main(String[] args) throws Exception{
        fileDirectory += "/source/";
        connect();
    }
    // public Server(){
    //     fileDirectory += "/source";
    // }
    // public Server(String dir){
    //     fileDirectory = dir;
    // }

    public static void connect(){
        // Var declaration section
        String currentFileName = "";
        FileInputStream fileInputStream;
        InputStream inputStream;
        OutputStream outputStream;
        DataInputStream dataInputStream;
        DataOutputStream dataOutputStream;
        byte[] bufferArray;

        // Check if source directory exist
        Utils ulti = new Utils();
        File sourceDirectory = ulti.checkDirectory(fileDirectory);

        // Get all file name in the queue
        File[] fileList = sourceDirectory.listFiles();
        Queue<String> fileNameList = new LinkedList<>();
        //  Making text file gather all file name in folder
        // makeFileNameText(fileList, sourceDirectory);
        // fileNameList.offer("_name.txt");

        for(File i : fileList){
            if(i.isFile()) fileNameList.offer(i.getName());
        }

        try{
            // Starting socket
            // ServerSocket serverSocket = new ServerSocket();
            // SocketAddress socketAddress = new InetSocketAddress("localhost", 1037);
            // serverSocket.bind(socketAddress);
            Socket socket = new Socket();

            // Await connection to get accept
            System.out.println("Await on connection...");
            //Socket socket = serverSocket.accept();
            socket.connect(new InetSocketAddress("localhost", 1037));
            System.out.println("Connecting " + socket.getInetAddress() + "/" + socket.getPort() + "...");

            
            while(!fileNameList.isEmpty() && socket.isConnected()){
                currentFileName = fileNameList.poll();

                File currentFile = new File(fileDirectory + currentFileName);
                bufferArray = new byte[(int) currentFile.length()];

                fileInputStream = new FileInputStream(currentFile);
                inputStream = new BufferedInputStream(fileInputStream);
                dataInputStream = new DataInputStream(inputStream);
                outputStream = socket.getOutputStream();

                // data read in file data
                dataInputStream.readFully(bufferArray, 0, bufferArray.length);
                // send file name and size to server
                dataOutputStream = new DataOutputStream(outputStream);
                dataOutputStream.writeUTF(currentFile.getName());
                dataOutputStream.writeLong(bufferArray.length);
                dataOutputStream.write(bufferArray, 0, bufferArray.length);
                // send file over to server
                outputStream.write(bufferArray, 0, bufferArray.length);

                // flush out the buffer
                dataOutputStream.flush();
                outputStream.flush();

                // closing streams when current iteration is already at last file
                if(fileNameList.isEmpty()){
                    outputStream.close();
                    dataOutputStream.close();
                    break;
                }
            }
            System.out.println("All files completed transfer...");


        }catch(IOException e){
            e.printStackTrace();
        }

    }

    // Likely not needed due to UTF function
    private void makeFileNameText(File[] fileList, File source){
        File textFile = new File(source, "_name.txt");
        if(!textFile.exists()){
            try{
                textFile.createNewFile();
                System.out.println("_name.txt is created...");
            } catch(IOException e){
                e.printStackTrace();
            }
        }
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(textFile, false));
            for(File i : fileList){
                if(i.isFile() && i.getName().equals("_name.txt")) continue;
                writer.write(i.getName() + "\n");
            }
            writer.close();
            System.out.println("Edited _name.txt...");
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
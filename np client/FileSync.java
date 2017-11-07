/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package filesync;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
//import org.apache.commons.net.ftp.FTPClient;
/**
 *
 * @author DELL
 */
public class FileSync extends Links{

    /**
     * @param args the command line arguments
     */
    public static boolean check_server_files(HashMap server_files){
        HttpURLConnection h;
        h= open_con("http://samuelrobinroy.com/np/read.php");
        BufferedReader r; String line;
        try{
            r = new BufferedReader(new InputStreamReader(h.getInputStream()));
            while ((line = r.readLine()) != null) {
                String file_data[] = line.split(" ");
                server_files.put(file_data[0],Integer.parseInt(file_data[1]));
                //System.out.println("Server File " + file_data[0]+": "+file_data[1]);
            }                                    

        }catch(IOException e){
                return false;
        }
        return true;
    }
    public static boolean check_client_files(HashMap client_files){
        File folder = new File("C:\\Users\\DELL\\Desktop\\np term projet\\FileSync");
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
          if (listOfFiles[i].isFile()) {
             client_files.put(listOfFiles[i].getName(), Math.round(listOfFiles[i].lastModified()/1000));
           // System.out.println("Client File " + listOfFiles[i].getName()+": "+Math.round(listOfFiles[i].lastModified()/1000));
          }
        }
        return true;
    }
    public static synchronized boolean compare_client_server(final HashMap client_files, final HashMap server_files){
        new java.util.Timer().schedule( 
            new java.util.TimerTask() {
                @Override
                public void run() {
                    check_client_files(client_files);
                    Set set = client_files.entrySet();
                    Iterator i = set.iterator();
                    while(i.hasNext()) {
                       Map.Entry me = (Map.Entry)i.next();
                       if(server_files.containsKey(me.getKey())){
                           if( ((Integer)me.getValue()).intValue() > ((Integer)server_files.get(me.getKey())).intValue())
                             this.cancel();
                       }else {
                           this.cancel();
                       }
                    }
                }
            }, 
            5000 
         );
        return false;
    }
    public static boolean file_upload(String files[]){
       String ftpUrl = "ftp://%s:%s@%s/%s;type=i";
        String host = "www.samuelrobinroy.com";
        String user = "samuelrobinroy";
        String pass = "Mfc493445";
        String filebase = "C:\\Users\\DELL\\Desktop\\np term projet\\FileSync\\";
        for(String file: files){
        String filePath = filebase+file;
        String uploadPath = "public_html/np/FileSync/"+file;
        
        ftpUrl = String.format(ftpUrl, user, pass, host, uploadPath);
        System.out.println("Upload URL: " + ftpUrl);

        try {
            URL url = new URL(ftpUrl);
            URLConnection conn = url.openConnection();
            OutputStream outputStream = conn.getOutputStream();
            FileInputStream inputStream = new FileInputStream(filePath);

            byte[] buffer = new byte[4];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();
            System.out.println("File uploaded");
        } catch (IOException ex) {
            System.out.println("File not uploaded");
            ex.printStackTrace();
        }
        }
        return true;
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        HashMap server_files = new HashMap();
        HashMap client_files = new HashMap();
        String files_for_upload="";
            while(true){
             try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            //compare_client_server(client_files,server_files);
            files_for_upload="";
            check_client_files(client_files);
            check_server_files(server_files);
            Set set = client_files.entrySet();
                    Iterator i = set.iterator();
                    //System.out.println(files_for_upload);
                    while(i.hasNext()) {
                       Map.Entry me = (Map.Entry)i.next();
                       if(server_files.containsKey(me.getKey())){
                           if(((Integer)me.getValue()).intValue() > ((Integer)server_files.get(me.getKey())).intValue())
                             files_for_upload+=me.getKey()+" ";
                       }else {
                           System.out.println("hello");
                           files_for_upload+=me.getKey()+" ";
                       }
                    }
                    
                if(files_for_upload.trim()!=""){
                    System.out.println(files_for_upload);
                    String s[] = files_for_upload.split(" ");
                    file_upload(s);
                    System.out.println("Synced");
                }
            
            
        }
    }
    
}

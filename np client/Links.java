/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package filesync;

/**
 *
 * @author DELL
 */
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import javax.imageio.ImageIO;


abstract public class Links {
	
	static public String b_url = "C:\\PROGRAMS\\JAVA\\pickLinks\\";
	static HttpURLConnection open_con(String url){
		HttpURLConnection con;
		 try{
			con = (HttpURLConnection)new URL(url).openConnection();
			con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
		 }catch(IOException e){
			 return null;
		 }
		return con;
	}
	
	static FileReader read_file(String filename){
		FileReader f;
		try{
			f= new FileReader(b_url+filename);
		}catch(IOException e){
			return null;
		}
		return f;
	}
	
	static FileWriter write_file(String filename,boolean k){
		FileWriter f;
		try{
			f= new FileWriter(b_url+filename, k);
		}catch(IOException e){
			return null;
		}
		return f;
	}
	
	static FileReader url_xml(String url){
		HttpURLConnection h = open_con(url);
		try{
			FileWriter f = write_file("temp.xml",false);
			BufferedReader r  = new BufferedReader(new InputStreamReader(h.getInputStream()));
	    	StringBuilder sb = new StringBuilder();
	    	String line;
	    	while ((line = r.readLine()) != null) {
	    	    sb.append(line+"\n");
	    	}
	    	f.write(sb.toString());
	    	f.close();
	    	return read_file("temp.xml");
		}catch(IOException e){
			return null;
		}
		
	}
	
	static String save_img(String im_url,int ht){
		BufferedImage img = null;
		String name="";
		try {
			HttpURLConnection li= open_con(im_url);
		    img = ImageIO.read(li.getInputStream());
		    //img = ImageIO.read(new File("C:\\PROGRAMS\\JAVA\\pickLinks\\test.jpg"));
		    if(img!=null){
		    float h = img.getHeight();
		    float w =img.getWidth();
		    float r = h/w;
		    Image i=img.getScaledInstance((int)(ht/r), ht, Image.SCALE_SMOOTH);
		    BufferedImage buffered = new BufferedImage((int)(245/r), 245,BufferedImage.TYPE_INT_RGB);
		    buffered.getGraphics().drawImage(i, 0, 0 , null);
		    name = im_url.substring(im_url.lastIndexOf('/')+1);
		    ImageIO.write(buffered , "jpg", new File(b_url+"images\\"+name));
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
		return name;
		
	}
}

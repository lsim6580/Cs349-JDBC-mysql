import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class PhotoContainer implements Serializable{
    SQLEvent event;
    List<Photo> photoList;
    PhotoContainer() throws Exception{
        event = new SQLEvent();
    }


    public void delete(Photo photo){
        event.deletePhoto(photo);
    }

    public void add(Photo photo, int order){
        try {
            event.addPhoto(photo, order);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public void savePhoto(Photo photo){
        event.updatePhoto(photo);
    }
    public void delete(int x){

    }

    public int length(){
        return event.getMaxOrder();
    }

    public boolean isEmpty(){
        return event.getNumberOfPhotos() == 0;
    }


    public Photo next(int current){
       return  event.getNextPhoto(current);
    }

    public Photo prev(int current){
        return  event.getPrevPhoto(current);
    }

    public byte[] convertToByte(File file) throws IOException{

        Path path = Paths.get(file.getAbsolutePath());
        byte[] data;

            data = Files.readAllBytes(path);
            // write image to database



        return data;

    }
}

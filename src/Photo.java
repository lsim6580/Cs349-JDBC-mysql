import javax.swing.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

    public class Photo implements Serializable {
        private byte[] source;
        private String description;
        private String date;
        private int order;
        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        Photo(){

            this.date = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            this.description = "";

        }


        public byte[] getSource() {
            return source;
        }

        public void setSource(byte[] source) {
            this.source = source;
        }

//        public void setSource(byte[] source){
//            this.source = new ImageIcon(source);
//        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }



import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.*;

/**
 * Created by luke on 10/15/2016.
 */
public class SQLEvent {
    private Connection con;
    private Statement stmt;
    public SQLEvent() throws Exception{
        // List<Account> accountList = new ArrayList<Account>();
        String url = "jdbc:mysql://kc-sce-appdb01.kc.umkc.edu/lsg72";
        String userID = "lsg72";
        String password = "bMNLwflRlmmHhi58CaVD";
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch(java.lang.ClassNotFoundException e) {
            System.out.println(e);
            System.exit(0);
        }

        con = DriverManager.getConnection(url,userID,password);
        stmt = con.createStatement();
    }
    public Photo getNextPhoto(int currentOrder){
        String query;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Photo photo = new Photo();
        if(currentOrder == getMaxOrder()) {
            query = "Select * FROM cs349as8 Where pic_order = 1";


        }
        else{
            query  ="Select * From cs349as8 WHERE pic_order = "+(currentOrder + 1);
        }
        ResultSet result = queryPhoto(query);
        try {
            while (result.next()) {
                photo.setDescription(result.getString("description"));
                photo.setOrder(result.getInt("pic_order"));
                photo.setId(result.getInt("id"));
                photo.setDate(result.getDate("upload_date").toString());
                bos = new ByteArrayOutputStream();

                InputStream in = result.getBinaryStream("image");
                int c;
                while ((c = in.read()) != -1) {
                    bos.write(c);
                }
                photo.setSource(bos.toByteArray());

            }
        }
            catch(Exception e){
                e.printStackTrace();
            }

        return photo;
    }
    public Photo getPrevPhoto(int currentOrder){
        String query;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Photo photo = new Photo();
        if(currentOrder == 1) {
            query = "Select * FROM cs349as8 Where pic_order = " + getMaxOrder();


        }
        else{
            query  ="Select * From cs349as8 WHERE pic_order = "+(currentOrder - 1);
        }
        ResultSet result = queryPhoto(query);
        try {
            while (result.next()) {
                photo.setDescription(result.getString("description"));
                photo.setOrder(result.getInt("pic_order"));
                photo.setId(result.getInt("id"));
                photo.setDate(result.getDate("upload_date").toString());
                bos = new ByteArrayOutputStream();
                InputStream in = result.getBinaryStream("image");
                int c;
                while ((c = in.read()) != -1) {
                    bos.write(c);
                }
                photo.setSource(bos.toByteArray());

            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return photo;
    }
    public void deletePhoto(Photo photo){
        if(getNumberOfPhotos() != 0) {
            int order = photo.getOrder();
            String update = "DELETE FROM cs349as8 WHERE id = " + photo.getId();
            update(update);
            updateOrder(order, false);
        }

    }

    public int getNumberOfPhotos(){
        String query = "SELECT COUNT(*) AS COUNT FROM cs349as8";
        int value = 0;
        try {
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next())
            value =  rs.getInt(1);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return value;

    }
    public Photo addPhoto(Photo photo, int order) throws SQLDataException{
        if(order == 0){
            order = 1;
        }
        updateOrder(order, true);

            String query = "INSERT INTO cs349as8(image, pic_order) VALUES (?, " + order + ")";
            String getID = "SELECT LAST_INSERT_ID()";
            PreparedStatement pstmt = null;
            try {
                pstmt = con.prepareStatement(query);
                ByteArrayInputStream bis = new ByteArrayInputStream(photo.getSource());

                pstmt.setBinaryStream(1, bis, (int) photo.getSource().length);
                pstmt.executeUpdate(); // execute prepared statement
                pstmt.close();
//                stmt.executeQuery(query);
                photo.setOrder(order);
                ResultSet rs = stmt.executeQuery(getID);
                boolean canGetID = rs.next();
                if (canGetID) {
                    photo.setId(rs.getInt(1));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        return photo;
        }




    public void updatePhoto(Photo photo){
        String update = "UPDATE cs349as8 SET description = '" + photo.getDescription() + "' , upload_date = '"+ photo.getDate()+ "'" +
                " Where id = " + photo.getId();
        System.out.println(update);
        update(update);

    }

    public int getMaxOrder(){
        int max = 0;
        String query = "SELECT pic_order From cs349as8 order by pic_order desc limit 1";
        try{
              ResultSet resultSet = stmt.executeQuery(query);
            while (resultSet.next())
             max = resultSet.getInt(1);
        }
        catch (SQLException e){
            e.printStackTrace();

        }
        return max;
    }
    private void updateOrder(int order, boolean add) {
        if (getNumberOfPhotos() != 0) {
            if (add) {

                String update = "UPDATE cs349as8 Set pic_order = pic_order + 1 WHERE pic_order >= " + order;
                int result = update(update);
            } else {
                String update = "UPDATE cs349as8 Set pic_order = pic_order - 1 WHERE pic_order > " + order;
                int result = update(update);
            }
        }

    }

    private ResultSet queryPhoto(String query){
        ResultSet resultSet = null;
        try{
             resultSet = stmt.executeQuery(query);


        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }
    public void cleanup() throws SQLException {
        // Close connection and statement
        // Connections, statements, and result sets are
        // closed automatically when garbage collected
        // but it is a good idea to free them as soon
        // as possible.
        // Closing a statement closes its current result set.
        // Operations that cause a new result set to be
        // created for a statement automatically close
        // the old result set.
        stmt.close();
        con.close();
    }
    private int update(String update){
        int result = 0;
        try {
                result = stmt.executeUpdate(update);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}

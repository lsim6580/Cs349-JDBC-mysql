import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;

import javax.swing.*;

public class PhotoViewerLayout extends JFrame {
    JLabel imageLabel;
    JTextArea descriptionTextArea;
    JTextField dateTextField;
    JButton nextButton;
    JButton prevButton;
    PhotoContainer photoContainer;
    JTextField pictureNumberTextField;
    JLabel pictureCountLabel;
    JScrollPane scrollPane;
    volatile Photo photo;
    boolean editing = false;
    public PhotoViewerLayout() throws Exception{
        photoContainer = new PhotoContainer();
        imageLabel = new JLabel("", SwingConstants.CENTER);
        scrollPane = new JScrollPane(imageLabel);
        photo = photoContainer.next(0);
        System.out.println(photo.getDescription());
        System.out.println(photo.getDate());
        descriptionTextArea = new JTextArea(4,20);
        dateTextField = new JTextField();
        pictureNumberTextField = new JTextField(0);
        pictureCountLabel = new JLabel();
        if(!photoContainer.isEmpty()){
            ImageIcon image = new ImageIcon(photo.getSource());
            imageLabel.setIcon(image);
            updateUI();

        }
        else{
            imageLabel.setIcon(new ImageIcon());
        }




//        try{
//            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("photoClass.txt"));
//            photoContainer = (PhotoContainer) ois.readObject();
//            ois.close();
//
//        }
//        catch (Exception e){
//            System.out.println("no photoContainer type Found");
//            Photo photo = new Photo();

//            photo.setSource("src/Baby.jpg");
//            photo.setDescription("BABY");
//            photoContainer.add(photo);

//
//
//        }


//        Photo photo = new Photo();
//        photo.setSource("src/Baby.jpg");
//        photoContainer.add(photo);




        JLabel descriptionLabel = new JLabel("Description:");
        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setPreferredSize(new Dimension(descriptionLabel.getPreferredSize().width,dateLabel.getPreferredSize().height));


      //  if(photoContainer.length() == 0){

  //      }
    //    else{
//            pictureNumberTextField = new JTextField(Integer.toString(photoContainer.getIndex()+1));
//            pictureCountLabel = new JLabel(" of "+ photoContainer.length());
//            //imageLabel.setIcon(photoContainer.getCurrent().getSource());
//            descriptionTextArea.setText(photoContainer.getCurrent().getDescription());
//            dateTextField = new JTextField(photoContainer.getCurrent().getDate());




        Container contentPane = getContentPane();





        contentPane.add(scrollPane, BorderLayout.CENTER);

        JPanel controlPane = new JPanel();
        controlPane.setLayout(new BoxLayout(controlPane, BoxLayout.PAGE_AXIS));

        JPanel descriptionPane = new JPanel();
        descriptionPane.setLayout(new FlowLayout(FlowLayout.LEFT));


        descriptionPane.add(descriptionLabel);
        descriptionPane.add(descriptionTextArea);

        JPanel datePane = new JPanel();
//		datePane.setLayout(new FlowLayout(FlowLayout.LEFT));
//		datePane.setLayout(new BoxLayout(datePane, BoxLayout.LINE_AXIS));


        datePane.add(dateLabel);
        datePane.add(dateTextField);
        //datePane.add(Box.createHorizontalGlue());
        JPanel buttonPane = new JPanel();
        JButton addButton = new JButton("Add Photo");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(contentPane);
                int order = photo.getOrder();
                savePhoto();
                photo = new Photo();
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        photo.setSource(photoContainer.convertToByte(selectedFile));
                        photoContainer.add(photo, order);
                        updateUI();
                    }
                    catch (Exception e1){
                        e1.printStackTrace();
                    }

                    // System.out.println(photoContainer.getPhoto(1).getSource());


                }
            }
        });
        JButton deleteButton  = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 final Runnable codeToRunOnUIThread = new Runnable() {
                @Override
                public void run() {
                    updateUI();
                }
            };
            Runnable SqlThread = new Runnable() {
                @Override
                public void run() {

                    photoContainer.delete(photo);
                    photo = photoContainer.prev(photo.getOrder());
                    SwingUtilities.invokeLater(codeToRunOnUIThread);
                }
            };
            Thread seperateThread = new Thread(SqlThread);
            seperateThread.start();
            }
        });
        buttonPane.add(deleteButton);
        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                photoContainer.savePhoto(photo);
            }
        });
        buttonPane.add(saveButton);
        buttonPane.add(addButton);

        JPanel leftRightPane = new JPanel();
        leftRightPane.setLayout(new BorderLayout());
        leftRightPane.add(datePane,BorderLayout.WEST);
        leftRightPane.add(buttonPane,BorderLayout.EAST);


        JPanel southButtonPanel = new JPanel();
        prevButton = new JButton("<prev");
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final Runnable codeToRunOnUIThread = new Runnable() {
                @Override
                public void run() {
                    updateUI();
                }
            };
            Runnable SqlThread = new Runnable() {
                @Override
                public void run() {
                    savePhoto();
                    photo = photoContainer.prev(photo.getOrder());
                    SwingUtilities.invokeLater(codeToRunOnUIThread);
                }
            };
            Thread seperateThread = new Thread(SqlThread);
            seperateThread.start();


        }
        });
        nextButton = new JButton("next>");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

 //               savePhoto(photoContainer);
                final Runnable codeToRunOnUIThread = new Runnable() {
                    @Override
                    public void run() {
                        updateUI();
                    }
                };
                Runnable SqlThread = new Runnable() {
                    @Override
                    public void run() {
                        savePhoto();
                        photo = photoContainer.next(photo.getOrder());
                        SwingUtilities.invokeLater(codeToRunOnUIThread);
                    }
                };
                Thread seperateThread = new Thread(SqlThread);
                seperateThread.start();

            }
        });

        southButtonPanel.add(pictureNumberTextField);
        southButtonPanel.add(pictureCountLabel);
        southButtonPanel.add(prevButton);
        southButtonPanel.add(nextButton);
        FlowLayout flowLayout = (FlowLayout) southButtonPanel.getLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);


        controlPane.add(descriptionPane);
        controlPane.add(leftRightPane);
        controlPane.add(southButtonPanel);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // Create the first menu.
        JMenu menu = new JMenu("File");
        JMenu view = new JMenu("View");
        menu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(menu);
        menuBar.add(view);
        JMenuItem browse =new JMenuItem("Browse");
        browse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editing = false;
                addButton.setVisible(false);
                saveButton.setVisible(false);
                deleteButton.setVisible(false);
                dateTextField.setEnabled(false);
                dateTextField.setEnabled(false);
            }
        });

        JMenuItem maintain = new JMenuItem("Maintain");
        maintain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addButton.setVisible(true);
                saveButton.setVisible(true);
                deleteButton.setVisible(true);
                dateTextField.setEnabled(true);
                descriptionTextArea.setEnabled(true);
            }
        });

        // Create an item for the first menu
        JMenuItem exitMenuItem = new JMenuItem("Exit",KeyEvent.VK_X);
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        //menuItem.setMnemonic(KeyEvent.VK_X); // set in constructor
        menu.add(exitMenuItem);
        view.add(browse);
        view.add(maintain);

        contentPane.add(controlPane, BorderLayout.SOUTH); // Or PAGE_END
    }

    public void savePhoto(){
        photo.setDate(dateTextField.getText());
        String des = descriptionTextArea.getText();
        if(!des.isEmpty()) {
            photo.setDescription(des);
        }
        else{
            photo.setDescription("");
        }
        photoContainer.savePhoto(photo);
    }

    public void updateUI(){
        dateTextField.setText(photo.getDate());
        if(photo.getDescription() == null){
            descriptionTextArea.setText("");
        }
        else {
            descriptionTextArea.setText(photo.getDescription());
        }
        pictureNumberTextField.setText(Integer.toString(photo.getOrder()));
        pictureCountLabel.setText("of "+ photoContainer.length());
        imageLabel.setIcon(new ImageIcon(photo.getSource()));
        scrollPane.setViewportView(imageLabel);


//
    }

    public static void main(String[] args) throws Exception {
        JFrame frame = new PhotoViewerLayout();
        frame.pack();
        frame.setVisible(true);
    }
}

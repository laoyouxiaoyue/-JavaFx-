package com.example.fxtest;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.sql.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

import static com.example.fxtest.AuthCodeUtils.getRandom;

public class MainController {

    int flag=3;
    @FXML
    private TextField mm;

    @FXML
    private Button register;

    @FXML
    private Button submit;

    @FXML
    private TextField zh;

    Stage RegisterStage=null;
    Connection conn=null;
    Statement stat=null;
    Stage stage=null;
    public MainController(Connection conn,Statement stat,Stage stage) {
        this.conn=conn;
        this.stat=stat;
        this.stage=stage;
    }

    @FXML
    void OnRegister(ActionEvent event) throws IOException {
        RegisterStage.getIcons().add(new Image("file:src/main/resources/SuCai/注册.png"));
        RegisterStage.show();
    }
    String yzmcode;
    @FXML
    public void initialize() throws IOException {
        yzm.setOnMouseClicked(mouseEvent -> {
            for (int i = 1; i < 2; i++) {
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(i + ".png");
                    yzmcode=getRandom(4);
                    AuthCodeUtils.draw(out, yzmcode);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                javafx.scene.image.Image image = new Image("file:1.png");
                yzm.setImage(image);
            }
        });
        for (int i = 1; i < 2; i++) {
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(i + ".png");
                yzmcode=getRandom(4);
                AuthCodeUtils.draw(out, yzmcode);
            } catch (IOException e) {
                e.printStackTrace();
            }
            javafx.scene.image.Image image = new Image("file:1.png");
            yzm.setImage(image);
        }

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("RegisterFrame.fxml"));
        fxmlLoader.setControllerFactory(t->new RegisterController(conn,stat));
        Parent RegisterRoot=fxmlLoader.load();
        //RegisterController registerController=fxmlLoader.getController();
        Scene scene = new Scene(RegisterRoot);
        RegisterStage=new Stage();
        RegisterStage.setTitle("注册页面");
        RegisterStage.setScene(scene);
    }

    @FXML
    ImageView yzm;
    @FXML
    TextField yzms;


    @FXML
    void shuaxin(MouseEvent event) {

    }

    @FXML
    void OnSubmit(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
        if(!yzms.getText().toLowerCase().equals(yzmcode.toLowerCase()))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "验证码错误，请重试");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("file:src/main/resources/SuCai/失败.png"));
            alert.show();
            return;
        }
        /*InetAddress address = InetAddress.getLocalHost();
        System.out.println("Host Name: " + address.getHostName());
        System.out.println("Host Address: " + address.getHostAddress());*/
                String user=zh.getText();
                String mima=mm.getText();
                String Rightmima;
                ResultSet rs=null;
        System.out.println("hello");
                String sql="select 密码 from users where 用户名='"+user+"';";
                rs=stat.executeQuery(sql);
               if(!rs.next())
               {
                   Alert alert = new Alert(Alert.AlertType.ERROR, "用户名不存在，请重试");
                   Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                   stage.getIcons().add(new Image("file:src/main/resources/SuCai/失败.png"));
                   alert.show();
                   return;
               }

                    Rightmima=rs.getString("密码");
                    if(Rightmima.equals(mima)) {
                        stage.close();
                        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Student-view.fxml"));
                        fxmlLoader.setControllerFactory(t->new StudentController(conn,stat,user));
                        Parent StudentRoot=fxmlLoader.load();
                        //RegisterController registerController=fxmlLoader.getController();
                        Scene scene = new Scene(StudentRoot );
                        Stage StudentSystem=new Stage();
                        StudentSystem=new Stage();
                        StudentSystem.setTitle("学生管理系统--用户:"+user);
                        StudentSystem.getIcons().add(new Image("file:src/main/resources/SuCai/icon.png"));
                        StudentSystem.setScene(scene);
                        StudentSystem.show();



                    }
                    else {
                        flag--;
                        if(flag==0)System.exit(0) ;
                        Alert alert=new Alert(Alert.AlertType.ERROR,"密码错误或用户名不存在,你还有"+flag+"次机会系统将自动关闭");
                        alert.show();
                    }

    }

}

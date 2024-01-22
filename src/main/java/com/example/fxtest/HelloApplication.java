package com.example.fxtest;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.*;

public class HelloApplication extends Application {

    ResultSet rs=null;
    Connection conn=null;

    Statement stat=null;
    @Override
    public void start(Stage stage) throws IOException, SQLException, ClassNotFoundException {
        init_jdbc();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        fxmlLoader.setControllerFactory(t->new MainController(conn,stat,stage));
        Parent mainRoot=fxmlLoader.load();
        MainController mainController=fxmlLoader.getController();
        Scene scene = new Scene(mainRoot);
        stage.getIcons().add(new Image("file:src/main/resources/SuCai/icon.png"));
        stage.setTitle("管理系统");
        stage.setScene(scene);
        stage.show();
    }
    void initRegisterFrame() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("RegisterFrame.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage RegisterStage=new Stage();
       RegisterStage.getIcons().add(new Image("file:src/main/resources/SuCai/icon.png"));
        RegisterStage.setTitle("注册页面");
        RegisterStage.setScene(scene);
    }
    public static void main(String[] args) {
        //Player s=new Player();
        launch();

    }
    void init_jdbc() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url="jdbc:mysql://localhost:3306/work?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull";
        String username="root";
        String password="123456";
        conn= DriverManager.getConnection(url,username,password);
        stat= conn.createStatement();
    }
}
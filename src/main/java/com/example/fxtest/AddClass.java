package com.example.fxtest;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.sql.*;

public class AddClass {

    @FXML
    private TextField Sname;

    @FXML
    private TextField Sno;

    @FXML
    private Button Submit;
    @FXML
    private TextField Ssno;

    public void initialize() {
        Image image8 = new Image("file:src/main/resources/SuCai/提交.png");
        ImageView imageView8 = new ImageView(image8);
        imageView8.setPreserveRatio(true);
        imageView8.setFitHeight(40);
        HBox hBox8 = new HBox(imageView8);
        Submit.setGraphic(hBox8);
    }
    @FXML
    void OnSubmit(ActionEvent event) throws SQLException {
        String sno=Sno.getText();
        String sname=Sname.getText();
        String ssno=Ssno.getText();
        String sql = "insert into class (编号,全称,班长学号) values (?,?,?)";
        PreparedStatement preStmt = conn.prepareStatement(sql);
        String check="select * from class where 编号='"+sno+"'";
        //System.out.println(ssno);
        ResultSet rs= stat.executeQuery(check);
        if(rs.next())
        {
            Alert alert=new Alert(Alert.AlertType.ERROR,"班级号已存在,请重试");
            alert.show();
            return;
        }
        check="select * from class where 全称='"+sname+"'";
        System.out.println(check);
        rs= stat.executeQuery(check);
        if(rs.next())
        {
            Alert alert=new Alert(Alert.AlertType.ERROR,"班级已存在,请重试");
            alert.show();
            return;
        }
        check="select * from students where 学号='"+ssno+"'";
        rs= stat.executeQuery(check);
        /*if(!rs.next())
        {
            Alert alert=new Alert(Alert.AlertType.ERROR,"班长学号不存在,请重试");
            alert.show();
            return;
        }*/
        preStmt.setString(1, sno);
        preStmt.setString(2, sname);
        preStmt.setString(3, ssno);
        int d = preStmt.executeUpdate();
        if (d == 1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "注册成功");
            alert.show();
        }
    }
    Connection conn;
    Statement stat;
    public AddClass(Connection conn, Statement stat) {
        this.conn = conn;
        this.stat = stat;
    }
}

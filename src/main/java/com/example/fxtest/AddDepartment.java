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

public class AddDepartment {

    @FXML
    private TextField Sname;

    @FXML
    private TextField Sno;

    @FXML
    private Button Submit;

    @FXML
    void OnSubmit(ActionEvent event) throws SQLException {
            String sno=Sno.getText();
            String sname=Sname.getText();
        String sql = "insert into department (编号,全称) values (?,?)";
        PreparedStatement preStmt = conn.prepareStatement(sql);
        String check="select * from department where 编号='"+sno+"'";
        //System.out.println(ssno);
        ResultSet rs= stat.executeQuery(check);
        if(rs.next())
        {
            Alert alert=new Alert(Alert.AlertType.ERROR,"编号已存在,请重试");
            alert.show();
            return;
        }
        check="select * from department where 全称='"+sname+"'";
        rs= stat.executeQuery(check);
        if(rs.next())
        {
            Alert alert=new Alert(Alert.AlertType.ERROR,"学院已存在,请重试");
            alert.show();
            return;
        }
        preStmt.setString(1, sno);
        preStmt.setString(2, sname);
        int d = preStmt.executeUpdate();
        if (d == 1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "注册成功");
            alert.show();
        }
    }

    public  void initialize() {
        Image image8 = new Image("file:src/main/resources/SuCai/提交.png");
        ImageView imageView8 = new ImageView(image8);
        imageView8.setPreserveRatio(true);
        imageView8.setFitHeight(40);
        HBox hBox8 = new HBox(imageView8);
        Submit.setGraphic(hBox8);
    }
    Connection conn;
    Statement stat;
    public AddDepartment(Connection conn, Statement stat) {
        this.conn = conn;
        this.stat = stat;
    }
}

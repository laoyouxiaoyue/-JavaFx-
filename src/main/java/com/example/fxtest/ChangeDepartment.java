package com.example.fxtest;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class ChangeDepartment {

    @FXML
    private TextField Sname;

    @FXML
    private TextField Sno;

    @FXML
    private Button Submit;
    String beforesno;
    String beforesname;
    ObservableList observableList;
    @FXML
    void OnSubmit(ActionEvent event) throws SQLException {
        String sno=Sno.getText();
        String sname=Sname.getText();
        String sql = "update department set 编号='"+sno+"',全称='"+sname+"' where 编号='"+beforesno+"'";
        PreparedStatement preStmt = conn.prepareStatement(sql);
        String check="select * from department where 编号='"+sno+"'";
        //System.out.println(ssno);
        ResultSet rs= stat.executeQuery(check);
        if(rs.next()&&!sno.equals(beforesno))
        {
            Alert alert=new Alert(Alert.AlertType.ERROR,"编号已存在,请重试");
            alert.show();
            return;
        }
        check="select * from department where 全称='"+sname+"'";
        rs= stat.executeQuery(check);
        if(rs.next()&&!sname.equals(beforesname))
        {
            Alert alert=new Alert(Alert.AlertType.ERROR,"学院已存在,请重试");
            alert.show();
            return;
        }
        stat.execute(sql);
        Alert alert=new Alert(Alert.AlertType.INFORMATION,"更改成功");
        alert.show();
    }

    public void initialize()
    {
        Image image8 = new Image("file:src/main/resources/SuCai/提交.png");
        ImageView imageView8 = new ImageView(image8);
        imageView8.setPreserveRatio(true);
        imageView8.setFitHeight(40);
        HBox hBox8 = new HBox(imageView8);
        Submit.setGraphic(hBox8);
        beforesno=observableList.get(0).toString();
        beforesname=observableList.get(1).toString();
        Sno.setText(observableList.get(0).toString());
        Sname.setText(observableList.get(1).toString());
    }
    Connection conn;
    Statement stat;
    public ChangeDepartment(Connection conn, Statement stat,ObservableList observableList) {
        this.conn = conn;
        this.stat = stat;
        this.observableList=observableList;
    }
}

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
import org.w3c.dom.Text;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class ChangeClass {

    @FXML
    private TextField Sname;

    @FXML
    private TextField Sno;

    @FXML
    private TextField Ssno;
    @FXML
    private Button Submit;
    String beforesno;
    String beforesname;
    String beforessno;
    ObservableList observableList;
    @FXML
    void OnSubmit(ActionEvent event) throws SQLException {
        String sno=Sno.getText();
        String sname=Sname.getText();
        String  ssno=Ssno.getText();
        String sql = "update class set 编号='"+sno+"',全称='"+sname+"',班长学号='"+ssno+"' where 编号='"+beforesno+"'";
        PreparedStatement preStmt = conn.prepareStatement(sql);
        String check="select * from class where 编号='"+sno+"'";
        //System.out.println(ssno);
        ResultSet rs= stat.executeQuery(check);
        if(rs.next()&&!sno.equals(beforesno))
        {
            Alert alert=new Alert(Alert.AlertType.ERROR,"班级编号已存在,请重试");
            alert.show();
            return;
        }
        check="select * from class where 全称='"+beforesname+"'";
        rs= stat.executeQuery(check);
        if(rs.next()&&!sname.equals(beforesname))
        {
            Alert alert=new Alert(Alert.AlertType.ERROR,"班长名称已存在,请重试");
            alert.show();
            return;
        }
        check="select * from class where 班长学号='"+beforessno+"'";
        rs= stat.executeQuery(check);
        if(rs.next()&&!ssno.equals(beforessno))
        {
            Alert alert=new Alert(Alert.AlertType.ERROR,"班长已在别班存在,请重试");
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
        beforessno=observableList.get(2).toString();
        Sno.setText(observableList.get(0).toString());
        Sname.setText(observableList.get(1).toString());
        Ssno.setText(observableList.get(2).toString());
    }
    Connection conn;
    Statement stat;
    public ChangeClass(Connection conn, Statement stat,ObservableList observableList) {
        this.conn = conn;
        this.stat = stat;
        this.observableList=observableList;
    }
}

package com.example.fxtest;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class ChangeInformation {
    @FXML
    private Button Submit;

    @FXML
    private DatePicker birth;

    @FXML
    private TextField email;

    @FXML
    private PasswordField miam2;

    @FXML
    private PasswordField mima;

    @FXML
    private PasswordField mima1;

    @FXML
    private TextField name;

    @FXML
    private TextField phone;

    String beforename;
    String beforemima;

    @FXML
    TextField KaMi;
    @FXML
    void OnSubmit(ActionEvent event) throws SQLException {
        if(flag==0)
        {
            Alert alert=new Alert(Alert.AlertType.ERROR,"格式错误,请重新输入");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("file:src/main/resources/SuCai/失败.png"));
            alert.show();
            return;
        }
        String newname=name.getText();
        String newbirth=birth.getValue().toString();
        String newphone=phone.getText();
        String newemail=email.getText();
        String smima1=mima.getText();
        String smima2=mima1.getText();
        String smima3=miam2.getText();
        if(!smima2.equals(smima3))
        {
            Alert alter=new Alert(Alert.AlertType.WARNING);
            alter.setContentText("两次密码不相同,请重试");
            alter.show();
            return;
        }
        if(!beforemima.equals(smima1))
        {
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setContentText("原始密码错误,请重试");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("file:src/main/resources/SuCai/失败.png"));
            alert.show();
            return;
        }
        ResultSet rs;
        String sql="select * from users where 用户名='"+newname+"'";
        if(!newname.equals(beforename))
        {
            rs=stat.executeQuery(sql);
            if(rs.next())
            {
                Alert alert=new Alert(Alert.AlertType.WARNING);
                alert.setContentText("用户名已存在,请重试");
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("file:src/main/resources/SuCai/失败.png"));
                alert.show();

                return;
            }
        }
        if(!KaMi.getText().equals("")) {

            sql="select 有效,级别 from kami where ID='"+KaMi.getText()+"'";
            rs = stat.executeQuery(sql);
            if (rs.next()) {
                if (rs.getInt(1) == 1) {
                   QX_level=rs.getInt(2);
                    String sql2 = "update kami set 用户名='" + newname + "',时间='" + birth.getValue().toString() + "',有效=0 where ID='" + KaMi.getText()+"'";
                    stat.execute(sql2);
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "卡密失效");
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("file:src/main/resources/SuCai/失败.png"));
                    alert.show();
                    return;
                }
            }
            else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "卡密不存在");
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("file:src/main/resources/SuCai/失败.png"));
                alert.show();
                return;
            }
        }
        sql="update users set 用户名='"+newname+"',密码='"+smima2+"',出生日期='"+birth.getValue().toString()+"',手机号码='"+phone.getText()+"',电子邮箱='"+email.getText()+"',权限="+QX_level+" where 用户名='"+beforename+"'";
           // rs=stat.execute(sql);

        System.out.println(stat.execute(sql));
                Alert alert=new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("注册成功");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("file:src/main/resources/SuCai/成功.png"));
        alert.show();


    }
    @FXML
    Text information1;
    @FXML
    Text information2;
    @FXML
    Text information3;
    @FXML
    Text information4;
    @FXML
    Text information5;
    @FXML
    Text information6;
    int QX_level;
    int flag=1;
    public void initialize() throws SQLException {
        name.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                //System.out.println(t1);
                if(t1.length()>15||t1.length()<3)
                {
                    information1.setText("用户名由3-15个字符组成");
                    flag=0;
                }
                else {
                    information1.setText("");
                    flag=1;
                }
            }
        });
        mima1.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                // System.out.println(t1);
                if(t1.length()>15||t1.length()<3)
                {
                    information2.setText("密码由3-15个字符组成");
                    flag=0;
                }
                else {
                    information2.setText("");
                    flag=1;
                }
            }
        });
        miam2.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                // System.out.println(t1);
                if(!mima1.getText().equals(miam2.getText()))
                {
                    information3.setText("与上方密码不相同");
                    flag=0;
                }
                else {
                    information3.setText("");
                    flag=1;
                }
            }
        });

        birth.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observableValue, LocalDate localDate, LocalDate t1) {
                if(!t1.isBefore(LocalDate.now()))
                {
                    information4.setText("时间不合法,超出现在");
                    flag=0;
                }
                else {
                    information4.setText("");
                    flag=1;
                }
            }
        });
        phone.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                String pattern = "[0-9-()（）]{7,18}";
                if (!Pattern.compile(pattern).matcher(phone.getText()).matches()) {
                    information5.setText("手机号码格式输入错误");
                    flag=0;
                }
                else {
                    information5.setText("");
                    flag = 1;
                }
            }
        });
        email.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                String pattern = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
                if (!Pattern.compile(pattern).matcher(email.getText()).matches()) {
                    information6.setText("邮箱格式输入错误");
                    flag=0;
                }
                else
                {
                    information6.setText("");
                    flag=1;
                }
            }
        });
        String sql="select * from users where 用户名='"+user+"'";
        ResultSet rs=stat.executeQuery(sql);
        rs.next();
        beforename=rs.getString(1);
        name.setText(rs.getString(1));
        birth.setValue(rs.getDate(3).toLocalDate());
        phone.setText(rs.getString(4));
        email.setText(rs.getString(5));
        QX_level=rs.getInt(6);
        beforemima=rs.getString(2);
        Image image8 = new Image("file:src/main/resources/SuCai/提交.png");
        ImageView imageView8 = new ImageView(image8);
        imageView8.setPreserveRatio(true);
        imageView8.setFitHeight(40);
        HBox hBox8 = new HBox(imageView8);
        Submit.setGraphic(hBox8);


    }
    Connection conn;
    Statement stat;
    String user;

    public ChangeInformation(Connection conn, Statement stat, String user) {
        this.conn = conn;
        this.stat = stat;
        this.user = user;
    }
}

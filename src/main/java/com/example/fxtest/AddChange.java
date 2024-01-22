package com.example.fxtest;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class AddChange {

    @FXML
    private TextField Code;

    @FXML
    private TextArea MiaoShu;

    @FXML
    private DatePicker RecordTime;

    @FXML
    private Button Rest;

    @FXML
    private TextField Sno;

    @FXML
    private TextField Ssno;

    @FXML
    private Button Submit;

    Connection conn;
    Statement stat;
    public AddChange(Connection conn, Statement stat) {
        this.conn = conn;
        this.stat = stat;
    }
    @FXML
    void OnRest(ActionEvent event) {

    }
    int flag=1;
    @FXML
    Text information1;
    @FXML
    ComboBox<String>Code1;
    @FXML
    Text information2;
    @FXML
    Text information3;
    @FXML
    Text information4;
    public void initialize() throws SQLException {
        MiaoShu.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(t1.length()<1||t1.length()>50)
                {
                    flag=0;
                    information4.setText("描述过长,请控制在1-50个字符内");
                }
                else {
                    flag=1;
                    information4.setText("");
                }
            }
        });
        Ssno.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {

                String sql = "insert into changement (记录号, 学号, 变更代码, 记录时间,描述) values (?,?,?,?,?)";
                String check="select * from students where 学号='"+t1+"'";
                //System.out.println(ssno);
                ResultSet rs= null;
                try {
                    rs = stat.executeQuery(check);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                try {
                    if(!rs.next())
                    {
                        flag=0;
                        information3.setText("学号不存在");

                    }
                    else {
                        flag=1;
                        information3.setText("");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        });
        Sno.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
               // System.out.println(t1.length());
                if(t1.length()<1||t1.length()>15)
                {
                    flag=0;
                    information2.setText("记录号过长,请设置1-15个字符");
                }
                else {
                    flag=1;
                    information2.setText("");
                }
            }
        });
        RecordTime.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observableValue, LocalDate localDate, LocalDate t1) {
                if(t1.isAfter(LocalDate.now()))
                {
                    flag=0;
                    information1.setText("日期不合法，超出现在");
                }
                else {
                    flag=1;
                    information1.setText("");
                }
            }
        });
        Code1.getItems().add("0--转系");
        Code1.getItems().add("1--休学");
        Code1.getItems().add("2--复学");
        Code1.getItems().add("3--退学");
        Code1.getItems().add("4--毕业");
        Tooltip tooltip=new Tooltip();
        tooltip.setText("0为转系\n1为休学\n2为复学\n3为退学\n4为毕业");
        Code1.setTooltip(tooltip);
        RecordTime.setValue(LocalDate.now());
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String now3 = df.format(System.currentTimeMillis());
        Sno.setText(String.valueOf(now3));
            Image image8=new Image("file:src/main/resources/SuCai/提交.png");
            ImageView imageView8=new ImageView(image8);
            imageView8.setPreserveRatio(true);
            imageView8.setFitHeight(40);
            HBox hBox8=new HBox(imageView8);
            Submit.setGraphic(hBox8);
    }
    @FXML
    void OnSubmit(ActionEvent event) throws SQLException {
        if(flag==0||Code1.getValue()==null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "请检查输入的格式是否正确，请重试");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("file:src/main/resources/SuCai/失败.png"));
            alert.show();
            return;
        }
        String sno=Sno.getText();
        String ssno=Ssno.getText();
        String code=Code1.getValue().toString().substring(0,1);
        System.out.println(code);
        LocalDate recordtime=RecordTime.getValue();
        String miaoshu=MiaoShu.getText();
        String sql = "insert into changement (记录号, 学号, 变更代码, 记录时间,描述) values (?,?,?,?,?)";
        String check="select * from students where 学号='"+ssno+"'";
        //System.out.println(ssno);
        ResultSet rs= stat.executeQuery(check);
        if(!rs.next())
        {
            Alert alert=new Alert(Alert.AlertType.ERROR,"学号不存在,请重试");
            alert.show();
            return;
        }
        check="select * from change_code where 代码='"+code+"'";
        rs=stat.executeQuery(check);
        if(!rs.next())
        {
            Alert alert=new Alert(Alert.AlertType.ERROR,"变更代码不存在,请重试");
            alert.show();
            return;
        }
        check="select * from changement where 记录号='"+sno+"'";
        rs=stat.executeQuery(check);
        if(rs.next())
        {
            Alert alert=new Alert(Alert.AlertType.ERROR,"记录号已存在,请重试");
            alert.show();
            return;
        }

        PreparedStatement preStmt = conn.prepareStatement(sql);
        preStmt.setString(1, sno);
        preStmt.setString(2, ssno);
        preStmt.setString(3, code);
        preStmt.setDate(4, Date.valueOf(recordtime));
        preStmt.setString(5, miaoshu);
        int d = preStmt.executeUpdate();
        if (d == 1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "注册成功");
            alert.show();
        }
    }

}

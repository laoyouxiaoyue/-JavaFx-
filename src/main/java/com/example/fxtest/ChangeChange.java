package com.example.fxtest;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

public class ChangeChange {
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

    @FXML
    void OnRest(ActionEvent event) {
        Sno.setEditable(false);
        Ssno.setText("");
        Code.setText("");
        MiaoShu.setText("");
        RecordTime.setValue(LocalDate.now());
    }
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
    int flag=1;
    public  void initialize()
    {
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
        Image image8=new Image("file:src/main/resources/SuCai/提交.png");
        ImageView imageView8=new ImageView(image8);
        imageView8.setPreserveRatio(true);
        imageView8.setFitHeight(40);
        HBox hBox8=new HBox(imageView8);
        Submit.setGraphic(hBox8);
        Tooltip tooltip=new Tooltip();
        tooltip.setText("0为转系\n1为休学\n2为复学\n3为退学\n4为毕业");
        Code1.setTooltip(tooltip);
        Sno.setEditable(false);
        Sno.setText(observableList.get(0).toString());
        Ssno.setText(observableList.get(1).toString());

        RecordTime.setValue(LocalDate.parse(observableList.get(3).toString()));
        MiaoShu.setText(observableList.get(4).toString());

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
        String  recordTimeValue=RecordTime.getValue().toString();
        String miaoshu= MiaoShu.getText();
        String sql="update changement set 记录号='"+sno+"',学号='"+ssno+"',变更代码='"+code+"',记录时间='"+recordTimeValue+"',描述='"+miaoshu +"' where 记录号='"+sno+"'";
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
        check="select * from changement where 代码='"+sno+"'";
        stat.execute(sql);
        Alert alert=new Alert(Alert.AlertType.INFORMATION,"更改成功");
        alert.show();
    }

    ObservableList observableList;
    public ChangeChange(Connection conn, Statement stat, ObservableList observableList) {
        this.conn = conn;
        this.stat = stat;
        this.observableList=observableList;

    }
}

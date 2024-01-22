package com.example.fxtest;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class AddYongHu {



    @FXML
    private TextField ChongFuMiMa;

    @FXML
    private TextField Email;

    @FXML
    private TextField MiMa;

    @FXML
    private TextField Telephone;


    @FXML
    private DatePicker bitrh;

    @FXML
    private TextField YongHuMing;

    @FXML
    private DatePicker Birth;
    @FXML
    private Button reset;

    @FXML
    private Button submit;


    Statement stat=null;
    Connection conn = null;

    public AddYongHu(Connection conn,Statement stat) {
        this.conn=conn;

        this.stat=stat;
    }


    @FXML
    void OnReset(ActionEvent event) throws SQLException {
        String sql="select * from Users";
        Statement  stmt=conn.createStatement();
        ResultSet rs=stmt.executeQuery(sql);
        while (rs.next()){
            //6.2 获取数据  getXxx()
            String id = rs.getString("用户名");
            System.out.println(id);
            System.out.println("--------------");
        }

        YongHuMing.setText("");
        MiMa.setText("");
        Telephone.setText("");
        ChongFuMiMa.setText("");
        Email.setText("");
    }
    @FXML
    void OnDefault(ActionEvent event){

        String yonghumings=new StringBuffer(YongHuMing.getText()).reverse().toString();;
        if(yonghumings.equals(""))
        {
            Alert Wrong=new Alert(Alert.AlertType.ERROR,"请先输入正确的用户名");
            Wrong.show();
            return;
        }
        MiMa.setText(yonghumings);
        ChongFuMiMa.setText(yonghumings);
    }
    int flag=1;
    @FXML
    private Text information1;
    @FXML
    private Text information2;
    @FXML
    private Text information3;
    @FXML
    private Text information4;
    @FXML
    private Text information5;
    @FXML
    private Text information6;
    @FXML
    ComboBox<String>QX;
    public void initialize()
    {
        QX.getItems().add("1");
        QX.getItems().add("2");
        QX.getItems().add("3");

        YongHuMing.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                //System.out.println(t1);

                String check = "select * from Users where 用户名='" + t1 + "';";
                // System.out.println(check);
                ResultSet ok = null;
                try {
                    ok = stat.executeQuery(check);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                if(t1.length()>15||t1.length()<3)
                {
                    information1.setText("用户名由3-15个字符组成");
                    flag=0;
                }
                else {
                    try {
                        if(ok.next())
                        {
                            information1.setText("用户名已存在");
                            flag=0;
                        }
                        else {
                            information1.setText("");
                            flag=1;
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }


            }
        });
        MiMa.textProperty().addListener(new ChangeListener<String>() {
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
        ChongFuMiMa.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                // System.out.println(t1);
                if(!ChongFuMiMa.getText().equals(MiMa.getText()))
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

        Birth.valueProperty().addListener(new ChangeListener<LocalDate>() {
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
        Telephone.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                String pattern = "[0-9-()（）]{7,18}";
                if (!Pattern.compile(pattern).matcher(Telephone.getText()).matches()) {
                    information5.setText("手机号码格式输入错误");
                    flag=0;
                }
                else {
                    information5.setText("");
                    flag = 1;
                }
            }
        });
        Email.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                String pattern = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
                if (!Pattern.compile(pattern).matcher(Email.getText()).matches()) {
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

    }

    @FXML
    TextField KaMi;
    @FXML
    void OnSubmit(ActionEvent event) throws SQLException {
        //String sql="select * from Users";

        String mima = MiMa.getText();
        String chongfumima = ChongFuMiMa.getText();
        String telephone = Telephone.getText();
        String yonghuming = YongHuMing.getText();
        String email = Email.getText();
        if(flag==0)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "请检查输入的格式是否正确，请重试");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("file:src/main/resources/SuCai/失败.png"));
            alert.show();
            return;
        }
        if (!chongfumima.equals(mima)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "两遍密码输入不一致，请重试");
            alert.show();
            return;
        }
        String pattern = "\\d{4}(\\-|\\/|.)\\d{2}\\1\\d{2}";
        // System.out.println(Pattern.compile(pattern).matcher(Birth.getText()).matches());

        pattern = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
        if (!Pattern.compile(pattern).matcher(Email.getText()).matches()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "邮箱格式输入错误，请重试");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("file:src/main/resources/SuCai/失败.png"));
            alert.show();
            return;
        }
        pattern = "[0-9-()（）]{7,18}";
        if (!Pattern.compile(pattern).matcher(Telephone.getText()).matches()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "手机号码格式输入错误，请重试");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("file:src/main/resources/SuCai/失败.png"));
            alert.show();
            return;
        }
        String check = "select * from Users where 用户名='" + yonghuming + "';";
        ResultSet ok = stat.executeQuery(check);

        if (ok.next()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "用户名已存在，请重试");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("file:src/main/resources/SuCai/失败.png"));
            alert.show();
            return;
        }

        String sql = "insert into users (用户名, 密码, 出生日期, 手机号码, 电子邮箱,权限) values (?,?,?,?,?,?)";
        PreparedStatement preStmt = conn.prepareStatement(sql);
        preStmt.setString(1, yonghuming);
        preStmt.setString(2, mima);
        preStmt.setDate(3, Date.valueOf(Birth.getValue().toString()));
        preStmt.setString(4, telephone);
        preStmt.setString(5, email);
        preStmt.setString(6,QX.getValue());


        int d = preStmt.executeUpdate();
        if (d == 1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "注册成功");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("file:src/main/resources/SuCai/成功.png"));
            alert.show();

        }
    }
}




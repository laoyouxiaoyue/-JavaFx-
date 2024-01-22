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

public class AddController {

    @FXML
    private TextField Sadress;



    @FXML
    private ComboBox<String> Sdepartment;

    @FXML
    private TextField Sname;

    @FXML
    private TextField Sno;

    @FXML
    private TextField Ssex;

    @FXML
    private DatePicker Sbirth;

    @FXML
    private Button Submit;

    Connection conn=null;
    Statement stat=null;

    public AddController(Connection conn, Statement stat) {
        this.conn = conn;
        this.stat = stat;
    }
    @FXML
    RadioButton femalButton;
    @FXML
    RadioButton maleButton;
    @FXML
    ImageView show;
    ToggleGroup genderGroup = new ToggleGroup();
    @FXML
    ComboBox<String>Sclass;
    @FXML
    Text information1;
    @FXML
    Text information2;
    @FXML
    Text information3;
    @FXML
    Text information4;
    int flag=1;
    public void initialize() throws SQLException {
        Sno.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                String check="select * from students where 学号='"+t1+"'";
                ResultSet rs= null;
                try {
                    rs = stat.executeQuery(check);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
              //  System.out.println(t1.length());
                if(t1.length()<1||t1.length()>15)
                {
                    information1.setText("学号格式不合法（1-15）字符");
                    flag=0;
                }
                else {
                    try {
                        if(rs.next())
                        {
                            information1.setText("学号已存在");
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
        Sname.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(t1.length()<1||t1.length()>15)
                {
                    information2.setText("姓名长度不合法(1-15）字符");
                    flag=0;
                }
                else {
                    information2.setText("");
                    flag=1;
                }
            }
        });
        Sadress.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(t1.length()<1||t1.length()>20)
                {
                    information4.setText("籍贯不合法（1-20）字符");
                    flag=0;
                }
                else {
                    information4.setText("");
                    flag=1;
                }
            }
        });
        Sbirth.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observableValue, LocalDate localDate, LocalDate t1) {
                if(t1.isAfter(LocalDate.now()))
                {
                    information3.setText("时间不合法,超出现在");
                    flag=0;
                }
                else {
                    information3.setText("");
                    flag=1;
                }
            }
        });
        Sclass.setEditable(true);
        Sdepartment.setEditable(true);
        String sql="select 编号 from class";
        ResultSet rs=conn.createStatement().executeQuery(sql);
        while(rs.next()) {
            //Iterate Row

            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                //Iterate Column
               // System.out.println(rs.getString(i));
                Sclass.getItems().add(rs.getString(i));
            }
        }
        sql="select 编号 from department";
        rs=conn.createStatement().executeQuery(sql);
        while(rs.next()) {
            //Iterate Row

            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                //Iterate Column
                //System.out.println(rs.getString(i));
                Sdepartment.getItems().add(rs.getString(i));
            }
        }
        maleButton.setSelected(true);
        maleButton.setToggleGroup(genderGroup);
        femalButton.setToggleGroup(genderGroup);
        Image image8=new Image("file:src/main/resources/SuCai/提交.png");
        ImageView imageView8=new ImageView(image8);
        imageView8.setPreserveRatio(true);
        imageView8.setFitHeight(40);
        HBox hBox8=new HBox(imageView8);
        Submit.setGraphic(hBox8);
        show.setPreserveRatio(true);
        //show.setFitHeight(40);
        show.setImage(new Image("file:src/main/resources/SuCai/新增学生.png"));
    }
    @FXML TextField KaMi;
    @FXML
    void OnSubmit(ActionEvent event) throws SQLException {
        if(flag==0||Sdepartment.getValue()==null||Sclass.getValue()==null||Sno.getText().equals("")||Sname.equals("")||Sadress.equals("")||Sbirth.getValue()==null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "请检查输入的格式是否正确，请重试");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("file:src/main/resources/SuCai/失败.png"));
            alert.show();
            return;
        }
        String sno=Sno.getText();
        String ssex;
        if(genderGroup.getSelectedToggle()==maleButton)
        {
            ssex="男";
        }
        else {
            ssex="女";
        }
        String sclass=Sclass.getValue();
        LocalDate sbirth=Sbirth.getValue();
        String sname=Sname.getText();
        String sadress=Sadress.getText();
        String sdepartment=Sdepartment.getValue();
        String sql = "insert into students (学号, 姓名, 性别, 班级编号,籍贯,院系编号,生日) values (?,?,?,?,?,?,?)";
        String check="select * from students where 学号='"+sno+"'";
        ResultSet rs= stat.executeQuery(check);
        if(rs.next())
        {
            Alert alert=new Alert(Alert.AlertType.ERROR,"学号已存在,请重试");
            alert.show();
            return;
        }
        check="select * from department where 编号="+"'"+sdepartment+"'";
        rs= stat.executeQuery(check);
        if(!rs.next())
        {
            Alert alert=new Alert(Alert.AlertType.ERROR,"院系不存在,请重试");
            alert.show();
            return;
        }
        check="select * from class where 编号="+"'"+sclass+"'";
        rs= stat.executeQuery(check);
        if(!rs.next())
        {
            Alert alert=new Alert(Alert.AlertType.ERROR,"班级不存在,请重试");
            alert.show();
            return;
        }
        if(sname.length()<=0||sname.length()>=15)
        {
            Alert alert=new Alert(Alert.AlertType.ERROR,"姓名不合法,请重试");
            alert.show();
            return;
        }
        PreparedStatement preStmt = conn.prepareStatement(sql);
        preStmt.setString(1, sno);
        preStmt.setString(2, sname);
        preStmt.setString(3, ssex);
        preStmt.setString(4, sclass);
        preStmt.setString(5, sadress);
        preStmt.setString(6, sdepartment);
        preStmt.setString(7, sbirth.toString());

        int d = preStmt.executeUpdate();
        if (d == 1) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "注册成功");
            alert.show();
        }

    }
}

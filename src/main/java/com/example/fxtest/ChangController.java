package com.example.fxtest;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class ChangController {

    @FXML
    private TextField Sadress;

    @FXML
    private DatePicker Sbirth;

    @FXML
    private ComboBox<String> Sclass;

    @FXML
    private TextField Sname;

    @FXML
    private TextField Sno;

    @FXML
    private ComboBox<String> Sdepartment;
    @FXML
    private TextField Ssex;

    @FXML
    private Button Submit;

    Connection conn=null;
    Statement stat=null;
    String sno;
    String sname;

    String sadress;

    String ssex;
    String sclass;
    String sdepartment;
    String sbirth;
    ObservableList observableList;
    public ChangController(Connection conn, Statement stat, ObservableList observableList) {
        this.conn = conn;
        this.stat = stat;
        this.observableList=observableList;

    }
    @FXML
    ImageView show;

    @FXML
    RadioButton maleButton;
    @FXML
    RadioButton femaleButton;

    ToggleGroup genderGroup = new ToggleGroup();



    @FXML
    public  void initialize() throws SQLException {

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

        maleButton.setToggleGroup(genderGroup);
        femaleButton.setToggleGroup(genderGroup);
        Image image8=new Image("file:src/main/resources/SuCai/提交.png");
        ImageView imageView8=new ImageView(image8);
        imageView8.setPreserveRatio(true);
        imageView8.setFitHeight(40);
        HBox hBox8=new HBox(imageView8);
        Submit.setGraphic(hBox8);
        show.setPreserveRatio(true);
        //show.setFitHeight(40);
     //   show.setImage(new Image("file:src/main/resources/SuCai/新增学生.png"));
        Sno.setEditable(false);
        sno=observableList.get(0).toString();
        sname=observableList.get(1).toString();
        ssex=observableList.get(2).toString();
        sclass=observableList.get(3).toString();
        sadress=observableList.get(4).toString();
        sdepartment=observableList.get(5).toString();
        sbirth=observableList.get(6).toString();
        Sbirth.setValue(LocalDate.parse(sbirth));
       Sno.setText(sno);
       Sname.setText(sname);
        if(ssex.equals("男")) {
            maleButton.setSelected(true);
        }
        else {
            femaleButton.setSelected(false);
        }
       Sadress.setText(sadress);
        Sdepartment.setValue(sdepartment);
       Sclass.setValue(sclass);
    }

    @FXML
    void OnSubmit(ActionEvent event) throws SQLException {
            sname=Sname.getText();
        if(genderGroup.getSelectedToggle()==maleButton)
        {
            ssex="男";
        }
        else {
            ssex="女";
        }
            sadress=Sadress.getText();
            sclass=Sclass.getValue();
            sbirth=Sbirth.getValue().toString();
            sdepartment=Sdepartment.getValue();
            String sql="update students set 姓名='"+sname+"',性别='"+ssex+"',籍贯='"+sadress+"',院系编号='"+sdepartment+"',班级编号='"+sclass+"',生日='"+sbirth+"' where 学号='"+sno+"'";

        if(sname.length()<=0||sname.length()>=15)
        {
            Alert alert=new Alert(Alert.AlertType.ERROR,"姓名不合法,请重试");
            alert.show();
            return;
        }
            stat.execute(sql);
            Alert alert=new Alert(Alert.AlertType.INFORMATION,"更改成功");
            alert.show();
    }

}
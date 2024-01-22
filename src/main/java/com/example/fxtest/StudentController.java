package com.example.fxtest;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.HostServices;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StudentController  {

    @FXML
    Label ZhuLabel;
    @FXML
    Label CiLabel;
    @FXML
    private TreeView<String> tv;
    @FXML
    private Button Add;

    int choice;
    @FXML
    private Button Change;

    @FXML
    private Button Remove;

    @FXML
    private TextField SearchSno;

    @FXML
    private  TextField SearchSno2;
    @FXML
    private AnchorPane ap;
    @FXML
    private Button Check;

    @FXML
    private Button F5;
    @FXML
    void OnCheck(ActionEvent event) throws SQLException {
        String Cun;
        String Zhu;
        String Ci;
        switch (choice)
        {
            case 1:Cun="department";Zhu="编号";Ci="全称";break;
            case 2:Cun="class";Zhu="编号";Ci="全称";break;
            case 3:Cun="students";Zhu="学号";Ci="姓名";break;
            case 4:Cun="changement";Zhu="记录号";Ci="学号";break;
            case 5:Cun="reward";Zhu="记录号";Ci="学号";break;
            case 6:Cun="punish";Zhu="记录号";Ci="学号";break;
            case 7:Cun="YongHu";Zhu="用户名";Ci="电子邮箱";break;
            default:Cun="students";Zhu="学号";Ci="姓名";break;
        }
        data = FXCollections.observableArrayList();
        String ZhuSearch=SearchSno.getText();
        String CiSearch=SearchSno2.getText();
        String SQL;
        if(CiSearch.equals(""))
        {
            SQL="select * from "+Cun+" where "+Zhu+" like '%"+SearchSno.getText()+"%'";
        }
        else if(ZhuSearch.equals(""))
        {
            SQL="select * from "+Cun+" where "+Ci+" like '%"+SearchSno2.getText()+"%'";
        }
        else {
            SQL="select * from "+Cun+" where "+Ci+" like '%"+SearchSno2.getText()+"%' and "+Zhu+" like '%"+SearchSno.getText()+"%'";
        }

        System.out.println(SQL);
        if(SearchSno.getText().equals("")&&SearchSno2.getText().equals(""))
        {
            data = FXCollections.observableArrayList();
            String SbL="select * from "+Cun;
            ResultSet rs=conn.createStatement().executeQuery(SbL);
            while(rs.next()){

                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){

                    row.add(rs.getString(i));
                }

                data.add(row);
            }
            StudentTable.setItems(data);
            return;
        }
        ResultSet rs=conn.createStatement().executeQuery(SQL);
        while(rs.next()){

            ObservableList<String> row = FXCollections.observableArrayList();
            for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                //Iterate Column
                row.add(rs.getString(i));
            }
            data.add(row);

        }

        StudentTable.setItems(data);
    }

    @FXML
    void OnF5(ActionEvent event) throws SQLException {
        if(QX_level<=1)
        {
            Add.setVisible(false);
            Remove.setVisible(false);
            Change.setVisible(false);
        }
        else {
            Add.setVisible(true);
            Remove.setVisible(true);
            Change.setVisible(true);
        }
        StudentTable.getColumns().clear();
        StudentTable.getItems().clear();
        String Cun;
        switch (choice)
        {
            case 1:Cun="department";break;
            case 2:Cun="class";break;
            case 3:Cun="students";break;
            case 4:Cun="changement";break;
            case 5:Cun="reward";break;
            case 6:Cun="punishment";break;
            case 7:Cun="Users";break;
            default:Cun="students";break;
        }
        data = FXCollections.observableArrayList();
        String SQL="select * from "+Cun;
        System.out.println("hellosb");
        ResultSet rs=conn.createStatement().executeQuery(SQL);
        int flag=0;
        for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++) {
            //Iterate Column
            System.out.print(i + " ");
            System.out.println(rs.getMetaData().getColumnName(i));
            TableColumn tableColumn = new TableColumn(rs.getMetaData().getColumnName(i));
            int finalI = i-1;
            tableColumn.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(finalI).toString());
                }
            });
            StudentTable.getColumns().add(tableColumn);

        }

        while(rs.next()){
            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();
            for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                //Iterate Column

                row.add(rs.getString(i));
            }

            System.out.println("Row [1] added "+row );
            data.add(row);

        }

        //FINALLY ADDED TO TableView
        StudentTable.setItems(data);
    }
    @FXML
    private Text SearchText;
    Connection conn=null;
    Statement stat=null;
    ObservableList<ObservableList> data;

    @FXML
    void OnAdd(ActionEvent event) throws IOException {
        String Cun;
        switch (choice)
        {
            case 1:addDepartment();break;
            case 2:addClass();break;
            case 3:addStudent();break;
            case 4:addChange();break;
            case 5:addReward();break;
            case 6:addPunishment();break;
            case 7:addYonghu();break;
            default:Cun="students";break;
        }

    }
    void addYonghu() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("AddYongHu.fxml"));
        fxmlLoader.setControllerFactory(t->new AddYongHu(conn,stat));
        Parent StudentRoot=fxmlLoader.load();
        //RegisterController registerController=fxmlLoader.getController();
        Scene scene = new Scene(StudentRoot );
        Stage AddStage=new Stage();
        AddStage.getIcons().add(new Image("file:src/main/resources/SuCai/用户管理.png"));
        AddStage.setTitle("班级管理系统");
        AddStage.setScene(scene);
        AddStage.show();
    }
    void addClass() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("AddClass.fxml"));
        fxmlLoader.setControllerFactory(t->new AddClass(conn,stat));
        Parent StudentRoot=fxmlLoader.load();
        //RegisterController registerController=fxmlLoader.getController();
        Scene scene = new Scene(StudentRoot );
        Stage AddStage=new Stage();
        AddStage.getIcons().add(new Image("file:src/main/resources/SuCai/班级管理.png"));
        AddStage.setTitle("班级管理系统");
        AddStage.setScene(scene);
        AddStage.show();
    }
    void addStudent() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("add-view.fxml"));
        fxmlLoader.setControllerFactory(t->new AddController(conn,stat));
        Parent StudentRoot=fxmlLoader.load();
        //RegisterController registerController=fxmlLoader.getController();
        Scene scene = new Scene(StudentRoot );
        Stage AddStage=new Stage();
        AddStage.getIcons().add(new Image("file:src/main/resources/SuCai/学生.png"));
        AddStage.setTitle("学生管理系统");
        AddStage.setScene(scene);
        AddStage.show();
    }
    void addChange() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("AddChange.fxml"));
        fxmlLoader.setControllerFactory(t->new AddChange(conn,stat));
        Parent StudentRoot=fxmlLoader.load();
        //RegisterController registerController=fxmlLoader.getController();
        Scene scene = new Scene(StudentRoot );
        Stage AddStage=new Stage();
        AddStage.getIcons().add(new Image("file:src/main/resources/SuCai/学籍管理.png"));
        AddStage.setTitle("学籍管理");
        AddStage.setScene(scene);
        AddStage.show();
    }
    void addReward() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("AddReward.fxml"));
        fxmlLoader.setControllerFactory(t->new AddReward(conn,stat));
        Parent StudentRoot=fxmlLoader.load();
        //RegisterController registerController=fxmlLoader.getController();
        Scene scene = new Scene(StudentRoot );
        Stage AddStage=new Stage();
        AddStage.getIcons().add(new Image("file:src/main/resources/SuCai/奖励管理.png"));
        AddStage.setTitle("奖励管理");
        AddStage.setScene(scene);
        AddStage.show();
    }
    void addDepartment() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("AddDepartment.fxml"));
        fxmlLoader.setControllerFactory(t->new AddDepartment(conn,stat));
        Parent StudentRoot=fxmlLoader.load();
        //RegisterController registerController=fxmlLoader.getController();
        Scene scene = new Scene(StudentRoot );
        Stage AddStage=new Stage();
        AddStage.getIcons().add(new Image("file:src/main/resources/SuCai/院系管理.png"));
        AddStage.setTitle("新增学院");
        AddStage.setScene(scene);
        AddStage.show();
    }
    void addPunishment() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("AddPunishment.fxml"));
        fxmlLoader.setControllerFactory(t->new AddPunishment(conn,stat));
        Parent StudentRoot=fxmlLoader.load();
        //RegisterController registerController=fxmlLoader.getController();
        Scene scene = new Scene(StudentRoot );
        Stage AddStage=new Stage();
        AddStage.getIcons().add(new Image("file:src/main/resources/SuCai/处罚管理.png"));
        AddStage.setTitle("处罚管理");
        AddStage.setScene(scene);
        AddStage.show();
    }
    @FXML
    void OnChange(ActionEvent event) throws IOException {
        String Cun;
        System.out.println(choice);
        if(StudentTable.getSelectionModel().getSelectedItem()==null)
        {
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setContentText("请先选中要修改的项");
            alert.show();
            return;
        }
        switch (choice)
        {
            case 1:ChangeDepartment();break;
            case 2:ChangeClass();break;
            case 3:ChangeStudent();break;
            case 4:ChangeChange();break;
            case 5:ChangeReward();break;
            case 6:ChangePunishment();break;
            case 7:ChangeYongHu();break;
            default:Cun="students";break;
        }
    }
    void ChangeYongHu() throws IOException {
        ObservableList observableList=StudentTable.getSelectionModel().getSelectedItem();
        String sno=observableList.get(1).toString();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ChangeYongHu.fxml"));
        fxmlLoader.setControllerFactory(t->new ChangeYongHu(conn,stat,observableList));
        Parent StudentRoot=fxmlLoader.load();
        //RegisterController registerController=fxmlLoader.getController();
        Scene scene = new Scene(StudentRoot );
        Stage Stage=new Stage();
        Stage.getIcons().add(new Image("file:src/main/resources/SuCai/用户管理.png"));
        Stage.setTitle("修改用户");
        Stage.setScene(scene);
        Stage.show();
    }
    void ChangeClass() throws IOException {
        ObservableList observableList=StudentTable.getSelectionModel().getSelectedItem();
        String sno=observableList.get(1).toString();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ChangeClass.fxml"));
        fxmlLoader.setControllerFactory(t->new ChangeClass(conn,stat,observableList));
        Parent StudentRoot=fxmlLoader.load();
        //RegisterController registerController=fxmlLoader.getController();
        Scene scene = new Scene(StudentRoot );
        Stage Stage=new Stage();
        Stage.getIcons().add(new Image("file:src/main/resources/SuCai/班级管理.png"));
        Stage.setTitle("修改班级");
        Stage.setScene(scene);
        Stage.show();
    }
    void ChangeDepartment() throws IOException {
        ObservableList observableList=StudentTable.getSelectionModel().getSelectedItem();
        String sno=observableList.get(1).toString();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ChangeDepartment.fxml"));
        fxmlLoader.setControllerFactory(t->new ChangeDepartment(conn,stat,observableList));
        Parent StudentRoot=fxmlLoader.load();
        //RegisterController registerController=fxmlLoader.getController();
        Scene scene = new Scene(StudentRoot );
        Stage Stage=new Stage();
        Stage.getIcons().add(new Image("file:src/main/resources/SuCai/院系管理.png"));
        Stage.setTitle("修改院系");
        Stage.setScene(scene);
        Stage.show();
    }
    void ChangeReward() throws IOException {
        ObservableList observableList=StudentTable.getSelectionModel().getSelectedItem();
        String sno=observableList.get(1).toString();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ChangeReward.fxml"));
        fxmlLoader.setControllerFactory(t->new ChangeReward(conn,stat,observableList));
        Parent StudentRoot=fxmlLoader.load();
        //RegisterController registerController=fxmlLoader.getController();
        Scene scene = new Scene(StudentRoot );
        Stage Stage=new Stage();
        Stage.getIcons().add(new Image("file:src/main/resources/SuCai/奖励管理.png"));
        Stage.setTitle("修改奖励");
        Stage.setScene(scene);
        Stage.show();
    }
    void ChangePunishment() throws IOException {
        ObservableList observableList=StudentTable.getSelectionModel().getSelectedItem();
        String sno=observableList.get(1).toString();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ChangePunishment.fxml"));
        fxmlLoader.setControllerFactory(t->new ChangePunishment(conn,stat,observableList));
        Parent StudentRoot=fxmlLoader.load();
        //RegisterController registerController=fxmlLoader.getController();
        Scene scene = new Scene(StudentRoot );
        Stage Stage=new Stage();
        Stage.getIcons().add(new Image("file:src/main/resources/SuCai/处罚管理.png"));
        Stage.setTitle("修改处罚");
        Stage.setScene(scene);
        Stage.show();
    }
    void ChangeStudent() throws IOException {
        ObservableList observableList=StudentTable.getSelectionModel().getSelectedItem();
        String sno=observableList.get(1).toString();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Chang-view.fxml"));
        fxmlLoader.setControllerFactory(t->new ChangController(conn,stat,observableList));
        Parent StudentRoot=fxmlLoader.load();
        //RegisterController registerController=fxmlLoader.getController();
        Scene scene = new Scene(StudentRoot );
        Stage Stage=new Stage();
        Stage.getIcons().add(new Image("file:src/main/resources/SuCai/学生.png"));
        Stage.setTitle("修改学生");
        Stage.setScene(scene);
        Stage.show();
    }
    String user;
    @FXML
    Button information;
    @FXML
    void Oninformation(ActionEvent event) throws IOException {
        //ObservableList observableList=StudentTable.getSelectionModel().getSelectedItem();
        //String sno=observableList.get(1).toString();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("information.fxml"));
        fxmlLoader.setControllerFactory(t->new ChangeInformation(conn,stat,user));
        Parent StudentRoot=fxmlLoader.load();
        //RegisterController registerController=fxmlLoader.getController();
        Scene scene = new Scene(StudentRoot );
        Stage Stage=new Stage();
        Stage.getIcons().add(new Image("file:src/main/resources/SuCai/个人信息icon.png"));
        Stage.setTitle("个人信息");
        Stage.setScene(scene);
        Stage.show();
    }
    void ChangeChange() throws IOException {
        ObservableList observableList=StudentTable.getSelectionModel().getSelectedItem();
        String sno=observableList.get(1).toString();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ChangeChange.fxml"));
        fxmlLoader.setControllerFactory(t->new ChangeChange(conn,stat,observableList));
        Parent StudentRoot=fxmlLoader.load();
        //RegisterController registerController=fxmlLoader.getController();
        Scene scene = new Scene(StudentRoot );
        Stage Stage=new Stage();
        Stage.getIcons().add(new Image("file:src/main/resources/SuCai/学籍管理.png"));
        Stage.setTitle("修改学籍");
        Stage.setScene(scene);
        Stage.show();
    }
    @FXML
    void OnRemove(ActionEvent event) throws SQLException {
       // URL url=this.getClass().getClassLoader().getResource("file:src/main/resources/Song/1.mp3");
        //System.out.println(url.toExternalForm());new File("file:///home/winged/IdeaProjects/MoviePlayer/video/barsandtone.flv").toURI().toString());file:src/main/resources/Song/1.mp3

        String Cun;
        String Zhu;
        switch (choice)
        {
            case 1:Cun="department";Zhu="编号";break;
            case 2:Cun="class";Zhu="编号";break;
            case 3:Cun="students";Zhu="学号";break;
            case 4:Cun="changement";Zhu="记录号";break;
            case 5:Cun="reward";Zhu="记录号";break;
            case 6:Cun="punishment";Zhu="记录号";break;
            case 7:Cun="Users";Zhu="用户名";break;
            default:Cun="students";Zhu="学号";break;
        }
        if(StudentTable.getSelectionModel().getSelectedItem()==null)
        {
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setContentText("请先选中要删除的项");
            alert.show();
            return;
        }
         ObservableList observableList=StudentTable.getSelectionModel().getSelectedItem();
        String sno=observableList.get(0).toString();
        System.out.println(sno);
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION,"确认删除吗");
        String Sql="delete from "+Cun+" where "+Zhu+"='"+sno+"'";
        if(alert.showAndWait().get()==ButtonType.OK)
        {
                stat.execute(Sql);
            data = FXCollections.observableArrayList();
            String SQL="select * from "+Cun;
            System.out.println("hellosb");
            ResultSet rs=conn.createStatement().executeQuery(SQL);
            while(rs.next()){
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    //Iterate Column

                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] added "+row );
                data.add(row);

            }
            StudentTable.setItems(data);

            //FINALLY ADDED TO TableView
        }
    }
    @FXML
    Label time;
    @FXML
    private TableView<ObservableList> StudentTable;
    @FXML
    int QX_level=1;
    @FXML
    Button Music;
    int Musicflag=0;

    int index=1;
    @FXML
    Button Next;
    @FXML
    void OnNext(ActionEvent event) {
        mediaPlayer.stop();
        if (index < 2) {
            index++;
        }
        else {
            index=1;
        }
        Media media = new Media(new File("src/main/resources/Song/" + index+".mp3").toURI().toString());
        mediaPlayer=new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
        Musicflag=1;
    }
    @FXML
    void OnMusic(ActionEvent event)
    {

        if(Musicflag==0)
        {
            mediaPlayer.play();
            Musicflag=1;
        }
        else{
            mediaPlayer.pause();
            Musicflag=0;
        }
    }
    Media media=new Media(new File("src/main/resources/Song/1.mp3").toURI().toString());
    MediaPlayer mediaPlayer=new MediaPlayer(media);
    public void initialize() throws SQLException {
        Image image37=new Image("file:src/main/resources/SuCai/播放.png");
        Image image47=new Image("file:src/main/resources/SuCai/下一首.png");
        ImageView imageView37=new ImageView(image37);
        imageView37.setPreserveRatio(true);
        imageView37.setFitHeight(40);
        HBox hBox37=new HBox(imageView37);
        Music.setGraphic(hBox37);
        ImageView imageView47=new ImageView(image47);
        imageView47.setPreserveRatio(true);
        imageView47.setFitHeight(40);
        HBox hBox47=new HBox(imageView47);
        Next.setGraphic(hBox47);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        //设置实时时间
        String sql="select 权限 from Users where 用户名='"+user+"'";
        System.out.println(sql);
        ResultSet rs=stat.executeQuery(sql);
      //  System.out.println(rs.getMetaData().getColumnCount());
        rs.next();
        QX_level=rs.getInt(1);
        if(QX_level<=1)
        {
            Add.setVisible(false);
            Remove.setVisible(false);
            Change.setVisible(false);
        }
        Image image19 = new Image("file:src/main/resources/SuCai/个人信息icon.png");
        ImageView imageView19 = new ImageView(image19);
        imageView19.setPreserveRatio(true);
        imageView19.setFitHeight(40);
        HBox hBox19 = new HBox(imageView19);
        information.setGraphic(hBox19);
        time.setFont(new Font(20));
        DateFormat currentTime = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");

        EventHandler<ActionEvent> eventHandler = e->{
            time.setText(currentTime.format(new Date()));
            //System.out.println(currentTime.format(new Date()));
        };
        Timeline animation = new Timeline(new KeyFrame(Duration.millis(1000), 		(javafx.event.EventHandler<ActionEvent>) eventHandler));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
        Image image111=new Image("file:src/main/resources/SuCai/"+"学号"+".png");

        Image image7=new Image("file:src/main/resources/SuCai/修改.png");
        ImageView imageView7=new ImageView(image7);
        imageView7.setPreserveRatio(true);
        imageView7.setFitHeight(40);
        HBox hBox7=new HBox(imageView7);
        Change.setGraphic(hBox7);
        Image image8=new Image("file:src/main/resources/SuCai/刷新.png");
        ImageView imageView8=new ImageView(image8);
        imageView8.setPreserveRatio(true);
        imageView8.setFitHeight(40);
        HBox hBox8=new HBox(imageView8);
        F5.setGraphic(hBox8);
        Image image9=new Image("file:src/main/resources/SuCai/删除.png");
        ImageView imageView9=new ImageView(image9);
        imageView9.setPreserveRatio(true);
        imageView9.setFitHeight(40);
        HBox hBox9=new HBox(imageView9);
        Remove.setGraphic(hBox9);
        Image image10=new Image("file:src/main/resources/SuCai/添加.png");
        ImageView imageView10=new ImageView(image10);
        imageView10.setPreserveRatio(true);
        imageView10.setFitHeight(40);
        HBox hBox10=new HBox(imageView10);
        Add.setGraphic(hBox10);
        Image image11=new Image("file:src/main/resources/SuCai/搜索.png");
        ImageView imageView11=new ImageView(image11);
        imageView11.setPreserveRatio(true);
        imageView11.setFitHeight(40);
        HBox hBox11=new HBox(imageView11);
        Check.setGraphic(hBox11);
        choice=3;
        StudentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TreeItem<String> t1=new TreeItem<>("学生管理系统");
        tv.setRoot(t1);
        Image image=new Image("file:src/main/resources/SuCai/学生管理.png");
        ImageView imageView=new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(30);
        HBox hBox=new HBox(imageView);
        //hBox.setStyle("-fx-background-color: rgb(27,34,55)");
        t1.setGraphic(hBox);
        TreeItem<String>yx=new TreeItem<>("院系管理");
        Image image1=new Image("file:src/main/resources/SuCai/院系管理.png");
        ImageView imageView1=new ImageView(image1);
        imageView1.setPreserveRatio(true);
        imageView1.setFitHeight(30);
        HBox hBox1=new HBox(imageView1);
        yx.setGraphic(hBox1);
        TreeItem<String>bj=new TreeItem<>("班级管理");
        Image image2=new Image("file:src/main/resources/SuCai/班级管理.png");
        ImageView imageView2=new ImageView(image2);
        imageView2.setPreserveRatio(true);
        imageView2.setFitHeight(30);
        HBox hBox2=new HBox(imageView2);
        //hBox2.setStyle("-fx-background-color: rgb(27,34,55)");
        bj.setGraphic(hBox2);
        TreeItem<String>xs=new TreeItem<>("学生管理");
        Image image3=new Image("file:src/main/resources/SuCai/学生.png");
        ImageView imageView3=new ImageView(image3);
        imageView3.setPreserveRatio(true);
        imageView3.setFitHeight(30);
        HBox hBox3=new HBox(imageView3);
        xs.setGraphic(hBox3);
        TreeItem<String>xj=new TreeItem<>("学籍管理");
        Image image4=new Image("file:src/main/resources/SuCai/学籍管理.png");
        ImageView imageView4=new ImageView(image4);
        imageView4.setPreserveRatio(true);
        imageView4.setFitHeight(30);
        HBox hBox4=new HBox(imageView4);
        xj.setGraphic(hBox4);
        TreeItem<String>jl=new TreeItem<>("奖励管理");
        Image image5=new Image("file:src/main/resources/SuCai/奖励管理.png");
        ImageView imageView5=new ImageView(image5);
        imageView5.setPreserveRatio(true);
        imageView5.setFitHeight(30);
        HBox hBox5=new HBox(imageView5);
        jl.setGraphic(hBox5);
        TreeItem<String>cf=new TreeItem<>("处罚管理");
        Image image6=new Image("file:src/main/resources/SuCai/处罚管理.png");
        ImageView imageView6=new ImageView(image6);
        imageView6.setPreserveRatio(true);
        imageView6.setFitHeight(30);
        HBox hBox6=new HBox(imageView6);
        cf.setGraphic(hBox6);
        Image image27=new Image("file:src/main/resources/SuCai/导出.png");
        ImageView imageView27=new ImageView(image27);
        imageView27.setPreserveRatio(true);
        imageView27.setFitHeight(40);
        HBox hBox27=new HBox(imageView27);
        DaoChu.setGraphic(hBox27);
        tv.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<String>> observableValue, TreeItem<String> stringTreeItem, TreeItem<String> t1) {
                System.out.println(observableValue.getValue().getValue());
                String choices=observableValue.getValue().getValue();
                if(choices.equals("院系管理"))
                {
                    choice=1;
                }
                else if(choices.equals("班级管理"))
                {
                    choice=2;
                }
                else if(choices.equals("学生管理")) {
                    choice = 3;
                }
                else if(choices.equals("学籍管理"))
                {
                    choice=4;
                }
                else if(choices.equals("奖励管理"))
                {
                    choice=5;
                }
                else if(choices.equals("处罚管理"))
                {
                    choice=6;
                }
                else if(choices.equals("用户管理"))
                {
                    choice=7;
                }
                try {
                    s1(choice);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        t1.getChildren().addAll(yx,bj,xs,xj,jl,cf);
        if(QX_level>=3)
        {
            TreeItem<String>yh=new TreeItem<>("用户管理");
            Image image17=new Image("file:src/main/resources/SuCai/用户管理.png");
            ImageView imageView17=new ImageView(image17);
            imageView17.setPreserveRatio(true);
            imageView17.setFitHeight(30);
            HBox hBox17=new HBox(imageView17);
            yh.setGraphic(hBox17);
            t1.getChildren().add(yh);
        }
        t1.setExpanded(true);
        data = FXCollections.observableArrayList();
        String SQL="select * from students";
        System.out.println("hellosb");
        rs=conn.createStatement().executeQuery(SQL);
        int flag=0;
        for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++) {
            //Iterate Column
            System.out.print(i + " ");
            System.out.println(rs.getMetaData().getColumnName(i));
            if (flag == 0) {

                TableColumn tableColumn = new TableColumn(rs.getMetaData().getColumnName(i));
                int finalI = i-1;
                tableColumn.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(finalI).toString());
                    }
                });
                StudentTable.getColumns().add(tableColumn);
            }
        }
        TableColumn caozuo=new TableColumn("操作");

        while(rs.next()){
            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();
            for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                //Iterate Column

                row.add(rs.getString(i));
            }

            System.out.println("Row [1] added "+row );
            data.add(row);

        }

        //FINALLY ADDED TO TableView
        StudentTable.setItems(data);
    }
    int cnt=0;
    int cnt2=0;
    @FXML
    Button DaoChu;
    @FXML
    void OnDaoChu(ActionEvent event) throws SQLException, IOException {
        String Cun;
        FileChooser fileChooser=new FileChooser();
        Stage stage=new Stage();
        switch (choice)
        {
            case 1:Cun="department";break;
            case 2:Cun="class";break;
            case 3:Cun="students";break;
            case 4:Cun="changement";break;
            case 5:Cun="reward";break;
            case 6:Cun="punishment";break;
            case 7:Cun="Users";break;
            default:Cun="students";break;
        }
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialFileName(Cun+".csv");
        File selectedFile = fileChooser.showSaveDialog(stage);
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        String SQL="select * from "+Cun;
        ResultSet rs=conn.createStatement().executeQuery(SQL);
        ObservableList<String> srow = FXCollections.observableArrayList();
        for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++) {
            srow.add(rs.getMetaData().getColumnName(i));
        }
        FileUtil.savebooks(srow,selectedFile.getPath());
        while(rs.next()){
            ObservableList<String> row = FXCollections.observableArrayList();
            for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                //Iterate Column
                row.add(rs.getString(i));

            }
            FileUtil.savebooks(row,selectedFile.getPath());

        }
    }
    public void s1(int choice) throws SQLException {
            cnt=0;
            cnt2 = 0;
            System.out.println(choice);
            StudentTable.getColumns().clear();
            StudentTable.getItems().clear();
            String Cun;

            String s;
            String s2;
            switch (choice)
            {
                case 1:Cun="department";s="编号";s2="全称";break;
                case 2:Cun="class";s="编号";s2="全称";break;
                case 3:Cun="students";s="学号";s2="姓名";break;
                case 4:Cun="changement";s="记录号";s2="学号";break;
                case 5:Cun="reward";s="记录号";s2="学号";break;
                case 6:Cun="punishment";s="记录号";s2="学号";break;
                case 7:Cun="Users";s="用户名";s2="电子邮箱";break;
                default:Cun="students";s="学号";s2="姓名";break;
            }
            ZhuLabel.setText(s);
            CiLabel.setText(s2);
            Image image=new Image("file:src/main/resources/SuCai/"+s+".png");

        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        String SQL="select * from "+Cun;
        System.out.println("hellosb");
        ResultSet rs=conn.createStatement().executeQuery(SQL);
        int flag=0;
        for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++) {
            //Iterate Column
            System.out.print(i + " ");
            System.out.println(rs.getMetaData().getColumnName(i));
                TableColumn tableColumn = new TableColumn(rs.getMetaData().getColumnName(i));
                //tableColumn.setStyle("-fx-background-color: rgb(41,47,76);-fx-text-fill:white;");
                //tableColumn.setStyle("-fx-text-fill:red");
                int finalI = i-1;
                tableColumn.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(finalI).toString());
                    }
                });
                StudentTable.getColumns().add(tableColumn);

        }
        /*TableColumn tableColumn=new TableColumn("操作");
        int finalCnt = cnt;
        tableColumn.setCellFactory(new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn tableColumn) {
                TableCell tableCell=new TableCell(){
                    @Override
                    protected void updateItem(Object o, boolean b) {
                        super.updateItem(o, b);
                        System.out.println(cnt2+" "+cnt);
                        if(cnt2<cnt) {
                            HBox hBox = new HBox(new Button("增加"));
                            this.setGraphic(hBox);

                        }
                        cnt2++;


                    }
                };
                return tableCell;
            }



        });


        StudentTable.getColumns().add(tableColumn);*/
        while(rs.next()){
            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();
            for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                //Iterate Column

                row.add(rs.getString(i));
            }
        cnt++;
            //System.out.println("Row [1] added "+row );
           row.add("1");
            data.add(row);

        }

        //FINALLY ADDED TO TableView
        StudentTable.setItems(data);

    }
    public StudentController(Connection conn, Statement stat,String user) {
        this.conn = conn;
        this.stat = stat;
        this.user=user;
    }
}

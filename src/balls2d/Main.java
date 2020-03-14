package balls2d;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.text.Text;

public class Main extends Application{
    
    public static void main(String[] args) {
        
        Application.launch(args);
    }
    
    @Override
    public void start(Stage stage) {
         
        Text text = new Text("Welcome in \"Moving balls in 2D\"!");
        text.setLayoutY(80);
        text.setLayoutX(100);
         
        Group group = new Group(text);
         
        Scene scene = new Scene(group);
        stage.setScene(scene);
        stage.setTitle("First Application");
        stage.setWidth(300);
        stage.setHeight(250);
        stage.show();
    }
}
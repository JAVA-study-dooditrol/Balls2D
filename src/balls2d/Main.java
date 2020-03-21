package balls2d;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

public class Main extends Application{
    
    public static void main(String[] args) {
        
        System.out.println("Launching Application");
        Application.launch(args);
    }
     
    @Override
    public void init() throws Exception {
         
        System.out.println("Application inits");
        super.init();
    }
    
    @Override
    public void start(Stage stage) throws Exception {
         
        System.out.println("Application starts");
        
        Parent root = FXMLLoader.load(getClass().getResource("..\\res\\Main.fxml"));
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        
        stage.setTitle("Moving balls in 2D");
        stage.show();
    }
    
    @Override
    public void stop() throws Exception {
         
        System.out.println("Application stops");
        super.stop();
    }
}
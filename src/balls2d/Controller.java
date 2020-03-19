package balls2d;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.ArrayList;

public class Controller implements Initializable{
    
    private GraphicsContext gc;
    private ArrayList<Ball> balls;
    private Ball centerPoint;
    private AnimationTimer timer;
    
    @FXML private Canvas img;
    @FXML private Button btn;
    @FXML private HBox hbox;
    @FXML private Label label;
    @FXML private VBox root;
    
    @FXML
    private void start(ActionEvent event) {
           
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        centerPoint = new Ball(new Point2D(img.getWidth() / 2, img.getHeight() / 2), Color.GREY);
        
        Ball.setRadius(20);
        Ball.setFriction(0.05);
        balls = new ArrayList<Ball>();
        gc = img.getGraphicsContext2D();
        
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                onUpdate();
            }
        };
        timer.start();
    }
    
    @FXML private void actionOnCanvas(MouseEvent event) {
        
        MouseButton button = event.getButton();
        Point2D mousePosition = new Point2D(event.getX(), event.getY());
        
        if (button == MouseButton.PRIMARY) {  
            for (Ball iter : balls) {
                
                if (iter.pointInBall(mousePosition)) {
                    
                    if(iter.getSelected()) {
                        iter.setSelect(false);
                    }
                    else {
                        Point2D velocity = centerPoint.getCoords().subtract(iter.getCoords());
                        velocity = velocity.normalize().multiply(0.8);
                        
                        iter.setVelocity(velocity);
                        iter.setSelect(true);
                    }
                    return;
                }
            }
            
            Ball newBall = new Ball(mousePosition);
            balls.add(newBall);
            correctingCollision();
        }
        else if (button == MouseButton.SECONDARY) {
            
            centerPoint.setCoords(mousePosition);
            
            for (Ball iter :balls) {
                
                if (iter.getSelected()) {
                    Point2D velocity = iter.getCoords().subtract(centerPoint.getCoords());
                    velocity = velocity.normalize().multiply(0.8);
                    iter.setVelocity(velocity);
                    iter.setSelect(true);
                }
            }
        }
    }
    
    private void correctingCollision() {
        
        boolean collision = (balls.size() == 1) ? false : true;
        
        while (collision) {
            
            collision = false;
            for (Ball iter1 : balls) {
                for (Ball iter2 : balls) {
                    
                    if (Ball.collision(iter1, iter2) && !iter1.equals(iter2)) {
                        System.out.println("collision");
                        collision = true;
                        Ball.collisionInAdding(iter1, iter2);
                    }
                }
            }
        }
    }
    
    private void onUpdate() {
        
        for (Ball iter : balls) {
            
            iter.update();
            if (Ball.collision(iter, centerPoint)) {
                iter.setVelocity(new Point2D(0, 0));
                iter.setSelect(false);
            }
        }
        correctingCollision();
        
        gc.clearRect(0, 0, img.getWidth(), img.getHeight());
        
        for (Ball item : balls) {
            
            item.paint(gc);
        }
        
        centerPoint.paint(gc);
    }
}

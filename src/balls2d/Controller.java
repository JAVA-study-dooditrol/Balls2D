package balls2d;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Slider;
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
    private double attractionVelocityValue;
    
    private AnimationTimer timer;
    
    @FXML private Canvas img;
    @FXML private ToggleButton button1;
    @FXML private Slider slider;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        attractionVelocityValue = 1;
        centerPoint = new Ball(new Point2D(img.getWidth() / 2, img.getHeight() / 2), Color.GREY);
        balls = new ArrayList<Ball>();
        
        gc = img.getGraphicsContext2D();
        
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                onUpdate();
            }
        };
        timer.start();
    }
    
    @FXML
    private void clickButton1(ActionEvent event) {
        
        if (button1.isSelected()) {
            for (Ball iter :balls) {
                
                Point2D velocity = centerPoint.getCoords().subtract(iter.getCoords());
                velocity = velocity.normalize().multiply(attractionVelocityValue);
                iter.setVelocity(velocity);
                iter.setSelect(true);
            }
            button1.setText("Unselect balls");
        }
        else {
            for (Ball iter :balls) {
                iter.setSelect(false);
            }
            button1.setText("Select all balls");
        }
    }
    
    @FXML
    private void clickButton2(ActionEvent event) {
        
        attractionVelocityValue = slider.getValue();
    }
    
    @FXML 
    private void actionOnCanvas(MouseEvent event) {
        
        MouseButton button = event.getButton();
        Point2D mousePosition = new Point2D(event.getX(), event.getY());
        
        if (button == MouseButton.PRIMARY)
            leftClickAction(mousePosition);
        else if (button == MouseButton.SECONDARY)
            rightClickAction(mousePosition);
    }
    
    private void leftClickAction(Point2D mousePosition) {
        
        for (Ball iter : balls) {
            
            if (iter.pointInBall(mousePosition)) {
                
                if(iter.isSelected()) {
                    iter.setSelect(false);
                }
                else {
                    Point2D velocity = centerPoint.getCoords().subtract(iter.getCoords());
                    velocity = velocity.normalize().multiply(attractionVelocityValue);   
                    iter.setVelocity(velocity);
                    iter.setSelect(true);
                }
                return;
            }
        }
        
        Ball newBall = new Ball(mousePosition);
        balls.add(newBall);
        
        int size = balls.size();
        boolean collision = (size == 1) ? false : true;
        
        while (collision) {
            
            collision = false;
            for (int i = 0 ; i < size; ++i) {
                for (int j = i + 1; j < size; ++j) {
                    
                    if (Ball.collision(balls.get(i), balls.get(j))) {
                        collision = true;
                        Ball.collisionInAdding(balls.get(i), balls.get(j));
                    }
                }
            }
        }
    }
    
    private void rightClickAction(Point2D mousePosition) {
        
        centerPoint.setCoords(mousePosition);
        
        for (Ball iter :balls) {
            
            if (iter.isSelected()) {
                Point2D velocity = centerPoint.getCoords().subtract(iter.getCoords());
                velocity = velocity.normalize().multiply(attractionVelocityValue);
                iter.setVelocity(velocity);
                iter.setSelect(true);
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
        
        int size = balls.size();
        for (int i = 0 ; i < size; ++i) {
            for (int j = i + 1; j < size; ++j) {
                
                if (Ball.collision(balls.get(i), balls.get(j))) {
                    Ball.collisionUpdate(balls.get(i), balls.get(j));
                }
            }
        }        
        
        for (Ball iter :balls) {
            
            if (iter.isSelected()) {
                Point2D velocity = centerPoint.getCoords().subtract(iter.getCoords());
                velocity = velocity.normalize().multiply(attractionVelocityValue);
                iter.setVelocity(velocity);
                iter.setSelect(true);
            }
        }
        
        draw();
    }
    
    private void draw() {
        
        gc.clearRect(0, 0, img.getWidth(), img.getHeight());
        
        for (Ball item : balls) {
            
            item.paint(gc);
        }
        
        centerPoint.paint(gc);
    }
}

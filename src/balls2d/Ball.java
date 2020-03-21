package balls2d;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.geometry.Point2D;

public class Ball {
    
    private static double radius;
    private static double friction;
    private static double recoveryRatio;
    
    private Point2D coords;
    private Point2D velocity;
    private boolean selected;
    private Color color;
    
    static {
        radius = 20;
        friction = 0.03;
        recoveryRatio = 1;
    }
    
    {
        coords = new Point2D(0, 0);
        velocity = new Point2D(0, 0);
        selected = false;
        color = Color.GREEN;
    }
    
    Ball () {
        
    }
    
    Ball (Point2D coords) {
        
        this.coords = coords;
    }
 
    Ball (Point2D coords, Color color) {
        
        this(coords);
        this.color = color;
    }
    
    public void setCoords(Point2D coords) {
        
        this.coords = coords;
    }
    
    public Point2D getCoords() {
        
        return coords;
    }
    
    public void setVelocity(Point2D velocity) {
        
        this.velocity = velocity;
    }
    
    public Point2D getVelocity() {
        
        return velocity;
    }
    
    public void setSelect (boolean select) {
        
        this.selected = select;
    }

    public boolean isSelected() {
        
        return selected;
    }
    
    public void paint(GraphicsContext gc) {
        
        gc.beginPath();
        gc.arc(coords.getX(), coords.getY(), radius, radius, 0, 360);
        if (selected)
            gc.setFill(Color.RED);
        else
            gc.setFill(color);
        gc.fill();
    }
    
    public boolean pointInBall(Point2D point) {
        
        if (Math.pow(point.getX() - coords.getX(), 2) +
                Math.pow(point.getY() - coords.getY(),2) <= Math.pow(radius, 2))
            return true;
        else
            return false;
    }
    
    public void update() {
        
        if (!selected) {
            
            Point2D decelerate = velocity.normalize().multiply(friction);
            velocity = velocity.subtract(decelerate);
        }
        coords = coords.add(velocity);
    }
    
    public static double getRadius() {
        
        return radius;
    }
    
    public static void setRadius(double newRadius) {
        
        radius = newRadius;
    }

    public static double getFriction() {
        
        return friction;
    }
     
    public static void setFriction(double newFriction) {
        
        friction = newFriction;
    }
    
    public static boolean collision(Ball a, Ball b) {
        
        if (2 * radius < a.getCoords().distance(b.getCoords())) 
            return false;
        else
            return true;
    }
    
    public static void collisionInAdding(Ball a, Ball b) {
        
        double distance = a.getCoords().distance(b.getCoords());
        
        if (distance == 0) {        
            a.setCoords(a.getCoords().add(new Point2D(0 , -distance / 2)));
            b.setCoords(b.getCoords().add(new Point2D(0 , distance / 2)));
        }
        else {            
            double offsetDistance = (2 * radius - distance) / 2 + 1;
            Point2D offsetA = a.getCoords().subtract(b.getCoords()).normalize().multiply(offsetDistance);
            a.setCoords(a.getCoords().add(offsetA));
            b.setCoords(b.getCoords().subtract(offsetA));
        }
    }

    public static void collisionUpdate(Ball a, Ball b) {
        
        Point2D v1 = a.getVelocity();
        Point2D v2 = b.getVelocity();
        Point2D interactionVector = a.getCoords().subtract(b.getCoords()).normalize();
        double v1p = v1.dotProduct(interactionVector);
        double v2p = v2.dotProduct(interactionVector);
        
        double newV1p = ((1 - recoveryRatio) * v1p + (1 + recoveryRatio) * v2p) / 2;
        double newV2p = ((1 - recoveryRatio) * v2p + (1 + recoveryRatio) * v1p) / 2;
        
        a.setVelocity(v1.add(interactionVector.multiply(newV1p - v1p)));
        b.setVelocity(v2.add(interactionVector.multiply(newV2p - v2p)));

        double distance = a.getCoords().distance(b.getCoords());
        double offsetDistance = (2 * radius - distance) / 2;
        Point2D offsetA = a.getCoords().subtract(b.getCoords()).normalize().multiply(offsetDistance);
        a.setCoords(a.getCoords().add(offsetA));
        b.setCoords(b.getCoords().subtract(offsetA));
    }
}

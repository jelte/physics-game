package platformer.PhysicsEngine;

import platformer.GameEngine.AbstractComponent;
import platformer.GameEngine.Behaviours.Drawable;
import platformer.GameEngine.Camera;
import platformer.GameEngine.Vector2D;

import java.awt.Color;
import java.awt.Graphics2D;

import static platformer.Game.DEBUG;

public class ElasticConnector extends AbstractComponent implements Drawable
{
    private final Body body1, body2;
    private final double naturalLength;
    private final double springConstant;
    private final double motionDampingConstant;
    private final boolean canGoSlack;
    private final Double hookesLawTruncation;

    public ElasticConnector(Body p1, Body p2, double naturalLength, double springConstant, double motionDampingConstant,
                            boolean canGoSlack, Double hookesLawTruncation) {
        this.body1 = p1;
        this.body2 = p2;
        this.naturalLength = naturalLength;
        this.springConstant = springConstant;
        this.motionDampingConstant=motionDampingConstant;
        this.canGoSlack = canGoSlack;
        this.hookesLawTruncation=hookesLawTruncation;
    }

    public double calculateTension() {
        // implementation of truncated hooke's law
        double dist= body1.getPosition().minus(body2.getPosition()).mag();
        if (dist<naturalLength && canGoSlack) return 0;

        double extensionRatio = (dist-naturalLength)/naturalLength;
        Double truncationLimit=this.hookesLawTruncation;// this stops Hooke's law giving too high a force which might cause instability in the numerical integrator
        if (truncationLimit!=null && extensionRatio>truncationLimit)
            extensionRatio=truncationLimit;
        if (truncationLimit!=null && extensionRatio<-truncationLimit)
            extensionRatio=-truncationLimit;
        double tensionDueToHookesLaw = extensionRatio*springConstant;
        double tensionDueToMotionDamping=motionDampingConstant*rateOfChangeOfExtension();
        return tensionDueToHookesLaw+tensionDueToMotionDamping;
    }

    public double rateOfChangeOfExtension() {
        Vector2D v12= body2.getPosition().minus(body1.getPosition()); // goes from p1 to p2
        v12=v12.normalise(); // make it a unit vector.
        Vector2D relativeVeloicty=body2.getVelocity().minus(body1.getVelocity()); // goes from p1 to p2
        return relativeVeloicty.scalarProduct(v12);// if this is positive then it means the
        // connector is getting longer
    }

    public void applyTensionForceToBothParticles() {
        double tension=calculateTension();
        Vector2D p12= body2.getPosition().minus(body1.getPosition()); // goes from p1 to p2
        p12=p12.normalise(); // make it a unit vector.
        Vector2D forceOnP1=p12.mult(tension);
        body1.applyForce(forceOnP1);

        Vector2D forceOnP2=p12.mult(-tension);// tension on second particle acts in opposite direction (an example of Newton's 3rd Law)
        body2.applyForce(forceOnP2);
    }

    public void draw(Graphics2D g, Camera camera)
    {
        if (DEBUG) { return; }

        g.setColor(Color.RED);
        g.drawLine(
            camera.convertWorldXtoScreenX(body1.getPosition().X()),
            camera.convertWorldYtoScreenY(body1.getPosition().Y()),
            camera.convertWorldXtoScreenX(body2.getPosition().X()),
            camera.convertWorldYtoScreenY(body2.getPosition().Y())
        );
    }

    @Override
    public void setColor(Color color) {

    }

    public boolean connects(Body b1, Body b2) {
        return (body1 == b1 && body2 == b2) || (body1 == b2 && body2 == b1);
    }
}

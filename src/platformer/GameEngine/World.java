package platformer.GameEngine;

import platformer.PhysicsEngine.Body;
import platformer.PhysicsEngine.Collider;
import platformer.PhysicsEngine.Physics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class World
{
    private final HashMap<Integer, Layer> layers = new HashMap<>();
    private final Physics physics = new Physics();

    public void add(GameObject gameObject)
    {
        this.add(gameObject, 0);
    }

    public void add(GameObject gameObject, int layer)
    {
        if (!layers.containsKey(layer)) {
            layers.put(layer, new Layer());
        }
        layers.get(layer).add(gameObject);
        if (layer == 0) {
            Body body = (Body) gameObject.getComponent(Body.class);
            if (body != null) {
                physics.register(body);
            } else {
                for (Component collider : gameObject.getComponentsInChildren(Collider.class)) {
                    physics.register((Collider) collider);
                }
            }
        }
    }

    public List<GameObject> getGameObjects()
    {
        List<GameObject> gameObjects = new ArrayList<>();
        for (Layer layer : layers.values()) {
            gameObjects.addAll(layer.getGameObjects());
        }
        return gameObjects;
    }

    public Physics getPhysics() {
        return physics;
    }
}

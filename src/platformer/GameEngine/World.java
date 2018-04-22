package platformer.GameEngine;

import platformer.Breakout.Ball;
import platformer.Breakout.Bar;
import platformer.Game;
import platformer.PhysicsEngine.Body;
import platformer.PhysicsEngine.Collider;
import platformer.PhysicsEngine.Physics;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class World
{
    private final HashMap<Integer, Layer> layers = new HashMap<>();
    private final Physics physics = new Physics();
    private List<Component> keyListeners = new ArrayList<>();

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

        keyListeners.addAll(gameObject.getComponentsInChildren(KeyListener.class));

        gameObject.setWorld(this);
    }

    public List<GameObject> getGameObjects()
    {
        List<GameObject> gameObjects = new ArrayList<>();
        for (Layer layer : layers.values()) {
            gameObjects.addAll(layer.getGameObjects());
        }
        return gameObjects;
    }

    public void update() {
        Time.update();
        for (Layer layer : layers.values()) {
            for (GameObject gameObject : layer.getGameObjects()) {
                gameObject.update();
            }
        }
    }
    public Physics getPhysics() {
        return physics;
    }

    public void remove(GameObject gameObject) {
        for (Layer layer : layers.values()) {
            if (layer.has(gameObject)) {
                layer.remove(gameObject);
                for (Component c : gameObject.getComponentsInChildren(Collider.class)) {
                    physics.unregister((Collider) c);
                }
                for (Component c : gameObject.getComponentsInChildren(Body.class)) {
                    physics.unregister((Body) c);
                }
            }
        }
    }

    public void onKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_F5) {
            Game.DEBUG = !Game.DEBUG;
        }
        for (Component c : keyListeners) {
            ((KeyListener) c).onKeyPressed(e);
        }
    }

    public void onKeyReleased(KeyEvent e) {
        for (Component c : keyListeners) {
            ((KeyListener) c).onKeyReleased(e);
        }
    }

    public GameObject getGameObject(Class<? extends GameObject> objectClass) {
        for (GameObject gameObject : getGameObjects()) {
            if (objectClass.isInstance(gameObject)) {
                return gameObject;
            }
        }
        return null;
    }

    public boolean hasGameObject(Class<? extends GameObject> objectClass) {
        return getGameObject(objectClass) != null;
    }
}

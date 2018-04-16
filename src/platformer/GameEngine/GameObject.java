package platformer.GameEngine;

import platformer.Breakout.Ball;
import platformer.PhysicsEngine.Collider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GameObject
{
    private World world;
    private GameObject parent;
    private List<GameObject> children = new ArrayList<>();
    private List<Component> components = new ArrayList<>();
    private Transform transform;

    public GameObject() {
        this(new Vector2D());
    }
    public GameObject(Vector2D position)
    {
        this.transform = new Transform(position);
    }

    public void add(GameObject child)
    {
        children.add(child);
        child.setParent(this);
    }

    public void remove(GameObject child) {
        children.remove(child);
        child.setParent(null);
    }

    public Component addComponent(Component component)
    {
        component.setGameObject(this);
        components.add(component);
        return component;
    }

    public void removeComponent(Class<? extends Component> componentClass) {
        components.remove(getComponent(componentClass));
    }

    public void removeComponents(Class<? extends Component> componentClass) {
        components.removeAll(getComponents(componentClass));
    }

    public boolean hasComponent(Class<? extends Component> componentClass)
    {
        return getComponent(componentClass) != null;
    }

    public Component getComponent(Class<? extends Component> behaviourClass)
    {
        for (Component component : components) {
            if (behaviourClass.isInstance(component)) {
                return component;
            }
        }
        return null;
    }

    public Vector2D getPosition()
    {
        return parent != null ? parent.getPosition().add(transform.getPosition()) : transform.getPosition();
    }

    public double getLocalRotation()
    {
        return transform.getLocalRotation();
    }

    public double getRotation()
    {
        return transform.getRotation() + (parent != null ? parent.getRotation() : 0);
    }

    public List<Component> getComponentsInChildren(Class<? extends Component> componentClass)
    {
        List<Component> components = new ArrayList<>();
        for (GameObject child : children) {
            if (child.hasComponent(componentClass)) {
                components.addAll(child.getComponents(componentClass));
            }
        }
        if (this.hasComponent(componentClass)) {
            components.addAll(getComponents(componentClass));
        }
        return components;
    }

    public Collection<? extends Component> getComponents(Class<? extends Component> componentClass)
    {
        List<Component> components = new ArrayList<>();
        for (Component component : this.components) {
            if (componentClass.isInstance(component)) {
                components.add(component);
            }
        }
        return components;
    }

    private void setParent(GameObject parent)
    {
        this.parent = parent;
    }

    public void rotate(double v) {
        transform.rotate(v);
    }

    public void setPosition(Vector2D position) {
        transform.setPosition(position);
    }

    public void setRotation(double rotation) {
        transform.setRotation(rotation);
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void destroy()
    {
        world.remove(this);
    }

    public void update() {}

    public GameObject findChildByType(Class<? extends GameObject> gameObjectClass) {
        for (GameObject gameObject : children) {
            if (gameObjectClass.isInstance(gameObject)) {
                return gameObject;
            }
        }
        return null;
    }

}

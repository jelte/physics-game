package platformer.GameEngine;

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
    private Vector2D position;
    private double rotation = 0.0;

    public GameObject() {
        this(new Vector2D());
    }
    public GameObject(Vector2D position)
    {
        this.position = position;
    }

    public void add(GameObject child)
    {
        children.add(child);
        child.setParent(this);
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

    public Vector2D getLocalPosition()
    {
        return position;
    }

    public Vector2D getPosition()
    {
        return parent != null ? parent.getPosition().add(position) : position;
    }

    public double getLocalRotation()
    {
        return this.rotation;
    }

    public double getRotation()
    {
        return rotation + (parent != null ? parent.getRotation() : 0);
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

    public GameObject getParent() {
        return parent;
    }

    public void rotate(double v) {
        this.rotation += v;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void destroy()
    {
        world.remove(this);
    }

    public void update() {}

}

package platformer.GameEngine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GameObject
{
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
        this.children.add(child);
        child.setParent(this);
    }

    public void addComponent(Component component)
    {
        this.components.add(component);
    }

    public boolean hasComponent(Class<?> componentClass)
    {
        return this.getComponent(componentClass) != null;
    }

    public Component getComponent(Class<?> behaviourClass)
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

    public List<Component> getComponentsInChildren(Class<?> componentClass)
    {
        List<Component> components = new ArrayList<>();
        if (this.hasComponent(componentClass)) {
            components.addAll(getComponents(componentClass));
        }
        for (GameObject child : children) {
            if (child.hasComponent(componentClass)) {
                components.addAll(child.getComponents(componentClass));
            }
        }
        return components;
    }

    public Collection<? extends Component> getComponents(Class<?> componentClass)
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
}

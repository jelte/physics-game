package platformer.GameEngine;

public class AbstractComponent implements Component
{
    protected GameObject gameObject;

    public void setGameObject(GameObject gameObject)
    {
        if (this.gameObject != null) {
            throw new IllegalArgumentException("Component already attached to a gameObject");
        }
        this.gameObject = gameObject;
    }

    public GameObject getGameObject() {
        return gameObject;
    }
}

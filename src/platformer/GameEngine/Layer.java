package platformer.GameEngine;

import java.util.ArrayList;
import java.util.List;

public class Layer {
    private List<GameObject> gameObjects = new ArrayList<>();

    public void add(GameObject gameObject)
    {
        gameObjects.add(gameObject);
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }
}

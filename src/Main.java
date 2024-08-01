import com.shir0dev.voxelframework.core.display.DisplayManager;
import com.shir0dev.voxelframework.core.display.GLHandler;
import com.shir0dev.voxelframework.core.display.debug.DebugScreen;
import com.shir0dev.voxelframework.core.display.gui.text.GUIManager;
import com.shir0dev.voxelframework.core.display.mesh.Mesh;
import com.shir0dev.voxelframework.core.display.mesh.OBJLoader;
import com.shir0dev.voxelframework.core.display.mesh.StaticMesh;
import com.shir0dev.voxelframework.core.display.render.Camera;
import com.shir0dev.voxelframework.core.display.render.Material;
import com.shir0dev.voxelframework.core.display.render.Renderer;
import com.shir0dev.voxelframework.core.display.shader.shaders.SimpleUnlit;
import com.shir0dev.voxelframework.core.input.CameraController;
import com.shir0dev.voxelframework.core.input.Input;
import com.shir0dev.voxelframework.core.transform.Transform;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

public class Main {
    private GLHandler glHandler;
    private CameraController cameraController;
    private StaticMesh cubeMesh;
    private Transform[] cubes;
    private Renderer[] renderers;
    private Material material;

    public Main() {
    }

    public void run() {
        this.init();
        this.loop();
        this.cleanUp();
    }

    private void init() {
        DisplayManager.create();
        this.glHandler = new GLHandler();
        this.cameraController = new CameraController();
        this.initWorld();
        this.initGUI();
    }

    private void initGUI() {
        GUIManager.init(this.glHandler);
        DebugScreen.init(this.glHandler);
    }

    private void initWorld() {
        this.cubeMesh = OBJLoader.loadObjModel("cube", this.glHandler);
        this.material = new Material(new SimpleUnlit(), this.glHandler.loadTexture("res/texture/image.png"));
        this.cubes = new Transform[]{new Transform(), new Transform(), new Transform(), new Transform()};
        this.cubes[0].setPosition(new Vector3f(5.0F, 0.0F, 0.0F));
        this.cubes[1].setPosition(new Vector3f(-5.0F, 0.0F, 0.0F));
        this.cubes[2].setPosition(new Vector3f(0.0F, 0.0F, 5.0F));
        this.cubes[3].setPosition(new Vector3f(0.0F, 0.0F, -5.0F));
        this.renderers = new Renderer[4];

        for(int i = 0; i < 4; ++i) {
            this.renderers[i] = new Renderer(this.cubes[i], this.cubeMesh, this.material);
        }

    }

    private void loop() {
        while(!Display.isCloseRequested()) {
            Input.poll();
            cameraController.update();
            Camera.update();
            DisplayManager.prepare();

            for (Renderer renderer : renderers) {
                renderer.render();
            }
            DebugScreen.render();
            DisplayManager.update();
            Input.lateUpdate();
        }

    }

    public void cleanUp() {
        cubeMesh.dispose();
    }

    public static void main() {
        (new Main()).run();
    }
}

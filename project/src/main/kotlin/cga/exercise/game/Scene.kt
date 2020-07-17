package cga.exercise.game

import cga.exercise.components.camera.ProjectCamera
import cga.exercise.components.camera.TronCamera
import cga.exercise.components.geometry.*
import cga.exercise.components.light.PointLight
import cga.exercise.components.light.SpotLight
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import cga.framework.*
import org.joml.Vector3f
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL30
import org.joml.Math
import org.joml.Vector2f
import org.joml.Vector4f
import org.lwjgl.glfw.GLFW.*
import java.awt.MouseInfo
import java.lang.Math.sin
import kotlin.math.sin


/**
 * Created by Fabian on 16.09.2017.
 */
class Scene(private val window: GameWindow) {
    private val staticShader: ShaderProgram
    val cycle:Renderable
    val cam: ProjectCamera
    //var pointLight: PointLight
    var spotLight: SpotLight
    val sonne : PointLight
    val loadedObjectGround : loadedObject
    val baum_01 : Renderable
    val spinne : Renderable

    var old_mouse_pos_x : Double
    var old_mouse_pos_y : Double


    //scene setup
    init {
        // Shader initialisieren
        staticShader = ShaderProgram("assets/shaders/tron_vert.glsl", "assets/shaders/tron_frag.glsl")

        glEnable(GL_CULL_FACE)
        glFrontFace(GL_CCW)
        glCullFace(GL_BACK)
        //initial opengl state
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f); GLError.checkThrow()
        glDisable(GL_CULL_FACE); GLError.checkThrow()
        glEnable(GL_DEPTH_TEST); GLError.checkThrow()
        glDepthFunc(GL_LESS); GLError.checkThrow()

        //Generate Cycle
        cycle = ModelLoader.loadModel("assets/Light Cycle/Light Cycle/HQ_Movie cycle.obj",  Math.toRadians(-90f), Math.toRadians(90f), 0f)?: throw IllegalArgumentException("Could not load the model")
        cycle.scaleLocal(Vector3f(0.8f, 0.8f, 0.8f))

        // Ground (Objekt + Texturen) einlesen
        loadedObjectGround = loadedObject("assets/models/ground.obj", "assets/textures/ground_diff.png", "assets/textures/ground_emit.png", "assets/textures/ground_spec.png")

        //Cam Setup
        cam= ProjectCamera(cycle)
        cam.rotateLocal(Math.toRadians(-20f),0f,0f)
        cam.translateLocal(Vector3f(0f,0f,4f))

        //licht und so
        //pointLight=PointLight(Vector3f(0f, 1f, 0f), Vector3f(1.0f, 0.5f, 0.1f), Vector3f(1f,1f,1f),cycle)

        spotLight= SpotLight(Vector3f(0f,1f,0f), Vector3f(0.5f, 0.05f, 0.01f),Vector3f(0.5f,0.5f,1f), cycle, 15f,20f)
        spotLight.rotateLocal(Math.toRadians(-20f),0f,0f)

        //Sonne
        sonne = PointLight(org.joml.Vector3f(0f, 9f, 0f), Vector3f(1.0f, 0.5f, 0.1f), Vector3f(1f,1f,0f), null)

        // Baum
        baum_01 = ModelLoader.loadModel("assets/complex objects/Tree/Tree.obj",  Math.toRadians(-90f), Math.toRadians(90f), 0f)?: throw IllegalArgumentException("Could not load the model")
        baum_01.translateGlobal(Vector3f(-2f, 0f, 0f))
        baum_01.rotateLocal(0f, 0f, Math.toRadians(-90f))

        // Spinne
        spinne = ModelLoader.loadModel("assets/complex objects/Spinne/Only_Spider_with_Animations_Export.obj",  Math.toRadians(-90f), Math.toRadians(90f), 0f)?: throw IllegalArgumentException("Could not load the model")
        spinne.translateGlobal(Vector3f(2f, 0f, 0f))
        spinne.scaleLocal(Vector3f(0.01f, 0.01f, 0.01f))
        spinne.rotateLocal(0f, 0f, Math.toRadians(-90f))

        // Maus
        old_mouse_pos_x = MouseInfo.getPointerInfo().location.getX()
        old_mouse_pos_y = MouseInfo.getPointerInfo().location.getY()
    }

    /* ***********************************************************
    ** Render Funktion                                          **
    ************************************************************ */

    fun render (dt: Float, t:Float)
    {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        staticShader.use()
        cam.bind(staticShader)
        //pointLight.bind(staticShader,"point")
        spotLight.bind(staticShader, "spot", cam.getCalculateViewMatrix())
        sonne.bind(staticShader, "point")
        staticShader.setUniform("colo", Vector3f(1f, 1f, 1f))
        loadedObjectGround.renderableObject.render(staticShader)
        cycle.render(staticShader)
        baum_01.render(staticShader)
        spinne.render(staticShader)
    }

    fun update(dt: Float, t: Float) {
        if (window.getKeyState(GLFW_KEY_W)) {
            cycle.translateLocal(Vector3f(0.0f, 0f, -5f*dt))
        }
        if (window.getKeyState(GLFW_KEY_S)) {
            cycle.translateLocal(Vector3f(0.0f, 0f, 5f*dt))
        }
        if (window.getKeyState(GLFW_KEY_A)&&(window.getKeyState(GLFW_KEY_W)||window.getKeyState(GLFW_KEY_S))) {
            cycle.rotateLocal(0f,Math.toRadians(20f*dt),0f)
        }
        if (window.getKeyState(GLFW_KEY_D)&&(window.getKeyState(GLFW_KEY_W)||window.getKeyState(GLFW_KEY_S))) {
            cycle.rotateLocal(0f,Math.toRadians(-20f*dt),0f)
        }
    }

    /* ***********************************************************
    ** Tastendruck                                              **
    ************************************************************ */

    fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {}

    fun onMouseMove(xpos: Double, ypos: Double)
    {
        cam.rotateAroundPoint(0.0f, (xpos-old_mouse_pos_x).toFloat()*0.002f,0f, Vector3f(0f))
        old_mouse_pos_x = xpos
    }

    fun cleanup() {}

}

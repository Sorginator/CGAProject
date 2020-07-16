package cga.exercise.game

import cga.exercise.components.camera.ProjectCamera
import org.joml.Math
import cga.exercise.components.camera.TronCamera
import cga.exercise.components.geometry.*
import cga.exercise.components.light.PointLight
import cga.exercise.components.light.SpotLight
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import cga.framework.*
import org.joml.Vector3f
import org.lwjgl.opengl.GL11.*
import org.lwjgl.glfw.GLFW.*


/**
 * Created by Fabian on 16.09.2017.
 */
class Scene(private val window: GameWindow) {
    private val staticShader: ShaderProgram
    val ground : loadedObject
    val cam : ProjectCamera

    //scene setup
    init {
        // Shader initialisieren
        staticShader = ShaderProgram("assets/shaders/project_vert.glsl", "assets/shaders/project_frag.glsl")

        glEnable(GL_CULL_FACE)
        glFrontFace(GL_CCW)
        glCullFace(GL_BACK)
        //initial opengl state
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f); GLError.checkThrow()
        glDisable(GL_CULL_FACE); GLError.checkThrow()
        glEnable(GL_DEPTH_TEST); GLError.checkThrow()
        glDepthFunc(GL_LESS); GLError.checkThrow()

        ground = loadedObject("assets/models/ground.obj", "assets/textures/ground_diff.png", "assets/textures/ground_emit.png", "assets/textures/ground_spec.png")

        //cam stuff

        cam= ProjectCamera(null)
        cam.rotateLocal(Math.toRadians(20f),0f,0f)
        cam.translateLocal(Vector3f(0f,0f,10f))
    }

    /* ***********************************************************
    ** Render Funktion                                          **
    ************************************************************ */

    fun render (dt: Float, t:Float)
    {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        staticShader.use()
    }

    fun update(dt: Float, t: Float) {
        if (window.getKeyState(GLFW_KEY_W)) {

        }
        if (window.getKeyState(GLFW_KEY_S)) {

        }
        if (window.getKeyState(GLFW_KEY_A)&&(window.getKeyState(GLFW_KEY_W)||window.getKeyState(GLFW_KEY_S))) {

        }
        if (window.getKeyState(GLFW_KEY_D)&&(window.getKeyState(GLFW_KEY_W)||window.getKeyState(GLFW_KEY_S))) {

        }
        if (window.getKeyState(GLFW_KEY_SPACE)) {

        } else {

        }

    }

    /* ***********************************************************
    ** Tastendruck                                              **
    ************************************************************ */

    fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {}

    fun onMouseMove(xpos: Double, ypos: Double) {}

    fun cleanup() {}

}

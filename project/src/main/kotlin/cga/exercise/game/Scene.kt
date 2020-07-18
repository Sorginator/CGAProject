package cga.exercise.game

import cga.exercise.components.`object`.Baum
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
    val baum_01 : Baum
    val spinne : Renderable
    val cat: Renderable
    val rose: Renderable
    val ente: Renderable
    val ente_w: Renderable
    val castle: Renderable
    val gras: Renderable
    val haus: Renderable
    val vogel: Renderable

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
        baum_01 = Baum(-2f, 0f, 0f)
        baum_01.animationSpeed = 0.5f

        // Spinne
        spinne = ModelLoader.loadModel("assets/complex objects/Spinne/Only_Spider_with_Animations_Export.obj",  Math.toRadians(-90f), Math.toRadians(90f), 0f)?: throw IllegalArgumentException("Could not load the model")
        spinne.translateGlobal(Vector3f(2f, 0f, 0f))
        spinne.scaleLocal(Vector3f(0.005f, 0.005f, 0.005f))
        spinne.rotateLocal(0f, 0f, Math.toRadians(-90f))

        // Katze
        cat = ModelLoader.loadModel("assets/complex objects/cat/12221_Cat_v1_l3.obj",  Math.toRadians(-90f), Math.toRadians(90f), 0f)?: throw IllegalArgumentException("Could not load the model")
        cat.translateGlobal(Vector3f(2f, 0f, 1f))
        cat.scaleLocal(Vector3f(0.01f, 0.01f, 0.01f))
        cat.rotateLocal(0f, Math.toRadians(-90f), 0f)

        // Rose
        rose = ModelLoader.loadModel("assets/complex objects/Rose/rose.obj",  Math.toRadians(-90f), Math.toRadians(90f), 0f)?: throw IllegalArgumentException("Could not load the model")
        rose.translateGlobal(Vector3f(2f, 0f, 2f))
        rose.scaleLocal(Vector3f(0.01f, 0.01f, 0.01f))
        rose.rotateLocal(0f, 0f, Math.toRadians(-90f))

        // Ente
        ente = ModelLoader.loadModel("assets/complex objects/Nagnag/12248_Bird_v1_L2.obj",  Math.toRadians(-90f), Math.toRadians(90f), 0f)?: throw IllegalArgumentException("Could not load the model")
        ente.translateGlobal(Vector3f(2f, 0f, 3f))
        ente.scaleLocal(Vector3f(0.01f, 0.01f, 0.01f))
        ente.rotateLocal(0f, Math.toRadians(-90f), 0f)

        // Ente Weiblich
        ente_w = ModelLoader.loadModel("assets/complex objects/Nagnag_w/12249_Bird_v1_L2.obj",  Math.toRadians(-90f), Math.toRadians(90f), 0f)?: throw IllegalArgumentException("Could not load the model")
        ente_w.translateGlobal(Vector3f(3f, 0f, 3f))
        ente_w.scaleLocal(Vector3f(0.01f, 0.01f, 0.01f))
        ente_w.rotateLocal(0f, Math.toRadians(-90f), 0f)

        // Castle
        castle = ModelLoader.loadModel("assets/complex objects/Castle/Castle OBJ.obj",  Math.toRadians(-90f), Math.toRadians(90f), 0f)?: throw IllegalArgumentException("Could not load the model")
        castle.translateGlobal(Vector3f(2f, 0f, -5f))
        castle.rotateLocal(0f, 0f, Math.toRadians(-90f))

        // Gras
        gras = ModelLoader.loadModel("assets/complex objects/gras/10450_Rectangular_Grass_Patch_v1_iterations-2.obj",  Math.toRadians(-90f), Math.toRadians(90f), 0f)?: throw IllegalArgumentException("Could not load the model")
        gras.translateGlobal(Vector3f(2f, 0f, 3f))
        gras.scaleLocal(Vector3f(0.01f, 0.01f, 0.01f))

        // Haus
        haus = ModelLoader.loadModel("assets/complex objects/Farmhouse/farmhouse_obj.obj",  Math.toRadians(-90f), Math.toRadians(90f), 0f)?: throw IllegalArgumentException("Could not load the model")
        haus.translateGlobal(Vector3f(2f, 0f, 10f))
        haus.scaleLocal(Vector3f(0.2f, 0.2f, 0.2f))
        haus.rotateLocal(0f, 0f, Math.toRadians(-90f))

        // Vogel
        vogel = ModelLoader.loadModel("assets/complex objects/Bird/12214_Bird_v1max_l3.obj",  Math.toRadians(-90f), Math.toRadians(90f), 0f)?: throw IllegalArgumentException("Could not load the model")
        vogel.translateGlobal(Vector3f(4f, 0f, 5f))
        vogel.scaleLocal(Vector3f(0.01f, 0.01f, 0.01f))

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
        baum_01.animate()
        cat.render(staticShader)
        rose.render(staticShader)
        ente.render(staticShader)
        ente_w.render(staticShader)
        gras.render(staticShader)
        //castle.render(staticShader)
        haus.render(staticShader)
        vogel.render(staticShader)
    }

    fun update(dt: Float, t: Float) {
        if (window.getKeyState(GLFW_KEY_W)) {
            cycle.translateLocal(Vector3f(0.0f, 0f, -5f*dt))
            //baum_01.startAnimation()
        }
        if (window.getKeyState(GLFW_KEY_S)) {
            cycle.translateLocal(Vector3f(0.0f, 0f, 5f*dt))
            //baum_01.resetAnimation()
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

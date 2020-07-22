package cga.exercise.game

import cga.exercise.components.`object`.Baum
import cga.exercise.components.`object`.GrasGround
import cga.exercise.components.`object`.texturedObject
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
    val spinne : texturedObject
    val cat: texturedObject
    val ente: texturedObject
    val ente_w: texturedObject
    val vogel: texturedObject
    val beagle: texturedObject
    val haus2: texturedObject
    val haus: texturedObject
    val castle: texturedObject

    val baum_01 : Baum
    val rose: Renderable
    //val ground: GrasGround
    val gras: texturedObject

    var old_mouse_pos_x : Double
    var old_mouse_pos_y : Double


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
        baum_01 = Baum(-2f, 0f, 0f, 0f, 0f, 0f, 0)
        baum_01.animationSpeed = 0.5f

        // Spinne
        spinne = texturedObject("assets/complex objects/Spinne/Only_Spider_with_Animations_Export.obj", 2f, 0f, 0f, -90f, 90f, -90f, 0.005f, 0.005f, 0.005f)

        // Katze
        cat = texturedObject("assets/complex objects/cat/12221_Cat_v1_l3.obj", 2f, 0f, 1f, -90f, 0f, 0f, 0.01f, 0.01f, 0.01f)

        // Ente
        ente = texturedObject("assets/complex objects/Nagnag/12248_Bird_v1_L2.obj", 2f, 0f, 3f, -90f, 0f, 0f, 0.01f, 0.01f, 0.01f)

        // Ente Weiblich
        ente_w = texturedObject("assets/complex objects/Nagnag_w/12249_Bird_v1_L2.obj", 3f, 0f, 3f, -90f, 0f, 0f, 0.01f, 0.01f, 0.01f)

        // Vogel
        vogel = texturedObject("assets/complex objects/Bird/12214_Bird_v1max_l3.obj", 4f, 0f, 5f, -90f, 90f, 0f, 0.01f, 0.01f, 0.01f)

        // Hund
        beagle = texturedObject("assets/complex objects/Beagle/13041_Beagle_v1_L1.obj", 4f, 0f, 5f, -90f, 90f, 0f, 0.01f, 0.01f, 0.01f)

        // Castle
        castle = texturedObject("assets/complex objects/Castle/Castle OBJ.obj", 2f, 0f, -5f, -90f, 90f, -90f)

        // Haus
        haus = texturedObject("assets/complex objects/Farmhouse/farmhouse_obj.obj", 2f, 0f, 20f, -90f, 90f, -90f, 0.2f, 0.2f, 0.2f)

        // Haus 2
        haus2 = texturedObject("assets/complex objects/cottage2/abandoned_cottage.obj", 15f, 0f, 20f, 0f, 0f, 0f, 0.01f, 0.01f, 0.01f)

        // Rose
        rose = ModelLoader.loadModel("assets/complex objects/Rose/rose.obj",  Math.toRadians(-90f), Math.toRadians(90f), 0f)?: throw IllegalArgumentException("Could not load the model")
        rose.translateGlobal(Vector3f(2f, 0f, 2f))
        rose.scaleLocal(Vector3f(0.01f, 0.01f, 0.01f))
        rose.rotateLocal(0f, 0f, Math.toRadians(-90f))

        // Gras
        // 1 Objekt mit gestreckter Textur
        gras = texturedObject("assets/complex objects/gras/10450_Rectangular_Grass_Patch_v1_iterations-2.obj", 0f, 0f, 0f, -90f, 90f, 0f, 0.5f, 0.01f, 0.5f)
        // Objekte aneiander gereiht
        //ground = GrasGround(20, 20, -10f * 3, -10f * 3)

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
        cycle.render(staticShader)
        baum_01.render(staticShader)
        spinne.render(staticShader)
        baum_01.animate()
        cat.render(staticShader)
        rose.render(staticShader)
        ente.render(staticShader)
        ente_w.render(staticShader)
        //castle.render(staticShader)
        haus.render(staticShader)
        vogel.render(staticShader)
        beagle.render(staticShader)
        haus2.render(staticShader)
        //ground.render(staticShader)
        gras.render(staticShader)
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
        if (window.getKeyState(GLFW_KEY_A)) {
            cycle.rotateLocal(0f,Math.toRadians(40f*dt),0f)
        }
        if (window.getKeyState(GLFW_KEY_D)) {
            cycle.rotateLocal(0f,Math.toRadians(-40f*dt),0f)
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

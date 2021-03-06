package cga.exercise.game

import cga.exercise.components.`object`.*
import cga.exercise.components.animation.Flying
import cga.exercise.components.animation.Roaming
import cga.exercise.components.camera.ProjectCamera
import cga.exercise.components.geometry.*
import cga.exercise.components.light.PointLight
import cga.exercise.components.light.SpotLight
import cga.exercise.components.shader.ShaderProgram
import cga.framework.*
import org.joml.Vector3f
import org.lwjgl.opengl.GL11.*
import org.joml.Math
import org.lwjgl.glfw.GLFW
import java.awt.MouseInfo


/**
 * Created by Fabian on 16.09.2017.
 */
class Scene(private val window: GameWindow) {
    private val staticShader: ShaderProgram
    val toonShader: ShaderProgram
    val normalenShader: ShaderProgram
    var aktuellerShader: ShaderProgram
    var cam: ProjectCamera
    var referenzObjekt:Walkable
    //var pointLight: PointLight
    var spotLight: SpotLight
    val sonne : PointLight
    val loadedObjectGround : loadedObject
    val spinne : Spinne
    val spinnenschwarm:MutableList<Spinne>
    val cat: texturedObject
    val ente: Ente
    val ente_w: Ente
    val vogel: texturedObject
    val beagle: texturedObject
    val haus2: texturedObject
    val haus: texturedObject
    val castle: texturedObject
    val flycam: Walkable

    val baum_01 : Baum
    val baum_02 : Baum
    val boom:MutableList<Baum>
    val rose: Renderable
    //val ground: GrasGround
    val gras: texturedObject

    val wald: Wald

    val beet:Rosenbeet

    var old_mouse_pos_x : Double
    var old_mouse_pos_y : Double

    var shaderAuswahl: Int = 1
    var aktKameraAuswahl: Int = 1


    //scene setup
    init {
        // Shader initialisieren
        staticShader = ShaderProgram("assets/shaders/project_vert.glsl", "assets/shaders/project_frag.glsl")
        toonShader = ShaderProgram("assets/shaders/toon_vert.glsl", "assets/shaders/toon_frag.glsl")
        normalenShader = ShaderProgram("assets/shaders/normalen_vert.glsl", "assets/shaders/normalen_frag.glsl")
        aktuellerShader = staticShader

        glEnable(GL_CULL_FACE)
        glFrontFace(GL_CCW)
        glCullFace(GL_BACK)
        //initial opengl state
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f); GLError.checkThrow()
        glDisable(GL_CULL_FACE); GLError.checkThrow()
        glEnable(GL_DEPTH_TEST); GLError.checkThrow()
        glDepthFunc(GL_LESS); GLError.checkThrow()

        // Ground (Objekt + Texturen) einlesen
        loadedObjectGround = loadedObject("assets/models/ground.obj", "assets/textures/ground_diff.png", "assets/textures/ground_emit.png", "assets/textures/ground_spec.png")

        //licht und so
        //pointLight=PointLight(Vector3f(0f, 1f, 0f), Vector3f(1.0f, 0.5f, 0.1f), Vector3f(1f,1f,1f),cycle)

        //Sonne
        sonne = PointLight(org.joml.Vector3f(0f, 9f, 0f), Vector3f(1.0f, 0.5f, 0.1f), Vector3f(1f,1f,0f), null)

        // Baum
        baum_01 = Baum(-2f, 0f, 0f, 90f, 0f, 90f, 1, true, true)
        baum_02 = Baum(-4f, 0f, 0f, 90f, 0f, 90f, 1, false)

        //mehr Baum
        boom= listOf(
                Baum(2f, 0f, 2f, 90f, 0f, 90f, 1, false),
                Baum(3f, 0f, 5f, 90f, 0f, 90f, 1, false),
                Baum(4f, 0f, 1f, 90f, 0f, 90f, 1, false),
                Baum(5f, 0f, 3f, 90f, 0f, 90f, 1, false),
                Baum(6f, 0f, 7f, 90f, 0f, 90f, 1, false),
                Baum(7f, 0f, 8f, 90f, 0f, 90f, 1, false),
                Baum(8f, 0f, 6f, 90f, 0f, 90f, 1, false),
                Baum(9f, 0f, 4f, 90f, 0f, 90f, 1, false)).toMutableList()

        // Spinne
        spinne = Spinne(2f, 0.1f, 0f, 0f, 0f, 0f)

        spinnenschwarm= mutableListOf(
                Spinne(2f, 0.1f, 0f, 0f, 0f, 0f),
                Spinne(2f, 0.1f, 0f, 0f, 0f, 0f),
                Spinne(2f, 0.1f, 0f, 0f, 0f, 0f),
                Spinne(2f, 0.1f, 0f, 0f, 0f, 0f),
                Spinne(2f, 0.1f, 0f, 0f, 0f, 0f)
        )

        // Katze
        cat = texturedObject("assets/complex objects/cat/12221_Cat_v1_l3.obj", -4f, 0.05f, -4f, -90f, 0f, 0f, 0.01f, 0.01f, 0.01f)

        //Beet
        beet=Rosenbeet(50, -2.5f,-2.5f,-6.5f,-6.5f)

        // Ente
        ente = Ente("assets/complex objects/Nagnag/12248_Bird_v1_L2.obj", 2f, 0.1f, 3f, -90f, 0f, 0f, 0.01f, 0.01f, 0.01f, true)

        // Ente Weiblich
        ente_w = Ente("assets/complex objects/Nagnag_w/12249_Bird_v1_L2.obj", 3f, 0.1f, 3f, -90f, 0f, 0f, 0.01f, 0.01f, 0.01f, true)

        // Vogel
        vogel = texturedObject("assets/complex objects/Bird/12214_Bird_v1max_l3.obj", 4f, 2f, 5f, -90f, 90f, 0f, 0.01f, 0.01f, 0.01f)

        // Hund
        beagle = texturedObject("assets/complex objects/Beagle/13041_Beagle_v1_L1.obj", -3f, 0.05f, 11f, -90f, 0f, 0f, 0.01f, 0.01f, 0.01f)

        // Castle
        castle = texturedObject("assets/complex objects/Castle/Castle OBJ.obj", 2f, 0f, -5f, -90f, 90f, -90f)

        // Haus
        haus = texturedObject("assets/complex objects/Farmhouse/farmhouse_obj.obj", 2f, 0f, 10f, -90f, 90f, -90f, 0.2f, 0.2f, 0.2f)

        // Haus 2
        haus2 = texturedObject("assets/complex objects/cottage2/abandoned_cottage.obj", 15f, 0f, 10f, 0f, 0f, 0f, 0.01f, 0.01f, 0.01f)

        flycam = Walkable("assets/complex objects/Beagle/13041_Beagle_v1_L1.obj", -3f, 0.05f, 11f, -90f, 0f, 0f, 0.01f, 0.01f, 0.01f)

        // Rose
        rose = ModelLoader.loadModel("assets/complex objects/Rose/rose.obj",  Math.toRadians(-90f), Math.toRadians(90f), 0f)?: throw IllegalArgumentException("Could not load the model")
        rose.translateGlobal(Vector3f(1f, 0f, 1f))
        rose.scaleLocal(Vector3f(0.01f, 0.01f, 0.01f))
        rose.rotateLocal(0f, 0f, Math.toRadians(-90f))

        // Gras
        gras = texturedObject("assets/complex objects/gras/10450_Rectangular_Grass_Patch_v1_iterations-2.obj", 0f, 0f, 0f, -90f, 90f, 0f, 0.5f, 0.01f, 0.5f)

        wald = Wald(40, -30f, -30f, 30f, 30f, 5f)

        // Kamera
        referenzObjekt = ente
        cam= ProjectCamera(referenzObjekt.loadedObject)
        referenzObjekt.initCamera(cam)

        spotLight= SpotLight(Vector3f(0f,1f,0f), Vector3f(0.5f, 0.05f, 0.01f),Vector3f(0.5f,0.5f,1f), ente.loadedObject, 15f,20f)
        spotLight.rotateLocal(Math.toRadians(-20f),0f,0f)

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
        when (shaderAuswahl) {
            1 -> aktuellerShader = staticShader
            2 -> aktuellerShader = toonShader
            3 -> aktuellerShader = normalenShader
            else -> {}
        }
        aktuellerShader.use()
        cam.bind(aktuellerShader)
        spotLight.bind(aktuellerShader, "spot", cam.getCalculateViewMatrix())  // <-- Grund für die Punktlichquellen sicht die sich mit der Kamera bewegt
        sonne.bind(aktuellerShader, "point")
        aktuellerShader.setUniform("colo", Vector3f(1f, 1f, 1f))
        baum_01.render(aktuellerShader)
        spinne.render(aktuellerShader)
        cat.render(aktuellerShader)
        rose.render(aktuellerShader)
        ente.render(aktuellerShader)
        ente_w.render(aktuellerShader)
        vogel.render(aktuellerShader)
        beagle.render(aktuellerShader)
        haus2.render(aktuellerShader)
        gras.render(aktuellerShader)
        baum_02.render(aktuellerShader)
        wald.render(aktuellerShader)
        haus.render(aktuellerShader)
        beet.render(aktuellerShader)
        boom.forEach { it.render(aktuellerShader) }
        spinnenschwarm.forEach { it.render(aktuellerShader) }
    }

    /* ***********************************************************
    ** Update Funktion                                          **
    ************************************************************ */

    fun update(dt: Float, t: Float)
    {
        if(cam.parent==null) {
            Flying.fly(cam,dt,window)
        } else {
            referenzObjekt.walk(dt, window, t)
        }
        baum_01.animate(dt)
        wald.update(dt)
        Roaming.roam(beagle.loadedObject, dt, 3f)
        Roaming.roam(cat.loadedObject, dt, 0.0001f, 3)
        if(referenzObjekt!=spinne)Roaming.roam(spinne.loadedObject, dt, 15f)
        if(referenzObjekt!=ente_w)Roaming.roam(ente_w.loadedObject, dt, 5f, 3)
        if(referenzObjekt!=ente)Roaming.roam(ente.loadedObject, dt, 5f, 3)
        Roaming.roam(vogel.loadedObject, dt, 30f, 3)
        Flying.roamAltitude(vogel.loadedObject,dt,30f)
        spinnenschwarm.forEach { Roaming.roam(it.loadedObject, dt, 200f,1) }
    }

    /* ***********************************************************
    ** Tastendruck                                              **
    ************************************************************ */

    fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {
        if (key == GLFW.GLFW_KEY_TAB && action == 1 && mode == 0) { // TAB zum Wechseln des aktiven Shaders
            when (shaderAuswahl) {
                1 -> shaderAuswahl = 2
                2 -> shaderAuswahl = 3
                3 -> shaderAuswahl = 1
                else -> {
                    shaderAuswahl = 1
                }
            }
        }
        /*print(key)
        print(", ")
        println(GLFW.GLFW_KEY_TAB)*/
        // Wechsel des Walkables
        if(key == GLFW.GLFW_KEY_RIGHT && action == 1 && mode == 0) // Rechts
        {
            camWechsel(1)
        } else if (key == GLFW.GLFW_KEY_LEFT && scancode == 331 && action == 1 && mode == 0) { // Links
            camWechsel(-1)
        }
    }

    /* ***********************************************************
    ** Wechsel der Kamera                                       **
    ************************************************************ */

    fun camWechsel(richtung: Int) {
        if ((aktKameraAuswahl + richtung) > 4) {
            aktKameraAuswahl = 1
        } else if ((aktKameraAuswahl + richtung) < 1) {
            aktKameraAuswahl = 4
        } else {
            aktKameraAuswahl += richtung
        }
        when (aktKameraAuswahl) {
            1 -> referenzObjekt = ente
            2 -> referenzObjekt = ente_w
            3 -> referenzObjekt = spinne
            else -> referenzObjekt = flycam
        }
        if (referenzObjekt != flycam) {
            cam = ProjectCamera(referenzObjekt.loadedObject)
            referenzObjekt.initCamera(cam)
        } else {
            cam = ProjectCamera(null)
            cam.translateLocal(Vector3f(0f,2f,0f))
            cam.rotateLocal(0f,Math.toRadians(180f),0f)
        }
    }

    fun onMouseMove(xpos: Double, ypos: Double)
    {
        if(cam.parent!=null)
        {
            cam.rotateAroundPoint(0.0f, (xpos - old_mouse_pos_x).toFloat() * 0.002f, 0f, Vector3f(0f))
        }
        else
        {
            cam.rotateLocal((ypos - old_mouse_pos_y).toFloat() * -0.002f, (xpos - old_mouse_pos_x).toFloat() * -0.002f, 0f)
        }
        old_mouse_pos_x = xpos
        old_mouse_pos_y = ypos
    }

    fun cleanup() {}

}

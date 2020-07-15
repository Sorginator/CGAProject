package cga.exercise.game

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.geometry.Material
import cga.exercise.components.geometry.Mesh
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.geometry.VertexAttribute
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
    val groundMeshDaten : MutableList<OBJLoader.OBJMesh>
    val groundMeshes : MutableList<Mesh>
    val ground : Renderable
    val cycle:Renderable
    val cam:TronCamera
    var baum:Float=0f
    var ground_diff_texture : Texture2D
    var ground_emit_texture : Texture2D
    var ground_spec_texture : Texture2D
    var ground_Material : Material
    var pointLight: PointLight
    var spotLight: SpotLight
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
        //glFrontFace(GL_CCW); GLError.checkThrow()
        //glCullFace(GL_BACK); GLError.checkThrow()
        glEnable(GL_DEPTH_TEST); GLError.checkThrow()
        glDepthFunc(GL_LESS); GLError.checkThrow()

        //Generate Cycle
        cycle = ModelLoader.loadModel("assets/Light Cycle/Light Cycle/HQ_Movie cycle.obj",  Math.toRadians(-90f), Math.toRadians(90f), 0f)?: throw IllegalArgumentException("Could not load the model")
        cycle.scaleLocal(Vector3f(0.8f, 0.8f, 0.8f))

        // Ground (Objekt + Texturen) einlesen
        var objResultGround = OBJLoader.loadOBJ("assets/models/ground.obj")
        groundMeshDaten = objResultGround.objects[0].meshes
        groundMeshes = mutableListOf()
        ground_diff_texture = Texture2D("assets/textures/ground_diff.png", true)
        ground_diff_texture.setTexParams(GL30.GL_REPEAT, GL30.GL_REPEAT, GL30.GL_LINEAR_MIPMAP_LINEAR, GL30.GL_LINEAR)

        ground_emit_texture = Texture2D("assets/textures/ground_emit.png", true)
        ground_emit_texture.setTexParams(GL30.GL_REPEAT, GL30.GL_REPEAT, GL30.GL_LINEAR_MIPMAP_LINEAR, GL30.GL_LINEAR)

        ground_spec_texture = Texture2D("assets/textures/ground_spec.png", true)
        ground_spec_texture.setTexParams(GL30.GL_REPEAT, GL30.GL_REPEAT, GL30.GL_LINEAR_MIPMAP_LINEAR, GL30.GL_LINEAR)

        ground_Material = Material(ground_diff_texture, ground_emit_texture, ground_spec_texture, 500f, Vector2f(64.0f, 64.0f))
        for ((index, aktMeshData) in groundMeshDaten.withIndex()) {
            groundMeshes.add(
                    Mesh(
                            aktMeshData.vertexData,
                            aktMeshData.indexData,
                            arrayOf(
                                    VertexAttribute(3, GL30.GL_FLOAT, 32, 0),
                                    VertexAttribute(2, GL30.GL_FLOAT, 32, 12),
                                    VertexAttribute(3, GL30.GL_FLOAT, 32, 20)
                            ),
                            ground_Material
                    )
            )
        }
        ground= Renderable(groundMeshes)

        //Cam Setup
        cam= TronCamera(cycle)
        cam.rotateLocal(Math.toRadians(-20f),0f,0f)
        cam.translateLocal(Vector3f(0f,0f,4f))

        //licht und so
        pointLight=PointLight(Vector3f(0f, 1f, 0f), Vector3f(1.0f, 0.5f, 0.1f), Vector3f(0.2f,0.9f,0.8f),cycle)

        spotLight= SpotLight(Vector3f(0f,1f,0f), Vector3f(0.5f, 0.05f, 0.01f),Vector3f(0.5f,0.5f,1f), cycle, 15f,20f)
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
        staticShader.use()
        cam.bind(staticShader)
        pointLight.light_color = Vector3f(((sin(t-0.3f)+1f)/2f),( sin(t-0.6f)+1f)/2f, (sin(t)+1f)/2f)
        pointLight.bind(staticShader,"point")
        spotLight.bind(staticShader, "spot", cam.getCalculateViewMatrix())
        staticShader.setUniform("colo", Vector3f(0f, 1f, 0f))
        ground.render(staticShader)
        staticShader.setUniform("colo", Vector3f(((sin(t-0.3f)+1f)/2f),( sin(t-0.6f)+1f)/2f, (sin(t)+1f)/2f))
        cycle.render(staticShader)
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
        if (window.getKeyState(GLFW_KEY_SPACE)) {
            cycle.translateLocal(Vector3f(0.0f, 10f * dt, 0f))
            baum+=5f*dt
        }
        else{
            if(baum>0)
            {
                cycle.translateLocal(Vector3f(0.0f, -10f*dt, 0f))
                baum-=5f*dt
            }
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

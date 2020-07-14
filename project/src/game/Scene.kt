package game

import framework.GameWindow
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL11

class Scene(private val window: GameWindow) {

    init {

    }

    /* ***********************************************************
    ** Render Funktion                                          **
    ************************************************************ */

    fun render (dt: Float, t:Float)
    {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)
    }

    /* ***********************************************************
    ** Tastensteuerung Funktion                                 **
    ************************************************************ */

    fun update(dt: Float, t: Float) {
        if (window.getKeyState(GLFW.GLFW_KEY_W)) {

        }
        if (window.getKeyState(GLFW.GLFW_KEY_S)) {

        }
        if (window.getKeyState(GLFW.GLFW_KEY_A)&&(window.getKeyState(GLFW.GLFW_KEY_W)||window.getKeyState(GLFW.GLFW_KEY_S))) {

        }
        if (window.getKeyState(GLFW.GLFW_KEY_D)&&(window.getKeyState(GLFW.GLFW_KEY_W)||window.getKeyState(GLFW.GLFW_KEY_S))) {

        }
        if (window.getKeyState(GLFW.GLFW_KEY_SPACE)) {

        } else{

        }

    }

    /* ***********************************************************
    ** Tastendruck                                              **
    ************************************************************ */

    fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {}

    fun onMouseMove(xpos: Double, ypos: Double) {}

    fun cleanup() {}
}
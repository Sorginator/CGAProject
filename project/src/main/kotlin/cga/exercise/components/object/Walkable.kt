package cga.exercise.components.`object`

import cga.exercise.components.geometry.Mesh
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.shader.ShaderProgram
import cga.framework.GameWindow
import org.joml.Math
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW

open class Walkable(path: String, posX: Float, posY: Float, posZ: Float, rotX: Float= 0f, rotY: Float= 0f, rotZ: Float= 0f, scaleX: Float= 1f, scaleY: Float= 1f, scaleZ: Float= 1f, horizontalReverse: Boolean= false, verticalReverse: Boolean= false) : texturedObject(path, posX, posY, posZ, rotX, rotY, rotZ, scaleX, scaleY, scaleZ) {

    var hReverse: Int = 1
    var vReverse: Int = 1
    var speedForward: Float = 5f
    var speedBackwards: Float = 5f
    var speedRotate: Float = 40f
    var speedPush: Float = 1f
    var isMoving: Boolean = false
    var bleibtAufDemBoden: Boolean = true
    var defaultHeight: Float = loadedObject.getPosition().y

    init {
        if (horizontalReverse)
            hReverse = -1
        if (verticalReverse)
            vReverse = -1
    }

    open fun walk(timeDifference: Float, window: GameWindow, time: Float){
        isMoving = false
        // Bewegung
        if (window.getKeyState(GLFW.GLFW_KEY_SPACE)) {
            speedPush = 4f
        } else {
            speedPush = 1f
        }
        if (window.getKeyState(GLFW.GLFW_KEY_W)) {
            if (bleibtAufDemBoden) {
                loadedObject.translateLocal(Vector3f(0.0f, 0f, -1f * timeDifference * hReverse * speedForward * speedPush))
                loadedObject.translateGlobal(Vector3f(0f, defaultHeight-loadedObject.getPosition().y, 0f))
            } else {
                loadedObject.translateLocal(Vector3f(0.0f, 0f, -1f * timeDifference * hReverse * speedForward * speedPush))
            }
            isMoving = true
        }
        if (window.getKeyState(GLFW.GLFW_KEY_S)) {
            if (bleibtAufDemBoden) {
                loadedObject.translateLocal(Vector3f(0.0f, 0f, 1f * timeDifference * hReverse * speedBackwards))
            } else {
                loadedObject.translateLocal(Vector3f(0.0f, 0f, 1f * timeDifference * hReverse * speedBackwards))
            }
            isMoving = true
        }
        if (window.getKeyState(GLFW.GLFW_KEY_A)) {
            loadedObject.rotateAroundPoint(0f, Math.toRadians(1f*timeDifference*vReverse*speedRotate),0f, loadedObject.getPosition())
        }
        if (window.getKeyState(GLFW.GLFW_KEY_D)) {
            loadedObject.rotateAroundPoint(0f, Math.toRadians(-1f*timeDifference*vReverse*speedRotate),0f,loadedObject.getPosition())
        }
        /*
        print("Position: ")
        print(loadedObject.getPosition().x)
        print(", ")
        print(loadedObject.getPosition().y)
        print(", ")
        println(loadedObject.getPosition().z)
        */
    }
}
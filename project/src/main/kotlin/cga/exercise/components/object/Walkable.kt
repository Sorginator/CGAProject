package cga.exercise.components.`object`

import cga.exercise.components.geometry.Mesh
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.shader.ShaderProgram
import cga.framework.GameWindow
import org.joml.Math
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW

class Walkable(path: String, posX: Float, posY: Float, posZ: Float, rotX: Float= 0f, rotY: Float= 0f, rotZ: Float= 0f, scaleX: Float= 1f, scaleY: Float= 1f, scaleZ: Float= 1f, horizontalReverse: Boolean= false, verticalReverse: Boolean= false) : texturedObject(path, posX, posY, posZ, rotX, rotY, rotZ, scaleX, scaleY, scaleZ) {

    var hReverse: Int = 1
    var vReverse: Int = 1
    var speedForward: Float = 5f
    var speedBackwards: Float = 5f
    var speedRotate: Float = 40f
    var speedPush: Float = 1f
    /*var watschelRichtung: Int = 1
    var watschelRot: Float = 0f
    var letzterWatschler = 0*/
    init {
        if (horizontalReverse)
            hReverse = -1
        if (verticalReverse)
            vReverse = -1
    }

    fun walk(timeDifference: Float, window: GameWindow, time: Float){
        var isMoving: Boolean = false
        // Bewegung
        if (window.getKeyState(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            speedPush = 4f
        } else {
            speedPush = 1f
        }
        if (window.getKeyState(GLFW.GLFW_KEY_W)) {
            loadedObject.translateLocal(Vector3f(0.0f, 0f, -1f*timeDifference*hReverse*speedForward*speedPush))
            isMoving = true
        }
        if (window.getKeyState(GLFW.GLFW_KEY_S)) {
            loadedObject.translateLocal(Vector3f(0.0f, 0f, 1f*timeDifference*hReverse*speedBackwards))
            isMoving = true
        }
        if (window.getKeyState(GLFW.GLFW_KEY_A)) {
            loadedObject.rotateLocal(0f, Math.toRadians(1f*timeDifference*vReverse*speedRotate),0f)
        }
        if (window.getKeyState(GLFW.GLFW_KEY_D)) {
            loadedObject.rotateLocal(0f, Math.toRadians(-1f*timeDifference*vReverse*speedRotate),0f)
        }
        // Watschelgang
        /*if (isMoving) {
            if (watschelRot < 20) {
                loadedObject.rotateLocal(0f, 0f, Math.toRadians(timeDifference * watschelRichtung * 50 * speedPush))
                watschelRot += timeDifference * 50 * speedPush
            } else {
                watschelRichtung *= -1
                watschelRot = -20f
            }
            letzterWatschler = 0
        } else {
            if (watschelRot > 0) {
                if (letzterWatschler != 1) {
                    watschelRichtung = -1
                    loadedObject.rotateLocal(0f, 0f, Math.toRadians(timeDifference * watschelRichtung * 50 * speedPush))
                    watschelRot += timeDifference * 50 * speedPush
                    letzterWatschler = -1
                }
            } else if (watschelRot < 0) {
                if (letzterWatschler != -1) {
                    watschelRichtung = 1
                    loadedObject.rotateLocal(0f, 0f, Math.toRadians(timeDifference * watschelRichtung * 50 * speedPush))
                    watschelRot += timeDifference * 50 * speedPush
                    letzterWatschler = 1
                }
            }
        }*/
    }
}
package cga.exercise.components.`object`

import cga.exercise.components.geometry.Renderable
import cga.exercise.components.shader.ShaderProgram
import cga.framework.ModelLoader
import org.joml.Math
import org.joml.Vector3f

class Baum(posX: Float, posY: Float, posZ: Float, rotX: Float = 0f, rotY: Float = 0f, rotZ: Float = 0f, variant: Int = 0) {

    val loadedObject: Renderable
    var animate: Boolean = false
    var fallingState: Float = 0f
    var animationSpeed: Float = 1f


    init {
        if (variant == 0)
            loadedObject = ModelLoader.loadModel("assets/complex objects/Tree02/Tree.obj",  Math.toRadians(-90f + rotX), Math.toRadians(90f + rotY), Math.toRadians(-90f + rotZ))?: throw IllegalArgumentException("Could not load the model")
        else
            loadedObject = ModelLoader.loadModel("assets/complex objects/Tree/Tree.obj",  Math.toRadians(-90f + rotX), Math.toRadians(90f + rotY), Math.toRadians(-90f + rotZ))?: throw IllegalArgumentException("Could not load the model")
        loadedObject.translateGlobal(Vector3f(posX, posY, posZ))
    }

    fun render(shader: ShaderProgram) {
        loadedObject.render(shader)
    }

    fun animate() {
        if (animate) {
            anim_falling()
        }
    }

    fun resetAnimation() {
        animate = false
        loadedObject.rotateLocal(0f, 0f, Math.toRadians(fallingState))
        loadedObject.translateGlobal(Vector3f(0f, -0.2f / 90 * fallingState, 0f))
        fallingState = 0f
    }

    fun stopAnimation() {
        animate = false
    }

    fun startAnimation() {
        animate = true
    }

    fun anim_falling() {
        if (fallingState < 90) {
            fallingState += 1*animationSpeed
            loadedObject.rotateLocal(0f, 0f, Math.toRadians(-1f * animationSpeed))
            loadedObject.translateGlobal(Vector3f(0f, 0.2f / 90 * animationSpeed, 0f))
        } else {
            animate = false
        }
    }
}
package cga.exercise.components.`object`

import cga.exercise.components.geometry.Mesh
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.shader.ShaderProgram
import cga.framework.ModelLoader
import org.joml.Math
import org.joml.Vector3f

class Baum(posX: Float, posY: Float, posZ: Float, rotX: Float = 0f, rotY: Float = 0f, rotZ: Float = 0f, variant: Int = 0, val growable: Boolean= false, growingAtInit: Boolean = false, meshes: MutableList<Mesh>? = null) {

    var loadedObject: Renderable
    var animate: Boolean = false
    var growingActive: Boolean = false
    var fallingActive: Boolean = false
    var fallingState: Float = 0f
    var animationSpeed: Float = 0.25f
    var growingState: Float
    var timeTillNextAnimState: Float
    var growingStateMax: Float


    init {
        // Man kann zwischen 2 Baumvarianten wählen, oder aber Meshes übergeben, aus denen dann ein Rederable erstellt wird
        if (meshes != null) {
            loadedObject = Renderable(meshes)
        } else {
            if (variant == 0)
                loadedObject = ModelLoader.loadModel("assets/complex objects/Tree02/Tree.obj", Math.toRadians(-90f + rotX), Math.toRadians(90f + rotY), Math.toRadians(-90f + rotZ))
                        ?: throw IllegalArgumentException("Could not load the model")
            else
                loadedObject = ModelLoader.loadModel("assets/complex objects/Tree/Tree.obj", Math.toRadians(-90f + rotX), Math.toRadians(90f + rotY), Math.toRadians(-90f + rotZ))
                        ?: throw IllegalArgumentException("Could not load the model")
        }
        loadedObject.translateGlobal(Vector3f(posX, posY, posZ))
        growingState = 0f
        timeTillNextAnimState = 0f
        growingStateMax = 2f
        // Wenn der Baum wachsen soll, wird das hier aktiviert
        if (growable) {
            loadedObject.scaleLocal(Vector3f(0.1f, 0.1f, 0.1f))
            if (growingAtInit) {
                growingActive = true
                animate = true
            }
        }
    }

    // Ausführen des Renderaufrufes von dem Renderable
    fun render(shader: ShaderProgram) {
        loadedObject.render(shader)
    }

    // Animation vom fallen/Wachsen der Bäume
    fun animate(timeDifference: Float) {
        if (animate) {
            if (timeTillNextAnimState > 0.1) {
                if (fallingActive)
                    anim_falling()
                else if (growingActive)
                    grow(timeDifference)
            }
            timeTillNextAnimState += timeDifference
        }
    }

    fun resetFalling() {
        animate = false
        loadedObject.rotateLocal(0f, 0f, Math.toRadians(fallingState))
        loadedObject.translateGlobal(Vector3f(0f, -0.2f / 90 * fallingState, 0f))
        fallingState = 0f
    }

    fun pauseAnimation() {
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

    fun grow(timeDifference: Float) {
        if (growingState < growingStateMax) {
            growingState += (timeDifference * animationSpeed)
            loadedObject.scaleLocal(Vector3f(1f + 0.01f * animationSpeed, 1f + 0.01f * animationSpeed, 1f + 0.01f * animationSpeed))
            timeTillNextAnimState = 0f
        }
    }
}
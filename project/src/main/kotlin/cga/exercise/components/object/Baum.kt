package cga.exercise.components.`object`

import cga.exercise.components.geometry.Mesh
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.shader.ShaderProgram
import cga.framework.ModelLoader
import org.joml.Math
import org.joml.Vector3f

class Baum(posX: Float, posY: Float, posZ: Float, rotX: Float = 0f, rotY: Float = 0f, rotZ: Float = 0f, variant: Int = 0, val growable: Boolean= false, growingAtInit: Boolean = false, meshes: MutableList<Mesh>? = null, lebenszeit: Float = 60f, gehoertZumWald: Wald? = null) {

    var loadedObject: Renderable
    var animate: Boolean = false
    var growingActive: Boolean = false
    var fallingActive: Boolean = false
    var fallingState: Float = 0f
    var animationSpeed: Float = 0.25f
    var growingState: Float
    var timeTillNextAnimState: Float
    var growingStateMax: Float
    var timeToLive: Float
    var dieable: Boolean = true
    var nowDie: Boolean = false
    var timeToDie: Float = 5f
    var teilDesWaldes: Wald? = null
    var render: Boolean = true


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
        timeToLive = lebenszeit
        teilDesWaldes = gehoertZumWald
    }

    // Ausführen des Renderaufrufes von dem Renderable
    fun render(shader: ShaderProgram) {
        if (render)
            loadedObject.render(shader)
    }

    // Animation vom fallen/Wachsen der Bäume Ausführen
    fun animate(timeDifference: Float) {
        if (animate) {
            // Prüfen ob die Lebenszeit des Baumes abgelaufen ist
            if (!fallingActive && !growingActive && dieable && !nowDie) {
                if (timeToLive < 0) {
                    fallingActive = true
                } else {
                    timeToLive -= timeDifference
                }
            }
            // Baum wachsen oder Fallen lassen
            if (timeTillNextAnimState > 0.1) {
                if (fallingActive) {
                    anim_falling()
                    //println("Falling!")
                } else if (growingActive) {
                    grow(timeDifference)
                    //println("Growing!")
                }else if (nowDie) {
                    anim_die(timeDifference)
                    //println("Dying!")
                }
            }
            timeTillNextAnimState += timeDifference
        }
    }

    // Resettet einen gefallenen Baum
    fun resetFalling() {
        animate = false
        loadedObject.rotateLocal(0f, 0f, Math.toRadians(fallingState))
        loadedObject.translateGlobal(Vector3f(0f, -0.2f / 90 * fallingState, 0f))
        fallingState = 0f
    }

    // Pausiert eine Baum Animation
    fun pauseAnimation() {
        animate = false
    }

    // (Re-)Startet eine Animation
    fun startAnimation() {
        animate = true
    }

    // Führt das Fallen eines Baumes aus
    fun anim_falling() {
        if (fallingState < 90) {
            fallingState += 1*animationSpeed
            loadedObject.rotateLocal(0f, 0f, Math.toRadians(-1f * animationSpeed))
            loadedObject.translateGlobal(Vector3f(0f, 0.2f / 90 * animationSpeed, 0f))
        } else {
            fallingActive = false
            growingActive = false
            nowDie = true
        }
    }

    // Löscht einen Baum nach ablauf einer Zeitfrist
    fun anim_die(timeDifference: Float) {
        timeToDie -= timeDifference
        if (timeToDie < 0) {
            delete()
        }
    }

    // Führt das wachsen eines Baumes durch
    fun grow(timeDifference: Float) {
        if (growingState < growingStateMax) {
            growingState += (timeDifference * animationSpeed)
            loadedObject.scaleLocal(Vector3f(1f + 0.01f * animationSpeed, 1f + 0.01f * animationSpeed, 1f + 0.01f * animationSpeed))
            timeTillNextAnimState = 0f
        } else {
            growingActive = false
        }
    }

    // Löscht einen Baum aus der Liste der gerenderten Bäume in einem Wald
    fun delete() {
        if (teilDesWaldes != null) {
            teilDesWaldes?.deleteTree(this)
        }
    }
}
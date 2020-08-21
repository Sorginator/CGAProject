package cga.exercise.components.`object`

import cga.exercise.components.geometry.Mesh
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.shader.ShaderProgram
import cga.framework.ModelLoader
import java.util.*
import kotlin.concurrent.schedule
import kotlin.concurrent.timerTask

class Wald(val numberOfTrees: Int, val posMinX: Float, val posMinY: Float, val posMaxX: Float, val posMaxY: Float, val timeToNextTree: Float = 5f) {
    var Bäume: MutableList<Baum>
    var timePassed: Float
    val baumMeshes: Array<MutableList<Mesh>>
    var bäumeGesetzt = 0

    // Flächen auf denen keine Bäume wachsen sollen
    // Aufbau: floatArrayOf(kleinstes X, kleinstes Z, größtes X, größtes Z)
    var blacklistKoords: MutableList<FloatArray> = mutableListOf(
            // X1, x2, z1, z2
            floatArrayOf(-5.5f + 2f, 6.5f + 2f, -5f + 20f, 1.5f + 20f),   //cottage
            floatArrayOf(-4f + 15f, 6f + 15f, -2.5f + 20f, 2.5f + 20f)
    )

    init {
        Bäume = mutableListOf()
        timePassed = 0f
        val loadedBaum0 = ModelLoader.loadModel("assets/complex objects/Tree02/Tree.obj", org.joml.Math.toRadians(-90f), org.joml.Math.toRadians(90f), org.joml.Math.toRadians(-90f))?: throw IllegalArgumentException("Could not load the model")
        val loadedBaum1 = ModelLoader.loadModel("assets/complex objects/Tree/Tree.obj", org.joml.Math.toRadians(-90f), org.joml.Math.toRadians(90f), org.joml.Math.toRadians(-90f))?: throw IllegalArgumentException("Could not load the model")
        baumMeshes = arrayOf(
                loadedBaum0.meshes,
                loadedBaum1.meshes
        )
    }

    // Update Funktion, Erstellung eines neuen Baums alle "timeToNextTree"-Sekunden
    fun update(timeDifference: Float) {
        timePassed += timeDifference
        if (timePassed >= timeToNextTree) {
            timePassed = 0f
            if (bäumeGesetzt < numberOfTrees) {
                bäumeGesetzt += 1
                plantTree()
            }
        }
        Bäume.forEach { it.animate(timeDifference) }
    }

    // Funktion zum erstellen eines neuen Baumes (Koordinaten berechnen + Baum erstellen)
    fun plantTree() {
        var x = (Math.random()*(posMaxX - posMinX) + posMinX).toFloat()
        var y = (Math.random()*(posMaxY-posMinY) + posMinY).toFloat()
        var escape = 0
        while (!AreaIsClear(x, y) && escape < 30) {
            x = (Math.random()*(posMaxX - posMinX) + posMinX).toFloat()
            y = (Math.random()*(posMaxY-posMinY) + posMinY).toFloat()
            escape += 1
        }
        if (escape <= 30) {
            val rot = (Math.random()*360).toFloat()
            var variant = 0
            if (Math.random() > 0.5) {
                variant = 1
            }
            blacklistKoords.add(floatArrayOf(x-0.5f, x+0.5f, y-0.5f, y+0.5f))
            Bäume.add(Baum(x, 0f, y, rot, 0f, rot, variant, true, true, baumMeshes[variant], 6f, this))
        }
    }

    // Löschen eines gelöschten Baumes aus der Liste des Walds
    fun deleteTree(baum: Baum) {
        var neueBaeume: MutableList<Baum> = mutableListOf()
        Bäume.forEach {
            if (it != baum) {
                neueBaeume.add(it)
            }
        }
        Bäume = neueBaeume
    }

    // Prüfung ob ermittelte Koordinaten innerhalb einer Blacklistfläche sind
    fun AreaIsClear(x: Float, y: Float): Boolean {
        var b: Boolean = true
        for ((index, value) in blacklistKoords.withIndex()) {
            if ((x > value[0] && x < value[1]) || (y > value[2] && y < value[3])) {
                b = false
            }
        }
        return b
    }

    // Rendern der Bäume die aktuell in der Baumliste sind
    fun render(shader: ShaderProgram){
        Bäume.forEach { it.render(shader) }
    }
}
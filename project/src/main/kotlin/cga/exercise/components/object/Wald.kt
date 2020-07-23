package cga.exercise.components.`object`

import cga.exercise.components.shader.ShaderProgram
import java.util.*
import kotlin.concurrent.schedule
import kotlin.concurrent.timerTask

class Wald(val numberOfTrees: Int, val posMinX: Float, val posMinY: Float, val posMaxX: Float, val posMaxY: Float, val timeToNextTree: Float = 5f) {
    var Bäume: MutableList<Baum>
    var timePassed: Float

    var blacklistKoords: Array<FloatArray> = arrayOf(
            // X1, x2, z1, z2
            floatArrayOf(-5.5f + 2f, 6.5f + 2f, -5f + 20f, 1.5f + 20f),   //cottage
            floatArrayOf(-4f + 15f, 6f + 15f, -2.5f + 20f, 2.5f + 20f)      //farmhouse
    )
    var bäumeGesetzt = 0

    init {
        Bäume = mutableListOf()
        timePassed = 0f
    }

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

    fun plantTree() {
        var x = (Math.random()*(posMaxX - posMinX) + posMinX).toFloat()
        var y = (Math.random()*(posMaxY-posMinY) + posMinY).toFloat()
        var escape = 0
        /*while (!AreaIsClear(x, y) && escape < 30) {
            x = (Math.random()*(posMaxX - posMinX) + posMinX).toFloat()
            y = (Math.random()*(posMaxY-posMinY) + posMinY).toFloat()
            escape += 1
            println(escape)
        }*/
        print("Escape: ")
        println(escape)
        if (escape <= 30) {
            val rot = (Math.random()*360).toFloat()
            var variant = 0
            if (Math.random() > 0.5) {
                variant = 1
            }
            Bäume.add(Baum(x, 0f, y, rot, 0f, rot, variant, true, true))
        }
    }

    fun AreaIsClear(x: Float, y: Float): Boolean {
        var b: Boolean = true
        for ((index, value) in blacklistKoords.withIndex()) {
            if ((x > value[0] && x < value[1]) || (y > value[2] && y < value[3])) {
                b = false
            }
        }
        println(b.toString())
        return b
    }

    fun render(shader: ShaderProgram){
        Bäume.forEach { it.render(shader) }
    }
}
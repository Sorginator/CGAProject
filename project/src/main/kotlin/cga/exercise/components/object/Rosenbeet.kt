package cga.exercise.components.`object`

import cga.exercise.components.geometry.Mesh
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.geometry.loadedObject
import cga.exercise.components.shader.ShaderProgram
import cga.framework.ModelLoader
import java.util.*
import kotlin.concurrent.schedule
import kotlin.concurrent.timerTask

class Rosenbeet (val c: Int, val posMinX: Float, val posMinY: Float, val posMaxX: Float, val posMaxY: Float)
{
        var rosen: MutableList<Renderable>
        val roseMeshes: Array<MutableList<Mesh>>
    val lRose:Renderable


        init
        {
            rosen = mutableListOf()
            lRose = ModelLoader.loadModel("assets/complex objects/Rose/rose.obj",  org.joml.Math.toRadians(-90f), org.joml.Math.toRadians(90f), 0f)?: throw IllegalArgumentException("Could not load the model")
            roseMeshes = arrayOf(
                    lRose.meshes
            )

        }

        fun plantRose()
        {
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
                rosen.add((x, 0f, y, rot, 0f, rot, variant, true, true, baumMeshes[variant]))
            }
        }

        fun AreaIsClear(x: Float, y: Float): Boolean {
            var b: Boolean = true
            for ((index, value) in blacklistKoords.withIndex()) {
                if ((x > value[0] && x < value[1]) || (y > value[2] && y < value[3])) {
                    b = false
                }
            }
            return b
        }

        fun render(shader: ShaderProgram){
            Bäume.forEach { it.render(shader) }
        }
    }
}
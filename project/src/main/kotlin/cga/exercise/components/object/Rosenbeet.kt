package cga.exercise.components.`object`

import cga.exercise.components.geometry.Mesh
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.geometry.Transformable
import cga.exercise.components.geometry.loadedObject
import cga.exercise.components.shader.ShaderProgram
import cga.framework.ModelLoader
import org.joml.Vector3f
import java.util.*
import kotlin.concurrent.schedule
import kotlin.concurrent.timerTask

class Rosenbeet (val c: Int, val posMinX: Float, val posMinY: Float, val posMaxX: Float, val posMaxY: Float, p: Transformable? = null):Transformable(p)
{
    var rosen: MutableList<Renderable>
    val lRose:Renderable


        init
        {
            rosen = mutableListOf()
            lRose = ModelLoader.loadModel("assets/complex objects/Rose/rose.obj",  org.joml.Math.toRadians(-90f), org.joml.Math.toRadians(90f), 0f)?: throw IllegalArgumentException("Could not load the model")
            plant()
        }

        fun plant()
        {
            var a=c
            while(a>0)
            {
                var x = (Math.random() * (posMaxX - posMinX) + posMinX).toFloat()
                var y = (Math.random() * (posMaxY - posMinY) + posMinY).toFloat()
                var r = Renderable(lRose.meshes, this)
                r.translateLocal(Vector3f(x, 0f, y))
                rosen.add(r)
                a--
            }
        }

        fun render(shader: ShaderProgram)
        {
            rosen.forEach { it.render(shader) }
        }
    }

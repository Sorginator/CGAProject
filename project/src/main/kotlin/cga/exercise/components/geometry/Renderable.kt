package cga.exercise.components.geometry

import cga.exercise.components.shader.ShaderProgram
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.opengl.GL11
import kotlin.math.absoluteValue
import kotlin.math.sin

class Renderable(m:MutableList<Mesh>, p: Transformable? = null) :Transformable(p), IRenderable
{
    val meshes : MutableList<Mesh> = m

    fun render(shaderProgram: ShaderProgram, colo:Vector4f, t:Float)
    {
        var c:Vector3f
        if (colo.z==1f)
        {
            c= Vector3f(sin(colo.w*t).absoluteValue,sin(colo.x*t).absoluteValue, sin(colo.y*t).absoluteValue)
        }
        else c= Vector3f(colo.w,colo.x,colo.y)
        shaderProgram.setUniform("colo", c)
        shaderProgram.setUniform("model_matrix", getWorldModelMatrix(), false)
        for (item in meshes){
            item.render(shaderProgram)
        }
    }

    override fun render(shaderProgram: ShaderProgram)
    {
        shaderProgram.setUniform("model_matrix", getWorldModelMatrix(), false)
        for (item in meshes){
            item.render(shaderProgram)
        }
    }

}
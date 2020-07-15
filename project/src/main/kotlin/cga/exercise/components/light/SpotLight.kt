package cga.exercise.components.light

import cga.exercise.components.geometry.Transformable
import cga.exercise.components.shader.ShaderProgram
import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.Math
import org.joml.Vector4f

class SpotLight(light_position: Vector3f, attenuation: Vector3f, light_color: Vector3f, p: Transformable?, private val innerCone: Float, private val outerCone : Float) : PointLight(light_position, attenuation, light_color, p), ISpotLight
{

    init
    {
        translateGlobal(light_position)
    }

    override fun bind(shaderProgram: ShaderProgram, name: String, viewMatrix: Matrix4f)
    {
        shaderProgram.setUniform(name+"_position", getWorldPosition())
        shaderProgram.setUniform(name+"_color", light_color)
        shaderProgram.setUniform(name+"_inner_cone", Math.cos(Math.toRadians(innerCone)))
        shaderProgram.setUniform(name+"_outer_cone", Math.cos(Math.toRadians(outerCone)))
        var temp :Vector4f = Vector4f(getWorldZAxis().negate(), 0f).mul(viewMatrix)
        shaderProgram.setUniform(name+"_direction", Vector3f(temp.x, temp.y, temp.z))
        shaderProgram.setUniform(name+"_attenuation", attenuation)
    }

}